package com.example.jsonparser;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FlickrAPI {
    MainActivity mainActivity;
    static final String BaseURL = "https://api.flickr.com/services/rest";
    static final String APIKEY = "9eeb68ac9ac984c525bb0fde4f3ec576";
    static final String Tag = "WebServicesFunTag";

    public FlickrAPI(MainActivity MainActivity){
        this.mainActivity = MainActivity;
    }
    public String fetchData(){
        String URL = ConstructList();
        Log.d(Tag,"fetch: "+URL);
        return URL;
    }

    private String ConstructList() {
        String URL = BaseURL;
        URL += "?method=flickr.interestingness.getList";
        URL += "&api_key="+APIKEY;
        URL += "&format=json";
        URL += "&nojsoncallback=1";
        URL += "&extras=date_taken,url_h";
        return URL;
    }

}
