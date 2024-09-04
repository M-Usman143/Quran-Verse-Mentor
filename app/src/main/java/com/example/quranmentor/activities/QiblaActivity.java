package com.example.quranmentor.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quranmentor.R;

public class QiblaActivity extends AppCompatActivity implements SensorEventListener, LocationListener{
    private ImageView compassImageView;
    private TextView qiblaDirectionTextView;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Location currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private float currentDegree = 0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);
        compassImageView = findViewById(R.id.compassImageView);
        qiblaDirectionTextView = findViewById(R.id.qiblaDirectionTextView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check for location permissions and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        if (currentLocation != null) {
            float qiblaDegree = calculateQiblaDirection(currentLocation);
            qiblaDirectionTextView.setText("Your Qibla Direction is : " + qiblaDegree + "°");
            compassImageView.setRotation(-degree + qiblaDegree);
        } else {
            compassImageView.setRotation(-degree);
        }

        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // Not used
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // Not used
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not used
    }

    private float calculateQiblaDirection(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        double qiblaLatitude = 21.4225; // Qibla's latitude
        double qiblaLongitude = 39.8262; // Qibla's longitude

        double phiK = Math.toRadians(qiblaLatitude);
        double lambdaK = Math.toRadians(qiblaLongitude);
        double phi = Math.toRadians(latitude);
        double lambda = Math.toRadians(longitude);

        double qiblaDirection = Math.toDegrees(Math.atan2(Math.sin(lambdaK - lambda), Math.cos(phi) * Math.tan(phiK) - Math.sin(phi) * Math.cos(lambdaK - lambda)));

        return (float) ((qiblaDirection + 360) % 360);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
            }
        }
    }
}
