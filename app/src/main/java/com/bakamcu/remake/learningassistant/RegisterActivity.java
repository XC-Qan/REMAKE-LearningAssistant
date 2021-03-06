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

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputEditText password;
    TextInputEditText passwordConfirm;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        register = findViewById(R.id.register);

        register.setOnClickListener(view -> Register());
    }

    void Register() {
        AlertDialog alertDialog = DialogUtils.getInstance(RegisterActivity.this).LoadingDialog();
        if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
            Toast.makeText(RegisterActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
            return;
        }
        if (!Objects.requireNonNull(password.getText()).toString().trim().equals(Objects.requireNonNull(passwordConfirm.getText()).toString().trim())) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
            return;
        }

        LCUser user = new LCUser();
        user.setUsername(Objects.requireNonNull(username.getText()).toString().trim());
        user.setPassword(password.getText().toString().trim());
        user.signUpInBackground().subscribe(new Observer<LCUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LCUser lcUser) {
                LCUser.logIn(username.getText().toString().trim(), password.getText().toString().trim()).subscribe(new Observer<LCUser>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onNext(LCUser user) {
                        // 登录成功
                        Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }

                    public void onError(Throwable throwable) {
                        // 登录失败（可能是密码错误）
                        Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                    public void onComplete() {
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }

            @Override
            public void onComplete() {

            }
        });
    }

}