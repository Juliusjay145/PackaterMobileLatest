package com.example.packaters;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.craftman.cardform.CardForm;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Payment extends CustomerDashboard implements View.OnClickListener {

    ArrayList<BookingDetailsList> list = new ArrayList<>();
    SharedPreferences prf;
    TextView name,txtaddress,txtdate,txttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        CardForm cardForm = findViewById(R.id.cardform);
        TextView txtDes = findViewById(R.id.payment_amount);
        Button btnPay = findViewById(R.id.btn_pay);
        name = findViewById(R.id.textView1);
        txtaddress = findViewById(R.id.textView2);
        txtdate = findViewById(R.id.textView3);
        txttime = findViewById(R.id.textView4);

        String sprice = getIntent().getStringExtra("s_price");
        txtDes.setText(sprice);
        btnPay.setText(String.format("Payer %s", txtDes.getText()));
        btnPay.setOnClickListener(this);

        String ID = getIntent().getStringExtra("transaction_id");
        String customer_id = prf.getString("id", "");

        try{
            URL url = new URL("http://192.168.43.19/Packaters/index.php/AndroidController/fetch_booking/"+customer_id);
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
                String cn = item.getString("customer_id");
                String cadd = item.getString("pack_address");
                String cdate = item.getString("pack_date");
                String ctime = item.getString("pack_time");
                //String cc = item.getString("client_contact");
                //String ct = item.getString("client_contact");
//	        	Toast.makeText(getApplicationContext(), cn, Toast.LENGTH_LONG).show();
                name.setText(cn);
                txtaddress.setText(cadd);
                txtdate.setText(cdate);
                txttime.setText(ctime);
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
        String ID = getIntent().getStringExtra("transaction_id");
        String customer_id = prf.getString("id", "");
        String customer_fname = name.getText().toString();
        String address = txtaddress.getText().toString();
        String date = txtdate.getText().toString();
        String time = txttime.getText().toString();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("customer_id", customer_id));
        nameValuePairs.add(new BasicNameValuePair("customer_fname", customer_fname));
        nameValuePairs.add(new BasicNameValuePair("pack_address", address));
        nameValuePairs.add(new BasicNameValuePair("pack_date", date));
        nameValuePairs.add(new BasicNameValuePair("pack_time", time));
        //nameValuePairs.add(new BasicNameValuePair("price", commercial_price));

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.119/Packaters/index.php/AndroidController/pay/"+ID);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is;
            is=entity.getContent();
            //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, BookingDetails.class);
            startActivity(intent);



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