package edu.uncc.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kalyan on 2/6/2017.
 */

public class GetBitMap extends AsyncTask<String, Void, Bitmap> {
    MainActivity activity;

    public GetBitMap(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try{
            URL url = new URL(strings[0]);
            HttpURLConnection hconn = (HttpURLConnection) url.openConnection();
            Bitmap bmp = BitmapFactory.decodeStream(hconn.getInputStream());
            return bmp;
        }catch (Exception e){
            Log.d("demo", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        activity.setUpImage(bitmap);
        super.onPostExecute(bitmap);
    }

    static public interface IImageSet{
        public void setUpImage(Bitmap bmp);
    }
}