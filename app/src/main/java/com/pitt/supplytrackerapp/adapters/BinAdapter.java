package com.pitt.supplytrackerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pitt.supplytrackerapp.*;

import java.util.List;

public class BinAdapter extends RecyclerView.Adapter<BinAdapter.BinViewHolder> {

    private List<Bin> bins;

    public BinAdapter(List<Bin> bins) {
        this.bins = bins;
    }

    public static class BinViewHolder extends RecyclerView.ViewHolder {
        TextView binNameText;
        TextView alertQuantityText;
        TextView quantityText;
        TextView totalQuantityText;

        public BinViewHolder(View itemView) {
            super(itemView);
            binNameText = itemView.findViewById(R.id.binNameText);
            alertQuantityText = itemView.findViewById(R.id.alertQuantityText);
            quantityText = itemView.findViewById(R.id.quantityText);
            totalQuantityText = itemView.findViewById(R.id.totalQuantityText);
        }
    }

    @Override
    public BinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bin_item, parent, false);
        return new BinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BinViewHolder holder, int position) {
        Bin bin = bins.get(position);
        holder.binNameText.setText(bin.getName());
        holder.alertQuantityText.setText("Alert Qty: " + bin.getAlertQuantity());
        holder.quantityText.setText("Individual Weight: " + bin.getIndividualWeight());
        holder.totalQuantityText.setText("Total Quantity: " + bin.getTotalWeight());
    }

    @Override
    public int getItemCount() {
        return bins.size();
    }

    public void updateBins(List<Bin> newBins) {
        this.bins = newBins;
        notifyDataSetChanged();
    }
}

