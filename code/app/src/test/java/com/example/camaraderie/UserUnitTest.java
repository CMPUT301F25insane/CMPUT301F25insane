package com.example.camaraderie;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.firebase.firestore.DocumentReference;


import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for the User class.
 *
 */

public class UserUnitTest {
    private User user;
    private DocumentReference mockEvent1;
    private DocumentReference mockEvent2;


        @Before
        public void setUp() {
            // Mock DocumentReferences so we don’t touch Firestore
            mockEvent1 = mock(DocumentReference.class);
            mockEvent2 = mock(DocumentReference.class);

            user = new User("Alice", "1234567890", "alice@example.com", "123 Main St", "user123", mock(DocumentReference.class));
        }

        @Test
        public void testGettersAndSetters() {
            user.setFirstName("Bob");
            user.setPhoneNumber("9876543210");
            user.setEmail("bob@example.com");
            user.setAddress("456 Side St");

            assertEquals("Bob", user.getFirstName());
            assertEquals("9876543210", user.getPhoneNumber());
            assertEquals("bob@example.com", user.getEmail());
            assertEquals("456 Side St", user.getAddress());
        }

        @Test
        public void testAddCreatedEvent() {
            user.addCreatedEvent(mockEvent1);
            assertTrue(user.getUserCreatedEvents().contains(mockEvent1));
        }

        @Test
        public void testAddCreatedEvent_NoDuplicates() {
            user.addCreatedEvent(mockEvent1);
            user.addCreatedEvent(mockEvent1); // duplicate add
            assertEquals(1, user.getUserCreatedEvents().size());
        }

        @Test
        public void testAddTwoCreatedEvent() {
        user.addCreatedEvent(mockEvent1);
        user.addCreatedEvent(mockEvent2); // adding two events
        assertEquals(2, user.getUserCreatedEvents().size());
         }

        @Test
        public void testAddAndRemoveWaitlistedEvent() {
            user.addWaitlistedEvent(mockEvent1);
            assertTrue(user.getWaitlistedEvents().contains(mockEvent1));

            user.removeWaitlistedEvent(mockEvent1);
            assertFalse(user.getWaitlistedEvents().contains(mockEvent1));
        }

        @Test
        public void testAddSelectedEvent() {
            user.addSelectedEvent(mockEvent1);
            assertEquals(1, user.getSelectedEvents().size());
        }

        @Test
        public void testAddAcceptedEvent() {
            user.addAcceptedEvent(mockEvent1);
            assertTrue(user.getAcceptedEvents().contains(mockEvent1));
        }

        @Test
        public void testSetAdmin() {
            assertFalse(user.isAdmin());
            user.setAdmin(true);
            assertTrue(user.isAdmin());
        }

        @Test
        public void testDeleteCreatedEvent_RemovesFromList() {
            user.addCreatedEvent(mockEvent1);
            user.deleteCreatedEvent(mockEvent1);
            assertFalse(user.getUserCreatedEvents().contains(mockEvent1));
        }

        @Test
        public void testUserConstructorStoresCorrectData() {
            assertEquals("Alice", user.getFirstName());
            assertEquals("1234567890", user.getPhoneNumber());
            assertEquals("alice@example.com", user.getEmail());
            assertEquals("123 Main St", user.getAddress());
            assertEquals("user123", user.getUserId());
        }
    }

