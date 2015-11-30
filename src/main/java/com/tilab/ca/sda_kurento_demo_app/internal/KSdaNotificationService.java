package com.tilab.ca.sda_kurento_demo_app.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.kurento.jsonrpc.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KSdaNotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(KSdaNotificationService.class);
    
    private final List<Session> activeSessions = new LinkedList<>();  //list of all active sessions (doesn not necessary belong to a room it contains all
    //users connected 
    private final Map<String, Session> participantSessionMap = new HashMap<>();
    private final Map<String, String> sessionIdParticipantIdMap = new HashMap<>();
    
    public void addNewSession(Session session) {
        activeSessions.add(session);
    }
    
    public void addNewParticipantSession(Session session, String partecipantId) {
        participantSessionMap.put(partecipantId, session);
        sessionIdParticipantIdMap.put(session.getSessionId(), partecipantId);
    }
    
    public void removeSession(Session session) {
        String participantId = sessionIdParticipantIdMap.get(session.getSessionId());
        sessionIdParticipantIdMap.remove(session.getSessionId());
        participantSessionMap.remove(participantId);
        activeSessions.remove(session);
    }
    
    public void removeSession(String sessionId) {
        String participantId = sessionIdParticipantIdMap.get(sessionId);
        sessionIdParticipantIdMap.remove(sessionId);
        Session session = participantSessionMap.get(participantId);
        participantSessionMap.remove(participantId);
        activeSessions.remove(session);
    }
    
    public void notifyAllActiveSessions(String method, Object notificationPayload) {
        activeSessions.forEach(session -> {
            try {
                session.sendNotification(method, notificationPayload);
            } catch (IOException ex) {
                log.error(String.format("failed to send notification to sessionId %s", session.getSessionId()), ex);
            }
        });
    }
    
    public void notifyPartecipant(String partecipantId, String method, Object notificationPayload) {
        Session session = participantSessionMap.get(partecipantId);
        try {
            session.sendNotification(method, notificationPayload);
        } catch (IOException ex) {
            log.error(String.format("failed to send notification to participant %s with sessionId %s", partecipantId, session.getSessionId()), ex);
        }
    }
}
