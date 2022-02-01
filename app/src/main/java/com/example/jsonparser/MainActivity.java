package com.example.jsonparser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> ss;
    TextView textView;
    int index = 0;
    ProgressBar progressBarPhoto;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textV);
        progressBarPhoto = findViewById(R.id.progressBarPhoto);
        Button next = findViewById(R.id.next);
        Button prev = findViewById(R.id.previous);
        imageView.setClickable(true);
        if(!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"No Internet Sorry",Toast.LENGTH_SHORT).show();
            textView.setText("No Internet");
        }else {

        /*
        String s = getStringFromJson();
        String[] array = null;
        try {
            JSONObject root = new JSONObject(s);
            JSONArray arrayJS = root.getJSONArray("members");
            array = new String[arrayJS.length()];
            for (int i = 0; i < arrayJS.length(); i++){
                JSONObject js = arrayJS.getJSONObject(i);
                array[i] = js.getString("name");
            }
            Log.d("yes", Arrays.toString(array));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,array);
        listView.setAdapter(adapter);*/
            FlickrAPI ap = new FlickrAPI(this);
            fetchInteresingPhotos f = new fetchInteresingPhotos();
            Log.i("url",ap.fetchData());
            f.execute(ap.fetchData());
            next.setOnClickListener(view -> {
                textView.setText("We found "+ss.size()+" photos");
                index++;
                prev.setEnabled(true);
                if(index < ss.size()) {
                    new DownloadImageTask(findViewById(R.id.imageView))
                            .execute(ss.get(index));
                }else {
                    Toast.makeText(getApplicationContext(),"No more photos to show",Toast.LENGTH_SHORT).show();
                    next.setEnabled(false);
                }

            });
            prev.setOnClickListener(view -> {
                index--;
                next.setEnabled(true);

                if(index >= 0) {
                    new DownloadImageTask(findViewById(R.id.imageView))
                            .execute(ss.get(index));
                }else {
                    Toast.makeText(getApplicationContext(),"The first one !!",Toast.LENGTH_SHORT).show();
                    prev.setEnabled(false);
                }
            });


        }
    }
   /* public String getStringFromJson() {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.source)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert br != null;
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("yes",sb.toString());
        return sb.toString();
    }*/
   private boolean isNetworkAvailable() {
       ConnectivityManager connectivityManager
               = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
       return activeNetworkInfo != null && activeNetworkInfo.isConnected();
   }


   @SuppressLint("StaticFieldLeak")
   class fetchInteresingPhotos extends AsyncTask<String, Integer, List<String>>{

        @Override
        protected List<String> doInBackground(String... strings) {
            String Url = strings[0];
            List<String> Result = new ArrayList<>();
            try {
                URL url = new URL(Url);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                String s = getStringFromJson(urlConnection.getInputStream());
                JSONObject root = new JSONObject(s);
                JSONObject root2 = root.getJSONObject("photos");
                JSONArray arrayJS = root2.getJSONArray("photo");
                Log.d("url", String.valueOf(arrayJS.length()));
                for (int i = 0; i < arrayJS.length(); i++){
                    JSONObject js = arrayJS.getJSONObject(i);
                    Result.add(js.getString("url_h"));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return Result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



       public String getStringFromJson(InputStream s) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(s));
                String temp;
                while ((temp = br.readLine()) != null)
                    sb.append(temp);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert br != null;
                    br.close(); // stop reading
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
       @SuppressLint("SetTextI18n")
       @Override
       protected void onPostExecute(List<String> strings) {
           super.onPostExecute(strings);
           ss = (ArrayList<String>) strings;
           new DownloadImageTask(findViewById(R.id.imageView))
                   .execute(ss.get(index));
           textView.setText("We found "+ss.size()+" photos");
       }



    }
    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarPhoto.setVisibility(View.VISIBLE);

        }


        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            progressBarPhoto.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBarPhoto.setProgress(values[0]);
        }
    }

}