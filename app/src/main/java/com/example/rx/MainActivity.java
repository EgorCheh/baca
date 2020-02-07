package com.example.rx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;


public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {
    private MapView mapView;
    private FirebaseFirestore db;
    private UserLocationLayer userLocationLayer;
    private CameraListener cameraListener = new CameraListener() {
        @Override
        public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateSource cameraUpdateSource, boolean b) {
           /*  Log.d("wtf","log: "+cameraPosition.getTarget().getLongitude()+
                                "   lat"+cameraPosition.getTarget().getLatitude());*/
             cameraCurrentPoint.setGeometry(cameraPosition.getTarget());

        }
    };
    private PlacemarkMapObject cameraCurrentPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("f1b53a01-68ff-4b27-9d15-0f1738c860b3");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
       // startActivity(new Intent(getApplicationContext(),Mai.class));
        mapView = findViewById(R.id.mapview);

        db = FirebaseFirestore.getInstance();

        addPoints();
        addEvents();
        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        //userLocationLayer.setHeadingEnabled(true);


        userLocationLayer.setObjectListener(this);

        Log.d("wtf","log: "+ mapView.getMap().getCameraPosition().getTarget().getLatitude());
        Point cameraPoint = new Point(
                mapView.getMap().getCameraPosition().getTarget().getLatitude(),
                mapView.getMap().getCameraPosition().getTarget().getLongitude()
        );
        cameraCurrentPoint = mapView.getMap().getMapObjects().addPlacemark(cameraPoint,
                ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(),
                        R.drawable.ic_pin_drop)),
                new IconStyle().
                        setAnchor(new PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f));


        mapView.getMap().addCameraListener(cameraListener);
        /*Log.d("wtf","log: "+cameraPosition.getTarget().getLongitude()+
                                "   lat"+cameraPosition.getTarget().getLatitude());*/
        Button addPoint = findViewById(R.id.btn_toAddPoint);
        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddPointActivity.class);
                intent.putExtra("getLatitude",mapView.getMap().getCameraPosition().getTarget().getLatitude());
                intent.putExtra("getLongitude",mapView.getMap().getCameraPosition().getTarget().getLongitude());
                startActivity(intent);
            }
        });
        Button addEvent = findViewById(R.id.btn_toAddEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddEventActivity.class);
                intent.putExtra("getLatitude",mapView.getMap().getCameraPosition().getTarget().getLatitude());
                intent.putExtra("getLongitude",mapView.getMap().getCameraPosition().getTarget().getLongitude());
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        MapKitFactory.getInstance().onStop();

        mapView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();


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
                                        ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_point)));
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
    private void addEvents(){
        db.collection("events")
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
                                        ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_event)));
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


