package com.example.camaraderie;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUp() {
        mockHost = mock(DocumentReference.class);
        mockEventRef = mock(DocumentReference.class);
        mockUser1 = mock(DocumentReference.class);
        mockUser2 = mock(DocumentReference.class);

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

    // ---------- BASIC DATA TESTS ----------

    @Test
    public void testEventConstructorStoresDataCorrectly() {
        assertEquals("Hackathon", event.getEventName());
        assertEquals("Edmonton Hall", event.getEventLocation());
        assertEquals("09:00 AM", event.getEventTime());
        assertEquals(2, event.getCapacity());
        assertEquals("E123", event.getEventId());
    }

    @Test
    public void testSettersAndGetters() {
        event.setEventName("Music Fest");
        event.setEventLocation("Stadium");
        event.setEventTime("8:00 PM");
        event.setDescription("Outdoor concert");
        event.setCapacity(50);

        assertEquals("Music Fest", event.getEventName());
        assertEquals("Stadium", event.getEventLocation());
        assertEquals("8:00 PM", event.getEventTime());
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
        event.updateDB();

        // Assert
        verify(mockEventRef, times(1)).set(eq(event), eq(SetOptions.merge()));
    }

    // ---------- WAITLIST LIMIT TESTS ----------

    @Test
    public void testSetWaitlistLimit() {
        event.setWaitlistLimit(10);
        assertEquals(10, event.getWaitlistLimit());
    }
}

