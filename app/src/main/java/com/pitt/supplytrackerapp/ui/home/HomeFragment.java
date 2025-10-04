package com.pitt.supplytrackerapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pitt.supplytrackerapp.*;

import com.pitt.supplytrackerapp.adapters.BinAdapter;
import com.pitt.supplytrackerapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private BinAdapter binAdapter;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        RecyclerView recyclerView = binding.binRecyclerView;
        binAdapter = new BinAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(binAdapter);

        // Add gray divider between items
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.recycler_divider));
        recyclerView.addItemDecoration(divider);

        // Observe LiveData bins
        homeViewModel.getBins().observe(getViewLifecycleOwner(), bins -> {
            // Update adapter
            binAdapter.updateBins(bins);

            // Check for low quantity and show a toast
            for (Bin bin : bins) {
                if (bin.getTotalQuantity() < bin.getAlertQuantity()) {
                    // Get the activity instance and then call the method
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).sendBinAlertNotification(bin.getName());
                    }
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}