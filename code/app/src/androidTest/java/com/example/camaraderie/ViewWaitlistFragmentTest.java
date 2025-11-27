package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.user_lists.waitlist_or_selected.ViewWaitlistOrSelectedFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewWaitlistFragmentTest {

    private FragmentScenario<ViewWaitlistOrSelectedFragment> scenario;

    @Before
    public void setUp() {
        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(ViewWaitlistOrSelectedFragment.class);
    }
    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void waitlistTest(){
        onView(withId(R.id.usersInWaitlist)).check(matches(isDisplayed()));
    }



}
