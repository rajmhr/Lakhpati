package com.lakhpati.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.GroupCampaignApiInterface;
import com.lakhpati.Services.MyTicketsApiInterface;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.adapters.AllUserTicketsAdapter;
import com.lakhpati.adapters.MyTicketsAdapter;
import com.lakhpati.models.AllUserTicketViewModel;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.MyTicketsViewModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUserTicketsFragment extends Fragment {
    @BindView(R.id.alluser_Tickets_recycler_view)
    RecyclerView alluser_Tickets_recycler_view;

    private AllUserTicketsAdapter allUserTicketsAdapter;

    private LotteryGroupCampaignDetailModel myGroupModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_alluser_tickets, parent, false);

        myGroupModel = GroupDetailActivity.commonFragmentModel;

        ButterKnife.bind(this, view);
        getAllUserTickets();
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
    private void getAllUserTickets() {
        MyTicketsApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(MyTicketsApiInterface.class);

        Call<ReturnModel> callValue = grpCampaignApi.getAllUserTicketsByGroupCampaignId(myGroupModel.getGroupCampaignId());

        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    populateRecycleView(returnData);
                }
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getActivity());
            }
        });
    }

    private void populateRecycleView(String returnData) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        List<AllUserTicketViewModel> models = gson.fromJson(returnData, new TypeToken<List<AllUserTicketViewModel>>() {
        }.getType());

        allUserTicketsAdapter = new AllUserTicketsAdapter(models, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        alluser_Tickets_recycler_view.setLayoutManager(mLayoutManager);
        alluser_Tickets_recycler_view.setItemAnimator(new DefaultItemAnimator());
        alluser_Tickets_recycler_view.setAdapter(allUserTicketsAdapter);
        allUserTicketsAdapter.notifyDataSetChanged();
    }
}