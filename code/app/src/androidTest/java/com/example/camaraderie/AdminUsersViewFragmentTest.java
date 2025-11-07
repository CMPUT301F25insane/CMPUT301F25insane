package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.admin_screen.AdminUsersViewFragment;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.not;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
// we are testing Admin View on Users Fragment
public class AdminUsersViewFragmentTest {
    private FragmentScenario<AdminUsersViewFragment> scenario;

    @Before
    public void setUp() {
        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(AdminUsersViewFragment.class);
    }
    @After
    public void tearDown() {
        scenario.close();
    }
    @Test
    public void testBackButtonIsVisible() {
        // Check if the "join" button is visible on screen
        onView(withId(R.id.back_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testBackButtonIsClickable() {
        // Try clicking the "join" button
        onView(withId(R.id.back_button))
                .perform(click());
    }
    @Test
    //test is Admin mode is Displayed
    public void testAdminModeAndListDisplayed() {
        onView(withId(R.id.admin_mode)).check(matches(isDisplayed()));
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }


}
