package com.example.packaters;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private Uri filepath;
    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String UPLOAD_URL = "http://192.168.43.118/washmycar/index.php/androidcontroller/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regsiter_customer);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        name = findViewById(R.id.editText31);
        lastname = findViewById(R.id.editText32);
        phonenumber = findViewById(R.id.editText33);
        address = findViewById(R.id.editText34);
        username = findViewById(R.id.editText35);
        password = findViewById(R.id.editText36);
        profile = findViewById(R.id.image);
        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
        profile.setOnClickListener(this);
        requestStoragePermission();
    }

    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

//       if(resultCode==RESULT_OK)
//       {
//           String filename1 = data.getData().getPath();
//           Uri uriImage = data.getData();
//           profile.setImageURI(uriImage);
//           filename.setText(filename1);
//       }

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                profile.setImageBitmap(bitmap);
            } catch (Exception ex) {

            }
        }

//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource("path/to/folder");
//        File file = new File(url.toURI());

    }

    private String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }



    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.image:

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 100);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }

        switch (id){
            case R.id.register:
                prf = getSharedPreferences("user_details",MODE_PRIVATE);
                String picture = getPath(filepath);
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
                    HttpPost httpPost = new HttpPost("http://192.168.43.19/Packaters/index.php/AndroidController/register_customer");
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



}
