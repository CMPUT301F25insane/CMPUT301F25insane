package com.example.camaraderie;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;

import androidx.test.espresso.contrib.RecyclerViewActions;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.dashboard.MainFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

/**
 *The purpose of this is to Test if we can find Mock event in the search results in main fragment
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEventInMainFragmentTest {
    private FragmentScenario<MainFragment> scenario2;
    private final Calendar cal = Calendar.getInstance();



    private DocumentReference mockHost;
    private DocumentReference mockEventRef;
    @Test
    public void SearchEventInMainFragment() {
        cal.set(2025, Calendar.DECEMBER, 5);
        Date registrationDeadline = cal.getTime();

        cal.set(2025, Calendar.DECEMBER, 12);
        Date eventDate = cal.getTime();


        // Create mock event data
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Events").document();
        DocumentReference hostRef = FirebaseFirestore.getInstance().collection("Users").document();
        mockHost = mock(DocumentReference.class);
        mockEventRef = mock(DocumentReference.class);
        hostRef.set(new User(
                "host",
                "1234567890",
                "email@email.com",
                "address",
                hostRef.getId(),
                null,
                hostRef
        )).addOnSuccessListener(aVoid -> {
            Event mockEvent = new Event(
                    "Free Tickets to Oilers Game",
                    "Edmonton Stadium",
                    registrationDeadline,
                    "20 Lucky individuals will get a front row seats to the Oilers game against Flames",
                    eventDate,
                    "20:00",
                    "21:00",
                    2,
                    2,
                    mockHost,
                    mockEventRef,
                    null,
                    false
            );

            docRef.set(mockEvent).addOnSuccessListener(aVoid2 -> {
                // Pass event data to fragment
                Bundle bundle = new Bundle();
                bundle.putString("eventDocRefPath", docRef.getPath());

                // Launch fragment with event data
                scenario2 = FragmentScenario.launchInContainer(MainFragment.class, bundle);

                // Verify event details are displayed correctly
                onView(withId(R.id.searchBar)).perform(ViewActions.typeText("Free Tickets to Oilers Game"));
                onView(withId(R.id.searchBar)).perform(closeSoftKeyboard());
                onView(withId(R.id.searchButton)).perform(click());
                onView(withId(R.id.list))
                        .perform(RecyclerViewActions.scrollTo(
                                hasDescendant(withText("Free Tickets to Oilers Game"))
                        ));
                onView(withText("Free Tickets to Oilers Game")).check(matches(isDisplayed()));


                scenario2.close();
            });
        });


    }
}


