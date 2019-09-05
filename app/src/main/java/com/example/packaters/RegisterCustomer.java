package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RegisterCustomer extends CustomerDashboard implements View.OnClickListener, OnItemClickListener {

    ImageView profile;
    InputStream is;
    Button btnRegister;
    EditText name,lastname,phonenumber,address,username,password;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regsiter_customer);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        name = findViewById(R.id.packagename);
        lastname = findViewById(R.id.name);
        phonenumber = findViewById(R.id.editText3);
        address = findViewById(R.id.editText4);
        username = findViewById(R.id.editText5);
        password = findViewById(R.id.editText6);
        profile = findViewById(R.id.image);
        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
        profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.image:

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
                break;
        }

        switch (id){
            case R.id.register:
                prf = getSharedPreferences("user_details",MODE_PRIVATE);
                String picture = prf.getString("uriImage", null);
                String c_name = name.getText().toString();
                String c_lastname = lastname.getText().toString();
                String c_number = phonenumber.getText().toString();
                String c_address = address.getText().toString();
                String c_username = username.getText().toString();
                String c_password = password.getText().toString();


                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("cust_name", c_name));
                nameValuePairs.add(new BasicNameValuePair("cust_lastname", c_lastname));
                nameValuePairs.add(new BasicNameValuePair("cust_phonenum", c_number));
                nameValuePairs.add(new BasicNameValuePair("cust_address", c_address));
                nameValuePairs.add(new BasicNameValuePair("username", c_username));
                nameValuePairs.add(new BasicNameValuePair("password", c_password));
                nameValuePairs.add(new BasicNameValuePair("path_image", picture));
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://192.168.43.19/packaters/index.php/AndroidController/bookCater");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is=entity.getContent();
                    Toast.makeText(getApplicationContext(), "Booking Transaction Success", Toast.LENGTH_SHORT).show();
                    String sp = getIntent().getStringExtra("serv_price");
                    Intent intent = new Intent(this, BookingDetails.class);
                    intent.putExtra("s_price", sp);
                    startActivityForResult(intent, 1);
                    //			txtname.setText("");
                    //			address.setText(caddress);
                    //			txtcontact.setText("");
                    //			txtusername.setText("");
                    //			txtpassword.setText("");


                }
                catch(ClientProtocolException e)
                {
                    Log.e("ClientProtocol","Log_tag");
                    e.printStackTrace();
                }
                catch(IOException e)
                {
                    Log.e("Log_tag", "IOException");
                    e.printStackTrace();
                }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            Uri uriImage = data.getData();
            profile.setImageURI(uriImage);

    }


}
