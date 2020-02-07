package com.example.rx;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MenuUITest {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(Menu.class);

    @Test
    public void clickButtonMap(){
        onView(withId(R.id.btn_map)).perform(click());
        onView(withId(R.id.mapview)).check(matches(isDisplayed()));
    }
    @Test
    public void clickButtonPoints(){
        onView(withId(R.id.btn_points)).perform(click());
        onView(withId(R.id.lv_top_point)).check(matches(isDisplayed()));
    }
    @Test
    public void clickButtonEvents(){
        onView(withId(R.id.btn_points)).perform(click());
        onView(withId(R.id.lv_top_point)).check(matches(isDisplayed()));
    }
    @Test
    public void clickButtonLogout(){
        onView(withId(R.id.log_out)).perform(click());
        onView(withId(R.id.btn_signIn)).check(matches(isDisplayed()));
    }
}
