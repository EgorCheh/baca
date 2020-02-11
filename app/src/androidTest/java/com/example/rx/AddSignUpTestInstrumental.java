package com.example.rx;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class AddSignUpTestInstrumental {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void displayEtMail(){
        onView(withId(R.id.et_mail)).check(matches(isDisplayed()));
    }
    @Test
    public void displayEtName(){
        onView(withId(R.id.et_name)).check(matches(isDisplayed()));
    }
    @Test
    public void displayEtPassword(){
        onView(withId(R.id.et_password)).check(matches(isDisplayed()));
    }
    @Test
    public void displayEtPassword2(){
        onView(withId(R.id.et_password2)).check(matches(isDisplayed()));
    }
    @Test
    public void displayBtnSet(){
        onView(withId(R.id.btn_set_new_user)).check(matches(isDisplayed()));
    }

}


