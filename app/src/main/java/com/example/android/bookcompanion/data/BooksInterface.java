package com.example.android.bookcompanion.data;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BooksInterface {
    @GET("volumes")
    public Call<Book> getBooks(
            @Query("q") String booktitle
    );
}
