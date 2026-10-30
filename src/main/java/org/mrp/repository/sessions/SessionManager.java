package org.mrp.repository.sessions;

import org.mrp.model.Session;
import org.mrp.model.User;
import org.mrp.model.exceptions.SessionTokenInvalid;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static SessionManager instance = null;

    private SessionManager() {
        Sessions = new ConcurrentHashMap<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    private static ConcurrentHashMap<String, Session> Sessions;

    public Session getNewSession(User user) {
        Session session = new Session(user.getUserId(), Duration.ofMinutes(30));
        Sessions.put(session.getToken(), session);
        return session;
    }

    public Session getSession(String token) {
        if(Sessions.containsKey(token)) {
            Session session = Sessions.get(token);
            if(!session.isExpired()) {
                session.refresh();
                return session;
            }
            Sessions.remove(token);
            throw new SessionTokenInvalid("Session token is expired");
        }
        throw new SessionTokenInvalid("invalid Session token");
    }

    public void invalidateSessions(String token) {
        Sessions.remove(token);
    }

    public void invalidateUserSessions(UUID userId) {
        Sessions.values().removeIf(session -> session.getUserId().equals(userId));
    }
}