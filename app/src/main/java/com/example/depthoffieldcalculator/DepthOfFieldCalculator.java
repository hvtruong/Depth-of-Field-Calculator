package com.example.depthoffieldcalculator;

import static java.lang.Math.pow;

public class DepthOfFieldCalculator {
    private Lens lens;
    private double distance;
    private double aperture;
    private double CIRCLE_OF_CONFUSION;

    public DepthOfFieldCalculator(Lens newlens, double newDistance, double newAperture, double cOf) {
        this.lens = newlens;
        this.distance = newDistance * 1000;
        this.aperture = newAperture;
        this.CIRCLE_OF_CONFUSION = cOf;
    }

    public double hyperfocalDistance (){
        return pow(lens.getFocalLength(),2)/(aperture * CIRCLE_OF_CONFUSION);
    }

    public double nearFocalPoint (){
        if(this.hyperfocalDistance() <= distance){
            return Double.POSITIVE_INFINITY;
        }
        return  (this.hyperfocalDistance() * distance) / (this.hyperfocalDistance() +
                (distance - lens.getFocalLength()));
    }

    public double farFocalPoint(){
        return (this.hyperfocalDistance() * distance) / (this.hyperfocalDistance() -
                (distance - lens.getFocalLength()));
    }

    public double depthOfField(){
        return this.farFocalPoint() - this.nearFocalPoint();
    }
}
