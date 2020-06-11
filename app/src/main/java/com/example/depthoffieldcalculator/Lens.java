package com.example.depthoffieldcalculator;

public class Lens {
    private String make;
    private double maximumAperture;
    private int focalLength;

    public Lens(String make, double max, int focalLength)
    {
        this.make = make;
        this.maximumAperture = max;
        this.focalLength = focalLength;
    }

    public String getMake()
    {
        return make;
    }

    public double getMaximumAperture()
    {
        return maximumAperture;
    }

    public int getFocalLength()
    {
        return focalLength;
    }

    public void setMake(String newMake)
    {
        this.make = newMake;
    }

    public void setMaximumAperture(double newMax)
    {
        this.maximumAperture = newMax;
    }

    public void setFocalLength(int newLength)
    {
        this.focalLength = newLength;
    }

    @Override
    public String toString() {
        return make + " " + focalLength + "mm " + "F" + maximumAperture;
    }
}
