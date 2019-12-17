package com.lakhpati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lakhpati.R;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.models.LotteryHistoryModel;
import com.lakhpati.models.MyTicketsViewModel;

import java.util.ArrayList;
import java.util.List;

public class GroupHistoryAdapter extends RecyclerView.Adapter<GroupHistoryAdapter.MyViewHolder> {

    private List<LotteryHistoryModel> myTicketList;
    private List<LotteryHistoryModel> orgMyTicketList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_campaignName, txt_period,txt_winner, txt_prizeMoney, txt_ticketPrice, txt_grouphistory_myTickets;
        ImageView img_trophy;

        public MyViewHolder(View view) {
            super(view);
            txt_campaignName = view.findViewById(R.id.txt_grouphistory_lotteryName);
            txt_period = view.findViewById(R.id.txt_grouphistory_period);
            img_trophy = view.findViewById(R.id.img_trophy);
            txt_winner = view.findViewById(R.id.txt_grouphistory_winner);
            txt_prizeMoney = view.findViewById(R.id.txt_grouphistory_prizeMoney);
            txt_ticketPrice = view.findViewById(R.id.txt_grouphistory_ticketPrice);
            txt_grouphistory_myTickets = view.findViewById(R.id.txt_grouphistory_myTickets);
        }
    }

    public GroupHistoryAdapter(List<LotteryHistoryModel> myTicketList, Context context) {
        this.myTicketList = myTicketList;
        this.context = context;
        this.orgMyTicketList = myTicketList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groupluckydrawhistory_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LotteryHistoryModel grpModel = myTicketList.get(position);

        holder.txt_campaignName.setText(grpModel.getLotteryName());

        holder.txt_period.setText("Completed on  " + grpModel.getCompletedDate().toString().substring(0,10));

        holder.txt_winner.setText("Winner : " + grpModel.getWinner() + " ( " + grpModel.getEmailId() + " )");

        holder.txt_prizeMoney.setText("Coin prize : " + grpModel.getNoOfTicketSold() * grpModel.getCoinPrize());

        holder.txt_ticketPrice.setText("Bet coin : " + grpModel.getCoinPrize() + " coins");


        if(grpModel.getEmailId().trim().equals(DrawerActivity.userCommonModel.getEmailId().trim())) {
            holder.txt_grouphistory_myTickets.setText("Congratulation!! you won this lottery.");
            holder.txt_grouphistory_myTickets.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.img_trophy.setVisibility(View.VISIBLE);
        }
        else{
            holder.txt_grouphistory_myTickets.setText("Opps!! You have lost this lottery.");
            holder.txt_grouphistory_myTickets.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.img_trophy.setVisibility(View.GONE);
        }
    }
    public void addItems(List<LotteryHistoryModel> modelList){
        myTicketList.clear();
        for (int i = 0; i< modelList.size(); i++){
            if(!modelList.get(i).getMyTickets().trim().equals("")){
                myTicketList.add(modelList.get(i));
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return myTicketList.size();
    }
}
