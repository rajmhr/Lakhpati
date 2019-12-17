package com.lakhpati.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lakhpati.R;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.models.CoinHistoryModel;

import java.util.List;

public class CoinTransactionRecyclerAdapter extends RecyclerView.Adapter<CoinTransactionRecyclerAdapter.MyViewHolder> {

    private List<CoinHistoryModel> coinList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_coins, txt_date, txt_user, txt_outStandingcoins;

        public MyViewHolder(View view) {
            super(view);
            txt_coins = view.findViewById(R.id.txt_coins);
            txt_date = view.findViewById(R.id.txt_date);
            txt_user = view.findViewById(R.id.txt_user);
            txt_outStandingcoins = view.findViewById(R.id.txt_outStandingcoins);
        }
    }

    public CoinTransactionRecyclerAdapter(List<CoinHistoryModel> coinList, Context context) {
        this.coinList = coinList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_coin_transaction, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CoinHistoryModel coinItem = coinList.get(position);
        holder.txt_coins.setText(String.valueOf(coinItem.getCoins()) + " coin used. " + "'" + coinItem.getTransactionTypeDesc() + "'");
        holder.txt_date.setText("On " + coinItem.getTransactionDate().toString().substring(0, 10));
        holder.txt_user.setText("By " + coinItem.getFromUser());
        holder.txt_outStandingcoins.setText(coinItem.getOutstandingCoin() + " coins remaining.");
    }

    public void addListItem(List<CoinHistoryModel> models) {
        coinList.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }
}
