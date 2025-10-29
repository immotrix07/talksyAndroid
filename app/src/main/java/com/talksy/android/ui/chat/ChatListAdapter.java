package com.talksy.android.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talksy.android.R;
import com.talksy.android.data.models.Chat;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<Chat> chatList;
    private OnItemClickListener listener;

    public ChatListAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void updateChatList(List<Chat> newChatList) {
        this.chatList.clear();
        this.chatList.addAll(newChatList);
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatarInitials;
        TextView tvChatName;
        TextView tvLastMessage;
        TextView tvTimestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatarInitials = itemView.findViewById(R.id.tv_avatar_initials);
            tvChatName = itemView.findViewById(R.id.tv_chat_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(chatList.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Chat chat) {
            // Set avatar initials (simplified for now, consider actual logic for group/individual chats)
            if (chat.getChatName() != null && !chat.getChatName().isEmpty()) {
                tvAvatarInitials.setText(String.valueOf(chat.getChatName().charAt(0)).toUpperCase());
            } else if (chat.getUsers() != null && !chat.getUsers().isEmpty()) {
                // For 1:1 chat, display the other user's initial
                // This needs more sophisticated logic to determine the other user
                tvAvatarInitials.setText("C"); // Placeholder
            } else {
                tvAvatarInitials.setText("?");
            }

            tvChatName.setText(chat.getChatName() != null ? chat.getChatName() : "Unknown Chat");

            if (chat.getLastMessage() != null) {
                tvLastMessage.setText(chat.getLastMessage().getContent());
                // TODO: Format timestamp properly
                tvTimestamp.setText(chat.getLastMessage().getCreatedAt());
            } else {
                tvLastMessage.setText("No messages yet");
                tvTimestamp.setText("");
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Chat chat);
    }
}
