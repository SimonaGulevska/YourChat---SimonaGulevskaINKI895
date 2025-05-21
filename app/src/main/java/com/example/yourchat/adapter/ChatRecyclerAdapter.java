package com.example.yourchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourchat.ChatActivity;
import com.example.yourchat.R;
import com.example.yourchat.model.ChatMessageModel;
import com.example.yourchat.utils.AndroidUtil;
import com.example.yourchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {
    //the Context from activity where this adapter is used
    Context context;
    //Constructor that takes options for FirestoreRecyclerAdapter and context
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context = context;
    }
    /**
     * Method which is called when data is connected to each ViewHolder row.
     * Selects whether the message should be displayed on the left or right.
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        Log.i("haushd","asjd");
        // Check if the message sender ID matches the current user ID
        if(model.getSenderId().equals(FirebaseUtil.currentUserId())){
            // If the message is sent by the current user: Hide the left chat layout (incoming message)
            holder.leftChatLayout.setVisibility(View.GONE);
            // Show the right chat layout (outgoing message)
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            // Set the message text in the right chat TextView
            holder.rightChatTextview.setText(model.getMessage());
        }else{
            // If the message is sent by another user: Hide the right chat layout (outgoing message)
            holder.rightChatLayout.setVisibility(View.GONE);
            // Show the left chat layout (incoming message)
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            // Set the message text in the left chat TextView
            holder.leftChatTextview.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate XML for single row from RecyclerView (redot za poraka)
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent,false);
        // return new ViewHolder with inflated layout (view)
        return new ChatModelViewHolder(view);
    }
    // Class ViewHolder which holds reference to all views (pogledi)
    class ChatModelViewHolder extends RecyclerView.ViewHolder{
        // Linear Layout for both right and left message
        LinearLayout leftChatLayout,rightChatLayout;
        // TextView for both right and left message
        TextView leftChatTextview,rightChatTextview;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            // Search and connect for the left layout of the message (received message)
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            // Search and connect for the right layout of the message (sent message)
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            // Search and connect of text field for left message
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            // Search and connect of text field for right message
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
