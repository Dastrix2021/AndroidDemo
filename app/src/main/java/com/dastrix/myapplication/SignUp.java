package com.dastrix.myapplication;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dastrix.myapplication.Models.User;

public class SignUp extends AppCompatActivity {

    private EditText editPhone, editName, editPassword;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        editPhone = findViewById(R.id.editTextPhone);
        editName = findViewById(R.id.editTextName);
        editPassword = findViewById(R.id.editTextPassword);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("User");

        btnSignUp.setOnClickListener(view -> table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(editPhone.getText().toString()).exists()) {
                    Toast.makeText(SignUp.this, "Такой пользователь уже есть", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(editName.getText().toString(), editPassword.getText().toString());
                    table.child(editPhone.getText().toString()).setValue(user);
                    Toast.makeText(SignUp.this, "Успешная регистрация", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
            }
        }));
    }
}