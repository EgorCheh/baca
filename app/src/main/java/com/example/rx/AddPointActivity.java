package com.example.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class AddPointActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        final EditText etTitle = findViewById(R.id.et_title);
        final EditText etDescription = findViewById(R.id.et_description);
        Button btnSetPoint = findViewById(R.id.set_new_point);
        db = FirebaseFirestore.getInstance();





        btnSetPoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Map<String, Object> point = new HashMap<>();
                GeoPoint geoPoint = new GeoPoint(56.5645,24.0623);
                String description = etDescription.getText().toString();
                String title = etTitle.getText().toString();
                point.put("gp", geoPoint);
                point.put("description",description);
                point.put("title", title);




                db.collection("points")
                        .add(point)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firebase", "Error adding document", e);
                            }
                        });
            }
        });
    }
}
