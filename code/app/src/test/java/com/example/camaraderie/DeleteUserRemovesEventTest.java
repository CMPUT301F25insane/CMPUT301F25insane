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

import org.junit.After;
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
    private User mUser1Object;
    private final Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {
        cal.set(2025, Calendar.DECEMBER, 5);
        Date registrationDeadline = cal.getTime();

        cal.set(2025, Calendar.DECEMBER, 12);
        Date eventDate = cal.getTime();

        mockHost = mock(DocumentReference.class);
        mockEventRef = mock(DocumentReference.class);
        mockUser1 = mock(DocumentReference.class);

        mUser1Object = new User(
                "user",
                "phone",
                "email",
                "address",
                mockUser1.getId(),
                null,
                mockUser1
        );

        mockUser1.set(mUser1Object);

        event = new Event(
                "Hackathon",
                "Edmonton Hall",
                registrationDeadline,
                "24-hour coding competition",
                eventDate,
                "09:00 AM",
                "9:00AM",
                2,
                2,
                mockHost,
                mockEventRef,
                null,
                false
        );
    }

    @After
    public void teardown() {
        mockUser1.delete();
        mockEventRef.delete();
        mockHost.delete();
    }

    @Test
    public void RemoveUserTest() {
        event.addWaitlistUser(mockUser1);
        assertTrue(event.getWaitlist().contains(mockUser1));


    }
}
