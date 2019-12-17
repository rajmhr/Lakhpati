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
import com.lakhpati.models.AllUserTicketViewModel;
import com.lakhpati.models.MyTicketsViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllUserTicketsAdapter extends RecyclerView.Adapter<AllUserTicketsAdapter.MyViewHolder> implements Filterable {

    private List<AllUserTicketViewModel> myTicketList;
    private List<AllUserTicketViewModel> orgMyTicketList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_ticketNo, txt_purchasedDate, txt_buyerInfo;

        public MyViewHolder(View view) {
            super(view);
            txt_ticketNo = view.findViewById(R.id.txt_ticketNo);
            txt_purchasedDate= view.findViewById(R.id.txt_purchasedDate);
            txt_buyerInfo= view.findViewById(R.id.txt_buyerInfo);
        }
    }

    public AllUserTicketsAdapter(List<AllUserTicketViewModel> myTicketList, Context context) {
        this.myTicketList = myTicketList;
        this.context = context;
        this.orgMyTicketList = myTicketList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_allusers_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AllUserTicketViewModel myTicketItem = myTicketList.get(position);
        holder.txt_ticketNo.setText(myTicketItem.getTicketNo());
        holder.txt_purchasedDate.setText("purchased on "+ myTicketItem.getPurchasedDate().toString().substring(0,10));
        holder.txt_buyerInfo.setText(myTicketItem.getDisplayName() + " ( "+ myTicketItem.getEmailId()+ " ) ");
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
                    List<AllUserTicketViewModel> filteredList = new ArrayList<AllUserTicketViewModel>();
                    for (final AllUserTicketViewModel g : orgMyTicketList) {
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
                myTicketList = (List<AllUserTicketViewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void addItem(AllUserTicketViewModel model) {
        myTicketList.add(model);
        notifyItemInserted(myTicketList.size());
    }

    @Override
    public int getItemCount() {
        return myTicketList.size();
    }
}
