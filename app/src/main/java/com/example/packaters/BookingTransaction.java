package com.example.packaters;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BookingTransaction extends CustomerDashboard implements View.OnClickListener {

    EditText package_name,address,date,time;
    Button btnBook;
    InputStream is;
    SharedPreferences prf;
    DateFormat format=DateFormat.getDateInstance();
    Calendar calendar=Calendar.getInstance();
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    int currentMinute = calendar.get(Calendar.MINUTE);
    TimePickerDialog timePickerDialog;
    String amPm;
    int year;
    int month;
    int dayOfMonth;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy mm:hh a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);

        package_name = findViewById(R.id.packagename);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        address = findViewById(R.id.name);
        date = findViewById(R.id.editText3);
        time = findViewById(R.id.editText4);
        btnBook = findViewById(R.id.book1);
        btnBook.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        String sID = getIntent().getStringExtra("serv_id");
        String cid = getIntent().getStringExtra("caterings_id");
        String sname = getIntent().getStringExtra("serv_name");
        String sprice = getIntent().getStringExtra("serv_price");
        String customer_id = prf.getString("id", "");
        String name = prf.getString("cust_name", null);
        String lname = prf.getString("cust_lastname", null);
        String picture = prf.getString("path_image", null);




        try{
            URL url = new URL("http://192.168.43.119/packaters/index.php/AndroidController/fetch_customer_profile/"+customer_id);
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
                String cd = item.getString("cust_address");
                package_name.setText(sname);
                address.setText(cd);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, arg1);
            calendar.set(Calendar.MONTH, arg2);
            calendar.set(Calendar.DAY_OF_MONTH, arg3);
            date.setText(format.format(calendar.getTime()));
        }
    };



    @Override
    public void onClick(View v) {


        timePickerDialog = new TimePickerDialog(BookingTransaction.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm;
//	        	if (hourOfDay >= 12) {
//	        	        amPm = "PM";
//	        	    } else {
//	        	        amPm = "AM";
//	        	    }
                if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    amPm = "PM";
                } else if (hourOfDay == 0) {
                    hourOfDay += 12;
                    amPm = "AM";
                } else if (hourOfDay == 12){
                    amPm = "PM";
                }else{
                    amPm = "AM";
                }
                calendar.set(Time.HOUR, hourOfDay);
                calendar.set(Time.MINUTE, minutes);
                time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
            }
        }, currentHour, currentMinute, false);






        switch (v.getId()){
            case R.id.book1:
                String sID = getIntent().getStringExtra("serv_id");
                String cid = getIntent().getStringExtra("caterings_id");
                String sprice = getIntent().getStringExtra("serv_price");
                String customers_id = prf.getString("id", "");

                String p_name = package_name.getText().toString();
                String p_address = address.getText().toString();
                String p_date = date.getText().toString();
                String p_time = time.getText().toString();

                prf = getSharedPreferences("user_details",MODE_PRIVATE);
                String name = prf.getString("cust_name", null);
                String lname = prf.getString("cust_lastname", null);
                String picture = prf.getString("path_image", null);

                if(p_date.equals("") || p_time.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(calendar.getTime().before(new Date()))
                {
                    Toast.makeText(getApplicationContext(), "Invalid Past Date", Toast.LENGTH_SHORT).show();
                }

                else{

                    List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                    nameValuePairs.add(new BasicNameValuePair("package_name", p_name));
                    nameValuePairs.add(new BasicNameValuePair("package_address", p_address));
                    nameValuePairs.add(new BasicNameValuePair("p_date", p_date));
                    nameValuePairs.add(new BasicNameValuePair("p_time", p_time));
                    nameValuePairs.add(new BasicNameValuePair("pack_caterer_id", cid));
                    nameValuePairs.add(new BasicNameValuePair("customer_id", customers_id));
                    nameValuePairs.add(new BasicNameValuePair("customer_fname", name));
                    nameValuePairs.add(new BasicNameValuePair("customer_lname", lname));
                    nameValuePairs.add(new BasicNameValuePair("path_image", picture));
                    nameValuePairs.add(new BasicNameValuePair("package_id", sID));
                    nameValuePairs.add(new BasicNameValuePair("price", sprice));
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



                break;
            case R.id.editText3:
                new DatePickerDialog(this,listener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.editText4:
                timePickerDialog.show();
                break;

        }


    }
}