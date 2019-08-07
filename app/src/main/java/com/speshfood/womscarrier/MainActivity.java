package com.speshfood.womscarrier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
            BottomNavigationView bnv;
            FrameLayout fl;
            Boolean statusOfGPS;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        bnv=findViewById (R.id.b1);
        fl=findViewById (R.id.container);
        bnv.setOnNavigationItemSelectedListener (navListener);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!statusOfGPS && isNetworkConnected ()){
            Toast.makeText (getApplicationContext (),"Check internet and location access",Toast.LENGTH_LONG).show ();

        }
        else{
            Toast.makeText (getApplicationContext (),"Good to go",Toast.LENGTH_LONG).show ();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new MapsFragment ()).commit();
        }


    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

  BottomNavigationView.OnNavigationItemSelectedListener navListener=
          new BottomNavigationView.OnNavigationItemSelectedListener () {
              @Override
              public
              boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                  Fragment selectedFragment=null;
                  switch(menuItem.getItemId()){
                      case R.id.map:
                          if(statusOfGPS && isNetworkConnected ()){
                              Toast.makeText (getApplicationContext (),"Good top go",Toast.LENGTH_LONG).show ();
                              selectedFragment=new MapsFragment ();
                          }
                          else{
                              Toast.makeText (getApplicationContext (),"check location and internet access",Toast.LENGTH_LONG).show ();
                          }
                          break;
                      case R.id.order:selectedFragment=new orderFragment ();
                          break;
                      case R.id.payment:selectedFragment= new paymentFragment ();
                          break;
                      case R.id.account:selectedFragment = new accountFragment ();
                  }
                  getSupportFragmentManager().beginTransaction().replace(R.id.container,selectedFragment).commit();
                  return true;
              }

          };
}