package com.bakamcu.remake.learningassistant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bakamcu.remake.learningassistant.utils.DialogUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputEditText password;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(view -> Login());
        register.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
    void Login() {
        AlertDialog alertDialog = DialogUtils.getInstance(LoginActivity.this).LoadingDialog();
        if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
            Toast.makeText(LoginActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        LCUser.logIn(Objects.requireNonNull(username.getText()).toString().trim(), Objects.requireNonNull(password.getText()).toString().trim()).subscribe(new Observer<LCUser>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCUser user) {
                // 登录成功
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）
                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }

            public void onComplete() {
            }
        });
    }
}