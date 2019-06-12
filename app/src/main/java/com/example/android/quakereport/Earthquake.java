package com.example.android.quakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private Long mDate;
    private String mUrl;

    public Earthquake(double Magnitude, String Location, Long Date,String Url) {
mMagnitude=Magnitude;
mLocation=Location;
mDate=Date;
mUrl=Url;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public Long getmDate() {
        return mDate;
    }

    public String getmLocation() {
        return mLocation;
    }
    public String getmUrl(){return mUrl;}
}