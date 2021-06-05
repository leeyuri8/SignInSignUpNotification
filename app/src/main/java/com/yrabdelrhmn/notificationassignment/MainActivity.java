package com.yrabdelrhmn.notificationassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String email, pass;
    private Button login;
    private TextView tv;
    boolean isEmailValid, isPassValid;
    String token_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          tv = findViewById(R.id.tv);

        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        getFirebaseToken();
    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("Token", "Failed to get the token" + task.getException());
                    Toast.makeText(MainActivity.this, "Failed to get token", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "" + task.getResult(), Toast.LENGTH_SHORT).show();
                token_reg = task.getResult();
                saveData(email, pass, token_reg);

            }
        });
    }

    private void saveData(String email, String pass, String token) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mcc-users-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api retrofitApi = retrofit.create(Api.class);
        DataModel model = new DataModel(email, pass);
        Call<DataModel> call = retrofitApi.createPost(model);
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                try {
                    JSONObject obj  = new JSONObject(String.valueOf(response));
                    tv.setText(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
       tv.setText(
               "Error!" + t.getMessage()
       );
            }
        });
    }
}