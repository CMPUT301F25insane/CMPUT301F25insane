package com.example.camaraderie;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.contrib.RecyclerViewActions;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;

import com.example.camaraderie.dashboard.MainFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import java.util.Date;

/**
 *The purpose of this is to Test if we can find Mock event in the search results in main fragment
 */
public class SearchEventInMainFragmentTest {
    private FragmentScenario<MainFragment> scenario2;
    @Test
    public void SearchEventInMainFragment() {
        // Create mock event data
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Events").document();
        DocumentReference hostRef = FirebaseFirestore.getInstance().collection("Users").document();
        hostRef.set(new User(
                "host",
                "1234567890",
                "email@email.com",
                "address",
                hostRef.getId(),
                hostRef
        )).addOnSuccessListener(aVoid -> {
            Event mockEvent = new Event("Free Tickets to Oilers Game",
                    "Edmonton Stadium",
                    new Date(),
                    "20 Lucky individuals will get a front row seats to the Oilers game against Flames",
                    new Date(),
                    "20:00",
                    100,
                    hostRef,
                    docRef,
                    docRef.getId());

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


