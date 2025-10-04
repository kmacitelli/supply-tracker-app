package com.pitt.supplytrackerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.pitt.supplytrackerapp.databinding.ActivityMainBinding;
import com.pitt.supplytrackerapp.ui.home.HomeViewModel;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private String binName;
    private int alertQuantity;
    private ArrayList<Bin> binList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No item in bin calibration");
        builder.setMessage("Please press OK when there are NO items in the bin");

        builder.setPositiveButton("OK", (dialog, which) -> {
            // TODO: read weight from sensor for empty bin
            double noItemBinWeight = 20.0;

            showOneItemPopup(tempBin, noItemBinWeight);
        });

        builder.show();
    }
    public void showOneItemPopup(Bin tempBin, double noItemBinWeight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("One item in bin calibration");
        builder.setMessage("Please press OK when there is ONE item in the bin");

        builder.setPositiveButton("OK", (dialog, which) -> {
            double oneItemBinWeight = 25.0;
            // TODO: read weight from sensor for one item in bin
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}