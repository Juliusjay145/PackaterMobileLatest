package com.example.packaters;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class AddFeedback extends CustomerDashboard implements View.OnClickListener {

    EditText comment;
    Button btnBook;
    InputStream is;
    SharedPreferences prf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        getSupportActionBar().setTitle("Comments and FeedBacks");


        comment = findViewById(R.id.editText2);
        btnBook = findViewById(R.id.book1);
        btnBook.setOnClickListener(this);

        String cid = getIntent().getStringExtra("caterings_id");
        String customer_id = prf.getString("id", "");
        String name = prf.getString("cust_name", null);
        String lname = prf.getString("cust_lastname", null);




    }

    @Override
    public void onClick(View v) {

        String cid = getIntent().getStringExtra("caterings_id");
        String customer_id = prf.getString("id", "");
        String name = prf.getString("cust_name", null);
        String lname = prf.getString("cust_lastname", null);
        String txtcomment = comment.getText().toString();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("comment", txtcomment));
        nameValuePairs.add(new BasicNameValuePair("pack_customer_name", name));
        nameValuePairs.add(new BasicNameValuePair("pack_customer_lname", lname));
        nameValuePairs.add(new BasicNameValuePair("pack_caterer_id", cid));
        nameValuePairs.add(new BasicNameValuePair("pack_customer_id", customer_id));
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.19/Packaters/index.php/AndroidController/comment");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is=entity.getContent();
            Toast.makeText(getApplicationContext(), "Rated Successfully", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, client.class);
//            startActivity(intent);

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
