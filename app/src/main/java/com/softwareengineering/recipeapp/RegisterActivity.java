package com.softwareengineering.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareengineering.recipeapp.requestManager.ReadWriteUserDetails;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword;
    private CheckBox checkboxGluten, checkboxNuts, checkboxLactose;
    private Button buttonRegister;
    private TextView textViewClickableLogin;
    List<String> allergies = new ArrayList<>();
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editText_register_name);
        editTextEmail = findViewById(R.id.editText_register_email);
        editTextPassword = findViewById(R.id.editText_register_password);

        checkboxGluten = findViewById(R.id.checkbox_gluten);
        checkboxNuts = findViewById(R.id.checkbox_nuts);
        checkboxLactose = findViewById(R.id.checkbox_lactose);

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allergies.clear();

                if (checkboxGluten.isChecked()) allergies.add("Gluten");
                if (checkboxNuts.isChecked()) allergies.add("Nuts");
                if (checkboxLactose.isChecked()) allergies.add("Lactose");

                String textUsername = editTextName.getText().toString().trim();
                String textEmail = editTextEmail.getText().toString().trim();
                String textPassword = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(textUsername)){
                    editTextName.setError("Please enter a username");
                    editTextName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)){
                    editTextEmail.setError("Please enter an email");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)){
                    editTextPassword.setError("Please enter a password");
                    editTextPassword.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    editTextEmail.setError("Please enter a valid email");
                    editTextEmail.requestFocus();
                } else if (textPassword.length() < 6){
                    editTextPassword.setError("Password must be at least 6 characters");
                    editTextPassword.requestFocus();
                } else {
                    registerUser(textUsername, textEmail, textPassword, allergies);
                }
            }
        });

        textViewClickableLogin = findViewById(R.id.textView_clickable_login);
        textViewClickableLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String textUsername, String textEmail, String textPassword, List<String> allergies) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textUsername).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textEmail, allergies);

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                firebaseUser.sendEmailVerification();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        editTextPassword.setError("Your password is too weak, kindly use a mix of alphabets, numbers, and symbols.");
                        editTextPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editTextEmail.setError("Invalid credentials, please re-enter.");
                        editTextEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        editTextEmail.setError("Email already in use, please use a different email.");
                        editTextEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}