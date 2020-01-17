 package com.example.rx;

        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.ArrayList;
        import java.util.HashMap;

public class Events extends AppCompatActivity {
    private HashMap<String, Object> point;
    private ArrayList<HashMap<String, Object>> listPoints = new ArrayList<>();
    private MySimpleAdapter adapter;
    private FirebaseFirestore db;
    private String TAG="topPoint";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_points);
        db = FirebaseFirestore.getInstance();
        db.collection("points")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                point = new HashMap<>();
                                point.put("title",document.getData().get("title"));
                                point.put("image",document.getData().get("image"));
                                point.put("point",document.getId());
                                listPoints.add(point);
                            }

                            setAdapter();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    private void setAdapter() {
        String[] from = { "title" ,"image"};
        int[] to = { R.id.item_tv_title,R.id.item_image_view};

        adapter = new MySimpleAdapter(this, listPoints, R.layout.item_list_view_points, from, to);
        ListView listView = findViewById(R.id.lv_top_point);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PointsDetails.class);
                intent.putExtra("point",listPoints.get(position).get("point").toString());
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
    }
}

