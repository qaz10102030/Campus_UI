package net.kaijie.campus_ui;

import android.content.Context;
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

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView recyText;
        public Viewholder(View itemView) {
            super(itemView);
            recyText = (TextView)itemView.findViewById(R.id.recyText);
        }
    }

    public TopicChatAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public TopicChatAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Viewholder holder = new Viewholder(LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false));
        return holder;
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
