package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.camaraderie.admin_screen.AdminDashboardFragment;
import com.example.camaraderie.main.MainActivity;
import com.example.camaraderie.updateUserStuff.UpdateUserFragment;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.google.common.base.CharMatcher.is;
import static com.google.common.base.Predicates.instanceOf;
import static org.hamcrest.Matchers.not;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * we are testing if AdminDashboard is working well
 * if the Admin is able to switch to User database, Event database and Image database respectively with the possibility
 * of switch back to Admin Dashboard
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminDashboardFragmentTest {
    //this rule allows us to bypass the notification dialog
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);
//    private FragmentScenario<AdminDashboardFragment> scenario;
    @Before
    public void setUp() {
        User fakeUser = new User();
        fakeUser.setFirstName("TestUser");
        fakeUser.setEmail("test@ex.com");
        fakeUser.setPhoneNumber("0000000");
        fakeUser.setAddress("Nowhere");
//        AdminDashboardFragment.TEST_MODE = true;

        // Inject into MainActivity.user and make sure they are admin
        MainActivity.user = fakeUser;
        fakeUser.setAdmin(true);


        // Create a TestNavHostController
//        TestNavHostController navController = new TestNavHostController(
//                ApplicationProvider.getApplicationContext());

        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<AdminDashboardFragment> scenario = FragmentScenario.launchInContainer(AdminDashboardFragment.class);

        // Launch the fragment in a container just like in a real Activity
//        scenario = FragmentScenario.launchInContainer(AdminDashboardFragment.class);

        scenario = FragmentScenario.launchInContainer(
                AdminDashboardFragment.class,
                null,   // fragment arguments
                R.style.Base_Theme_Camaraderie
        );
//        scenario.moveToState(Lifecycle.State.STARTED);
         scenario.onFragment(fragment -> {
             // Set the graph on the TestNavHostController
             navController.setGraph(R.navigation.nav_graph);
//             navController.setCurrentDestination(R.id.admin_main_screen);
             // Make the NavController available via the findNavController() APIs
             Navigation.setViewNavController(fragment.requireView(), navController);
         });

    }
//    @After
//    public void tearDown() {
//        if (scenario != null) {
//            scenario.close();
//        };
//    }
    /**
     * we are testing if User database button is visible on the main screen
     */
    @Test
    public void AdminSeeUserButtonIsVisible () {
        onView(withId(R.id.admin_see_users)).check(matches(isDisplayed()));
    }
    /**
     * we are testing if User database button is clickable on the main screen
     */
    @Test
    public void AdminSeeUserButtonClickable() {
        onView(withId(R.id.admin_see_users)).perform(click());
    }


    /**
     * we are testing if Event database button is visible on the main screen
     */
    @Test
    public void AdminEventDatabaseButtonIsVisible () {
        onView(withId(R.id.admin_see_events)).check(matches(isDisplayed()));
    }


    /**
     * we are testing if Event database button is clickable on the main screen
     */
    @Test
    public void AdminEventDatabaseButtonClickable() {
        onView(withId(R.id.admin_see_events)).perform(click());
    }

    /**
     * we are testing if Image database button is visible on the main screen
     */
    @Test
    public void AdminImageDatabaseButtonIsVisible () {
        onView(withId(R.id.admin_see_pics)).check(matches(isDisplayed()));
    }

    /**
     * we are testing if Image database button is clickable on the main screen
     */
    @Test
    public void AdminImageDatabaseButtonIsClickable() {
        onView(withId(R.id.admin_see_pics)).perform(click());
    }

    /**
     * we are testing if back button for Admin is visible on the main screen
     */
    @Test
    public void AdminBackButtonIsVisible () {
        onView(withId(R.id.back)).check(matches(isDisplayed()));
    }
    /**
     * we are testing if Image database button for Admin is clickable on the main screen
     */

    @Test
    public void AdminBackButtonIsClickable() {
        onView(withId(R.id.back)).perform(click());
    }
    /**
     * test is Admin mode is Displayed
     */
    @Test
    public void testAdminModeAndListDisplayed() {
        onView(withId(R.id.admin_mode)).check(matches(isDisplayed()));
    }

    /**
     * Check whether the activity correctly switched to View User in Admin perspective and then we switch back to Admin Dashboard
     */
    @Test
    public void testActivitySwitchAdminDashboardToViewAdminUser() {
        onView(withId(R.id.admin_see_users)).perform(click());
        // Checking that we are in admin_users_view screen
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).check(matches(isDisplayed()));

        //Now we switch back to Admin Dashboard
        onView(withId(R.id.back_button)).perform(click());

        // Checking that we are in Admin Dashboard screen
        onView(withId(R.id.admin_mode)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_see_users)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_see_events)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_see_pics)).check(matches(isDisplayed()));
        onView(withId(R.id.back)).check(matches(isDisplayed()));
    }

    /**
     * Check whether the activity correctly switched to View Events in Admin perspective and then we switch back to Admin Dashboard
     */
    @Test
    public void testActivitySwitchAdminDashboardToViewAdminEvents() {
        onView(withId(R.id.admin_see_events)).perform(click());
        // Checking that we are in admin_users_view screen
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).check(matches(isDisplayed()));

        //Now we switch back to Admin Dashboard
        onView(withId(R.id.back_button)).perform(click());

        // Checking that we are in Admin Dashboard screen
        onView(withId(R.id.admin_mode)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_see_users)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_see_events)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_see_pics)).check(matches(isDisplayed()));
        onView(withId(R.id.back)).check(matches(isDisplayed()));
    }

}
