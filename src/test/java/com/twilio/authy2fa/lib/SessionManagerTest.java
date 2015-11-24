package com.twilio.authy2fa.lib;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionManagerTest {

    private static final long USER_ID = 1101;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void logInAddsAuthenticatedAttributeToSession() {
        when(request.getSession()).thenReturn(session);

        long userId = USER_ID;
        SessionManager sessionManager = new SessionManager();
        sessionManager.logIn(request, userId);

        verify(session).setAttribute(SessionManager.AUTHENTICATED, true);
        verify(session).setAttribute(SessionManager.USER_ID, userId);
    }

    @Test
    public void logOutInvalidatesTheSession() {
        when(request.getSession(false)).thenReturn(session);

        SessionManager sessionManager = new SessionManager();
        sessionManager.logOut(request);

        verify(session, times(1)).invalidate();
    }

    @Test
    public void getLoggedUserId() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionManager.USER_ID)).thenReturn(USER_ID);

        SessionManager sessionManager = new SessionManager();
        long result = sessionManager.getLoggedUserId(request);

        assertEquals(USER_ID, result);
    }

    @Test
    public void isAuthorized() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionManager.AUTHENTICATED)).thenReturn(true);

        SessionManager sessionManager = new SessionManager();
        boolean result = sessionManager.isAuthenticated(request);

        assertTrue(result);
    }
}
