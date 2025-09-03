package com.example.test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.test.R;
import com.example.test.config.ApiConfig;
import com.example.test.models.RegisterRequest;
import com.example.test.models.RegisterResponse;
import com.example.test.network.ApiService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.net.ssl.*;
import java.security.cert.CertificateException;


public class RegisterActivity extends AppCompatActivity {

    private EditText lastNameInput, firstNameInput, emailInput, phoneInput, birthDateInput, addressInput, passwordInput;
    private Spinner roleInput;
    private RadioGroup genderInput;
    private Button registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        setContentView(R.layout.activity_register);

        lastNameInput = findViewById(R.id.lastNameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        emailInput = findViewById(R.id.emailInput);
        roleInput = findViewById(R.id.roleInput);
        phoneInput = findViewById(R.id.phoneInput);
        birthDateInput = findViewById(R.id.birthDateInput);
        addressInput = findViewById(R.id.addressInput);
        genderInput = findViewById(R.id.genderInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // Spinner pour le rôle
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"PATIENT", "DOCTOR"});
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleInput.setAdapter(roleAdapter);

        loginLink.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String lastName = lastNameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String role = roleInput.getSelectedItem().toString(); // PATIENT ou DOCTOR
        String phone = phoneInput.getText().toString().trim();
        String birthDate = birthDateInput.getText().toString().trim(); // format YYYY-MM-DD
        String address = addressInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        int selectedGenderId = genderInput.getCheckedRadioButtonId();
        String gender = "";
        if (selectedGenderId != -1) {
            RadioButton selectedGender = findViewById(selectedGenderId);
            String text = selectedGender.getText().toString();
            if (text.equalsIgnoreCase("Masculin")) gender = "MALE";
            else if (text.equalsIgnoreCase("Féminin")) gender = "FEMALE";
        }

        if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || role.isEmpty() ||
                phone.isEmpty() || birthDate.isEmpty() || address.isEmpty() || gender.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // OkHttpClient pour SSL auto-signé
        OkHttpClient client;
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(getUnsafeSSLContext().getSocketFactory(), new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                    })
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur SSL : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        RegisterRequest request = new RegisterRequest(lastName, firstName, email, role, phone, birthDate, address, gender, password);

        Call<RegisterResponse> call = apiService.register(request); // <-- endpoint fixé dans ApiService
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                            .putString("token", registerResponse.getToken()).apply();
                    Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erreur inconnue";
                        Toast.makeText(RegisterActivity.this, "Échec : " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SSLContext getUnsafeSSLContext() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        return sslContext;
    }
}

