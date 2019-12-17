package com.lakhpati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lakhpati.R;
import com.lakhpati.models.MyTicketsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyTicketsAdapter extends RecyclerView.Adapter<MyTicketsAdapter.MyViewHolder> implements Filterable {

    private List<MyTicketsViewModel> myTicketList;
    private List<MyTicketsViewModel> orgMyTicketList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_ticketNo, txt_purchasedDate;

        public MyViewHolder(View view) {
            super(view);
            txt_ticketNo = view.findViewById(R.id.txt_ticketNo);
            txt_purchasedDate= view.findViewById(R.id.txt_purchasedDate);

        }
    }

    public MyTicketsAdapter(List<MyTicketsViewModel> myTicketList, Context context) {
        this.myTicketList = myTicketList;
        this.context = context;
        this.orgMyTicketList = myTicketList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mytickets_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyTicketsViewModel myTicketItem = myTicketList.get(position);
        holder.txt_ticketNo.setText(myTicketItem.getTicketNo());
        holder.txt_purchasedDate.setText("purchased on "+ myTicketItem.getPurchasedate().toString().substring(0,10));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                final FilterResults oReturn = new FilterResults();
                if (charString.isEmpty())
                    myTicketList = orgMyTicketList;
                else {
                    List<MyTicketsViewModel> filteredList = new ArrayList<MyTicketsViewModel>();
                    for (final MyTicketsViewModel g : orgMyTicketList) {
                        if (g.getTicketNo().toLowerCase()
                                .contains(constraint.toString().toLowerCase()))
                            filteredList.add(g);
                    }
                    myTicketList = filteredList;
                }
                oReturn.values = myTicketList;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                myTicketList = (List<MyTicketsViewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void addItem(MyTicketsViewModel model) {
        myTicketList.add(model);
        notifyItemInserted(myTicketList.size());
    }

    @Override
    public int getItemCount() {
        return myTicketList.size();
    }
}
