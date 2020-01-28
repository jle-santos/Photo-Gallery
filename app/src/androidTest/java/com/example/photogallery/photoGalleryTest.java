package com.example.photogallery;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class photoGalleryTest {

    // Create matcher to access element value
    // Source: https://stackoverflow.com/questions/45597008/espresso-get-text-of-element
    private Matcher<View> hasValueEqualTo(final String content) {

        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Has EditText/TextView the value:  " + content);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView) && !(view instanceof EditText)) {
                    return false;
                }
                if (view != null) {
                    String text;
                    if (view instanceof TextView) {
                        text = ((TextView) view).getText().toString();
                        Log.d("hasValueEqualTo", text);
                    } else {
                        text = ((EditText) view).getText().toString();
                        Log.d("hasValueEqualTo", text);
                    }

                    return (text.equalsIgnoreCase(content));
                }
                return false;
            }
        };
    }

    private static final String testCaption = "Test Caption";
    private static final String testSearch = "Test";

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void galleryWorking() throws Exception {

        // Create a caption for the current image
        //onView(withId(R.id.btnCaption)).perform(click());
        //onView(withId(R.id.captionText)).perform(clearText(), closeSoftKeyboard());
        //onView(withId(R.id.captionText)).perform(typeText(testCaption), closeSoftKeyboard());

        // Confirm that the caption now matches the test caption
        //onView(withId(R.id.captionText)).check(matches(hasValueEqualTo(testCaption)));

        // Check the search feature
        onView(withId(R.id.btnSearch)).perform(click());

        // Test the caption search
        onView(withId(R.id.searchCaption)).perform(typeText("bag"), closeSoftKeyboard());
        onView(withId(R.id.btnSearch)).perform(click());

        // Change to a different photo
        onView(withId(R.id.btnPrev)).perform(click());
        onView(withId(R.id.btnNext)).perform(click());

    }
}
