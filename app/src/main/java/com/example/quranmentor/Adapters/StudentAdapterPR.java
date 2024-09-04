package com.example.quranmentor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranmentor.R;
import com.example.quranmentor.activities.ProgressReport_Gen;
import com.example.quranmentor.models.scheduleClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapterPR extends RecyclerView.Adapter<StudentAdapterPR.StudentViewHolder> {
    private List<scheduleClass> studentList;
    private Context context;
    public StudentAdapterPR(List<scheduleClass> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        scheduleClass student = studentList.get(position);
        holder.textViewStudentName.setText(student.getStudentName());

        // Load profile image using Picasso
        String studentID = student.getStudentID();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(studentID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).placeholder(R.drawable.men).into(holder.imageViewStudent);
                    } else {
                        Picasso.get().load(R.drawable.men).into(holder.imageViewStudent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load profile image: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProgressReport_Gen.class);
                intent.putExtra("studentID", studentID);
                intent.putExtra("studentName", student.getStudentName());
                intent.putExtra("teacherName", student.getTutorName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName;
        CircleImageView imageViewStudent;

        StudentViewHolder(View itemView) {
            super(itemView);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            imageViewStudent = itemView.findViewById(R.id.imageViewStudent);
        }
    }
}

