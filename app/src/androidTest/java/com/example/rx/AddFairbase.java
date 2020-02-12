package com.example.rx;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.admin.v1beta1.FirestoreAdminGrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AddFairbase {

    @Test
    public void initFire()
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("points").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
            }
        });
    }

}
