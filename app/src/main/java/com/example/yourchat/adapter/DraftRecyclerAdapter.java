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
    List<Draft> messages;

    OnClickListener listener;
    public DraftRecyclerAdapter(List<Draft> dataSet, OnClickListener listener) {
        messages = dataSet;
        this.listener = listener;
    }

    public void setMessages(List<Draft> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }

    @NonNull
    @Override
    public DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draft_message_recycler_row, parent, false);
        return new DraftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraftViewHolder holder, int position) {
        Draft draft = messages.get(position);
        holder.messageTextview.setText(draft.message);
        holder.draftMessageLayout.setOnClickListener(view -> listener.onClick(draft));
    }

    @Override
    public int getItemCount() {
        if (messages != null ){
            return messages.size();
        }
        return 0;
    }

    public static class DraftViewHolder extends RecyclerView.ViewHolder {

        LinearLayout draftMessageLayout;
        TextView messageTextview;

        public DraftViewHolder(@NonNull View itemView) {
            super(itemView);

            draftMessageLayout = itemView.findViewById(R.id.draft_message_layout);
            messageTextview = itemView.findViewById(R.id.message_textview);
        }
    }

    public interface OnClickListener {
        void onClick(Draft draft);
    }
}