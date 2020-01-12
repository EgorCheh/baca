package com.example.rx;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopPointsDetails extends AppCompatActivity {
    private FirebaseFirestore db;
    private String TAG="MyLog";
    private MySimpleAdapter adapter;
    private HashMap<String, Object> comment;
    private ArrayList<HashMap<String, Object>> listComments = new ArrayList<>();
    private String userName;
    private Profile profile;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String pointId;
    private GeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_points_details);
        Intent intent = getIntent();
        final TextView tvTitle,tvDescription;
        final ImageView imageView;
        tvDescription=findViewById(R.id.tv_description_detail);
        tvTitle=findViewById(R.id.tv_title_detail);
        imageView=findViewById(R.id.imageView_detail);


        pointId = intent.getStringExtra("point");
        final EditText etComment=findViewById(R.id.detail_et_comment);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        profile= Profile.getCurrentProfile();

        getUserInfo();
        setAdapter();
        Button btnRout=findViewById(R.id.btn_to_rout);
        btnRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 db.collection("points").document(pointId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if (task.isSuccessful()) {
                             DocumentSnapshot document = task.getResult();

                                  geoPoint = (GeoPoint) document.getData().get("gp");
                                 Log.d(TAG,"gp==  "+geoPoint);
                             String lat = String.valueOf(geoPoint.getLatitude());
                             String lon = String.valueOf(geoPoint.getLongitude());
                             Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                     Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lon));
                             startActivity(intent);
                         }
                     }
                 });

            }
        });
        Button btn = findViewById(R.id.detail_btn_set_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> updateMap = new HashMap();
                updateMap.put("user",userName);
                updateMap.put("comment",etComment.getText().toString());
                updateMap.put("date", Timestamp.now());

                db.collection("points").document(pointId)
                        .collection("comments")
                        .add(updateMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(getApplicationContext(),"super",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        DocumentReference docRef = db.collection("points").document(pointId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: ");
                        tvDescription.setText(document.getData().get("description").toString());
                        tvTitle.setText(document.getData().get("title").toString());

                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                        mStorageRef.child(document.getData().get("image").toString()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(imageView);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }

    private void setAdapter() {


        db.collection("points").document(pointId).collection("comments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        comment = new HashMap<>();
                        comment.put("comment",document.getData().get("comment"));
                        comment.put("user",document.getData().get("user"));
                        Date date = (Date) document.getData().get("date");


                        comment.put("date","45");
                        listComments.add(comment);
                    }

                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });




        String[] from = { "comment","user","date" };
        int[] to = { R.id.detail_et_comment,R.id.detail_iv_profile,R.id.item_tv_date_comment};

        adapter = new MySimpleAdapter(this, listComments, R.layout.item_list_view_comment, from, to);
        ListView listView = findViewById(R.id.lv_comment);
        listView.setAdapter(adapter);
    }
    private void getUserInfo(){

        if(profile!=null)
        {
            ImageView ivUser = findViewById(R.id.detail_iv_profile);
            userName = profile.getFirstName();
            Picasso.get().load(profile.getProfilePictureUri(20,20)).into(ivUser);
        }else {
            ImageView ivUser = findViewById(R.id.detail_iv_profile);
            userName = currentUser.getEmail();
            Picasso.get().load(R.drawable.ic_person_pin).into(ivUser);
        }

    }
}
