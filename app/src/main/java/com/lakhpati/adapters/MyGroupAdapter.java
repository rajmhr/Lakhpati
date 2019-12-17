package com.lakhpati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lakhpati.R;
import com.lakhpati.models.GroupMembersViewModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.RelatedLotteryGroupModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyGroupAdapter extends ArrayAdapter<RelatedLotteryGroupModel> implements Filterable {
    private Context context;
    private List<RelatedLotteryGroupModel> lotteryGroups;
    private List<RelatedLotteryGroupModel> orgLotteryGrops;
    private int resource;

    //constructor, call on creation
    public MyGroupAdapter(Context context, int resource, ArrayList<RelatedLotteryGroupModel> lotteryGroups) {
        super(context, resource, lotteryGroups);
        this.context = context;
        this.lotteryGroups = lotteryGroups;
        this.resource = resource;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        RelatedLotteryGroupModel grpModel = lotteryGroups.get(position);
        View view = convertView;
        if(view == null) {
             view = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView groupName = (TextView) view.findViewById(R.id.groupName);
        TextView inviteStatus = (TextView) view.findViewById(R.id.inviteStatus);
        ImageView img = (ImageView) view.findViewById(R.id.img_user);


        groupName.setText(grpModel.getGroupName());
        String status = "";
        if (!grpModel.getCampaignStatus().equals("")) {
            status = ("Lottery Status: " + String.valueOf(grpModel.getCampaignStatus()));
        } else {
            status = "No active lottery.";
        }
        inviteStatus.setText(status);
        if (grpModel.isAdmin()) {
            img.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.img_admin));
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<RelatedLotteryGroupModel> results = new ArrayList<RelatedLotteryGroupModel>();
                if (orgLotteryGrops == null)
                    orgLotteryGrops = lotteryGroups;
                if (constraint != null) {
                    if (orgLotteryGrops != null && orgLotteryGrops.size() > 0) {
                        for (final RelatedLotteryGroupModel g : orgLotteryGrops) {
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
                lotteryGroups = (ArrayList<RelatedLotteryGroupModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return lotteryGroups.size();
    }

    @Override
    public RelatedLotteryGroupModel getItem(int position) {
        return lotteryGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
