package com.lakhpati.fragments;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.lakhpati.Utilities.ListViewUtil;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.adapters.MyGroupRecyclerAdapter;
import com.lakhpati.adapters.MyTicketsAdapter;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.LotteryTicketViewModel;
import com.lakhpati.models.MyTicketsViewModel;
import com.lakhpati.models.RelatedLotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTicketsFragment extends Fragment {

    @BindView(R.id.myTickets_recycler_view)
    RecyclerView myTickets_recycler_view;

    private MyTicketsAdapter myTicketsAdapter;
    private LotteryGroupCampaignDetailModel myGroupModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytickets, parent, false);
        myGroupModel = GroupDetailActivity.commonFragmentModel;
        ButterKnife.bind(this, view);
        getMyTickets();
        return view;
    }

    private void getMyTickets() {
        MyTicketsApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(MyTicketsApiInterface.class);

        LotteryGroupCampaignModel model = new LotteryGroupCampaignModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setGroupCampaignId(myGroupModel.getGroupCampaignId());

        Call<ReturnModel> callValue = grpCampaignApi.getMyTicketByGroupCampaignId(model);

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

    public void reloadFragment() {
        Fragment frg = getFragmentManager().findFragmentById(R.id.view_pager);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void populateRecycleView(String returnData) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        List<MyTicketsViewModel> models = gson.fromJson(returnData, new TypeToken<List<MyTicketsViewModel>>() {
        }.getType());

        myTicketsAdapter = new MyTicketsAdapter(models, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myTickets_recycler_view.setLayoutManager(mLayoutManager);
        myTickets_recycler_view.setItemAnimator(new DefaultItemAnimator());
        myTickets_recycler_view.setAdapter(myTicketsAdapter);
        myTicketsAdapter.notifyDataSetChanged();
    }
}