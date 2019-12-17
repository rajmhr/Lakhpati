package com.lakhpati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lakhpati.R;
import com.lakhpati.models.GroupMembersViewModel;
import com.lakhpati.models.RelatedLotteryGroupModel;

import java.util.ArrayList;
import java.util.List;

public class AssignAdminRecyclerAdapter extends RecyclerView.Adapter<AssignAdminRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<GroupMembersViewModel> myGroupList;
    private List<GroupMembersViewModel> orgMyGroupList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_userInfo;
        private CheckBox chk_IsAdmin;


        private MyViewHolder(View view) {
            super(view);
            txt_userInfo = view.findViewById(R.id.txt_userInfo);
            chk_IsAdmin = view.findViewById(R.id.chk_IsAdmin);
        }
    }


    public AssignAdminRecyclerAdapter(List<GroupMembersViewModel> myGroupList, Context context) {
        this.myGroupList = myGroupList;
        this.context = context;
        this.orgMyGroupList = myGroupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_assign_admin_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupMembersViewModel myGroupItem = myGroupList.get(position);
        holder.txt_userInfo.setText(myGroupItem.getName() + " ( " + myGroupItem.getEmail() + " ) ");
        holder.chk_IsAdmin.setChecked(myGroupItem.isAdmin());
        holder.chk_IsAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myGroupItem.setAdmin(isChecked);
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
                    List<GroupMembersViewModel> filteredList = new ArrayList<GroupMembersViewModel>();
                    for (final GroupMembersViewModel g : orgMyGroupList) {
                        if (g.getEmail().toLowerCase()
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
                myGroupList = (List<GroupMembersViewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public GroupMembersViewModel getItem(int position) {
        return myGroupList.get(position);
    }

    @Override
    public int getItemCount() {
        return myGroupList.size();
    }
}
