package com.example.quranmentor.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quranmentor.R;
import com.example.quranmentor.activities.Feedback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback_receiving_bottom_sheet extends BottomSheetDialogFragment {
    private TextView feedbackTitle;
    private AppCompatButton giveFeedbackButton;
    private AppCompatButton notNowButton;
    private String teacherId;
    private String studentId;
    private String teacherName;
    private String studentName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feedback_receiving_bottom_sheet, container, false);
        // Initialize variables
        feedbackTitle = view.findViewById(R.id.feedbackTitle);
        giveFeedbackButton = view.findViewById(R.id.giveFeedbackButton);
        notNowButton = view.findViewById(R.id.notNowButton);

        // Get data from Bundle
        if (getArguments() != null) {
            teacherId = getArguments().getString("teacherId");
            studentId = getArguments().getString("studentId");
            teacherName = getArguments().getString("teacherName");
            studentName = getArguments().getString("studentName");
        }
        // Set up any other logic needed, like setting the text of the TextViews
        // Example:
        feedbackTitle.setText("Give Your Precious Feedback to your teacher: " + "'"+teacherName+"'");

        // Set up button listeners
        giveFeedbackButton.setOnClickListener(v -> {
            // Start Feedback activity and pass the data
            Intent intent = new Intent(getActivity(), Feedback.class);
            intent.putExtra("teacherId", teacherId);
            intent.putExtra("studentId", studentId);
            intent.putExtra("teacherName", teacherName);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
            // Handle feedback submission
        });
        notNowButton.setOnClickListener(v -> {
            // Handle dismissing the bottom sheet
            dismiss();
        });
        return view;
    }
}