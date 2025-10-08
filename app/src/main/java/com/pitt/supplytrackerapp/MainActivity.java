package com.pitt.supplytrackerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.pitt.supplytrackerapp.databinding.ActivityMainBinding;
import com.pitt.supplytrackerapp.ui.home.HomeViewModel;
import com.pitt.supplytrackerapp.ui.home.WebValueHandler;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private WebValueHandler webValueHandler = new WebValueHandler();

    private String binName;
    private int alertQuantity;
    private ArrayList<Bin> binList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();

        createNotificationChannel();
    }

    private double readWeightFromServer() {
        double value = 0.0;
        try {
            String url = "http://10.0.2.2:8080/response.txt"; //access thru emulator

            Object[] input = {url};
            value = webValueHandler.doInBackground(input).getWeight();

        } catch (Exception e) {
            Log.e("MainActivity", "received error: " + e);
        }

        return value;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bin Alerts";
            String description = "Notifications when bin quantity is low";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("BIN_ALERT_CHANNEL", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendBinAlertNotification(String binName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BIN_ALERT_CHANNEL")
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("Low Bin Alert")
                .setContentText(binName + " is below its alert quantity!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(new Random().nextInt(), builder.build());
        }
    }

    public void showBinNamePopup(Bin tempBin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter bin name");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogueView = inflater.inflate(R.layout.bin_name_dialogue_input, null);
        builder.setView(dialogueView);

        final EditText binNameText = dialogueView.findViewById(R.id.binNameDialogInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binName = binNameText.getText().toString();
                tempBin.setName(binName);
                showAlertQuantityPopup(tempBin);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showAlertQuantityPopup(Bin tempBin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter alert quantity");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogueView = inflater.inflate(R.layout.alert_quantity_dialogue_input, null);
        builder.setView(dialogueView);

        final EditText binNameText = dialogueView.findViewById(R.id.alertQuantityDialogueInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertQuantity = Integer.parseInt(binNameText.getText().toString());
                tempBin.setAlertQuantity(alertQuantity);
                showNoItemPopup(tempBin);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showNoItemPopup(Bin tempBin) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No item in bin calibration");
        builder.setMessage("Please press OK when there are NO items in the bin");

        builder.setPositiveButton("OK", (dialog, which) -> {
            double newWeight = readWeightFromServer();
            Log.d("MainActivity", "The new weight is " + newWeight);
            tempBin.setEmptyWeight(newWeight);
            showOneItemPopup(tempBin, newWeight);
        });

        builder.show();
    }
    public void showOneItemPopup(Bin tempBin, double noItemBinWeight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("One item in bin calibration");
        builder.setMessage("Please press OK when there is ONE item in the bin");

        builder.setPositiveButton("OK", (dialog, which) -> {
            double oneItemBinWeight = readWeightFromServer() + 10;
            tempBin.setTotalWeight(oneItemBinWeight);
            double individualWeight = oneItemBinWeight - noItemBinWeight;
            tempBin.setIndividualWeight(individualWeight);

            binList.add(tempBin);
            HomeViewModel homeViewModel = new ViewModelProvider(MainActivity.this).get(HomeViewModel.class);
            homeViewModel.setBins(binList);
        });



        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_device) {
            Toast.makeText(this, "Added a new bin.", Toast.LENGTH_LONG).show();
            Bin tempBin = new Bin();
            // Prompts the popup chain that gets required info from user
            // Name of bin, alert qty, individual weight
            showBinNamePopup(tempBin);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}