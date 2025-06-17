package com.example.fraga;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public DatabaseReference dbRef;
    public String email;
    public String phoneNum;
    public String address;
    public Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {

        EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText editPassword = findViewById(R.id.editTextTextPassword2);
        email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        EditText editAdd = findViewById(R.id.editTextTextPostalAddress);
                        EditText editPhone = findViewById(R.id.editTextPhone);
                        address = editAdd.getText().toString();
                        phoneNum = editPhone.getText().toString();
                        if (task.isSuccessful() && !address.isEmpty() && !phoneNum.isEmpty()) {
                            Toast.makeText(MainActivity.this, "reg successfully", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragment3_to_fragment2);
                            person = new Person(email , phoneNum , address , "");
                            startingStr();
                            createUser(view);
                        } else {
                            Toast.makeText(MainActivity.this, "reg failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void startingStr () {
        String name = person.getEmail().split("@")[0];
        person.setPersonStr("Hello " + name + "! Your phone number is " + phoneNum +
                " and your address is " + address + ".");
    }

    public void login(View view) {

        EditText editEmail = findViewById(R.id.editTextTextEmailAddress2);
        EditText editPassword = findViewById(R.id.editTextTextPassword);
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUser();
                            Toast.makeText(MainActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragment1_to_fragment2);
                        } else {
                            Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getUser () {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                person = snapshot.getValue(Person.class);
                TextView insertNewInfo = findViewById(R.id.textView7);
                insertNewInfo.setText(person.getPersonStr());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createUser(View view) {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference("users");
            dbRef.child(uid).setValue(person)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Data saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void updateUserString(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                person = snapshot.getValue(Person.class);
                TextView insertNewInfo = findViewById(R.id.textView7);
                TextView input = findViewById(R.id.editTextText3);
                String userInput = input.getText().toString();
                insertNewInfo.setText(userInput);
                person.setPersonStr(userInput);
                dbRef = FirebaseDatabase.getInstance().getReference("users");
                dbRef.child(uid).setValue(person)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Data saved!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}