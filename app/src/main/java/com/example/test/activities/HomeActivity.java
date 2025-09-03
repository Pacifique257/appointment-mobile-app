package com.example.test.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.adapters.AvailabilityAdapter;
import com.example.test.local.AppDatabase;
import com.example.test.models.Availability;
import com.example.test.config.ApiConfig;
import com.example.test.network.ApiService;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AvailabilityAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);

        fetchAvailabilities();
    }

    private void fetchAvailabilities() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Availability>> call = apiService.getAvailabilities();

        call.enqueue(new Callback<List<Availability>>() {
            @Override
            public void onResponse(Call<List<Availability>> call, Response<List<Availability>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Availability> availabilities = response.body();
                    // Stocker localement
                    new Thread(() -> db.availabilityDao().insertAll(availabilities)).start();

                    // Afficher
                    adapter = new AvailabilityAdapter(availabilities);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Erreur lors de la récupération des disponibilités", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Availability>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
