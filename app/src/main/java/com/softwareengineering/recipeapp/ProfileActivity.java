package com.softwareengineering.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwareengineering.recipeapp.adapters.AllergyAdapter;
import com.softwareengineering.recipeapp.requestManager.ReadWriteUserDetails;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewProfileName, textViewProfileEmail;
    private String username, email;
    private FirebaseAuth authProfile;
    RecyclerView recyclerViewAllergies;
    List<String> allergies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewProfileName = findViewById(R.id.textView_profile_username);
        textViewProfileEmail = findViewById(R.id.textView_profile_email);
        authProfile = FirebaseAuth.getInstance();

        recyclerViewAllergies = findViewById(R.id.recyclerView_allergies);
        recyclerViewAllergies.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        } else {
            showUserProfile(firebaseUser);
        }


        final LinearLayout favoriteLayout = findViewById(R.id.favorite_layout);
        final LinearLayout homeLayout = findViewById(R.id.home_layout);
        final LinearLayout aboutLayout = findViewById(R.id.about_layout);


        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    username = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    textViewProfileName.setText(username);
                    textViewProfileEmail.setText(email);

                    allergies = readUserDetails.allergies;
                    if (allergies != null && !allergies.isEmpty()) {
                        // Set up RecyclerView
                        AllergyAdapter adapter = new AllergyAdapter(ProfileActivity.this, allergies);
                        recyclerViewAllergies.setAdapter(adapter);
                    } else {
                        Toast.makeText(ProfileActivity.this, "No allergies found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}