
/*
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.android.bookcompanion.BookAdapter;
import com.example.android.bookcompanion.MainActivity;
import com.example.android.bookcompanion.R;
import com.example.android.bookcompanion.data.Book;
import com.example.android.bookcompanion.data.BooksInterface;
import com.example.android.bookcompanion.data.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookResultsFragment extends Fragment {
    Context mContext;
    public BookResultsFragment(){
    }

    @Override
    public voidzeateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ew mainView = inflater.inflate(R.layout.activity_main ,false);
        final BookAdapter bookAdapter = new BookAdapter(getActivity().getApplicationContext());

        //Getting the JSON Object with Retrofit
        BooksInterface booksInterface = RetrofitClient.getRetrofitInstance();
        Call<ArrayList<Book>> book = booksInterface.getBooks();

        book.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> books = response.body();
                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(getString(R.string.FETCHBOOKS), books);
                //set the bookAdapter, for this version will be used only 1 result
                bookAdapter.setBook(books);
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                Log.v("Retrofit has failed ", t.getMessage());
            }
        });
        //return the adapted view
    }
}

*/