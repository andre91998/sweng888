package com.example.practice4.util;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

//Scopes are stored in the database
public class Scope implements Parcelable {
    private int id; //database row id
    private String name; //user-configurable scope name
    private String brand; //brand of scope
    private float maxMagnification; //maximum magnification of scope (1 for non magnified scopes)
    private boolean variableMagnification; //whether the scope has variable magnification

    public String getName() {
        return name;
    }

    public Scope(int id, String name, String brand, float maxMagnification,
                 boolean variableMagnification) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.maxMagnification = maxMagnification;
        this.variableMagnification = variableMagnification;
    }

    protected Scope(Parcel in) {
        id = in.readInt();
        name = in.readString();
        brand = in.readString();
        maxMagnification = in.readFloat();
        variableMagnification = in.readByte() != 0;
    }

    public static final Creator<Scope> CREATOR = new Creator<Scope>() {
        @Override
        public Scope createFromParcel(Parcel in) {
            return new Scope(in);
        }

        @Override
        public Scope[] newArray(int size) {
            return new Scope[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(brand);
        parcel.writeFloat(maxMagnification);
        parcel.writeByte((byte) (variableMagnification ? 1 : 0));
    }

    @NonNull
    @Override
    public String toString() {
        return "Scope Name: " + name + "\nScope Brand: " + brand +
                "\n Max Magnification: " + maxMagnification + "\nHas Variable Magnification: "
                + (variableMagnification ? "yes" : "no");
    }
}
