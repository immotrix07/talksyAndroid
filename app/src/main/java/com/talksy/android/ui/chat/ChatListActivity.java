package com.talksy.android.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.talksy.android.data.models.Chat;
import com.talksy.android.data.network.ApiClient;
import com.talksy.android.data.network.ApiClient.ApiService;
import com.talksy.android.databinding.ActivityChatListBinding;
import com.talksy.android.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.OnItemClickListener {

    private ActivityChatListBinding binding;
    private ChatListAdapter chatListAdapter;
    private List<Chat> chatList;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getClient(this).create(ApiService.class);
        tokenManager = new TokenManager(this);

        chatList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatList);
        chatListAdapter.setOnItemClickListener(this);

        binding.recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChats.setAdapter(chatListAdapter);

        fetchChats();
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerViewChats.setVisibility(View.GONE);
        binding.tvEmptyState.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        binding.tvEmptyState.setVisibility(View.VISIBLE);
        binding.recyclerViewChats.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        binding.tvEmptyState.setVisibility(View.GONE);
        binding.recyclerViewChats.setVisibility(View.VISIBLE);
    }

    private void fetchChats() {
        showLoading();
        if (!tokenManager.isLoggedIn()) {
            hideLoading();
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            // Redirect to login if not logged in
            // startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        apiService.getChats().enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    chatList.clear();
                    chatList.addAll(response.body());
                    chatListAdapter.notifyDataSetChanged();
                    if (chatList.isEmpty()) {
                        showEmptyState();
                    } else {
                        hideEmptyState();
                    }
                } else {
                    Toast.makeText(ChatListActivity.this, "Failed to fetch chats: " + response.message(), Toast.LENGTH_SHORT).show();
                    showEmptyState(); // Show empty state on failure as well, or a specific error state
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                hideLoading();
                Toast.makeText(ChatListActivity.this, "Error fetching chats: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showEmptyState(); // Show empty state on network error
            }
        });
    }

    @Override
    public void onItemClick(Chat chat) {
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
        intent.putExtra("chatId", chat.getId());
        // You can pass other chat details if needed
        startActivity(intent);
    }
}
