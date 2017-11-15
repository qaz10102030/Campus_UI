package net.kaijie.campus_ui.ChatResource;

/**
 * Created by User on 2017/11/15.
 */
public class ChatMessage {
    public int left; //0:系統訊息,1:別人的訊息,2:自己的訊息,
    public String message;
    public String name;

    public ChatMessage(int left, String message,String name) {
        super();
        this.left = left;
        this.message = message;
        this.name = name;
    }
}
