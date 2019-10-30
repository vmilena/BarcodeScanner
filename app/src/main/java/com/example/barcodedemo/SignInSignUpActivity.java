package com.example.barcodedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodedemo.api.models.User;
import com.example.barcodedemo.orm.BarcodeScannerDatabase;
import com.example.barcodedemo.utils.Constants;
import com.example.barcodedemo.utils.UserManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInSignUpActivity extends AppCompatActivity {

    @BindView(R.id.usernameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    public static void start(Context context) {
        Intent starter = new Intent(context, SignInSignUpActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signInButton)
    public void signIn() {
        List<User> userList = BarcodeScannerDatabase
                .getInstance(this.getApplicationContext())
                .userDao()
                .getUserList(usernameEditText.getText().toString());

        if (!userList.isEmpty()) {
            if (userList.get(Constants.INDEX_FIRST).getPassword().equals(passwordEditText.getText().toString())) {
                UserManager.getInstance(this).saveUser(userList.get(Constants.INDEX_FIRST));
                MainActivity.start(this);
            } else {
                Toast.makeText(this, "Password you entered is wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Username you entered does not exist.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.signUpButton)
    public void signUp() {
        if (!BarcodeScannerDatabase.getInstance(this.getApplicationContext()).userDao().getUserList(usernameEditText.getText().toString()).isEmpty()) {
            Toast.makeText(this, "User with this username already exists.", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            user.setUserId((int) BarcodeScannerDatabase
                    .getInstance(this.getApplicationContext())
                    .userDao()
                    .insertUser(user));

            UserManager.getInstance(this).saveUser(user);
            MainActivity.start(this);
        }

    }

}
