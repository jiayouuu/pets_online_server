package com.jiayou.pets.service.impl;

import com.jiayou.pets.config.FileUploadConfig;
import com.jiayou.pets.dto.file.FileUploadReq;
import com.jiayou.pets.dto.response.ResEntity;
import com.jiayou.pets.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileServiceImpl implements FileService {
    private final FileUploadConfig fileUploadConfig;
    private static final long TIMEOUT_MS = 60 * 1000;
    private static final String TEMP_SUBDIR = "temp"; // 默认 temp 子目录名

    private final Map<String, Integer> chunkCountMap = new ConcurrentHashMap<>();
    private final Map<String, Long> lastUploadTimeMap = new ConcurrentHashMap<>();
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // 注入 WebSocket 消息模板
    public FileServiceImpl(FileUploadConfig fileUploadConfig,SimpMessagingTemplate messagingTemplate) {
        this.fileUploadConfig = fileUploadConfig;
        this.messagingTemplate = messagingTemplate;
    }
    @Override
    public ResEntity<HashMap<String, Object>> upload(FileUploadReq req) {
        MultipartFile chunk = req.getChunk();
        String fileId = req.getFileId();
        int chunkIndex = req.getChunkIndex();
        int totalChunks = req.getTotalChunks();
        String hash = req.getHash();
        String originalFileName = req.getFileName();
        if (chunk == null || fileId == null || chunkIndex < 0 || totalChunks < 0 || originalFileName == null) {
            return ResEntity.error(400, "参数缺失");
        }
        // 校验文件后缀名
        String fileExtension = getFileExtension(originalFileName);
        if (!fileUploadConfig.getAllowedExtensions().contains(fileExtension.toLowerCase())) {
            return ResEntity.error(400, "不支持的文件类型: " + fileExtension);
        }
        try {
            // 获取上传目录并拼接 temp 目录
            String uploadDir = fileUploadConfig.getDir() + File.separator;
            String tempDir = uploadDir + TEMP_SUBDIR + File.separator;
            Path tempDirPath = Paths.get(tempDir);
            Path uploadDirPath = Paths.get(uploadDir);
            if (!Files.exists(tempDirPath)) Files.createDirectories(tempDirPath);
            if (!Files.exists(uploadDirPath)) Files.createDirectories(uploadDirPath);
            // 保存分片到临时目录
            String chunkFileName = fileId + "_chunk_" + chunkIndex;
            Path chunkPath = Paths.get(tempDir, chunkFileName);
            chunk.transferTo(chunkPath.toFile());

            // 更新分片计数和最后上传时间
            // 使用 compute 原子更新 chunkCountMap
            int currentCount = chunkCountMap.compute(fileId, (key, oldValue) ->
                    oldValue == null ? 1 : oldValue + 1);
            lastUploadTimeMap.put(fileId, System.currentTimeMillis());
            // 计算并通过 WebSocket 发送上传进度
            int progress = (int) Math.round((currentCount / (double) totalChunks) * 100);
            messagingTemplate.convertAndSend("/topic/upload-progress/" + fileId, progress);
            if (currentCount > totalChunks) {
                return ResEntity.error(400, "分片数量异常");
            }
            // 检查是否所有分片都已上传
            if (currentCount == totalChunks) {
                for (int i = 0; i < totalChunks; i++) {
                    if (!Files.exists(Paths.get(tempDir + fileId + "_chunk_" + i))) {
                        return ResEntity.error(500, "分片丢失，无法合并");
                    }
                }
                String finalFileName = UUID.randomUUID().toString().replace("-", "")+fileExtension;
                Path finalPath = Paths.get(uploadDir, finalFileName);
                boolean isMerged =mergeChunks(fileId,hash, totalChunks, finalPath);
                cleanup(fileId, totalChunks);
                chunkCountMap.remove(fileId);
                lastUploadTimeMap.remove(fileId);
                if (isMerged) {
                    // 通过 WebSocket 发送最终文件名
                    messagingTemplate.convertAndSend("/topic/upload-complete/" + fileId, finalFileName);
                } else {
                    return ResEntity.error(500, "分片校验或合并失败");
                }
            }
            return ResEntity.success(new HashMap<>(), "分片 " + chunkIndex + " 上传成功");
        } catch (IOException e) {
            return ResEntity.error(500, "上传失败: " + e.getMessage());
        }
    }



    @Override
    public File getFileByName(String fileName) throws IOException {
        // 参数校验
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 构造完整路径
        String fullPath = fileUploadConfig.getDir() + File.separator + fileName;
        File file = new File(fullPath);

        // 防止路径遍历攻击
        String canonicalPath = file.getCanonicalPath();
        if (!canonicalPath.startsWith(new File(fileUploadConfig.getDir()).getCanonicalPath())) {
            throw new IOException("无效的文件路径: " + fullPath);
        }

        // 检查文件是否存在
        if (!file.exists()) {
            throw new IOException("文件不存在: " + fullPath);
        }

        // 检查是否为文件而非目录
        if (!file.isFile()) {
            throw new IOException("路径不是文件: " + fullPath);
        }

        return file;
    }

    // 合并分片
    private boolean mergeChunks(String fileId,String hash, int totalChunks, Path finalPath) throws IOException {
        File finalFile = finalPath.toFile();
        String tempDir = fileUploadConfig.getDir() + File.separator + TEMP_SUBDIR + File.separator;
        for (int i = 0; i < totalChunks; i++) {
            File chunkFile = new File(tempDir + fileId + "_chunk_" + i);
            FileUtils.writeByteArrayToFile(finalFile, FileUtils.readFileToByteArray(chunkFile), true);
        }
        try {
            String calcHash = this.calculateFileHash(finalFile);
            if (calcHash.equals(hash)) {
                return true;
            } else {
                finalFile.delete();
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }
    // 清理临时文件
    private void cleanup(String fileId, int totalChunks) throws IOException {
        String tempDir = fileUploadConfig.getDir() + File.separator + TEMP_SUBDIR + File.separator;
        for (int i = 0; i < totalChunks; i++) {
            File chunkFile = new File(tempDir + fileId + "_chunk_" + i);
            if (chunkFile.exists()) {
                chunkFile.delete();
            }
        }

    }
    private String calculateFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // 使用 SHA-256
        byte[] buffer = new byte[8192];
        int bytesRead;
        try (var inputStream = FileUtils.openInputStream(file)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = md.digest();

        // 转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    // 定时清理过期分片
    @Scheduled(fixedRate = 10000)
    public void cleanExpiredChunks() {
        long currentTime = System.currentTimeMillis();
        String tempDir = fileUploadConfig.getDir() + File.separator + TEMP_SUBDIR + File.separator;
        lastUploadTimeMap.forEach((fileId, lastUploadTime) -> {
            if (currentTime - lastUploadTime > TIMEOUT_MS) {
                try {
                    File dir = new File(tempDir);
                    File[] chunks = dir.listFiles((d, name) -> name.startsWith(fileId + "_chunk_"));
                    if (chunks != null) {
                        for (File chunk : chunks) {
                            chunk.delete();
                        }
                    }
                    chunkCountMap.remove(fileId);
                    lastUploadTimeMap.remove(fileId);
                } catch (Exception e) {
                    System.err.println("清理超时文件失败: " + e.getMessage());
                }
            }
        });
    }

    // 获取文件后缀名
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
