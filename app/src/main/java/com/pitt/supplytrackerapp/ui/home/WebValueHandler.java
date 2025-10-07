package com.pitt.supplytrackerapp.ui.home;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pitt.supplytrackerapp.ServerResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebValueHandler extends AsyncTask {

    public interface ValueCallback {
        void onValueRead(double value);
        void onError(Exception e);
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public ServerResponse doInBackground(Object[] urlInput) {
        double result = 10.0;

        String urlString = urlInput[0].toString();

        try {
            Log.d("WebValueHandler", "about to make the call");

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    dta.append(chunks);
                    Log.d("WebValueHandler", chunks);
                    //result = do logic here; if you're reading the weight line, grab the double from it
                }
            } else {
                //Handle else
                Log.d("WebValueHandler", "got a status code back: " + statusCode);
            }

        } catch (Exception e) {
            //mainHandler.post(() -> callback.onError(e));
            Log.d("WebValueHandler", "got an exception: " + e);
        }

        ServerResponse respObject = new ServerResponse();
        respObject.setWeight(result);

        Log.d("WebValueHandler", "got the weight back of " + result);

        return respObject;
    }
}

