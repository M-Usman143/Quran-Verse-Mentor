package com.example.quranmentor.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quranmentor.R;
import com.example.quranmentor.activities.Sign_up;
import com.example.quranmentor.activities.student_dashboard;
import com.example.quranmentor.activities.teacher_dashboard;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImage_fragment extends Fragment {
    CircleImageView circleImageView;
    ImageButton floatingActionButton;
    private FirebaseAuth mAuth;
    ImageView backarrow;
    AppCompatButton movingbtn;
    ProgressBar progressBar;
    FrameLayout blurView;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_image, container, false);

        circleImageView = rootView.findViewById(R.id.profiles);
        floatingActionButton = rootView.findViewById(R.id.selectImagebtn);
        movingbtn = rootView.findViewById(R.id.nextbtn);
        progressBar = rootView.findViewById(R.id.secondprogressbar);
        blurView = rootView.findViewById(R.id.secondblurView);
        backarrow = rootView.findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity() , Sign_up.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initialize Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
        floatingActionButton.setOnClickListener(v -> chooseImage());
        movingbtn.setOnClickListener(v -> simulateProgress());
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            UploadImage(imageUri);
        }
    }

    private void UploadImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Compress the image to reduce size
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageData = baos.toByteArray();

            // Get the current user's ID
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            StorageReference fileReference = storageReference.child("profile_images").child(userId);
            fileReference.putBytes(imageData)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            databaseReference.child(userId).child("profileImageUrl").setValue(uri.toString())
                                    .addOnSuccessListener(aVoid -> {
                                        Picasso.get()
                                                .load(uri)
                                                .networkPolicy(NetworkPolicy.OFFLINE) // Enable offline caching
                                                .into(circleImageView, new Callback() {
                                                    @Override
                                                    public void onSuccess() {
                                                    }
                                                    @Override
                                                    public void onError(Exception e) {
                                                        // Try again online if cache failed
                                                        Picasso.get().load(uri).into(circleImageView);
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error updating profile image URL
                                        Log.e(TAG, "Error updating profile image URL: " + e.getMessage());
                                        Toast.makeText(requireContext(), "Error updating profile image URL", Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred during upload
                        Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error compressing image", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage() {
        ImagePicker.with(this)
                .crop() // Enable cropping
                .compress(1024) // Compress image size
                .maxResultSize(1080, 1080) // Set max image resolution
                .start(PICK_IMAGE_REQUEST_CODE); // Start image picker activity with request code
    }

    private void simulateProgress() {
        // Show progress bar and blur view
        progressBar.setVisibility(View.VISIBLE);
        blurView.setVisibility(View.VISIBLE);

        // Simulate 5 seconds delay before navigating to the next screen
        new Handler().postDelayed(() -> {
            // Hide progress bar and blur view
            progressBar.setVisibility(View.GONE);
            blurView.setVisibility(View.GONE);

            SharedPreferences userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userRole = userPrefs.getString("spinnerOption", "");
            retrieveUserDataAndNavigate(userRole);
        }, 5000); // Delay for 5 seconds
    }

    private void retrieveUserDataAndNavigate(String userRole) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("fullname").getValue(String.class);
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                    navigateToDashboard(userRole, username, profileImageUrl);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error retrieving data from Firebase Database: " + error.getMessage());
                }
            });
        } else {
            Log.e(TAG, "Current user is null");
        }
    }
    private void navigateToDashboard(String userRole, String username, String profileImageUrl) {
        Intent intent;
        if (userRole.equals("As a Student")) {
            intent = new Intent(getActivity(), student_dashboard.class);
        } else if (userRole.equals("As a Teacher")) {
            intent = new Intent(getActivity(), teacher_dashboard.class);
        } else {
            Log.e(TAG, "Unexpected user role:" + userRole);
            return;
        }
        intent.putExtra("fullname", username);
        intent.putExtra("profileImageUrl", profileImageUrl);
        startActivity(intent);
    }
}
