package com.example.android.bookcompanion.data;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable
{

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    public final static Parcelable.Creator<Book> CREATOR = new Creator<Book>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return (new Book[size]);
        }

    }
            ;

    protected Book(Parcel in) {
        in.readList(this.items, (com.example.android.bookcompanion.data.Item.class.getClassLoader()));
    }

    public Book() {
    }


    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Book withItems(List<Item> items) {
        this.items = items;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
    }

    public int describeContents() {
        return 0;
    }

}
