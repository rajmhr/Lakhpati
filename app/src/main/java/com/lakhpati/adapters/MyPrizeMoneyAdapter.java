package com.lakhpati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lakhpati.R;
import com.lakhpati.models.LotteryHistoryModel;
import com.lakhpati.models.PrizeMoneyModel;

import java.util.ArrayList;
import java.util.List;

public class MyPrizeMoneyAdapter extends ArrayAdapter<PrizeMoneyModel> implements Filterable {
    private Context context;
    private List<PrizeMoneyModel> prizeMoneyModels;
    private List<PrizeMoneyModel> orgPrizeMoneyModels;
    private int resource;

    //constructor, call on creation
    public MyPrizeMoneyAdapter(Context context, int resource, ArrayList<PrizeMoneyModel> prizeMoneyModels) {
        super(context, resource, prizeMoneyModels);
        this.context = context;
        this.prizeMoneyModels = prizeMoneyModels;
        this.resource = resource;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        PrizeMoneyModel prizeMoneyModel = prizeMoneyModels.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        TextView title = view.findViewById(R.id.txt_prizemoney_title);
        TextView period = view.findViewById(R.id.txt_prizemoney_period);
        TextView prizeMoney = view.findViewById(R.id.txt_prizemoney_prizemoney);
        TextView remarks =view.findViewById(R.id.txt_prizemoney_remarks);
        title.setText(prizeMoneyModel.getGroupName() + "( " + prizeMoneyModel.getLotteryDefinition() + " )");
        period.setText("Started from " + prizeMoneyModel.getStartDate().toString().substring(0,10) + " to " + prizeMoneyModel.getCompletedDate().toString().substring(0,10));
        prizeMoney.setText("Prize Money : " + prizeMoneyModel.getBetAmount() * prizeMoneyModel.getTotalTicketCount() + "( " + prizeMoneyModel.getPaymentStatusDesc() + " )");
        if (prizeMoneyModel.getRemarks() != null && !prizeMoneyModel.getRemarks().trim().equals((""))) {
            remarks.setText("Remarks : " + prizeMoneyModel.getRemarks());
        }
        else {
            remarks.setText("Remarks : Nothing yet..");
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<PrizeMoneyModel> results = new ArrayList<PrizeMoneyModel>();
                if (orgPrizeMoneyModels == null)
                    orgPrizeMoneyModels = prizeMoneyModels;
                if (constraint != null) {
                    if (orgPrizeMoneyModels != null && orgPrizeMoneyModels.size() > 0) {
                        for (final PrizeMoneyModel g : orgPrizeMoneyModels) {
                            if (constraint.equals("-1+1")) {
                                results.add(g);
                            } else if (g.getGroupName().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                prizeMoneyModels = (ArrayList<PrizeMoneyModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return prizeMoneyModels.size();
    }

    @Override
    public PrizeMoneyModel getItem(int position) {
        return prizeMoneyModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
