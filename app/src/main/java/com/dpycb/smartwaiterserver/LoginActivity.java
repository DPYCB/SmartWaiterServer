package com.dpycb.smartwaiterserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dpycb.smartwaiterserver.model.Common;
import com.dpycb.smartwaiterserver.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class LoginActivity extends AppCompatActivity {

    FButton btnLogin;
    MaterialEditText editLogin;
    MaterialEditText editPassword;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        editLogin = findViewById(R.id.editLoginName);
        editPassword = findViewById(R.id.editLoginPassword);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(editLogin.getText().toString(), editPassword.getText().toString());
            }
        });
    }

    private void login(String login, String password) {
        final ProgressDialog mDialpg = new ProgressDialog(LoginActivity.this);
        mDialpg.setMessage("Пожалуйста, подождите...");
        mDialpg.show();

        final String log = login;
        final String pass = password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(log).exists()) {
                    mDialpg.dismiss();
                    User user = snapshot.child(log).getValue(User.class);
                    user.setName(log);

                    if (Boolean.parseBoolean(user.getIsStaff())) {
                        if (user.getPassword().equals(pass)) {
                            Common.currentUser = user;
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Неверный пароль!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Вход возможен только со служебным аккаунтом!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    mDialpg.dismiss();
                    Toast.makeText(LoginActivity.this, "Такого пользователя не существует!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
