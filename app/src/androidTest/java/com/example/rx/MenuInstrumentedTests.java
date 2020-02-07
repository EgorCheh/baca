package com.example.rx;

import android.support.test.rule.ActivityTestRule;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Rule;
import org.junit.Test;

import io.grpc.internal.Instrumented;
import io.grpc.internal.LogId;

public class MenuInstrumentedTests  {
    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(Menu.class);

    @Test
    public void MenuTest()
    {

    }
}
