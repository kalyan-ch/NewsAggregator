package edu.uncc.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GetBitMap.IImageSet, GetNews.IDataSet{
    final static String apiKey = "5399bda5ae4842da838d8a024f15ac3a";
    List<Articles> artList = null;
    int masterIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void getNews(View view){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && (ni.isConnected())){
            Spinner sr = (Spinner)findViewById(R.id.newsSource);
            String newsSource = sr.getSelectedItem().toString();
            (new GetNews(this)).execute(newsSource);
        }else{
            return;
        }
    }


    @Override
    public void setUpImage(Bitmap bmp) {
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        iv.setImageBitmap(bmp);
    }

    @Override
    public void setUpNewsData(List<Articles> art) {
        artList = art;
        if(artList != null){
            Articles firstArt = artList.get(0);
            (new GetBitMap(this)).execute(firstArt.getUrlToImage());
            setDataInView(firstArt);
        }else{
            Toast.makeText(this, "Some Problem", Toast.LENGTH_SHORT).show();
        }


    }

    private void setDataInView(Articles art){
        TextView tv1 = new TextView(this);
        tv1.setText(art.getTitle());

        TextView tv2 = new TextView(this);
        tv2.setText("Author: "+art.getAuthor());

        TextView tv3 = new TextView(this);
        String string = art.getPublishedAt();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date date1 = null;
        try{
            date1 = format.parse(string);
        }catch(Exception e){
            e.printStackTrace();
        }
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        String date = "";
        try{
            date = format.format(date1);
        }catch(Exception e){
            e.printStackTrace();
        }

        tv3.setText("Published on: "+date);

        TextView tv4 = new TextView(this);
        tv4.setText("Description: "+art.getDescription());

        LinearLayout ll = (LinearLayout)findViewById(R.id.displayLayout);
        ll.removeAllViews();
        ll.addView(tv1);
        ll.addView(tv2);
        ll.addView(tv3);
        ll.addView(tv4);
    }

    public void Finish(View view){
        finish();
    }

    public void firstButton(View view){
        if(artList != null) {
            Articles firstArt = artList.get(0);
            (new GetBitMap(this)).execute(firstArt.getUrlToImage());
            setDataInView(firstArt);
            masterIndex = 0;
        }
    }

    public void prevButton(View view){
        if(artList != null) {
            if(masterIndex > 0){
                masterIndex--;
                Articles firstArt = artList.get(masterIndex);
                (new GetBitMap(this)).execute(firstArt.getUrlToImage());
                setDataInView(firstArt);
            }

        }
    }

    public void nextButton(View view){
        if(artList != null) {
            if(masterIndex < artList.size()-1){
                masterIndex++;
                Articles firstArt = artList.get(masterIndex);
                (new GetBitMap(this)).execute(firstArt.getUrlToImage());
                setDataInView(firstArt);
            }

        }
    }

    public void lastButton(View view){
        if(artList != null) {
            Articles firstArt = artList.get(artList.size()-1);
            (new GetBitMap(this)).execute(firstArt.getUrlToImage());
            setDataInView(firstArt);
            masterIndex = artList.size()-1;
        }
    }


}
