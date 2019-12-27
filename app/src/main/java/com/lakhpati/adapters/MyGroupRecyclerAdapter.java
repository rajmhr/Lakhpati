package com.lakhpati.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.lakhpati.R;
import com.lakhpati.Utilities.EnumCollection;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.models.RelatedLotteryGroupModel;

import java.util.ArrayList;
import java.util.List;

public class MyGroupRecyclerAdapter extends RecyclerView.Adapter<MyGroupRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<RelatedLotteryGroupModel> myGroupList;
    private List<RelatedLotteryGroupModel> orgMyGroupList;
    Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView grpName, notification_badge;
        private RelativeLayout myGroupListItem_layout;
        MaterialButton btn_inviteStatus;
        private ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            grpName = view.findViewById(R.id.groupName);
            btn_inviteStatus = view.findViewById(R.id.btn_inviteStatus);
            userImg = view.findViewById(R.id.img_user);
            notification_badge = view.findViewById(R.id.notification_badge);
            myGroupListItem_layout = view.findViewById(R.id.myGroupListItem_layout);
            myGroupListItem_layout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = context.getMenuInflater();

            int pos = this.getAdapterPosition();

            RelatedLotteryGroupModel item = myGroupList.get(pos);
            DrawerActivity.longPressSelectedItem = item;

            if (item.isAdmin()) {
                inflater.inflate(R.menu.menu_mygroup_admin, menu);
            } else
                inflater.inflate(R.menu.menu_mygroup, menu);

            menu.setHeaderTitle("-- Select Operations --");
            menu.setHeaderIcon(R.drawable.admin);
            menu.setGroupDividerEnabled(true);
        }
    }

    public MyGroupRecyclerAdapter(List<RelatedLotteryGroupModel> myGroupList, Activity context) {
        this.myGroupList = myGroupList;
        this.context = context;
        this.orgMyGroupList = myGroupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_mygroup, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RelatedLotteryGroupModel myGroupItem = myGroupList.get(position);
        holder.grpName.setText(myGroupItem.getGroupName());

        String status;
        if (!myGroupItem.getCampaignStatus().equals("")) {
            status = ("Lottery Status: " + myGroupItem.getCampaignStatus());
            if (!myGroupItem.getCampaignStatus().equals(EnumCollection.CampaignStatus.Completed.toString())) {
                holder.btn_inviteStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else if (!myGroupItem.getCampaignStatus().equals(EnumCollection.CampaignStatus.InProgress.toString())) {
                holder.btn_inviteStatus.setTextColor(context.getResources().getColor(R.color.light_blue));
            } else if (!myGroupItem.getCampaignStatus().equals(EnumCollection.CampaignStatus.Stopped.toString())) {
                holder.btn_inviteStatus.setTextColor(context.getResources().getColor(R.color.primary_darker));
            }
        } else {
            holder.btn_inviteStatus.setTextColor(context.getResources().getColor(R.color.black));
            status = "No active lottery.";
        }
        holder.btn_inviteStatus.setText(status);

        if (myGroupItem.isAdmin())
            holder.userImg.setImageResource(R.drawable.img_admin);

        holder.notification_badge.setText(String.valueOf(myGroupItem.getMembersInGroup()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     if(!CheckConnection.isNetworkConnected(context)){
                    ((DrawerActivity)context).onInternetUnavailable();
                    return;
                }*/
                Intent drawerIntent = new Intent(context, GroupDetailActivity.class);
                DrawerActivity.activeLotteryGroup = myGroupItem;
                context.startActivity(drawerIntent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                final FilterResults oReturn = new FilterResults();
                if (charString.isEmpty())
                    myGroupList = orgMyGroupList;
                else {
                    List<RelatedLotteryGroupModel> filteredList = new ArrayList<RelatedLotteryGroupModel>();
                    for (final RelatedLotteryGroupModel g : orgMyGroupList) {
                        if (g.getGroupName().toLowerCase()
                                .contains(constraint.toString().toLowerCase()))
                            filteredList.add(g);
                    }
                    myGroupList = filteredList;
                }
                oReturn.values = myGroupList;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                myGroupList = (List<RelatedLotteryGroupModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void removeItem(int groupId) {
        int listSize = myGroupList.size();
        for (int i = 0; i < listSize; i++) {
            if (myGroupList.get(i).getGroupId() == groupId) {
                RelatedLotteryGroupModel toRemove = myGroupList.get(i);
                myGroupList.remove(toRemove);
                notifyItemRemoved(i);
                ((DrawerActivity) context).noItemVisible(myGroupList.size() == 0);
                break;
            }
        }
    }

    public void addItem(RelatedLotteryGroupModel model) {
        myGroupList.add(model);
        notifyItemInserted(myGroupList.size() - 1);
        ((DrawerActivity) context).noItemVisible(false);
    }

    public void addItems(List<RelatedLotteryGroupModel> models) {
        myGroupList.clear();
        myGroupList.addAll(models);
    }

    public RelatedLotteryGroupModel getItem(int pos) {
        return myGroupList.get(pos);
    }

    @Override
    public int getItemCount() {
        return myGroupList.size();
    }
}
