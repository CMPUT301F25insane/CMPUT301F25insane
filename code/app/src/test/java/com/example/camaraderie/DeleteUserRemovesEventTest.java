package com.example.camaraderie;

import static com.example.camaraderie.main.MainActivity.user;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.net.Uri;

import com.example.camaraderie.updateUserStuff.UpdateUserFragment;
import com.example.camaraderie.utilStuff.UserDeleter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
/**
 * Unit tests for the Event class.
 *
 */


public class DeleteUserRemovesEventTest {
    private Event event;
    private DocumentReference mockHost;
    private DocumentReference mockEventRef;
    private DocumentReference mockUser1;
    private final Calendar cal = Calendar.getInstance();
    private UserDeleter deleter;

    @Before
    public void setUp() {
        cal.set(2025, Calendar.DECEMBER, 5);
        Date registrationDeadline = cal.getTime();

        cal.set(2025, Calendar.DECEMBER, 12);
        Date eventDate = cal.getTime();

        mockHost = mock(DocumentReference.class);
        mockEventRef = mock(DocumentReference.class);
        mockUser1 = mock(DocumentReference.class);
        event = new Event(
                "Hackathon",
                "Edmonton Hall",
                registrationDeadline,
                "24-hour coding competition",
                eventDate,
                "09:00 AM",
                2,
                2,
                mockHost,
                mockEventRef,
                "E123",
                null,
                false
        );
    }

    @Test
    public void RemoveUserTest() {
        event.addWaitlistUser(mockUser1);
        assertTrue(event.getWaitlist().contains(mockUser1));
        deleter.DeleteUser((Runnable) mockUser1);
        assertFalse(event.getWaitlist().contains(mockUser1));
    }
}
}
