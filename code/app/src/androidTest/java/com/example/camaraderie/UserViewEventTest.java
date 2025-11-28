package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.user_lists.waitlist_or_selected.ViewWaitlistOrSelectedFragment;
import com.example.camaraderie.event_screen.user_view.UserViewEventFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.not;

import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * We are testing UserViewEventFragment on whether it's buttons or text are visible and clickable.
 * We are also testing on the join and unjoin button functionality
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserViewEventTest {
    private FragmentScenario<UserViewEventFragment> scenario2;

    @Before
    public void setUp() {
        // Create mock event data
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Events").document();
        DocumentReference hostRef = FirebaseFirestore.getInstance().collection("Users").document();
        hostRef.set(new User(
                "host",
                "1234567890",
                "email@email.com",
                "address",
                hostRef.getId(),
                null,
                hostRef
        )).addOnSuccessListener(aVoid -> {
            Event mockEvent = new Event("Free Tickets to Oilers Game",
                    "Edmonton Stadium",
                    new Date(),
                    "20 Lucky individuals will get a front row seats to the Oilers game against Flames",
                    new Date(),
                    "20:00",
                    100,
                    -1,
                    hostRef,
                    docRef,
                    docRef.getId(),
                    null,
                    false);
            docRef.set(mockEvent).addOnSuccessListener(aVoid2 -> {
                // Pass event data to fragment
                Bundle bundle = new Bundle();
                bundle.putString("eventDocRefPath", docRef.getPath());

                // Launch fragment with event data
                scenario2 = FragmentScenario.launchInContainer(UserViewEventFragment.class, bundle);
            });
        });
    }
    @After
    public void tearDown() {
        if (scenario2 != null) {
            scenario2.close();
        }
    }


    @Test
    public void testJoinButtonDisplayed() {
        // Check if the "join" button is visible on screen
        onView(withId(R.id.join_button_user_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testJoinButtonIsClickable() {
        // Try clicking the "join" button
        onView(withId(R.id.join_button_user_view))
                .perform(click());
    }

    @Test
    public void testUnjoinButtonDisplayed() {
        // Check if the "Unjoin" button is visible on screen
        onView(withId(R.id.unjoin_button_user_view))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testUnjoinButtonIsClickable() {
        // Try clicking the "Unjoin" button
        onView(withId(R.id.unjoin_button_user_view))
                .perform(click());
    }

    @Test
    public void testQrButtonUserIsDisplayed() {
        // Check if the "unjoin" button is visible on screen
        onView(withId(R.id.qr_button_user_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testQrButtonUserIsClickable() {
        // Check if the "QR" button is clickable
        onView(withId(R.id.qr_button_user_view))
                .perform(click());
    }


    @Test
    public void viewAttendeesButtonIsDisplayed() {
        // Check if the view attendees button is visible on screen
        onView(withId(R.id.view_lists_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void viewAttendeesButtonIsClickable(){
        // Check if the "QR" button is clickable
        onView(withId(R.id.view_lists_button))
                .perform(click());
    }

    @Test
    public void viewPhotoButtonIsDisplayed() {
        // Check if the "unjoin" button is visible on screen
        onView(withId(R.id.view_photos_user_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void viewPhotoButtonIsClickable(){
        // Check if the "QR" button is clickable
        onView(withId(R.id.view_photos_user_view))
                .perform(click());
    }
//    @Test
//    //Test Event Info is properly displayed
//    public void testEventDetailsAreDisplayed() {
//        onView(withId(R.id.event_name_for_user_view)).check(matches(isDisplayed()));
//        onView(withId(R.id.userEventViewEventDate)).check(matches(isDisplayed()));
//        onView(withId(R.id.registration_deadline_text_user_view)).check(matches(isDisplayed()));
//        onView(withId(R.id.host_name_user_view)).check(matches(isDisplayed()));
//        onView(withId(R.id.name_of_useranizer)).check(matches(isDisplayed()));
//    }

    @Test
    //Test toggle Button Join tests
    public void testJoinButtonTogglesToUnjoin() {
        onView(withId(R.id.join_button_user_view)).perform(click());
        onView(withId(R.id.unjoin_button_user_view)).check(matches(isDisplayed()));
    }

    @Test
    // Test That Join and Unjoin button are mutually Exclusive
    public void testOnlyOneJoinButtonVisibleAtTime() {
        onView(withId(R.id.join_button_user_view)).perform(click());
        onView(withId(R.id.unjoin_button_user_view)).check(matches(isDisplayed()));
        onView(withId(R.id.join_button_user_view)).check(matches(not(isDisplayed())));
    }

    @Test
    //we are testing that edit button is not in User View
    public void testNoEditButtonInUserView() {
        onView(withId(R.id.delete_button_org_view)).check(doesNotExist()); // should not appear in UserView

    }
    @Test
    //we are testing that Delete button is not in User View
    public void testNoDeleteButtonInUserView() {
        onView(withId(R.id.delete_button_org_view)).check(doesNotExist()); // should not appear in UserView

    }

}








