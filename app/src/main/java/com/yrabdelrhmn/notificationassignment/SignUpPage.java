package com.yrabdelrhmn.notificationassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpPage extends AppCompatActivity {
private EditText firstName, lastName, userEmail,userPass;
private Button save;
private TextView tResponse, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        firstName = findViewById(R.id.Fname);
        userEmail = findViewById(R.id.emailReg);
        userPass = findViewById(R.id.passReg);
        lastName = findViewById(R.id.Lname);
        tResponse = findViewById(R.id.response);
        save = findViewById(R.id.btnSignup);
        login = findViewById(R.id.loginink);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignInPage.class);
                startActivity(i);
            }
        });
        save.setOnClickListener(
                v -> {
                    if(firstName.getText().toString().isEmpty() && lastName.getText().toString().isEmpty() && userEmail.getText().toString().isEmpty() && userPass.getText().toString().isEmpty()){
                        Toast.makeText(SignUpPage.this, "Please enter the values", Toast.LENGTH_SHORT).show();
                    return;
                    }
                    postData(firstName.getText().toString(), lastName.getText().toString(),userEmail.getText().toString(),userPass.getText().toString());
                });

    }

    private void postData(String fName, String lName, String email, String pass) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mcc-users-api.herokuapp.com/").addConverterFactory(GsonConverterFactory.create()).build();
       Api retrofitApi = retrofit.create(Api.class);
       DataModel model = new DataModel(fName,lName,email,pass);
        Call<DataModel> call = retrofitApi.createPost(model);
        call.enqueue(new Callback<DataModel>() {

            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                Toast.makeText(SignUpPage.this, "Data added to API", Toast.LENGTH_SHORT).show();
               firstName.setText("firstName");
               lastName.setText("lastName");
               userEmail.setText("userEmail");
               userPass.setText("userPass");
                DataModel responseFromAPI = response.body();
                String responseString = "Response Code: " + response.code() +
                        "\nfirst name: " + responseFromAPI.getFirstName()
                        + "\nLast Name: " + responseFromAPI.getLastName()
                        + "\nEmail: " + responseFromAPI.getEmail() +
                        "\nPassword" + responseFromAPI.getPassword();
                tResponse.setText(responseString);

            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
              tResponse.setText("Error found is : "+t.getMessage());
            }
        });

    }
}