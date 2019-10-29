package com.example.barcodedemo;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodedemo.api.models.User;
import com.example.barcodedemo.utils.Constants;
import com.example.barcodedemo.utils.UserManager;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInSignUpActivity extends AppCompatActivity {

    @BindView(R.id.usernameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signInButton)
    public void signIn() {
        List<User> userList = User.find(User.class, "username = ?", usernameEditText.getText().toString());
            if(!userList.isEmpty()){
            UserManager.getInstance(this).saveUser(userList.get(Constants.INDEX_FIRST));
        }
    }
    @OnClick(R.id.signUpButton)
    public void signUp() {
        User user = new User(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
        if(!User.find(User.class, "username = ?", user.getUsernme()).isEmpty()){
            signIn();
        } else {
            user.setUserID(UUID.randomUUID().toString());
            user.save();
            UserManager.getInstance(this).saveUser(user);
        }
    }

}
