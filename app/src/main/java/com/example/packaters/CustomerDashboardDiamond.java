package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;


public class CustomerDashboardDiamond extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lv;
    SharedPreferences prf;
    ArrayList<CateringList> list = new ArrayList<CateringList>();
    CompanyAdapter adapter;
    Button btnPremium, btnPlatinum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Diamond Catering Services");
        setContentView(R.layout.customer_dashboard_diamond);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        btnPlatinum = findViewById(R.id.platinum);
        btnPremium = findViewById(R.id.prem);
        btnPremium.setOnClickListener(this);
        btnPlatinum.setOnClickListener(this);
        this.lv = findViewById(R.id.ListView1);
        this.adapter = new CompanyAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        String customer_id = prf.getString("id", "");

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_caterer/diamond");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("pack_caterer");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String pestcontrol_name = item.getString("cat_name");
                String pestcontrolId = item.getString("id");
                String CompanyImage = item.getString("path_image");
                list.add(new CateringList(CompanyImage,pestcontrolId,pestcontrol_name));
                adapter.notifyDataSetChanged();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_booking/"+customer_id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("pack_transaction");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String status = item.getString("status");

                if(status.equals("Confirm"))
                {
                    AlertDialog dialog = new AlertDialog.Builder(this).create();
                    dialog.setMessage("Your booking request has been" + status);
                    dialog.setTitle("Notification");
                    dialog.show();
                }

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    //menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commonmenus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.payment){
            Toast.makeText(this, "Payments", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ProfileCustomer.class));
        }
        else
        if (id==R.id.details){
            Toast.makeText(this, "Booking Details", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,BookingDetails.class));
        }
        else
        if (id==R.id.logout){
            startActivity(new Intent(this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
//    end of menu


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CateringList selectedItem = list.get(position);
        String ID = selectedItem.getId();
        Intent intent = new Intent(this, CateringProfile.class);
        intent.putExtra("catering_id", ID);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onClick(View v) {

        if(v==btnPremium){
            Intent intent = new Intent(this, CustomerDashboard.class);
            startActivity(intent);
        }

        if(v==btnPlatinum){
            Intent intent = new Intent(this, CustomerDashboardPlatinum.class);
            startActivity(intent);
        }


    }
}