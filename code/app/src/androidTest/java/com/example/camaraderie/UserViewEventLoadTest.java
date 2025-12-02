package com.example.camaraderie;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Bundle;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.camaraderie.event_screen.user_view.UserViewEventFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
/**
 * The purpose of this test is to check if the user can view a mocked event.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserViewEventLoadTest {
    private FragmentScenario<UserViewEventFragment> scenario2;

    @Test
    public void userCanViewCreatedEvent() {

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
                    "21:00",
                    2,
                    2,
                    hostRef,
                    docRef,
                null,
                    false);

            docRef.set(mockEvent).addOnSuccessListener(aVoid2 -> {
                // Pass event data to fragment
                Bundle bundle = new Bundle();
                bundle.putString("eventDocRefPath", docRef.getPath());

                // Launch fragment with event data
                scenario2 = FragmentScenario.launchInContainer(UserViewEventFragment.class, bundle);

                // Verify event details are displayed correctly
                onView(withId(R.id.event_name_for_user_view))
                        .check(matches(withText("Free Tickets to Oilers Game")));

                onView(withId(R.id.userEventViewEventDate))
                        .check(matches(withText("2025-12-12")));

                onView(withId(R.id.event_description_user_view))
                        .check(matches(withText("20 Lucky individuals will get a front row seats to the Oilers game against Flames")));

                scenario2.close();
            });
        });


    }
}




