package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.List;


public class ProfileCustomer extends CustomerDashboard implements View.OnClickListener {

    TextView fname,lastname,number,address;
    ImageView image;
    Button btnUpdate;
    SharedPreferences prf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Customer Profile");

        fname = findViewById(R.id.cateringname);
        lastname = findViewById(R.id.cateringname2);
        address = findViewById(R.id.addr);
        number = findViewById(R.id.contact);
        btnUpdate = findViewById(R.id.button3);
        image = findViewById(R.id.imageView);
        btnUpdate.setOnClickListener(this);
        String cid = getIntent().getStringExtra("caterings_id");
        String customer_id = prf.getString("id", "");
        String name = prf.getString("cust_name", null);
        String lname = prf.getString("cust_lastname", null);

        try{
            URL url = new URL("http://192.168.43.19/Packaters/index.php/AndroidController/fetch_customer_profile/"+customer_id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("pack_customer");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String cn = item.getString("id");
                String cname = item.getString("cust_name");
                String clname = item.getString("cust_lastname");
                String cnumber = item.getString("cust_phonenum");
                String caddress = item.getString("cust_address");
                String picture = item.getString("path_image");
                //String cc = item.getString("client_contact");
                //String ct = item.getString("client_contact");
//	        	Toast.makeText(getApplicationContext(), cn, Toast.LENGTH_LONG).show();
                fname.setText(cname);
                lastname.setText(clname);
                number.setText(cnumber);
                address.setText(caddress);
                image.setImageBitmap(BitmapFactory.decodeFile(picture));
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

        Intent intent = new Intent(this, ProfileCustomerUpdate.class);
        startActivity(intent);

    }

}
