package com.example.android.bookcompanion.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable
{
    @SerializedName("volumeInfo")
    @Expose
    private VolumeInfo volumeInfo;

    public final static Parcelable.Creator<Item> CREATOR = new Creator<Item>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return (new Item[size]);
        }

    }
            ;

    protected Item(Parcel in) {
        this.volumeInfo = ((VolumeInfo) in.readValue((VolumeInfo.class.getClassLoader())));
    }

    public Item() {
    }


    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public Item withVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(volumeInfo);
    }

    public int describeContents() {
        return 0;
    }

}
