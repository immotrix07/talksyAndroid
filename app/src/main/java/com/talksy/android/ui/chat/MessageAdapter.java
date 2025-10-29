package com.talksy.android.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talksy.android.R;
import com.talksy.android.data.models.Message;
import com.talksy.android.utils.TokenManager;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSender().getId().equals(currentUserId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    // ViewHolder for sent messages
    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_message_content);
            timeText = itemView.findViewById(R.id.tv_message_timestamp);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());
            // TODO: Format timestamp properly
            timeText.setText(message.getCreatedAt());
        }
    }

    // ViewHolder for received messages
    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, senderText, senderInitials;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_message_content);
            timeText = itemView.findViewById(R.id.tv_message_timestamp);
            senderText = itemView.findViewById(R.id.tv_message_sender);
            senderInitials = itemView.findViewById(R.id.tv_sender_initials);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());
            // TODO: Format timestamp properly
            timeText.setText(message.getCreatedAt());
            senderText.setText(message.getSender().getFullName());
            if (message.getSender().getFullName() != null && !message.getSender().getFullName().isEmpty()) {
                senderInitials.setText(String.valueOf(message.getSender().getFullName().charAt(0)).toUpperCase());
            } else {
                senderInitials.setText("?");
            }
        }
    }
}
