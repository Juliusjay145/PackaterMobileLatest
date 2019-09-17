package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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


public class Service_profile extends CustomerDashboard implements View.OnClickListener {

    ListView lv;
    SharedPreferences prf;
    ArrayList<ServiceProfileList> list = new ArrayList<>();
    ServiceProfileAdapter adapter;
    Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Profile Services");

        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        lv = findViewById(R.id.ListView1);
        this.adapter = new ServiceProfileAdapter(this, list);
        lv.setAdapter(adapter);
        btnMenu = findViewById(R.id.button3);
        btnMenu.setOnClickListener(this);
        String ID = getIntent().getStringExtra("service_id");
        String sname = getIntent().getStringExtra("serve_name");
        String Catering_ID = getIntent().getStringExtra("cate_id");
        String service_price = getIntent().getStringExtra("serve_price");
        //Toast.makeText(getApplicationContext(), Catering_ID, Toast.LENGTH_SHORT).show();

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_service_profile/"+ ID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("pack_service");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String serv_name = item.getString("service_name");
                String serv_price = item.getString("service_price");
                String serv_desc = item.getString("service_description");
                String serv_id = item.getString("id");
                String ServiceImage = item.getString("path_image");
                String pest = item.getString("pack_caterer_id");
                list.add(new ServiceProfileList(ServiceImage,serv_id,serv_name,serv_desc,serv_price,pest));
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


    @Override
    public void onClick(View v) {

        String ID = getIntent().getStringExtra("service_id");
        String sname = getIntent().getStringExtra("serve_name");
        String Catering_ID = getIntent().getStringExtra("service_id");
        String service_price = getIntent().getStringExtra("serve_price");
        String cat = getIntent().getStringExtra("cate_id");
        Intent intent = new Intent(this, MenuService.class);
        intent.putExtra("serve_id", ID);
        intent.putExtra("c_id", Catering_ID);
        intent.putExtra("s_name", sname);
        intent.putExtra("s_price", service_price);
        intent.putExtra("ca_id", cat);
        startActivityForResult(intent, 1);
    }
}