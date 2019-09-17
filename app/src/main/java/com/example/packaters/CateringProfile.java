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


public class CateringProfile extends CustomerDashboard implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView lv;
    SharedPreferences prf;
    ArrayList<CateringProfileList> list = new ArrayList<>();
    CateringProfileAdapter adapter;
    Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_catering);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Catering Profile");

        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        this.lv = findViewById(R.id.listView1);
        this.adapter = new CateringProfileAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        btnBook = findViewById(R.id.button2);
        btnBook.setOnClickListener(this);
        String ID = getIntent().getStringExtra("catering_id");
        Toast.makeText(getApplicationContext(), ID, Toast.LENGTH_SHORT).show();

        try{
            URL url = new URL("http://192.168.43.19/packaters/index.php/AndroidController/fetch_catering_provider/"+ID);
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
                String pestcontrol_address = item.getString("cat_address");
                String pestcontrol_contact = item.getString("cat_contactno");
                String pestcontrol_detail = item.getString("cat_details");
                String pestcontrolId = item.getString("id");
                String CompanyImage = item.getString("path_image");
                list.add(new CateringProfileList(CompanyImage,pestcontrolId,pestcontrol_name,pestcontrol_address,pestcontrol_contact,pestcontrol_detail));
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

        String ID = getIntent().getStringExtra("catering_id");
        Intent intent = new Intent(this, Service.class);
        intent.putExtra("cater_id", ID);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CateringProfileList selectedItem = list.get(position);
        String ID = selectedItem.getId();
        Intent intent = new Intent(this, AddFeedback.class);
        intent.putExtra("caterings_id", ID);
        startActivityForResult(intent, 1);
    }

    //menus
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.commonmenus,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id==R.id.home){
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this,CustomerDashboard.class));
//        }
//        else
//        if (id==R.id.payment){
//            Toast.makeText(this, "Payments", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this,ProfileCustomer.class));
//        }
//        else
//        if (id==R.id.details){
//            Toast.makeText(this, "Booking Details", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this,BookingDetails.class));
//        }
////        else
////        if (id==R.id.map){
////            Toast.makeText(this, "Details", Toast.LENGTH_SHORT).show();
////            startActivity(new Intent(this,MapsActivity.class));
////        }
//        else
//        if (id==R.id.logout){
//            startActivity(new Intent(this,MainActivity.class));
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
////    end of menu
}
