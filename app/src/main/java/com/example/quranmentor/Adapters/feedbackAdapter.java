package com.example.quranmentor.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranmentor.R;
import com.example.quranmentor.models.Give_Feedback;
import com.example.quranmentor.models.scheduleClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
public class feedbackAdapter extends RecyclerView.Adapter<feedbackAdapter.feedbackViewHolder> {

    private Context context;
    private List<Give_Feedback> feedbackList;

    public feedbackAdapter(Context context, List<Give_Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }
    public void addFeedback(Give_Feedback feedback) {
        feedbackList.add(feedback);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public feedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feedback_recyclerview, parent, false);
        return new feedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull feedbackViewHolder holder, int position) {
        Give_Feedback feedback = feedbackList.get(position);
        holder.bind(feedback);
        changeStarColor(holder.ratingBar);

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class feedbackViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView,commentTextView,dateTextview,dislikeCounting,likeCounting;
        RatingBar ratingBar;
        ImageView likeEmoji,dislikeEmoji;
        private int likeCount = 0, dislikeCount = 0;

        public feedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentname);
            dateTextview = itemView.findViewById(R.id.feedbackdate);
            commentTextView = itemView.findViewById(R.id.comments);
            dislikeCounting = itemView.findViewById(R.id.dislkeCounting);
            likeCounting = itemView.findViewById(R.id.likeCounting);
            likeEmoji = itemView.findViewById(R.id.likeemoji);
            dislikeEmoji = itemView.findViewById(R.id.dislikeemoji);
            ratingBar = itemView.findViewById(R.id.ratingshow);
            ratingBar.setIsIndicator(true);


            likeEmoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeCount++;
                    likeCounting.setText(String.valueOf(likeCount));
                }
            });

            dislikeEmoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislikeCount++;
                    dislikeCounting.setText(String.valueOf(dislikeCount));
                }
            });

        }
        public void bind(Give_Feedback giveFeedback) {
            studentNameTextView.setText(giveFeedback.getStudentName());
            commentTextView.setText(giveFeedback.getComment());
            ratingBar.setRating(giveFeedback.getRating());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());
            String date = sdf.format(new Date(giveFeedback.getTimestamp()));
            dateTextview.setText(date);

        }

    }
    private void changeStarColor(RatingBar ratingBar) {
        int goldColor = ratingBar.getContext().getResources().getColor(android.R.color.holo_blue_light); // Get gold color
        ratingBar.setProgressTintMode(PorterDuff.Mode.SRC_ATOP); // Apply color to stars
        ratingBar.setProgressTintList(android.content.res.ColorStateList.valueOf(goldColor));
    }
    }