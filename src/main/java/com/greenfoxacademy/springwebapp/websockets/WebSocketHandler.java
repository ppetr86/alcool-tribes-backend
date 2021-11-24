package com.greenfoxacademy.springwebapp.websockets;

import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final HashMap<Long, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("websocket session removed");
        sessionMap.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String[] path = session.getUri().getPath().split("/");
        long id = Integer.parseInt(path[path.length - 1]);
        log.info("websocket sessionmap updated. Kingdom ID added: " + id);
        sessionMap.put(id, session);
    }

    public boolean hasSession(Long id) {
        return sessionMap.containsKey(id);
    }

    public boolean sendMessage(long id, String json) {
        WebSocketSession session = sessionMap.get(id);
        try {
            session.sendMessage(new TextMessage(json));
            log.info("websocket message sent");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("unable to send websocket message");
            return false;
        }
    }
}