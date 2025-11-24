package com.example.camaraderie;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import com.example.camaraderie.qr_code.QRCodeDialogFragment;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QRCodeDialogFragmentTest {

    @Test
    public void isQRGeneratedTest() {

        QRCodeDialogFragment fragment = new QRCodeDialogFragment().newInstance("123");

        FragmentScenario<QRCodeDialogFragment> scenario = FragmentScenario.launchInContainer(QRCodeDialogFragment.class, fragment.getArguments(), R.style.Theme_Camaraderie);

        onView(withId(R.id.qrImageView)).check(matches(isDisplayed()));

        onView(withId(R.id.back_button)).check(matches(withText("Close")));
    }

}