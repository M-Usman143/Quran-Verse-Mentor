package com.example.quranmentor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quranmentor.R;
import com.example.quranmentor.activities.TeacherDetail;
import com.example.quranmentor.models.TeacherProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class selectteacherAdapterClass extends RecyclerView.Adapter<selectteacherAdapterClass.ViewHolder> {
    private List<TeacherProfile> teacherProfileList;
    private Context context;
    private OnItemClickListener listener;

    public selectteacherAdapterClass(List<TeacherProfile> teacherProfileList, Context context) {
        this.teacherProfileList = teacherProfileList;
        this.context = context;}
    // Add this method to your adapter class
    public void addTeacherProfile(TeacherProfile teacherProfile) {
        teacherProfileList.add(teacherProfile);
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }
    public void setFiltertedList(List<TeacherProfile> filterList){
        this.teacherProfileList = filterList;
        notifyDataSetChanged();
    }
    // Method to set the click listener for items
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface to define the item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacherprofile_rec_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
// Get the current Teacher object
        TeacherProfile teacher = teacherProfileList.get(position);
        holder.bind(teacher);
 // Set onClickListener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teacherId = teacher.getTeacherID(); // Get the ID of the clicked teacher
                Intent intent = new Intent(context, TeacherDetail.class);
                intent.putExtra("teacherID", teacherId);// Pass teacher ID to TeacherDetail activity
                intent.putExtra("fullname", teacher.getName());
                intent.putExtra("profileImageUrl", teacher.getProfileImageUri());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, genderTextView, ratesTextView , citytextview , countrytextview;
        public Button completeInfoTextView;
        public CircleImageView profileImageView;
        public RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_teacher_name);
            genderTextView = itemView.findViewById(R.id.text_view_gender);
            ratesTextView = itemView.findViewById(R.id.text_view_rates);
            profileImageView = itemView.findViewById(R.id.teacher_profile_recView);
            citytextview = itemView.findViewById(R.id.text_view_city);
            countrytextview = itemView.findViewById(R.id.text_view_country);
            ratingBar = itemView.findViewById(R.id.teacherRatingBar);
            completeInfoTextView = itemView.findViewById(R.id.completeInfo);

}
        public void bind(TeacherProfile teacherProfile) {
            Log.d("TeacherProfileList", "Size: " + teacherProfileList.size());
            for (TeacherProfile profile : teacherProfileList) {
                Log.d("TeacherProfile", "Name: " + profile.getName() + ", Gender: " + profile.getGender() + ", Rates: " + profile.getPrice());
            }
            // Set the data to the views in the ViewHolder
            Glide.with(context)
                    .load(teacherProfile.getProfileImageUri())
                    .into(profileImageView);
            nameTextView.setText(teacherProfile.getName());
            genderTextView.setText(teacherProfile.getGender());
            String ratess = teacherProfile.getPrice()+ " PKR/Month";
            ratesTextView.setText(ratess);
            String cityCountry = teacherProfile.getCity() + ", ";
            citytextview.setText(cityCountry);
            countrytextview.setText(String.valueOf(teacherProfile.getCountry()));
            fetchAndSetAverageRating(teacherProfile.getTeacherID());


        }
        private void fetchAndSetAverageRating(String teacherId) {
            DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedback").child(teacherId);
            feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    float totalRating = 0;
                    int totalReviews = 0;

                    for (DataSnapshot feedbackSnapshot : dataSnapshot.getChildren()) {
                        Float rating = feedbackSnapshot.child("rating").getValue(Float.class);
                        if (rating != null) {
                            totalRating += rating;
                            totalReviews++;
                        }
                    }

                    if (totalReviews > 0) {
                        float averageRating = totalRating / totalReviews;
                        ratingBar.setRating(averageRating);
                        ratingBar.setIsIndicator(true); // Make the RatingBar non-interactive
                    } else {
                        ratingBar.setRating(0);
                        ratingBar.setIsIndicator(true); // Make the RatingBar non-interactive
                    }
                    changeStarColor(ratingBar);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to fetch ratings: " + databaseError.getMessage());
                }
            });

        }

    }
    private void changeStarColor(RatingBar ratingBar) {
            int goldColor = ratingBar.getContext().getResources().getColor(android.R.color.holo_blue_light); // Get gold color
            ratingBar.setProgressTintMode(PorterDuff.Mode.SRC_ATOP); // Apply color to stars
            ratingBar.setProgressTintList(android.content.res.ColorStateList.valueOf(goldColor));}

}