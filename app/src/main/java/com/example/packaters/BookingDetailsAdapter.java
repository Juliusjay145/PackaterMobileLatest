package com.example.packaters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class BookingDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<BookingDetailsList> list;
    LayoutInflater inflater;

    public BookingDetailsAdapter(Context context,
                                 ArrayList<BookingDetailsList> list) {
        super();
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        BookingDetailsHandler handler = null;
        if(arg1==null)
        {
            arg1 = inflater.inflate(R.layout.booking_details_data, null);
            handler = new BookingDetailsHandler();
            handler.name = arg1.findViewById(R.id.cateringname);
            handler.address =  arg1.findViewById(R.id.addr);
            handler.date =  arg1.findViewById(R.id.contact);
            handler.time =  arg1.findViewById(R.id.details);
            handler.price =  arg1.findViewById(R.id.price);
            handler.status =  arg1.findViewById(R.id.status);
            handler.packname = arg1.findViewById(R.id.packages);
            handler.image =  arg1.findViewById(R.id.imageView);
            arg1.setTag(handler);
        }else

            handler=(BookingDetailsHandler) arg1.getTag();
            Bitmap bm  = BitmapFactory.decodeFile(list.get(arg0).getImage());
            handler.name.setText(list.get(arg0).getName());
            handler.address.setText(list.get(arg0).getAddress());
            handler.date.setText(list.get(arg0).getDate());
            handler.time.setText(list.get(arg0).getTime());
            handler.price.setText(list.get(arg0).getPrice());
            handler.status.setText(list.get(arg0).getRstatus());
            handler.packname.setText(list.get(arg0).getPname());
            handler.image.setImageBitmap(bm);



        return arg1;
    }

    static Bitmap getBitmapFromURL(String src)
    {
        try{
            URL url = new URL(src);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream is = con.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(is);
            Log.e("Bitmap", "returned");
            return myBitmap;
        }catch(IOException e){
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    static class BookingDetailsHandler
    {
        TextView name,packname,address,date,time,price,status;
        ImageView image;
    }












}
