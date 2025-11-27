package com.example.camaraderie;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;

import com.example.camaraderie.event_screen.user_view.UserViewEventFragment;
/**
 * The purpose of this test is to test when the user to join and unjoin an event.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserJoinUnjoinEventTest {

    private FragmentScenario<UserViewEventFragment> scenario;

    @Before
    public void setUp() {
        scenario = FragmentScenario.launchInContainer(UserViewEventFragment.class);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void testUserJoinsAndUnjoinsEvent() {
        // Initially: Join button visible, Unjoin button hidden
        onView(withId(R.id.join_button_user_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.unjoin_button_user_view))
                .check(matches(not(isDisplayed())));

        // Step 1: Click Join button
        onView(withId(R.id.join_button_user_view)).perform(click());

        // Step 2: Verify Join button hidden, Unjoin button now visible
        onView(withId(R.id.join_button_user_view))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.unjoin_button_user_view))
                .check(matches(isDisplayed()));

        // Step 3: Click Unjoin button
        onView(withId(R.id.unjoin_button_user_view)).perform(click());

        // Step 4: Verify Unjoin hidden again, Join visible again
        onView(withId(R.id.unjoin_button_user_view))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.join_button_user_view))
                .check(matches(isDisplayed()));
    }
}
