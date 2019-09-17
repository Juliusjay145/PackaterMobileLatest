package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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


public class Service extends CustomerDashboard implements AdapterView.OnItemClickListener {

    SharedPreferences prf;
    ArrayList<CateringServiceList> list = new ArrayList<>();
    ServiceAdapter adapter;
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("List of Services");

        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        gv = findViewById(R.id.gridView1);
        this.adapter = new ServiceAdapter(this, list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
        String ID = getIntent().getStringExtra("cater_id");
        Toast.makeText(getApplicationContext(), ID, Toast.LENGTH_SHORT).show();

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_service/"+ ID);
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
                String serv_id = item.getString("id");
                String ServiceImage = item.getString("path_image");
                String pest = item.getString("pack_caterer_id");
                list.add(new CateringServiceList(ServiceImage,serv_id,serv_name,serv_price,pest));
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        CateringServiceList selectedItem = list.get(position);
        String s_id = selectedItem.getId();
        String cater_id = selectedItem.getCaterid();
        String service_name = selectedItem.getName();
        String service_price = selectedItem.getPrice();
        Intent intent = new Intent(this, Service_profile.class);
        intent.putExtra("service_id", s_id);
        intent.putExtra("cate_id", cater_id);
        intent.putExtra("serve_name", service_name);
        intent.putExtra("serve_price", service_price);
        startActivityForResult(intent, 1);

    }
}
