package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


public class CustomerDashboardPlatinum extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lv;
    SharedPreferences prf;
    ArrayList<CateringList> list = new ArrayList<CateringList>();
    CompanyAdapter adapter;
    Button btnPremium;
    Button btnGold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.customer_dashboard_platinum);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        btnPremium = findViewById(R.id.prem);
        btnGold = findViewById(R.id.gold);
        btnPremium.setOnClickListener(this);
        btnGold.setOnClickListener(this);
        this.lv = findViewById(R.id.ListView1);
        this.adapter = new CompanyAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_caterer_platinum/platinum");
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
        if (id==R.id.settings){
            Toast.makeText(this, "Details", Toast.LENGTH_SHORT).show();
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

       if(v==btnGold){
           Intent intent = new Intent(this, CustomerDashboardGold.class);
           startActivity(intent);
       }


    }
}