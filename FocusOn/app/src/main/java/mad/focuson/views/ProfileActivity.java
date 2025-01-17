package mad.focuson.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import mad.focuson.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, usernameEditText, emailEditText;
    private Button logoutButton, changePasswordButton, deleteAccountButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Reference to the Firestore document for the current user
        userDocRef = firestore.collection("users").document(currentUser.getUid());

        // Initialize Views
        nameEditText = findViewById(R.id.editTextName);
        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        logoutButton = findViewById(R.id.buttonLogout);
        changePasswordButton = findViewById(R.id.buttonChangePassword);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);


        // Fetch and display user data
        fetchAndDisplayUserData();

        // Pre-fill email
        emailEditText.setText(currentUser.getEmail());

        // Update Profile Info
        findViewById(R.id.saveNameButton).setOnClickListener(v -> updateName());
        findViewById(R.id.saveUsernameButton).setOnClickListener(v -> updateusername());
        findViewById(R.id.saveEmailButton).setOnClickListener(v -> updateEmail());

        // Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Change Password
        changePasswordButton.setOnClickListener(v -> {
            mAuth.sendPasswordResetEmail(currentUser.getEmail())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Delete Account
        deleteAccountButton.setOnClickListener(v -> confirmDeleteAccount());
    }

    private void fetchAndDisplayUserData() {
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Extract user data from Firestore document
                String name = task.getResult().getString("name");
                String username = task.getResult().getString("username");

                // Set the data in the EditText fields
                nameEditText.setText(name != null ? name : "");
                usernameEditText.setText(username != null ? username : "");
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateName() {
        String newName = nameEditText.getText().toString();
        if (!TextUtils.isEmpty(newName)) {
            userDocRef.update("name", newName)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Name updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update name.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateusername() {
        String newusername = usernameEditText.getText().toString();
        if (!TextUtils.isEmpty(newusername)) {
            userDocRef.update("username", newusername)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "username updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "username cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmail() {
        String newEmail = emailEditText.getText().toString();
        if (!TextUtils.isEmpty(newEmail)) {
            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Email updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAccount() {
        currentUser.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userDocRef.delete();
                        Toast.makeText(this, "Account deleted.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete account.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void confirmDeleteAccount() {

        // Show confirmation dialog
        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                .setNegativeButton("No", null) // Dismiss the dialog
                .show();

    }
}

