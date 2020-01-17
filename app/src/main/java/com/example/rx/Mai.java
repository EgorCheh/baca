package com.example.rx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import static com.example.rx.MainActivity.getBitmapFromVectorDrawable;

public class Mai extends Activity implements UserLocationObjectListener {

    private final String MAPKIT_API_KEY = "f1b53a01-68ff-4b27-9d15-0f1738c860b3";

    private MapView mapView;
    private UserLocationLayer userLocationLayer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.mapview);

        db = FirebaseFirestore.getInstance();
        addPoints();
        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(new CameraPosition(new Point(0, 0), 14, 0, 0));
        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        //userLocationLayer.setHeadingEnabled(true);


        userLocationLayer.setObjectListener(this);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.ic_person_pin));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        userLocationView.getAccuracyCircle().setFillColor(Color.LTGRAY);
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    private void addPoints(){
        db.collection("points")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firebase", document.getId() + " => " + document.getData().get("gp"));
                                GeoPoint geoPoint = (GeoPoint) document.getData().get("gp");

                                PlacemarkMapObject newObj = mapView.getMap().getMapObjects().addPlacemark(
                                        new Point(geoPoint.getLatitude(), geoPoint.getLongitude()),
                                        ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_person_pin)));
                                        newObj.setUserData(document.getId());
                                        newObj.addTapListener(new MapObjectTapListener() {
                                            @Override
                                            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                                                Log.d("wtf","   "+mapObject.getUserData());


                                                Intent intent = new Intent(getApplicationContext(), PointsDetails.class);
                                                intent.putExtra("point",mapObject.getUserData().toString());
                                                startActivity(intent);
                                                return false;
                                            }
                                        });
                            }
                        } else {
                            Log.w("Firebase", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
