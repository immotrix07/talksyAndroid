package com.talksy.android.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.talksy.android.BuildConfig;
import com.talksy.android.data.models.Chat;
import com.talksy.android.data.models.Message;
import com.talksy.android.data.models.User;
import com.talksy.android.data.network.ApiClient;
import com.talksy.android.data.network.ApiClient.ApiService;
import com.talksy.android.databinding.ActivityChatBinding;
import com.talksy.android.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private ApiService apiService;
    private TokenManager tokenManager;
    private String chatId;
    private Socket mSocket;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatId = getIntent().getStringExtra("chatId");
        if (chatId == null) {
            Toast.makeText(this, "Chat ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = ApiClient.getClient(this).create(ApiService.class);
        tokenManager = new TokenManager(this);

        // Get current user details from TokenManager (assuming they are saved there)
        currentUser = new User();
        currentUser.setId(tokenManager.getUserId());
        currentUser.setFullName(tokenManager.getUserFullName());
        currentUser.setEmail(tokenManager.getUserEmail());

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUser.getId());
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessages.setAdapter(messageAdapter);

        fetchMessages();
        setupSocket();

        binding.buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerViewMessages.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerViewMessages.setVisibility(View.VISIBLE);
    }

    private void fetchMessages() {
        showLoading();
        apiService.getMessages(chatId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                    binding.recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to fetch messages: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                hideLoading();
                Toast.makeText(ChatActivity.this, "Error fetching messages: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSocket() {
        try {
            IO.Options options = new IO.Options();
            options.query = "token=" + tokenManager.getToken();
            mSocket = IO.socket(BuildConfig.SOCKET_URL, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(this, "Socket connection error", Toast.LENGTH_SHORT).show();
            return;
        }

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("Socket.IO", "Connected");
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Connected to chat", Toast.LENGTH_SHORT).show());
                // Join the chat room
                mSocket.emit("joinChat", chatId);
            }
        }).on("messageReceived", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String messageId = data.getString("_id");
                    String chat_Id = data.getString("chat");
                    String content = data.getString("content");
                    String senderId = data.getJSONObject("sender").getString("_id");
                    String senderFullName = data.getJSONObject("sender").getString("fullName");
                    String createdAt = data.getString("createdAt");

                    User sender = new User();
                    sender.setId(senderId);
                    sender.setFullName(senderFullName);

                    Message newMessage = new Message();
                    newMessage.setId(messageId);
                    newMessage.setChatId(chat_Id);
                    newMessage.setContent(content);
                    newMessage.setSender(sender);
                    newMessage.setCreatedAt(createdAt);

                    runOnUiThread(() -> {
                        messageAdapter.addMessage(newMessage);
                        binding.recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    });

                } catch (JSONException e) {
                    Log.e("Socket.IO", "Error parsing message: " + e.getMessage());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("Socket.IO", "Disconnected");
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Disconnected from chat", Toast.LENGTH_SHORT).show());
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("Socket.IO", "Connection error: " + args[0]);
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Socket connection error: " + args[0], Toast.LENGTH_SHORT).show());
            }
        });

        mSocket.connect();
    }

    private void sendMessage() {
        String messageContent = binding.inputMessage.getText().toString().trim();
        if (TextUtils.isEmpty(messageContent)) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject message = new JSONObject();
            message.put("chatId", chatId);
            message.put("content", messageContent);
            // Emit message to server
            mSocket.emit("newMessage", message);

            binding.inputMessage.setText("");

            // Optimistically add message to UI (will be confirmed by socket event)
            Message optimisticMessage = new Message();
            optimisticMessage.setChatId(chatId);
            optimisticMessage.setContent(messageContent);
            optimisticMessage.setSender(currentUser);
            // Use current timestamp for optimistic update
            optimisticMessage.setCreatedAt(String.valueOf(System.currentTimeMillis())); 
            runOnUiThread(() -> {
                messageAdapter.addMessage(optimisticMessage);
                binding.recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            });

        } catch (JSONException e) {
            Log.e("ChatActivity", "Error creating JSON for message: " + e.getMessage());
            Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT);
            mSocket.off("messageReceived");
            mSocket.off(Socket.EVENT_DISCONNECT);
            mSocket.off(Socket.EVENT_CONNECT_ERROR);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
