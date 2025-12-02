package com.example.camaraderie;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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


public class EventUnitTest {
    private Event event;
    private DocumentReference mockHost;
    private DocumentReference mockEventRef;
    private DocumentReference mockUser1;
    private DocumentReference mockUser2;
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
        mockUser2 = mock(DocumentReference.class);
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
        mockHost.delete();
        mockEventRef.delete();
        mockUser1.delete();
        mockUser2.delete();
    }

    // ---------- BASIC DATA TESTS ----------

    @Test
    public void testEventConstructorStoresDataCorrectly() {
        cal.set(2025, Calendar.DECEMBER, 12);
        Date eventDate = cal.getTime();

        cal.set(2025, Calendar.DECEMBER, 5);
        Date registrationDeadline = cal.getTime();

        assertEquals("Hackathon", event.getEventName());
        assertEquals("Edmonton Hall", event.getEventLocation());
        assertEquals("09:00 AM", event.getEventDateTime());
        assertEquals(2, event.getCapacity());
//        assertEquals(mockEventRef, event.getEventId());
        assertEquals(eventDate, event.getEventDate());
        assertEquals(registrationDeadline, event.getRegistrationDeadline());
    }

    @Test
    public void testSettersAndGetters() {
        event.setEventName("Music Fest");
        event.setEventLocation("Stadium");
        event.setEventDateTime("8:00 PM");
        event.setDescription("Outdoor concert");
        event.setCapacity(50);

        assertEquals("Music Fest", event.getEventName());
        assertEquals("Stadium", event.getEventLocation());
        assertEquals("8:00 PM", event.getEventDateTime());
        assertEquals("Outdoor concert", event.getDescription());
        assertEquals(50, event.getCapacity());
    }

    // ---------- LIST MANAGEMENT TESTS ----------

    @Test
    public void testAddWaitlistUser() {
        event.addWaitlistUser(mockUser1);
        assertTrue(event.getWaitlist().contains(mockUser1));
    }

    @Test
    public void testAddWaitlistUser_NoDuplicates() {
        event.addWaitlistUser(mockUser1);
        event.addWaitlistUser(mockUser1);
        assertEquals(1, event.getWaitlist().size());
    }
    @Test
    public void addTwoUserToWaitlist() {
        event.addWaitlistUser(mockUser1);
        event.addWaitlistUser(mockUser2);
        assertEquals(2, event.getWaitlist().size());
    }

    @Test
    public void testRemoveWaitlistUser() {
        event.addWaitlistUser(mockUser1);
        event.removeWaitlistUser(mockUser1);
        assertFalse(event.getWaitlist().contains(mockUser1));
    }

    @Test
    public void testAddAcceptedUser() {
        event.addAcceptedUser(mockUser1);
        assertTrue(event.getAcceptedUsers().contains(mockUser1));
    }

    @Test
    public void testAddAcceptedUser_NoDuplicates() {
        event.addAcceptedUser(mockUser1);
        event.addAcceptedUser(mockUser1);
        assertEquals(1, event.getAcceptedUsers().size());
    }

    // ---------- LOTTERY TESTS ----------

//    @Test
//    public void testRunLotteryMovesUsersFromWaitlistToSelected() {
//        // add users to waitlist
//        event.addWaitlistUser(mockUser1);
//        event.addWaitlistUser(mockUser2);
//
//        // run lottery with capacity = 2
//        event.runLottery();
//
//        // selected users should not exceed capacity
//        assertTrue(event.getSelectedUsers().size() <= event.getCapacity());
//
//        // selected users must be removed from waitlist
//        assertTrue(event.getWaitlist().size() <= 2 - event.getSelectedUsers().size());
//    }

//    @Test
//    public void testRunLotteryUpdatesUserFields() {
//        // add a user
//        event.addWaitlistUser(mockUser1);
//
//        event.runLottery();
//
//        // verify update() called on user for waitlistedEvents removal
//        verify(mockUser1, atLeastOnce()).update(eq("waitlistedEvents"), any());
//        verify(mockUser1, atLeastOnce()).update(eq("selectedEvents"), any());
//    }

    // ---------- FIRESTORE TEST ----------

    @Test
    public void testUpdateDBCallsFirestoreSet() {
        // Arrange
        Task<Void> mockTask = mock(Task.class);
        when(mockEventRef.set(any(), any())).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        // Act
        event.updateDB(() -> {
            // Assert
            verify(mockEventRef, times(1)).set(eq(event), eq(SetOptions.merge()));
        });


    }

    // ---------- WAITLIST LIMIT TESTS ----------

    @Test
    public void testSetWaitlistLimit() {
        event.setWaitlistLimit(10);
        assertEquals(10, event.getWaitlistLimit());
    }
}

