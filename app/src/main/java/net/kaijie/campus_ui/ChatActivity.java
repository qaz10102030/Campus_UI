package net.kaijie.campus_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.kaijie.campus_ui.ChatResource.ChatAdapter;
import net.kaijie.campus_ui.ChatResource.ChatMessage;
import net.kaijie.campus_ui.NetworkResource.ChatSocket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 2017/11/15.
 */
public class ChatActivity extends AppCompatActivity{
    public ChatSocket chatSocket;
    private ChatAdapter chatAdapter;
    private EditText chatText;
    public String username;
    public String roomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        username = getIntent().getExtras().getString("username");
        roomID = getIntent().getExtras().getString("roomID");
        chatSocket = MainActivity.mainActivity.chatSocket;
        chatSocket.setSocketCallback(socketCallback);
        Log.d("Chat", chatSocket.isConnected + "");
        initView();
        chatAdapter.add(new ChatMessage(0, username + "已進入房間", username));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.chat_actionbar, null);
        TextView actionbar_room_num = (TextView) actionbarLayout.findViewById(R.id.actionbar_room_num);
        String forActionbar = roomID + " 房間";
        actionbar_room_num.setText(forActionbar);
        getSupportActionBar().setCustomView(actionbarLayout);
    }

    private void initView() {
        ImageButton buttonSend = (ImageButton) findViewById(R.id.bt_send);
        ListView listView = (ListView) findViewById(R.id.lv_chat);
        chatAdapter = new ChatAdapter(ChatActivity.this, R.layout.list_item_send);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatAdapter);
        chatText = (EditText) findViewById(R.id.et_send_msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && sendChatMessage();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String check = chatText.getText().toString();
                if(chatText.length() != 0 || !( check.trim().equals("") ) ) {
                    sendChatMessage();
                }
            }
        });
    }

    public ChatSocket.SocketCallback socketCallback = new ChatSocket.SocketCallback() {
        @Override
        public void onReceived(String result) {
            Log.d("Chat_Activity",result);
            try {
                RecvHandle(result);
            } catch (JSONException e) {
                Log.d("Chat_JSON_err",e.getMessage());
            }
        }

        @Override
        public void onError(Exception err) {
            Log.d("Chat_Activity_err",err.getMessage());
            finish();
        }
    };

    private void RecvHandle(String result) throws JSONException {
        JSONObject data = new JSONObject(result);
        final int type = data.optInt("type");
        final String username = data.optString("username");
        final String msg = data.optString("msg");
        if(!( username.equals(this.username ))) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.add(new ChatMessage(type, msg, username));
                }
            });
        }
    }

    private boolean sendChatMessage() {
        String msg = chatText.getText().toString().trim();
        chatAdapter.add(new ChatMessage(2, msg, username));
        chatSocket.emit(ChatSocket.Chat_Msg, msg);
        chatText.setText("");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatSocket.setSocketCallback(MainActivity.mainActivity.socketCallback);
        chatSocket.disconnect();
    }
}
