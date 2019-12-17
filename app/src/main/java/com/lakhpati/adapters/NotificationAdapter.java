package com.lakhpati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lakhpati.R;
import com.lakhpati.Utilities.UtilityMethod;
import com.lakhpati.models.NotificationSpModel;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationSpModel> myNotificationList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_noti_groupName, txt_noti_message, txt_noti_date;

        public MyViewHolder(View view) {
            super(view);
            txt_noti_groupName = view.findViewById(R.id.txt_noti_groupName);
            txt_noti_message = view.findViewById(R.id.txt_noti_message);
            txt_noti_date = view.findViewById(R.id.txt_noti_date);
        }
    }

    public NotificationAdapter(List<NotificationSpModel> myNotificationList, Context context) {
        this.myNotificationList = myNotificationList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationSpModel notificationItem = myNotificationList.get(position);
        holder.txt_noti_groupName.setText(notificationItem.getGroupName());
        holder.txt_noti_date.setText(UtilityMethod.getDateString(notificationItem.getCreatedDate()));
        holder.txt_noti_message.setText(notificationItem.getMessage());
    }

    public void addItem(NotificationSpModel model) {
        myNotificationList.add(model);
        notifyItemInserted(myNotificationList.size());
    }

    public void addItems(List<NotificationSpModel> models) {
        myNotificationList.clear();
        myNotificationList.addAll(models);
        notifyDataSetChanged();
    }

    public  void clearItems(){
        myNotificationList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return myNotificationList.size();
    }
}
