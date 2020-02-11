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
public class AddPoinTestInstrumental {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(AddPointActivity.class);

    @Test
    public void displayEtTitle(){
        onView(withId(R.id.et_title)).check(matches(isDisplayed()));
    }
    @Test
    public void displayTvTitle(){
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }
    @Test
    public void displayEtDescription(){
        onView(withId(R.id.et_description)).check(matches(isDisplayed()));
    }
    @Test
    public void displayTvDescription(){
        onView(withId(R.id.textView2)).check(matches(isDisplayed()));
    }
    @Test
    public void displayBtnSet(){
        onView(withId(R.id.set_new_point)).check(matches(isDisplayed()));
    }
    @Test
    public void displayChooseImage(){
        onView(withId(R.id.btnChooseImage)).check(matches(isDisplayed()));
    }
    @Test
    public void displayImage(){
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }
}


