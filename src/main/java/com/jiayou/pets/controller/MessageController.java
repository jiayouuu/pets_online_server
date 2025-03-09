package com.jiayou.pets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/api/send")
    public String sendMessage(@RequestBody MessageRequest request) {
        // 向 /topic/messages 发送消息
        messagingTemplate.convertAndSend("/topic/messages", request.getMessage());
        return "Message sent: " + request.getMessage();
    }
    @MessageMapping("/send")
    public void sendMessagews(@Payload MessageRequest req) {
        // 向 /topic/messages 发送消息
        messagingTemplate.convertAndSend("/topic/messages", req);
    }
}

class MessageRequest {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}