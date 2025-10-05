package com.pitt.supplytrackerapp.handler;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;


import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebValueHandler extends AsyncTask {

    //private static final Log log = LogFactory.getLog(WebValueHandler.class);

    public interface ValueCallback {
        void onValueRead(double value);
        void onError(Exception e);
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected Object doInBackground(Object[] urlInput) {
        double result = 0;

        try {

            String urlString = urlInput[0].toString();
                try {
//                URL url = new URL(urlString);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = reader.readLine();
//                reader.close();
//
//                double value = Double.parseDouble(line.trim());
//
//                // Post result to main/UI thread
//                mainHandler.post(() -> callback.onValueRead(value));

                    Log.d("foo", "about to make the call");

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
                        }
                    } else {
                        //Handle else
                        Log.d("foo", "got an error back");

                    }

                } catch (Exception e) {
                    //mainHandler.post(() -> callback.onError(e));
                    Log.d("foo", "got an errror" + e);
                }


        } catch(Exception e) {
            Log.d("foo", "got an errorrr" + e);
        }

        return result;

    }
}
