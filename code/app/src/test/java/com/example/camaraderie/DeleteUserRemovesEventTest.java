package com.example.camaraderie;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.firebase.firestore.DocumentReference;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 *Here are ensuring that deleting a User removes them from an Event they joined.
 */

public class DeleteUserRemovesEventTest {
    private Event event;
    private DocumentReference mockHost;
    private DocumentReference mockEventRef;
    private DocumentReference mockUser1;

    @Before
    public void setUp() {
        mockHost = mock(DocumentReference.class);
        mockEventRef = mock(DocumentReference.class);
        mockUser1 = mock(DocumentReference.class);

        event = new Event(
                "Hackathon",
                "Edmonton Hall",
                new Date(),
                "24-hour coding competition",
                new Date(),
                "09:00 AM",
                2,
                mockHost,
                mockEventRef,
                "E123"
        );
    }

    @Test
    public void testAddWaitlistUser() {
        event.addWaitlistUser(mockUser1);
        assertTrue(event.getWaitlist().contains(mockUser1));

        assertFalse(event.getWaitlist().contains(mockUser1));
    }
}
