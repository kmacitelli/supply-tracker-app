package com.pitt.supplytrackerapp;

import java.util.ArrayList;
import java.util.List;

public class Bin {
    private String name;
    private int totalQuantity;
    private double individualWeight;
    private int alertQuantity;

    public Bin() {

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
    public void setAlertQuantity(int alertQuantity) {
        this.alertQuantity = alertQuantity;
    }

    public String getName() {
        return name;
    }
    public int getTotalQuantity() {
        return totalQuantity;
    }
    public double getIndividualWeight() {
        return individualWeight;
    }
    public int getAlertQuantity() {
        return alertQuantity;
    }

}
