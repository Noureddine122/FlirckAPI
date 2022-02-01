package com.example.jsonparser;

import android.util.Log;

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
