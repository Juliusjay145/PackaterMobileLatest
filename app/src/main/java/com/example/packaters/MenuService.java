package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

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


public class MenuService extends CustomerDashboard implements View.OnClickListener {


    ArrayList<MenuServiceList> list = new ArrayList<>();
    MenuServiceAdapter adapter;
    SharedPreferences prf;
    GridView gv;
    Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.menu);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        gv = findViewById(R.id.gridView1);
        btnBook = findViewById(R.id.button3);
        btnBook.setOnClickListener(this);
        this.adapter = new MenuServiceAdapter(this, list);
        gv.setAdapter(adapter);
        String sID = getIntent().getStringExtra("serve_id");
        String cID = getIntent().getStringExtra("c_id");
        String sName = getIntent().getStringExtra("s_name");
        String sPrice = getIntent().getStringExtra("s_price");

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_service_menu/"+ sID+"/"+cID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("pack_service_menu");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String serv_name = item.getString("menu_name");
                String serv_price = item.getString("menu_description");
                String serv_id = item.getString("id");
                String ServiceImage = item.getString("path_image");
                String pest = item.getString("pack_caterer_id");
                list.add(new MenuServiceList(ServiceImage,serv_id,serv_name,serv_price,pest));
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
        String sID = getIntent().getStringExtra("serve_id");
        String cID = getIntent().getStringExtra("c_id");
        String sName = getIntent().getStringExtra("s_name");
        String sPrice = getIntent().getStringExtra("s_price");
        Intent intent = new Intent(this,BookingTransaction.class);
        intent.putExtra("serv_id", sID);
        intent.putExtra("caterings_id", cID);
        intent.putExtra("serv_name", sName);
        intent.putExtra("serv_price", sPrice);
        startActivityForResult(intent, 1);




    }
}
