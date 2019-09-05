package com.example.packaters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class ProfileCustomerUpdate extends CustomerDashboard implements View.OnClickListener {

    EditText fname,number,address;
    Button btnUpdate;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile_update);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        fname = findViewById(R.id.editText1);
        address = findViewById(R.id.editText2);
        number = findViewById(R.id.editText3);
        btnUpdate = findViewById(R.id.book1);
        btnUpdate.setOnClickListener(this);
        String customer_id = prf.getString("id", "");

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
                //String cc = item.getString("client_contact");
                //String ct = item.getString("client_contact");
//	        	Toast.makeText(getApplicationContext(), cn, Toast.LENGTH_LONG).show();
                fname.setText(cname);
                number.setText(cnumber);
                address.setText(caddress);
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

        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_id = prf.getString("id", "");

        String customer_fname = fname.getText().toString();
        String txtaddress = address.getText().toString();
        String txtnumber = number.getText().toString();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("cust_name", customer_fname));
        nameValuePairs.add(new BasicNameValuePair("cust_address", txtaddress));
        nameValuePairs.add(new BasicNameValuePair("cust_phonenum", txtnumber));
        //nameValuePairs.add(new BasicNameValuePair("price", commercial_price));

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.19/Packaters/index.php/AndroidController/update_profile/"+customer_id);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is;
            is=entity.getContent();
            Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProfileCustomer.class);
            startActivity(intent);

//sample

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
