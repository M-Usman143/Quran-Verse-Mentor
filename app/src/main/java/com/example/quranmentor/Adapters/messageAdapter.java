package com.example.quranmentor.Adapters;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.quranmentor.R;
import com.example.quranmentor.models.ChatingSystem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MessageViewHolder> {
    private List<ChatingSystem> messageList;
    private  static Context context;
    private static String  currentUserId;
    public messageAdapter(List<ChatingSystem> messageList, Context context , String currentUserId) {
        this.messageList = messageList;
        this.context = context;
        this.currentUserId = currentUserId;
    }
    public void addMessage(ChatingSystem message) {
        messageList.add(message);
        notifyDataSetChanged();
    }
    public void setMessages(List<ChatingSystem> messages) {
        this.messageList = messages;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view  ;
        if (viewType==MessageViewHolder.MessageType.SENT) {
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_messages_recyclerview, parent, false);
        } else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_messages_recyclerview, parent, false);
        }
        return new MessageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatingSystem message = messageList.get(position);

        holder.bind(message);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteMessageDialog(message);
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    @Override
    public int getItemViewType(int position) {
        ChatingSystem message = messageList.get(position);
        if (message.getUserID().equals(currentUserId)) {
            return MessageViewHolder.MessageType.SENT;
        } else {
            return MessageViewHolder.MessageType.RECEIVED;
        }
    }
    private void showDeleteMessageDialog(ChatingSystem message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Message");
        builder.setMessage("Are you sure you want to delete this message?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = messageList.indexOf(message);
                if (position != -1) {
                    // Remove the message from the database
                    String messageId = message.getMessageID();
                    String teacherId = message.getReceiverID();
                    String senderId = message.getUserID();

                    if (messageId != null) {
                        // Assuming you have a reference to the Firebase database
                        FirebaseDatabase.getInstance().getReference("chats").child(teacherId).child(senderId).child(messageId).removeValue();
                        FirebaseDatabase.getInstance().getReference("chats").child(senderId).child(teacherId).child(messageId).removeValue();
                    }
                    messageList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage , textViewtimeStamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.text_view_message_content);
            textViewtimeStamp = itemView.findViewById(R.id.text_view_timestamp);
        }
        public void bind(ChatingSystem message) {
            textViewMessage.setText(message.getMessage());
            if (message.getTimestamp() > 0) {
                textViewtimeStamp.setVisibility(View.VISIBLE);
                textViewtimeStamp.setText(formatTimeStamp(message.getTimestamp()));
            } else {
                textViewtimeStamp.setVisibility(View.GONE);
            }
        }
        private String formatTimeStamp(long timeStamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(new Date(timeStamp));
        }
        public interface MessageType {
            int SENT = 0;
            int RECEIVED = 1;
        }
    }
}
