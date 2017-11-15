package net.kaijie.campus_ui.ChatResource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.kaijie.campus_ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/11/15.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left == 1) {
            convertView = inflater.inflate(R.layout.list_item_recv, parent, false);
        }else if(chatMessageObj.left == 2){
            convertView = inflater.inflate(R.layout.list_item_send, parent, false);
        }else{
            convertView = inflater.inflate(R.layout.list_item_server, parent, false);
        }

        TextView chatText = (TextView) convertView.findViewById(R.id.tv_item_msg);
        TextView chatUser = (TextView) convertView.findViewById(R.id.tv_item_user);
        chatText.setText(chatMessageObj.message);
        switch (chatMessageObj.left){
            case 1:
                String user = chatMessageObj.name;
                chatUser.setText(user);
                break;
            case 2:
                chatUser.setText("");
                break;
        }
        return convertView;
    }
}
