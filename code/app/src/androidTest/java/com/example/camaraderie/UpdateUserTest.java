package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static android.app.PendingIntent.getActivity;

import static com.google.common.base.CharMatcher.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.camaraderie.main.MainActivity;
import com.example.camaraderie.updateUserStuff.UpdateUserFragment;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * The Purpose of this test is to test if the UpdateUserFragment is successful.
 * we check if Buttons work and whether user can successfully enter information
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateUserTest {
    private FragmentScenario<UpdateUserFragment> scenario;
    private TestNavHostController navController;


    @Before
    public void setUp() {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
//         If your fragment uses navGraphViewModels(), set the ViewModelStore
        navController.setViewModelStore(new ViewModelStore());
        // Create a fake user so the fragment does not crash
        User fakeUser = new User();
        fakeUser.setFirstName("TestUser");
        fakeUser.setEmail("test@ex.com");
        fakeUser.setPhoneNumber("0000000");
        fakeUser.setAddress("Nowhere");

        // Inject into MainActivity.user
        MainActivity.user = fakeUser;

        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(
                UpdateUserFragment.class,
                null,   // fragment arguments
                R.style.Base_Theme_Camaraderie
        );
        scenario.onFragment(fragment -> {
            NavController mockNav = mock(NavController.class);
            Navigation.setViewNavController(fragment.requireView(), mockNav);
            });


        scenario.moveToState(Lifecycle.State.STARTED);


    }
    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        };
    }



    /**
    *testing if save button is visible
     */
    @Test
    public void testSaveButtonIsVisible() {
        onView(withId(R.id.confirm_button_for_user_profile))
                .check(matches(isDisplayed()));
    }
    /**
     *testing if save button is clickable
     */
    @Test
    public void testSaveButtonIsClickable() {
        onView(withId(R.id.confirm_button_for_user_profile))
                .perform(click());

    }
    /**
     *testing if delete button is visible
     */
    @Test
    public void testDeleteProfileButtonIsVisible() {
        onView(withId(R.id.Delete_button_for_user_profile))
                .check(matches(isDisplayed()));

    }
    /**
     *testing if delete button is clickable
     */
    @Test
    public void testDeleteProfileButtonIsClickable() {
        onView(withId(R.id.Delete_button_for_user_profile))
                .perform(click());

    }


    /**
     *testing if we can successfully update user information
     */
    @Test
    public void testUpdateUser() {

//        onView(withId(R.id.update_name)).perform(click());
//        onView(withId(R.id.update_name)).perform(ViewActions.typeText("Bob"));
//        onView(withId(R.id.update_name)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.update_email)).perform(click());
//        onView(withId(R.id.update_email)).perform(ViewActions.typeText("bobisgreat@gmail.com"));
//        onView(withId(R.id.update_email)).perform(closeSoftKeyboard());
//
//
//        onView(withId(R.id.update_phone_no)).perform(click());
//        onView(withId(R.id.update_phone_no)).perform(ViewActions.typeText("+15879009811"));
//        onView(withId(R.id.update_phone_no)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.update_address)).perform(click());
//        onView(withId(R.id.update_address)).perform(ViewActions.typeText("99 Sooner street"));
//        onView(withId(R.id.update_address)).perform(closeSoftKeyboard());

        onView(withId(R.id.name_field_for_update_user)).perform(replaceText("Bob"), closeSoftKeyboard());
        onView(withId(R.id.email_field_for_update_user)).perform(replaceText("bobisgreat@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.phone_field_for_update_user3)).perform(replaceText("+15879009811"), closeSoftKeyboard());
        onView(withId(R.id.address_field_for_update_user2)).perform(replaceText("99 Sooner street"), closeSoftKeyboard());

        onView(withId(R.id.confirm_button_for_user_profile)).perform(click());
        onView(withText("Profile updated!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

    }

    /**
     * the purpose of this test is to ensure an error is thrown when the user enters an invalid email
     */
    @Test
    public void testInvalidEmail() {
        // Type an invalid email (missing "@")
        onView(withId(R.id.email_field_for_update_user))
                .perform(replaceText("bobisgreatgmail.com"), closeSoftKeyboard());

        // Click save
        onView(withId(R.id.confirm_button_for_user_profile)).perform(click());

        // Check that Alert Error appears
        onView(withText("Please Enter a valid email address")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Type an invalid email (missing ".com")
        onView(withId(R.id.email_field_for_update_user)).perform(replaceText("bobisgreatgmail@"), closeSoftKeyboard());

        // Click save
        onView(withId(R.id.confirm_button_for_user_profile)).perform(click());

        // Check that Alert Error appears
        onView(withText("Please Enter a valid email address")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
    /**
     * This Test is to ensure an Alert error is thrown when the user enters an invalid phone number
     */
    @Test
    public void testInvalidPhoneNumberShowsError() {
        // Type invalid phone (letters or too short)
        onView(withId(R.id.phone_field_for_update_user3)).perform(replaceText("abc123"), closeSoftKeyboard());

        // Try saving
        onView(withId(R.id.confirm_button_for_user_profile)).perform(click());

        // Verify Toast message
        onView(withText("Please enter valid phone number")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
    /**
    * we are testing what happens when user decides to delete their profile
     */
    @Test
    public void DeleteProfileTest() {
        onView(withId(R.id.Delete_button_for_user_profile)).perform(click());
        onView(withText("Are you sure you want to delete your profile?")).check(matches(isDisplayed()));
//        onView(withId(R.id.Positive_button)).perform(click()); //suppose to select yes button
        onView(withText("Profile deleted!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }




}
