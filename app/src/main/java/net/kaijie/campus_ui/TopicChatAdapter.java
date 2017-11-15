package net.kaijie.campus_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rabbit on 2017/10/26.
 */
public class TopicChatAdapter extends RecyclerView.Adapter<TopicChatAdapter.Viewholder> {

    private List<String> list;
    private Context context;

    public static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recyText;
        private MyItemClickListener mListener;
        public Viewholder(View itemView, MyItemClickListener listener) {
            super(itemView);
            mListener = listener;
            recyText = (TextView)itemView.findViewById(R.id.recyText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.clickOnView(v, getLayoutPosition());
        }

        public interface MyItemClickListener {
            void clickOnView(View v, int position);
        }
    }

    public TopicChatAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public TopicChatAdapter.Viewholder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false), new Viewholder.MyItemClickListener() {
            @Override
            public void clickOnView(View v, int position) {
                Snackbar.make(v, position + "", Snackbar.LENGTH_LONG).show();
                MainActivity.mainActivity.chatSocket.connect(position ,"rabbit-phone");
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("roomID", list.get(position));
                bundle.putString("username","rabbit-phone");
                intent.putExtras(bundle);
                intent.setClass(context,ChatActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(TopicChatAdapter.Viewholder holder, int position) {
        holder.recyText.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
