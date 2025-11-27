package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.organizer_view.CreateEventFragment;
import com.example.camaraderie.updateUserStuff.GuidelinesFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GuidelinesFragmentTest {

    private FragmentScenario<GuidelinesFragment> scenario;


    @Before
    public void setUp() {
        scenario = FragmentScenario.launchInContainer(GuidelinesFragment.class);

    }
    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        }
    }

    @Test
    public void testGuidelinesVisible(){
        onView(withId(R.id.guidelinestextview)).check(matches(isDisplayed()));
    }

    @Test
    public void tesHeaderVisible(){
        onView(withId(R.id.header_above_guidelines)).check(matches(isDisplayed()));
    }

}
