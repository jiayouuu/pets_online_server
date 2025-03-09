package com.jiayou.pets.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/upload-progress/{sessionId}")
public class UploadProgress {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String sessionId = session.getPathParameters().get("sessionId");
        sessions.put(sessionId, session);
        sendMessage(session, "连接成功");
    }

    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getPathParameters().get("sessionId");
        sessions.remove(sessionId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        String sessionId = session.getPathParameters().get("sessionId");
        sessions.remove(sessionId);
    }

    public static void sendProgress(String sessionId, float progress) {
        Session session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            sendMessage(session, String.format("%.2f", progress));
        }
    }

    private static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}