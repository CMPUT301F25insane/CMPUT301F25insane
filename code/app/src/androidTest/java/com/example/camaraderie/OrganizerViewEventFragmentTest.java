package com.example.camaraderie;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.organizer_view.OrganizerViewEventFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * We are testing the Organizer View Event Fragment. We want to ensure that the organizer is able
 * to edit and delete Events
 */
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


    /**
     *we are testing if the Edit event button is visible on the main screen
     */
    @Test
    public void editEventButtonVisible() {
        onView(withId(R.id.event_edit_button_ord_view))
                .check(matches(isDisplayed()));
    }

    /**
    *testing if Edit event Button if Clickable
     */
    @Test
    public void editEventButtonClickable() {
        onView(withId(R.id.event_edit_button_ord_view))
                .perform(click());
    }

    /**
     *we are testing if the Edit event button is visible on the main screen
     */
    @Test
    public void deleteButtonVisible() {
        onView(withId(R.id.delete_button_org_view))
                .check(matches(isDisplayed()));
    }

    /**
    *  testing if Edit event Button if Clickable
     */
    @Test
    public void deleteButtonClickable() {
        onView(withId(R.id.delete_button_org_view))
                .perform(click());
    }

    /**
    *testing if the QR button is visible on the main screen
     */
    @Test
    public void testQrButtonOrgIsDisplayed() {

        onView(withId(R.id.qr_button_org_view))
                .check(matches(isDisplayed()));
    }

    /**
     *Check if the "QR" button is clickable
     */
    @Test
    public void testQrButtonOrgIsClickable() {
        onView(withId(R.id.qr_button_org_view))
                .perform(click());
    }

    /**
     *Check if the view attendees button is visible on screen
     */
    @Test
    public void viewAttendeesButtonIsDisplayed() {
        //
        onView(withId(R.id.view_attendees_button))
                .check(matches(isDisplayed()));
    }

    /**
     *Check if the view attendees button is clickable on screen
     */
    @Test
    public void viewAttendeesButtonIsClickable(){
        onView(withId(R.id.view_attendees_button))
                .perform(click());
    }

    /**
     *Check if the view photo button is visible on screen
     */
    @Test
    public void viewPhotoButtonIsDisplayed() {
        onView(withId(R.id.view_photos_org_view))
                .check(matches(isDisplayed()));
    }

    /**
     *Check if the view photo button is clickable on screen
     */
    @Test
    public void viewPhotoButtonIsClickable(){
        onView(withId(R.id.view_photos_org_view))
                .perform(click());
    }

    /**
     *Test Event Info is properly displayed
     */
    @Test
    public void testEventDetailsAreDisplayed() {
        onView(withId(R.id.event_name_for_org_view)).check(matches(isDisplayed()));
        onView(withId(R.id.orgEventViewEventDate)).check(matches(isDisplayed()));
        onView(withId(R.id.registration_deadline_text_org_view)).check(matches(isDisplayed()));
        onView(withId(R.id.host_name_org_view)).check(matches(isDisplayed()));
        onView(withId(R.id.name_of_useranizer)).check(matches(isDisplayed()));
    }

}


