package com.yrabdelrhmn.notificationassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInPage extends AppCompatActivity {
private EditText email,pass;
private Button login;
private TextView register,tResponse;
boolean isEmailValid, isPassValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        email = findViewById(R.id.emailLog);
        pass = findViewById(R.id.passLog);
        login = findViewById(R.id.btn);
        register = findViewById(R.id.sinup);
        tResponse = findViewById(R.id.response);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetValidation();
                loginProcess(email.getText().toString(),pass.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignInPage.class);
                startActivity(intent);
            }


        });
    }

private void loginProcess(String email,String pass){
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mcc-users-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Api retrofitApi = retrofit.create(Api.class);
   DataModel model = new DataModel(email,pass);
    Call<DataModel> call = retrofitApi.createPost(model);
    call.enqueue(new Callback<DataModel>() {


        @Override
        public void onResponse(Call<DataModel> call, Response<DataModel> response) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class)
                    .putExtra("email",email)
                    .putExtra("password",pass)
            );
            finish();
        }

        @Override
        public void onFailure(Call<DataModel> call, Throwable t) {
       tResponse.setText("Error found in"+t.getMessage(), TextView.BufferType.NORMAL);
        }
    });

}
    private void SetValidation() {
        if(email.getText().toString().isEmpty()){
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid=false;

        }else {
            isEmailValid = true;
        }
        if(pass.getText().toString().isEmpty()) {
            pass.setError(getResources().getString(R.string.password_error));
            isPassValid = false;
        }else if (pass.getText().length()<6){
                pass.setError(getResources().getString(R.string.error_invalid_pass));
            isPassValid = false;
        }else{
            isPassValid = true;
        }
        if(isEmailValid && isPassValid){
            Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}