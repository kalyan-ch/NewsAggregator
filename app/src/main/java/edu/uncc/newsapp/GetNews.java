package edu.uncc.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalyan on 2/6/2017.
 */

public class GetNews extends AsyncTask<String, Void, List<Articles>> {
    MainActivity activity;

    public GetNews(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Articles> doInBackground(String... strings) {
        String newsS = strings[0];
        String urlToSend = "https://newsapi.org/v1/articles?apiKey=";
        urlToSend += MainActivity.apiKey+"&source=";
        String source = "";
        switch (newsS){
            case "BBC":
                source = "bbc-news";
                break;
            case "CNN":
                source = "cnn";
                break;
            case "BuzzFeed":
                source = "buzzfeed";
                break;
            case "ESPN":
                source = "espn";
                break;
            case "Sky News":
                source = "sky-news";
                break;
        }

        urlToSend += source;
        Log.d("demo", urlToSend);
        String newsJSONString = "";
        try{
            StringBuilder sb = new StringBuilder();
            URL url1 = new URL(urlToSend);
            HttpURLConnection hconn = (HttpURLConnection) url1.openConnection();
            hconn.setRequestMethod("GET");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(hconn.getInputStream()));
            String line = "";
            while((line = bfr.readLine()) != null){
                sb.append(line);
            }

            newsJSONString = sb.toString();
        }catch(Exception e){
            Log.d("demo", e.getMessage());
        }
        List<Articles> art_list = parseArticles(newsJSONString);
        return art_list;
    }

    @Override
    protected void onPostExecute(List<Articles> articles) {
        activity.setUpNewsData(articles);
        super.onPostExecute(articles);
    }

    public List<Articles> parseArticles(String jsonString){
        List<Articles> article_list = new ArrayList<Articles>();
        if(!"".equals(jsonString)){
            try {
                JSONObject root = new JSONObject(jsonString);
                JSONArray jArr = root.getJSONArray("articles");
                for (int i=0; i<jArr.length();i++){
                    JSONObject jObj = jArr.getJSONObject(i);
                    Articles art = new Articles();
                    art.setAuthor(jObj.getString("author"));
                    art.setTitle(jObj.getString("title"));
                    art.setDescription(jObj.getString("description"));
                    art.setUrlToImage(jObj.getString("urlToImage"));
                    art.setPublishedAt(jObj.getString("publishedAt"));
                    article_list.add(art);
                }
            }catch(Exception e){
                Log.d("demo", e.getMessage());
            }
        }

        return article_list;
    }

    static public interface IDataSet{
        public void setUpNewsData(List<Articles> art);
    }
}