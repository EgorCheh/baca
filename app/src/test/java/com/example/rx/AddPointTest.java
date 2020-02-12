package com.example.rx;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;



public class AddPointTest  {
    private FirebaseFirestore db;
    private Map<String, Object> pointTest;
    private Map<String, Object> point = new HashMap<>();
    private Map<String, Object> point2 = new HashMap<>();
    @Mock
    Context context;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setInstance() {


        GeoPoint geoPoint = new GeoPoint(56.94913829314743, 24.119313828914244);
        point.put("gp", geoPoint);
        point.put("description", "descriptionTest");
        point.put("title", "titleTest");
        point.put("image", "imageTest");
        point.put("like", 0);


        GeoPoint geoPoint2 = new GeoPoint(56.94913829314743, 24.119313828914244);
        point2.put("gp", geoPoint2);
        point2.put("description", "descriptionTest");
        point2.put("title", "titleTest");
        point2.put("image", "imageTest");
        point2.put("like", 0);

        TestCase.assertEquals(point,point2);

    }


}

