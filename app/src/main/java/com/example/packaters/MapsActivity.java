package com.example.packaters;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    public static final String URL="http://192.168.1.10/GrabMyTrash/retLoc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling

            //buildGoogleApiClient();

        }

        map.setMyLocationEnabled(true);

        //SESSION SA USER LOGIN



//       Toast.makeText(getActivity(), uid, Toast.LENGTH_SHORT).show();

        try{

            //FETCH SA MGA NING REQUEST NGA MGA WASTE PRODUCER

            java.net.URL url = new URL("http://10.0.2.2/packaters/index.php/androidcontroller/get_map");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            //FETCH DATA

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("pack_caterer");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String lat_i = item.getString("lat");
                String long_i = item.getString("longitude");

                //KUTUB ARI

                //KANI NGA IF GI SPECIFIC NI NAKO SA WASTE COLLECTOR NGA TAGA
                //COGON PARA MAKITA ANG EFFECT SA TERRETORIAL LIMITS NGA
                //2KM
//                if(!uid.equals("74")){
//                    //ANG DISTANCE GANI MO LESS THAN UG 2 MA RETRIEVE NIYA ANG MGA
//                    //NING REQUEST
//                    if(d < 2) {
//                        //KUNG ANG STATUS UNCOLLECTED(DB) MO GAWAS ANG MGA MARKER UG ANG NOTIFICATION
//                        if (stat.equals("Uncollected")) {
//
//                            //KATONG MURAG MODAL NGA IMABAW SA MARKER
//
//                            map.addMarker(new MarkerOptions()
//                                    .position(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
//                                    .title(fname + " " + lname)
//                                    .snippet("Distance: " + String.format("%.2f", d) + "KM" + "\nApproximate Time: " + z + "min")
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                            );
//
//                        }
//                    }
//                }
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}
