package com.pitt.supplytrackerapp.handler;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebValueHandler {

    public interface ValueCallback {
        void onValueRead(double value);
        void onError(Exception e);
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void getValueFromWebsite(String urlString, ValueCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                reader.close();

                double value = Double.parseDouble(line.trim());

                // Post result to main/UI thread
                mainHandler.post(() -> callback.onValueRead(value));

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
}
