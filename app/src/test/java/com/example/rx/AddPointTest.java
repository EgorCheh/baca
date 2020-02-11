package com.example.rx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;
import static junit.framework.TestCase.assertEquals;
public class AddPointTest {
    private FirebaseFirestore db;
    private Map<String, Object> pointTest ;
    private Map<String, Object> point = new HashMap<>();

    @Mock
    Context context;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setInstance(){
//        FirebaseApp.initializeApp(context);
        db = Mockito.mock(getInstance().getClass());


        GeoPoint geoPoint = new GeoPoint(56.94913829314743,24.119313828914244);
        point.put("gp", geoPoint);
        point.put("description","descriptionTest");
        point.put("title", "titleTest");
        point.put("image","imageTest");
        point.put("like",0);


        db.collection("points")
                .add(point)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    assert document != null;
                                    if (document.exists()){

                                        pointTest = new HashMap<>();
                                        pointTest.put("gp", document.getData().get("gp"));
                                        pointTest.put("description",document.getData().get("description"));
                                        pointTest.put("title", document.getData().get("title"));
                                        pointTest.put("image",document.getData().get("image"));
                                        pointTest.put("like",document.getData().get("like"));
                                        pointTest.put("like",0);

                                    }}
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error adding document", e);
                    }
                });
    }
    @Test
    public void addpoint(){

        assertEquals(point,pointTest);

    }
    }

