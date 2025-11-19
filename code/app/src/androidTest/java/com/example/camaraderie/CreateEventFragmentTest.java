package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.widget.DatePicker;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.CreateEventFragment;
import com.example.camaraderie.event_screen.UserViewEventFragment;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * We are testing when the organizer is creating an event. If all buttons
 * are clickable and if the organizer can create event with all the details
 */
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
    /**
     * We are testing if the Create event button is visible on the main screen
     */
    public void testCreateEventButtonVisible() {
        onView(withId(R.id.button_for_confirm))
                .check(matches(isDisplayed()));
    }


    /**
     * We are testing if the Create event button is clickable on the main screen
     */
    @Test
    public void testCreateEventButtonClickable() {
        onView(withId(R.id.button_for_confirm))
                .perform(click());
    }
    /**
    we are creating the event by entering the event details as a Organizer and pressing confirm button
     */
    @Test
    //
    public void testEventDetails() {

        onView(withId(R.id.input_field_for_create_event_name)).perform(click());
        onView(withId(R.id.input_field_for_create_event_name)).perform(ViewActions.typeText("Free Tickets to Oilers Game"));
        onView(withId(R.id.input_field_for_create_event_name)).perform(closeSoftKeyboard());

        onView(withId(R.id.input_field_for_create_event_date)).perform(click());
        onView(withId(R.id.input_field_for_create_event_registration_deadline)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2025, 2, 15));
        onView(withId(R.id.input_field_for_create_event_date)).perform(closeSoftKeyboard());

        onView(withId(R.id.input_field_for_create_event_registration_deadline)).perform(click());
        onView(withId(R.id.input_field_for_create_event_registration_deadline)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2025, 2, 15));
        onView(withId(R.id.input_field_for_create_event_registration_deadline)).perform(closeSoftKeyboard());

        onView(withId(R.id.input_field_for_create_event_description)).perform(click());
        onView(withId(R.id.input_field_for_create_event_description)).perform(ViewActions.typeText("20 Lucky individuals will get a front row seats to the oilers game aganist flames"));
        onView(withId(R.id.input_field_for_create_event_description)).perform(closeSoftKeyboard());

        onView(withId(R.id.input_field_for_create_event_location)).perform(click());
        onView(withId(R.id.input_field_for_create_event_location)).perform(ViewActions.typeText("Edmonton Stadium"));
        onView(withId(R.id.input_field_for_create_event_location)).perform(closeSoftKeyboard());

        onView(withId(R.id.input_field_for_create_event_num_of_attendees)).perform(click());
        onView(withId(R.id.input_field_for_create_event_num_of_attendees)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.input_field_for_create_event_num_of_attendees)).perform(closeSoftKeyboard());

        onView(withId(R.id.createEventConfirmButton)).perform(click());
    }

    /**
     * Testing if error appear in the event a Organizer does not input anything or leaves blank field
     */
    @Test
    public void testEmptyFormShowsError() {
        onView(withId(R.id.createEventConfirmButton)).perform(click());
        onView(withText("Please fill in all fields")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

}