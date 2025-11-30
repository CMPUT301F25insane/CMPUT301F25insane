package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.not;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.event_screen.user_view.UserViewEventFragment;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserViewEventTest {

    private FragmentScenario<UserViewEventFragment> scenario;

    private TestNavHostController navController;

    @Before
    public void setUp() throws Exception {
        // 1. Create test Firestore data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference hostRef = db.collection("Users").document();
        User host = new User("host", "1234567890", "email@email.com", "address", hostRef.getId(), null, hostRef);
        Tasks.await(hostRef.set(host));

        DocumentReference docRef = db.collection("Events").document();
        Event mockEvent = new Event(
                "Free Tickets to Oilers Game",
                "Edmonton Stadium",
                new Date(),
                "20 Lucky individuals will get a front row seats",
                new Date(),
                "20:00",
                100,
                -1,
                hostRef,
                docRef,
                docRef.getId(),
                null,
                false
        );
        Tasks.await(docRef.set(mockEvent));

        // 2. Prepare bundle for fragment
        Bundle bundle = new Bundle();
        bundle.putString("eventDocRefPath", docRef.getPath());

        // 3. Launch fragment
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        scenario = FragmentScenario.launchInContainer(UserViewEventFragment.class, bundle);

        // 4. Attach TestNavHostController immediately
        scenario.onFragment(fragment -> {
            // 2. Set the graph and make the NavController available to the fragment
            navController.setGraph(R.navigation.nav_graph); // Replace with your nav graph
            Navigation.setViewNavController(fragment.requireView(), navController);


        });
    }

    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        }
    }

    // ---------- BUTTON TESTS ----------

    @Test
    public void testJoinButtonDisplayed() {
        onView(withId(R.id.join_button_user_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testJoinButtonIsClickable() {
        onView(withId(R.id.join_button_user_view)).perform(click());
    }

    @Test
    public void testUnjoinButtonDisplayed() {
        onView(withId(R.id.unjoin_button_user_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUnjoinButtonIsClickable() {
        onView(withId(R.id.unjoin_button_user_view)).perform(click());
    }

    @Test
    public void testQrButtonUserIsDisplayed() {
        onView(withId(R.id.qr_button_user_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testQrButtonUserIsClickable() {
        onView(withId(R.id.qr_button_user_view)).perform(click());
    }

    @Test
    public void viewAttendeesButtonIsDisplayed() {
        onView(withId(R.id.view_lists_button)).check(matches(isDisplayed()));
    }

    @Test
    public void viewAttendeesButtonIsClickable() {
        onView(withId(R.id.view_lists_button)).perform(click());
    }

    @Test
    public void viewPhotoButtonIsDisplayed() {
        onView(withId(R.id.view_photos_user_view)).check(matches(isDisplayed()));
    }

    @Test
    public void viewPhotoButtonIsClickable() {
        onView(withId(R.id.view_photos_user_view)).perform(click());
    }

    @Test
    public void testJoinButtonTogglesToUnjoin() {
        onView(withId(R.id.join_button_user_view)).perform(click());
        onView(withId(R.id.unjoin_button_user_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testOnlyOneJoinButtonVisibleAtTime() {
        onView(withId(R.id.join_button_user_view)).perform(click());
        onView(withId(R.id.unjoin_button_user_view)).check(matches(isDisplayed()));
        onView(withId(R.id.join_button_user_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testNoEditButtonInUserView() {
        onView(withId(R.id.delete_button_org_view)).check(doesNotExist());
    }

    @Test
    public void testNoDeleteButtonInUserView() {
        onView(withId(R.id.delete_button_org_view)).check(doesNotExist());
    }

}
