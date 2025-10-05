package com.pitt.supplytrackerapp;

import java.util.ArrayList;
import java.util.List;

public class Bin {
    private String name;
    private int totalQuantity;
    private double individualWeight;
    private double emptyWeight;
    private double totalWeight;
    private int alertQuantity;

    public Bin() {

    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }
    public double getEmptyWeight() {
        return emptyWeight;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    public void setIndividualWeight(double individualWeight) {
        this.individualWeight = individualWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
    public void setAlertQuantity(int alertQuantity) {
        this.alertQuantity = alertQuantity;
    }

    public String getName() {
        return name;
    }
    public int getTotalQuantity() {
        int actualQuantity = (int) (totalWeight - emptyWeight) / (int) individualWeight;
        return actualQuantity;
    }
    public double getIndividualWeight() {
        return individualWeight;
    }

    public double getTotalWeight() {
        return totalWeight;
    }
    public int getAlertQuantity() {
        return alertQuantity;
    }

}
