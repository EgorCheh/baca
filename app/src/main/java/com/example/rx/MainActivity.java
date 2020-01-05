package com.example.rx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;


public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private ImageButton buttonAdd;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(getApplicationContext(),TopPoints.class));
        MapKitFactory.setApiKey("f1b53a01-68ff-4b27-9d15-0f1738c860b3");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapview);
        buttonAdd = findViewById(R.id.btn_add_point);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddPointActivity.class));
            }
        });
        db = FirebaseFirestore.getInstance();



       /* LocationManager locationManager = MapKitFactory.getInstance().createLocationManager();
        locationManager.requestSingleUpdate(new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                mapview.getMap().move(
                        new CameraPosition(new Point(location.getPosition().getLatitude(), location.getPosition().getLongitude()), 18f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0),
                        null);
                mapview.getMap().getMapObjects().addPlacemark(
                        new Point(location.getPosition().getLatitude(), location.getPosition().getLongitude()),
                        ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(),R.drawable.ic_person_pin))
                );

            }
            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        })*/;
     /*   db.collection("points")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firebase", document.getId() + " => " + document.getData().get("gp"));
                                GeoPoint geoPoint = (GeoPoint) document.getData().get("gp");

                                mapview.getMap().getMapObjects().addPlacemark(
                                        new Point(geoPoint.getLatitude(),geoPoint.getLongitude()),
                                        ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(),R.drawable.ic_person_pin)));
                            }
                        } else {
                            Log.w("Firebase", "Error getting documents.", task.getException());
                        }
                    }
                });*/



    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            assert drawable != null;
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

 }
