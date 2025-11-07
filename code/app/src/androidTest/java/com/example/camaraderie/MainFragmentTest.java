package com.example.camaraderie;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.camaraderie.dashboard.MainFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
/**
 * This Test File, we will be looking at testing MainFragment
 * and testing if search functionality works
 */

@RunWith(AndroidJUnit4.class)
@LargeTest

public class MainFragmentTest {
    private FragmentScenario<MainFragment> scenario;

    @Before
    public void setUp() {
        // Launch the fragment in a container just like in a real Activity
        scenario = FragmentScenario.launchInContainer(MainFragment.class);
    }
    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    // we are testing if Search button is visible on the main screen
    public void SearchButtonIsVisible () {
        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
    }

    @Test
    // we are testing if Search button is clickable on the main screen
    public void SearchButtonClickable() {
        onView(withId(R.id.searchButton)).perform(click());
    }

    @Test
    public void searchTest(){
        onView(withId(R.id.searchBar)).perform(ViewActions.typeText("Oilers"));
        onView(withId(R.id.searchButton)).perform(click());



    }


}

