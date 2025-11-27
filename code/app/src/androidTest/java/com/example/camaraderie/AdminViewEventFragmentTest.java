package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.admin_screen.AdminEventsViewFragment;
import com.example.camaraderie.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * We are testing that Admin can view event description without any errors.
 * incomplete
 */
public class AdminViewEventFragmentTest {
    private FragmentScenario<AdminEventsViewFragment> scenario;
    @Before
    public void setUp() {
        // 1. Create mock admin user
        User adminUser = new User(
                "Admin",
                "1234567890",
                "admin@example.com",
                "Somewhere",
                "adminID",
                "token",
                null
        );

        adminUser.setAdmin(true);
        MainActivity.user = adminUser;

        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(AdminEventsViewFragment.class);
    }
    @After
    public void tearDown() {
        scenario.close();
    }
    /**
     * We are testing that Admin can view event description and all buttons are displayed
     */
    @Test
    public void testAdminCanViewEventDescription() {
        onView(withId(R.id.event_description_user_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.qr_button_user_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.fragment_list_testing_interface))
                .check(matches(isDisplayed()));
        onView(withId(R.id.view_photos_user_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.unjoin_button_user_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adminDeleteEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.join_button_user_view))
                .check(matches(isDisplayed()));

    }



    }
