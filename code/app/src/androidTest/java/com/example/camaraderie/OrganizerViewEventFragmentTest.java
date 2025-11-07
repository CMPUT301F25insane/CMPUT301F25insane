package com.example.camaraderie;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.CreateEventFragment;
import com.example.camaraderie.event_screen.OrganizerViewEventFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerViewEventFragmentTest {
    private FragmentScenario<OrganizerViewEventFragment> scenario;

    @Before
    public void setUp() {
        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(OrganizerViewEventFragment.class);
    }
    @After
    public void tearDown() {
        scenario.close();
    }


    // we are testing if the Edit event button is visible on the main screen
    @Test
    public void editEventButtonVisible() {
        onView(withId(R.id.event_edit_button_ord_view))
                .check(matches(isDisplayed()));
    }

    //testing if Edit event Button if Clickable
    @Test
    public void editEventButtonClickable() {
        onView(withId(R.id.event_edit_button_ord_view))
                .perform(click());
    }

    // we are testing if the Edit event button is visible on the main screen
    @Test
    public void deleteButtonVisible() {
        onView(withId(R.id.delete_button_org_view))
                .check(matches(isDisplayed()));
    }

    //testing if Edit event Button if Clickable
    @Test
    public void deleteButtonClickable() {
        onView(withId(R.id.delete_button_org_view))
                .perform(click());
    }

    @Test
    public void testQrButtonOrgIsDisplayed() {
        // Check if the "unjoin" button is visible on screen
        onView(withId(R.id.qr_button_org_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testQrButtonOrgIsClickable() {
        // Check if the "QR" button is clickable
        onView(withId(R.id.qr_button_org_view))
                .perform(click());
    }


    @Test
    public void viewAttendeesButtonIsDisplayed() {
        // Check if the "unjoin" button is visible on screen
        onView(withId(R.id.view_attendees_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void viewAttendeesButtonIsClickable(){
        // Check if the "QR" button is clickable
        onView(withId(R.id.view_attendees_button))
                .perform(click());
    }

    @Test
    public void viewPhotoButtonIsDisplayed() {
        // Check if the "unjoin" button is visible on screen
        onView(withId(R.id.view_photos_org_view))
                .check(matches(isDisplayed()));
    }

    @Test
    // Check if the "QR" button is clickable
    public void viewPhotoButtonIsClickable(){
        onView(withId(R.id.view_photos_org_view))
                .perform(click());
    }

    @Test
    //Test Event Info is properly displayed
    public void testEventDetailsAreDisplayed() {
        onView(withId(R.id.event_name_for_org_view)).check(matches(isDisplayed()));
        onView(withId(R.id.orgEventViewEventDate)).check(matches(isDisplayed()));
        onView(withId(R.id.registration_deadline_text_org_view)).check(matches(isDisplayed()));
        onView(withId(R.id.host_name_org_view)).check(matches(isDisplayed()));
        onView(withId(R.id.name_of_useranizer)).check(matches(isDisplayed()));
    }

}


