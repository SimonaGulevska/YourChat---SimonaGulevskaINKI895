package com.example.yourchat.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourchat.R;
import com.example.yourchat.db.Draft;

import java.util.List;

public class DraftRecyclerAdapter extends RecyclerView.Adapter<DraftRecyclerAdapter.DraftViewHolder> {
    // List of all Draft messages
    List<Draft> messages;
    //listener for clicking elements from the list
    OnClickListener listener;

    // Adapter's Constructor that receives the list of messages and the click listener
    public DraftRecyclerAdapter(List<Draft> dataSet, OnClickListener listener) {
        messages = dataSet;
        this.listener = listener;
    }
    //Method for updating the List of Draft messages in the adapter
    public void setMessages(List<Draft> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }

    @NonNull
    @Override
    public DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate XML for single row from RecyclerView (redot za draft poraka)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draft_message_recycler_row, parent, false);
        // return new ViewHolder with inflated layout (view)
        return new DraftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraftViewHolder holder, int position) {
        // takes specific message from the List by position
        Draft draft = messages.get(position);
        // sets content of the message in the text field
        holder.messageTextview.setText(draft.message);
        // We set a click listener that when the message is clicked, calls the onClick method and sends it the Message
        holder.draftMessageLayout.setOnClickListener(view -> listener.onClick(draft));
    }

    // Returns how many elements there are in the List of Draft messages (if the list is not empty)
    @Override
    public int getItemCount() {
        if (messages != null ){
            return messages.size();
        }
        //if the list is empty
        return 0;
    }

    // Inner class that represents the ViewHolder for a single row
    public static class DraftViewHolder extends RecyclerView.ViewHolder {
        // Linear Layout for the Draft message
        LinearLayout draftMessageLayout;
        // TextView for the Draft message
        TextView messageTextview;

        public DraftViewHolder(@NonNull View itemView) {
            super(itemView);

            // Finding the layout from XML by ID
            draftMessageLayout = itemView.findViewById(R.id.draft_message_layout);
            // Finding the TextView from the XML by ID
            messageTextview = itemView.findViewById(R.id.message_textview);
        }
    }
    // Interface for what will happen when the Draft message is clicked
    public interface OnClickListener {
        void onClick(Draft draft);
    }
}