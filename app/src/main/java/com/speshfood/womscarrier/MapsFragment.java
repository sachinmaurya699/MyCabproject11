package com.speshfood.womscarrier;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public
class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private PlacesClient placesClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate (R.layout.fragment_maps,container,false);
        String placesKey="AIzaSyA78ZOlXjR17dzUsI23tTssGnlGDDm4ZDk";
        locationManager = (LocationManager) getActivity ().getSystemService(LOCATION_SERVICE);
        if(!Places.isInitialized ()){
            Places.initialize (getActivity (),placesKey);
        }
        placesClient=Places.createClient (getActivity ());
        final AutocompleteSupportFragment autocompleteSupportFragment=(AutocompleteSupportFragment) getChildFragmentManager ()
                .findFragmentById (R.id.place_autocomplete_fragment);
        autocompleteSupportFragment.setCountry ("IN");
        autocompleteSupportFragment.setPlaceFields (Arrays.asList (Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        if(checkLocationPermission ()){
            autocompleteSupportFragment.setOnPlaceSelectedListener (new PlaceSelectionListener () {
                @Override
                public
                void onPlaceSelected(@NonNull Place place) {
                    Toast.makeText (getActivity (),place.getName (),Toast.LENGTH_LONG).show ();
                    LatLng latLng2=place.getLatLng ();
                    mMap.addMarker(new MarkerOptions().position(latLng2)
                            .title("my"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2,50));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

                }

                @Override
                public
                void onError(@NonNull Status status) {
                    Toast.makeText (getActivity (),status.toString (),Toast.LENGTH_LONG).show ();            }
            });
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager ()
                    .findFragmentById (R.id.map);
            mapFragment.getMapAsync (this);
        }
        return rootView;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public
    void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(getActivity (), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity (), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION+
                        Manifest.permission.ACCESS_COARSE_LOCATION))
                {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 0, 1,
                new LocationListener () {
                    @Override
                    public
                    void onLocationChanged(Location location) {
                        double lat, lng;
                        lat = location.getLatitude();
                        lng = location.getLongitude();

                        Geocoder geocoder = new Geocoder(getActivity (),
                                Locale.getDefault());
                        List<Address> list = null;
                        try {
                            list = geocoder.getFromLocation(lat, lng, 1);

                            LatLng latLng = new LatLng(lat, lng);
                            Toast.makeText(getActivity (), list.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .title(list.get(0).getAddressLine(0)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,50));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            /*CameraPosition position= new  CameraPosition.Builder().
                                    target(latLng).zoom(17).bearing(19).tilt(30).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                            mMap.addMarker(new
                                    MarkerOptions().position(latLng).title("start"));*/

                        } catch (IOException e) {

                            Toast.makeText(getActivity (),e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public
                    void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public
                    void onProviderEnabled(String s) {

                    }

                    @Override
                    public
                    void onProviderDisabled(String s) {

                    }
                });}
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity (),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity (),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity ())
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity (),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity (),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
       // mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng (-34, 151);
       // mMap.addMarker (new MarkerOptions ().position (sydney).title ("Marker in Sydney"));
       // mMap.moveCamera (CameraUpdateFactory.newLatLng (sydney));
       /*@Override
       public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
           super.onRequestPermissionsResult(requestCode, permissions, grantResults);

           if(requestCode==1)
           {
               if(grantResults [0]== PackageManager.PERMISSION_GRANTED)
               {
                   Toast.makeText(this, "gps allow", Toast.LENGTH_SHORT).show();
               }
               else if(grantResults[1]== PackageManager.PERMISSION_GRANTED)
               {
                   Toast.makeText(this, "Network allow", Toast.LENGTH_SHORT).show();
               }
           }
       }*/
       @Override
       public void onRequestPermissionsResult(int requestCode, @NonNull String []permission, @NonNull int[] grantResults) {
//           super.onRequestPermissionsResult(requestCode, permissions, grantResults);
           switch (requestCode) {
               case MY_PERMISSIONS_REQUEST_LOCATION: {
                   // If request is cancelled, the result arrays are empty.
                   if (grantResults.length > 0
                           && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                       // permission was granted, yay! Do the
                       // location-related task you need to do.
                       if (ContextCompat.checkSelfPermission(getActivity (),
                               Manifest.permission.ACCESS_FINE_LOCATION)
                               == PackageManager.PERMISSION_GRANTED) {
                           Toast.makeText(getActivity (), "gps allow", Toast.LENGTH_SHORT).show();
                           //Request location updates:
                          // locationManager.requestLocationUpdates(provider, 400, 1, this);
                       }

                   } else {
                            Toast.makeText (getActivity (),"denied",Toast.LENGTH_LONG).show ();
                       // permission denied, boo! Disable the
                       // functionality that depends on this permission.

                   }
                   return;
               }

           }
       }
    }

