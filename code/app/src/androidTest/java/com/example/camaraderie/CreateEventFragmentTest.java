package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.CreateEventFragment;
import com.example.camaraderie.event_screen.UserViewEventFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventFragmentTest {

    private FragmentScenario<CreateEventFragment> scenario;

    @Before
    public void setUp() {
        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(CreateEventFragment.class);
    }
    @After
    public void tearDown() {
        scenario.close();
    }



    @Test
    // we are testing if the Create event button is visible on the main screen
    public void testCreateEventButtonVisible() {
        onView(withId(R.id.createEventConfirmButton))
                .check(matches(isDisplayed()));
    }

    @Test
    //testing if create event Button if Clickable
    public void testCreateEventButtonClickable() {
        onView(withId(R.id.createEventConfirmButton))
                .perform(click());
    }
    @Test
    // we are creating the event by entering the event details as a Organizer and pressing confirm button
    public void testEventDetails() {

        onView(withId(R.id.createEventName)).perform(click());
        onView(withId(R.id.createEventName)).perform(ViewActions.typeText("Free Tickets to Oilers Game"));
        onView(withId(R.id.createEventName)).perform(closeSoftKeyboard());

        onView(withId(R.id.createEventDate)).perform(click());
        onView(withId(R.id.createEventDate)).perform(ViewActions.typeText("2025-12-12"));
        onView(withId(R.id.createEventDate)).perform(closeSoftKeyboard());


        onView(withId(R.id.createEventDeadline)).perform(click());
        onView(withId(R.id.createEventDeadline)).perform(ViewActions.typeText("2025-11-30"));
        onView(withId(R.id.createEventDeadline)).perform(closeSoftKeyboard());

        onView(withId(R.id.createEventDescription)).perform(click());
        onView(withId(R.id.createEventDescription)).perform(ViewActions.typeText("20 Lucky individuals will get a front row seats to the oilers game aganist flames"));
        onView(withId(R.id.createEventDescription)).perform(closeSoftKeyboard());

        onView(withId(R.id.createEventLocation)).perform(click());
        onView(withId(R.id.createEventLocation)).perform(ViewActions.typeText("Edmonton Stadium"));
        onView(withId(R.id.createEventLocation)).perform(closeSoftKeyboard());

        onView(withId(R.id.createEventCapacity)).perform(click());
        onView(withId(R.id.createEventCapacity)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.createEventCapacity)).perform(closeSoftKeyboard());

        onView(withId(R.id.createEventConfirmButton)).perform(click());


    }
    @Test
    //Testing if error appear in the event a Organizer does not input anything or leaves blank field
    public void testEmptyFormShowsError() {
        onView(withId(R.id.createEventConfirmButton)).perform(click());
        onView(withText("Please fill in all fields"))
                .check(matches(isDisplayed()));
    }

}