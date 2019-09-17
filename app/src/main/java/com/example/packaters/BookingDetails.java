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


public class BookingDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    SharedPreferences prf;
    ArrayList<BookingDetailsList> list = new ArrayList<BookingDetailsList>();
    BookingDetailsAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Booking Details");

        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        this.lv = findViewById(R.id.ListView1);
        this.adapter = new BookingDetailsAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        String customer_id = prf.getString("id", "");
        try{
            URL url = new URL("http://192.168.43.19/Packaters/index.php/AndroidController/fetch_booking/"+ customer_id);
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
                String pack_fname = item.getString("customer_fname");
                String pack_address = item.getString("pack_address");
                String pack_name = item.getString("package_name");
                String pack_date = item.getString("pack_date");
                String pack_time = item.getString("pack_time");
                String pack_status = item.getString("status");
                String pack_price = item.getString("price");
                String pestcontrolId = item.getString("pack_transaction_id");
                String cust_id = item.getString("customer_id");
                String CustomerImage = item.getString("path_image");
                list.add(new BookingDetailsList(CustomerImage,pack_fname,pack_address,pack_date,pack_time,pack_price,pack_status,pack_name,pestcontrolId,cust_id));
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
        if (id==R.id.home){
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,CustomerDashboard.class));
        }
        else
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


        BookingDetailsList selectedItem = list.get(position);
        String ID = selectedItem.getId();
        Intent intent = new Intent(this, Payment.class);
        intent.putExtra("transaction_id", ID);
        startActivityForResult(intent, 1);

    }

}