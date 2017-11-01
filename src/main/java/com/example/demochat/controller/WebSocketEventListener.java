package com.example.demochat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.demochat.model.ChatMessage;

@Component
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null) {
			logger.info("Usuário desconectou : " + username);
			
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(ChatMessage.MessageType.LEAVE);
			chatMessage.setSender(username);
			
			messagingTemplate.convertAndSend("/channel/public", chatMessage);
		}
	}
}
