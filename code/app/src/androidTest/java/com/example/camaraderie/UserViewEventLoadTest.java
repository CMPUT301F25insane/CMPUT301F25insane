package com.example.camaraderie;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Bundle;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.camaraderie.event_screen.CreateEventFragment;
import com.example.camaraderie.event_screen.UserViewEventFragment;

import java.io.Serializable;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserViewEventLoadTest {
    private FragmentScenario<UserViewEventFragment> scenario2;

    @Test
    public void userCanViewCreatedEvent() {
        // Create mock event data
        Event mockEvent = new Event("Free Tickets to Oilers Game", "Edmonton Stadium", "2025-12-12", "20 Lucky individuals will get a front row seats to the Oilers game against Flames",  "2025-12-12","20:00", 100);


        // Pass event data to fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", mockEvent);

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
    }
}


}




