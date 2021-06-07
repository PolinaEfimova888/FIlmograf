package com.example.filmograf;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieActivity extends AppCompatActivity {

    LinearLayout ln;

    Gson gson = new GsonBuilder().create();
    String api_key = "2afab14a5f39728f8f613d627e5dd9bb";
    int movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ln = (LinearLayout) findViewById(R.id.moviie_lin);

        String api_key = "2afab14a5f39728f8f613d627e5dd9bb";

        movie_id = getIntent().getIntExtra("movie_id", 0);

        getImagesTask task = new getImagesTask();
        task.execute();
    }

    public Call<Movie> getFilmCall () {
        String url = "https://api.themoviedb.org/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Manager api = retrofit.create(Manager.class);

        Call <Movie> call_film = api.getFilm(337404, api_key, "ru");
        return call_film;
    }

    public void setPopularFilms(Movie m) {
        LayoutInflater ltInflater = getLayoutInflater();

        String url = "https://www.themoviedb.org/t/p/w1280/";
        View new_movie = ltInflater.inflate(R.layout.movie, ln, false);

        TextView title = new_movie.findViewById(R.id.title);
        TextView original_title = new_movie.findViewById(R.id.original_title);
        TextView release_date = new_movie.findViewById(R.id.release_date);
        TextView popularity = new_movie.findViewById(R.id.popularity);
        TextView overview = new_movie.findViewById(R.id.overview);
        TextView genres = new_movie.findViewById(R.id.genres);

        ImageView poster = new_movie.findViewById(R.id.poster);

        title.setText("Название: " + m.title);
        original_title.setText("Полное Название: " + m.original_title);
        release_date.setText("Дата релиза: " + m.release_date);
        overview.setText("Описание: " + m.overview);
        popularity.setText("Популярность: " + String.valueOf(m.popularity));

        String genres_s = "";
        for (int i=0; i<m.genres.length; i++) {
            genres_s += m.genres[i].name + " ";
        }
        genres.setText("Жанры: " + genres_s);

        if (m.backdrop_path != null) {
            Uri uri_pic = Uri.parse(url + m.backdrop_path);
            Picasso p = new Picasso.Builder(getApplicationContext()).build();
            p.load(uri_pic).into(poster);
        }

        if (m.poster_path != null) {
            Uri uri_pic = Uri.parse(url + m.poster_path);
            Picasso p = new Picasso.Builder(getApplicationContext()).build();
            p.load(uri_pic).into(poster);
        }

        ln.addView(new_movie);
    }

    public class getImagesTask extends AsyncTask<Void , Void, Movie> {

        @Override
        protected Movie doInBackground(Void... voids) {
            Call<Movie> getFilmCall = getFilmCall();
            return getImageTask(getFilmCall);
        }

        @Override
        protected void onPostExecute(Movie result) {
            setPopularFilms(result);
        }

        private Movie getImageTask(Call<Movie> getFilmCall) {
            try {
                retrofit2.Response<Movie> response =  getFilmCall.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
