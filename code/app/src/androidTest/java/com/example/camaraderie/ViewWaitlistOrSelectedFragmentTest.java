package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.camaraderie.event_screen.user_lists.waitlist_or_selected.ViewWaitlistOrSelectedFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewWaitlistOrSelectedFragmentTest {
    private DocumentReference mockHost;
    private FragmentScenario<ViewWaitlistOrSelectedFragment> scenario2;
    private DocumentReference mockEventRef;
    private final Calendar cal = Calendar.getInstance();
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS);

    @Test
    public void navigateToWaitlist_andCheckIsVisible() {
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
                scenario2 = FragmentScenario.launchInContainer(ViewWaitlistOrSelectedFragment.class, bundle);
                // Verify User in waitlist are displayed correctly
                onView(withId(R.id.usersInWaitlist)).check(matches(isDisplayed()));
                scenario2.close();
            });
        });
    }
}





