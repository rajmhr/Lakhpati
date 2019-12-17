package com.lakhpati.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lakhpati.R;
import com.lakhpati.models.GroupChatModel;
import com.lakhpati.models.Message;
import com.lakhpati.models.MyTicketsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {

    List<Message> messages = new ArrayList<Message>();
    List<GroupChatModel> chatMessages = new ArrayList<GroupChatModel>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View avatar;
        private TextView name;
        private TextView messageBody;

        public MyViewHolder(View view) {
            super(view);
            messageBody = view.findViewById(R.id.message_body);
            avatar = view.findViewById(R.id.avatar);
            name = view.findViewById(R.id.name);
        }
    }

    public ChatMessageAdapter(List<GroupChatModel> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mytickets_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message message = messages.get(position);

        if (message.isBelongsToCurrentUser()) {
            holder.messageBody.setText(message.getText());
        } else {

            holder.name.setText(message.getMemberData().getName());
            holder.messageBody.setText(message.getText());
            GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
            drawable.setColor(Color.parseColor(message.getMemberData().getColor()));
        }
    }

    public void addItem(Message model) {
        this.messages.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
