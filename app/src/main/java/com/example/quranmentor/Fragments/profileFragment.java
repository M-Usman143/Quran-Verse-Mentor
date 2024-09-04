package com.example.quranmentor.Fragments;//package com.example.quranmentor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quranmentor.R;
import com.example.quranmentor.activities.Sign_up;
import com.example.quranmentor.models.Teacher;
import com.example.quranmentor.Adapters.selectteacherAdapterClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class profileFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String PROFILE_IMAGE_KEY = "profileImage";
    selectteacherAdapterClass adapter;
    ImageView backarrow;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private static final int PICK_FRONT_CNIC_REQUEST_CODE = 2;
    private static final int PICK_BACK_CNIC_REQUEST_CODE = 3;
    private static final int PICK_CERTIFICATE_REQUEST_CODE = 4;
    private static final int PICK_IMAGE_REQUEST_CODE = 5;

    private boolean frontCNICUploaded = false;
    private boolean backCNICUploaded = false;
    private boolean certificateUploaded = false;

    TextInputLayout qualification, rates, experience, biography , teacher_city , tecaher_country , specilizations;
    AppCompatButton movingbtn, certificateBTN, frontCNICbtn, backCNICbtn;
    ProgressBar progressBar;
     FrameLayout blurView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private Spinner spinneroption;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teacher_profile, container, false);

        movingbtn = rootView.findViewById(R.id.movingButton);
        qualification = rootView.findViewById(R.id.Qualification);
        experience = rootView.findViewById(R.id.teaching_experience);
        rates = rootView.findViewById(R.id.fees);
        biography = rootView.findViewById(R.id.aboutyourself);
        tecaher_country = rootView.findViewById(R.id.country);
        teacher_city = rootView.findViewById(R.id.City);
        specilizations = rootView.findViewById(R.id.Specilization);
        spinneroption = rootView.findViewById(R.id.spinner_gender);
        frontCNICbtn = rootView.findViewById(R.id.frontCnicPIC);
        backCNICbtn = rootView.findViewById(R.id.backCnicPIC);
        certificateBTN = rootView.findViewById(R.id.certificate);
        progressBar = rootView.findViewById(R.id.progressBar);
        blurView = rootView.findViewById(R.id.blurView);
        backarrow = rootView.findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity() , Sign_up.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("teachers_info");
        mStorage = FirebaseStorage.getInstance().getReference();


        frontCNICbtn.setOnClickListener(v -> chooseFile("Select CNIC Front", PICK_FRONT_CNIC_REQUEST_CODE));
        backCNICbtn.setOnClickListener(v -> chooseFile("Select CNIC Back", PICK_BACK_CNIC_REQUEST_CODE));
        certificateBTN.setOnClickListener(v -> chooseFile("Select Certificate", PICK_CERTIFICATE_REQUEST_CODE));

        movingbtn.setOnClickListener(v -> movingButtonClicked());
        setUpGenderSpinner();
        hideProgressBar();
        return rootView;}

    //=====================================================================================
    //===================================================STARTING TO INFO FUNCTIONALITIES=======
    //=====================================================================================
    private void updateUserProfile(String qualification, String experience, String rates, String description
            , String gender , String city , String country , String specilization) {
        if (qualification.isEmpty() && experience.isEmpty() && rates.isEmpty() && description.isEmpty() && gender.isEmpty()
                && description.isEmpty() && description.isEmpty() && description.isEmpty() && city.isEmpty()
                && country.isEmpty()&& specilization.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Teacher user = new Teacher(qualification, experience, rates, description, gender ,city , country , specilization);

       // adapter.addTeacherProfile(teacherProfile);
        mDatabase.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(getActivity(), "Failed to save user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.toException().printStackTrace();
                } else {
                    Toast.makeText(getActivity(), "User data saved successfully!", Toast.LENGTH_SHORT).show();
                    clearInputs();}}
        });
    }
    private void clearInputs() {
        experience.getEditText().setText("");
        qualification.getEditText().setText("");
        rates.getEditText().setText("");
        biography.getEditText().setText("");
        teacher_city.getEditText().setText("");
        tecaher_country.getEditText().setText("");
        specilizations.getEditText().setText("");
        spinneroption.setSelection(0); // Reset spinner to default selection
    }
    private boolean isValid(String qualification, String experience, String rates, String description,
                            String gender, String city, String country, String specilization) {
        // Validate user inputs (you can add more complex validation logic here)
        return !qualification.isEmpty() && !experience.isEmpty() && !rates.isEmpty() && !description.isEmpty()
                && !gender.isEmpty() && !city.isEmpty() && !country.isEmpty() && !specilization.isEmpty();
    }

    private void movingButtonClicked() {
        String qualifications = qualification.getEditText().getText().toString().trim();
        String experiences = experience.getEditText().getText().toString().trim();
        String budget = rates.getEditText().getText().toString().trim();
        String description = biography.getEditText().getText().toString().trim();
        String country = tecaher_country.getEditText().getText().toString().trim();
        String city = teacher_city.getEditText().getText().toString().trim();
        String specilization = specilizations.getEditText().getText().toString().trim();
        String gender = spinneroption.getSelectedItem().toString(); // Get selected gender from spinner
        // Check if all fields are filled and an image is uploaded
        if (isValid(qualifications, experiences, budget, description,country,city, specilization , gender)) {

            // Get the image URI from SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PROFILE_IMAGE_KEY, requireActivity().MODE_PRIVATE);
            String imageUri = sharedPreferences.getString(PROFILE_IMAGE_KEY, "");
            boolean filesUploaded = frontCNICUploaded && backCNICUploaded && certificateUploaded;
          if(filesUploaded){
                  showBlurEffect(true);
                  progressBar.setVisibility(View.VISIBLE);
                  updateUserProfile(qualifications, experiences, budget, description, gender ,city ,country ,specilization);
                 simulateProgress();}
            // Call the function to retrieve teacher data
        } else {
            // Show error message or prompt user to complete all fields
            Toast.makeText(getActivity(), "Please fill in all fields and upload profile image", Toast.LENGTH_SHORT).show();}
    }


    private void setUpGenderSpinner() {
        List<String> genderOptions = new ArrayList<>();
        genderOptions.add("Select Gender");
        genderOptions.add("Male");
        genderOptions.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneroption.setAdapter(adapter);
    }


    //=====================================================================================
    //===================================================ENDING TO INFO FUNCTIONALITIES=======
    //=====================================================================================


    private void navigateToNextFragment() {
        Fragment profileFragment = new ProfileImage_fragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //=====================================================================================
    //===================================================Starting OF UPLOADING FILE FUNCTIONALITIES=======
    //=====================================================================================



 @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            AppCompatButton buttonToUpdate = null;
            if (requestCode == PICK_FRONT_CNIC_REQUEST_CODE) {
                buttonToUpdate = frontCNICbtn;
            } else if (requestCode == PICK_BACK_CNIC_REQUEST_CODE) {
                buttonToUpdate = backCNICbtn;
            } else if (requestCode == PICK_CERTIFICATE_REQUEST_CODE) {
                buttonToUpdate = certificateBTN;
            }
            if (buttonToUpdate != null) {
                uploadFile(fileUri, buttonToUpdate);
            } else {
                Toast.makeText(requireActivity(), "Invalid request code", Toast.LENGTH_SHORT).show();}
        } else {
            Toast.makeText(requireActivity(), "Failed to retrieve file URI", Toast.LENGTH_SHORT).show();}
    }
    private void chooseFile(String title, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // Allow all file types to be selected
        startActivityForResult(intent, requestCode);}

    private void uploadFile(Uri fileUri, AppCompatButton button) {
        // Get the file name
        String fileName = fileUri.getLastPathSegment();
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Create a reference to the file to be uploaded with the user's ID as the file name
        StorageReference fileReference = mStorage.child("teacher_info").child(userId).child(fileName);
        // Upload file to Firebase Storage
        fileReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // File uploaded successfully
                    Toast.makeText(getActivity(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    // Set the corresponding boolean variable to true
                    if (button == frontCNICbtn) {
                        frontCNICUploaded = true;
                    } else if (button == backCNICbtn) {
                        backCNICUploaded = true;
                    } else if (button == certificateBTN) {
                        certificateUploaded = true;}
                    button.setBackgroundColor(Color.GREEN);
                })
                .addOnFailureListener(e -> {
                    // Error occurred during upload
                    Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    //=====================================================================================
    //===================================================ENDING OF UPLOADING FILE FUNCTIONALITIES=======
    //=====================================================================================






    //=====================================================================================
    //===================================================Starting OF UPLOADING FILE FUNCTIONALITIES=======
    //=====================================================================================
    private void simulateProgress() {
        final int progressIncrement = 10; // Increment progress by 10% at a time
        final int maxProgress = 100; // Maximum progress value
        final int[] currentProgress = {0}; // Current progress

        // Create a background task to simulate progress
        AsyncTask<Void, Integer, Void> progressTask = new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Simulate progress by incrementing progress value
                while (currentProgress[0] < maxProgress) {
                    try {
                        Thread.sleep(500); // Simulate delay
                        currentProgress[0] += progressIncrement;
                        publishProgress(currentProgress[0]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]); // Update ProgressBar with current progress value
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                navigateToNextFragment(); // Navigate to next activity
            }
        };

        progressTask.execute(); // Execute the background task
    }


    private void showBlurEffect(boolean show) {
        if (show) {
            // Apply blur effect
            blurView.setVisibility(View.VISIBLE);}
        else {
            // Remove blur effect
            blurView.setVisibility(View.GONE);}
        }
    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    //=====================================================================================
    //===================================================ENDING OF PROGRESSION FUNCTIONALITIES=======
    //=====================================================================================
}