package com.example.android.bookcompanion.data;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BooksInterface {
    @GET("{title}")
    public Call<ArrayList<Book>> getBooks();
}
