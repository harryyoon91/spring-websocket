package com.example.chat;

import java.util.ArrayList;
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

 
public class EchoHandler extends TextWebSocketHandler {
 
    private Logger logger = LoggerFactory.getLogger(EchoHandler.class);
 
    private List<WebSocketSession> connectedUsers;
 
    public EchoHandler() {
        connectedUsers = new ArrayList<WebSocketSession>();
    }
 
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        connectedUsers.add(session);
 
        logger.info(session.getId() + " is connected.");
        logger.info("Connected IP : " + session.getRemoteAddress().getHostName());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
 
        MessageVO messageVO = MessageVO.converMessage(message.getPayload());
        String hostName = "";
 
        for (WebSocketSession webSocketSession : connectedUsers) {
            if (messageVO.getType().equals("all")) {
                if (!session.getId().equals(webSocketSession.getId())) {
                    webSocketSession.sendMessage(
                            new TextMessage(session.getRemoteAddress().getHostName() + " :: " + messageVO.getMessage()));
                }
            } else {
                hostName = webSocketSession.getRemoteAddress().getHostName();
                if (messageVO.getTo().equals(hostName)) {
                    webSocketSession.sendMessage(
                            new TextMessage(
                                    "<span style='color:red; font-weight: bold;' >"
                                    + session.getRemoteAddress().getHostName() + " :: " + messageVO.getMessage()
                                    + "</span>") );
                    break;
                }
            }
        }
 

        logger.info(session.getId() + " sending message : " + message.getPayload());
    }
 

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
 
        connectedUsers.remove(session);
 
        for (WebSocketSession webSocketSession : connectedUsers) {

            if (!session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(new TextMessage(session.getRemoteAddress().getHostName() + " is left this chatroom."));
            }
        }
 
        logger.info(session.getId() + " disconnected.");
    }
}

