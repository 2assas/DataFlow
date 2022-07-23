package com.example.dataflow.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.function.Consumer;

import static android.content.Context.LOCATION_SERVICE;

public class SingleShotLocationProvider {

    public static interface LocationCallback {
        public void onNewLocationAvailable(GPSCoordinates location) throws IOException;
    }
    public static void requestCurrentLocation(final Context context, final LocationCallback callback) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            Log.e("checkCurrentLoc", "netWork");
            Criteria criteria = new Criteria();
            Consumer<Location> consumer;
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 22);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationManager.requestSingleUpdate(criteria, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                try {
                                    callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }
                            @Override
                            public void onProviderEnabled(String provider) {
                            }
                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        }, null);
//                    }
                Log.e("checkGps", "here");
                }
            }


        } else {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Log.e("checkCurrentLoc", "Gps");
                Criteria criteria = new Criteria();
                Consumer<Location> consumer;
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                    locationManager.requestSingleUpdate(criteria, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            try {
                                callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    }, null);
//                }
            }
            }
    }

    public static class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLatitude, float theLongitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }

}