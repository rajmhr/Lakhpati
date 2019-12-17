package com.lakhpati.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.GroupCampaignApiInterface;
import com.lakhpati.Services.LotteryHistoryApiInterface;
import com.lakhpati.Utilities.CheckConnection;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.adapters.GroupHistoryAdapter;
import com.lakhpati.customControl.ProgressLoader;
import com.lakhpati.models.LotteryHistoryModel;
import com.lakhpati.models.Pagination.CampaignGroupPaginationRequestModel;
import com.lakhpati.models.Pagination.PaginationResponseModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupLotteryHistoryFragment extends Fragment {

    @BindView(R.id.listview_groupLotteryHistory)
    RecyclerView listview_groupLotteryHistory;

    @BindView(R.id.pullToRefresh_groupHistory)
    SwipeRefreshLayout pullToRefresh_groupHistory;

    @BindView(R.id.layout_noItem)
    LinearLayout layout_noItem;

    private GroupHistoryAdapter groupHistoryAdapter;
    private AlertDialog alertDialog;

    public static GroupLotteryHistoryFragment newInstance() {
        GroupLotteryHistoryFragment fragment = new GroupLotteryHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        boolean result = CheckConnection.isNetworkConnected(getContext());
        if (!result) {
            View view = inflater.inflate(R.layout.no_internet_layout, container, false);
            MaterialButton btn_reTry = (MaterialButton) view.findViewById(R.id.btn_retry);
            btn_reTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadFragment();
                }
            });
            return view;
        }

        View view = inflater.inflate(R.layout.fragment_groupluckydrawhistory, container, false);
        ButterKnife.bind(this, view);

        alertDialog=  Dialogs.getInstance().initLoaderDialog(getActivity());

        pullToRefresh_groupHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupCampaignHistory();
                pullToRefresh_groupHistory.setRefreshing(false);
            }
        });
        setUpLotteryGroupHistoryAdapter();
        getGroupCampaignHistory();

        return view;
    }

    private void reloadFragment() {

        Fragment frg = getFragmentManager().findFragmentById(R.id.frame_layout);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void getGroupCampaignHistory() {
        alertDialog.show();

        LotteryHistoryApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(LotteryHistoryApiInterface.class);
        CampaignGroupPaginationRequestModel model = new CampaignGroupPaginationRequestModel();
        model.setLimit(10);
        model.setOffset(0);
        model.setGroupId(GroupDetailActivity.commonFragmentModel.getGroupId());
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());

        Call<ReturnModel> callValue = grpCampaignApi.getLotteryHistory(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();

                    PaginationResponseModel responseModel = HelperClass.getSingleModelFromJson(PaginationResponseModel.class, returnData);

                    List<LotteryHistoryModel> historyList = HelperClass.getListModelFromJson(new TypeToken<List<LotteryHistoryModel>>() {
                    }.getType(), responseModel.getData());
                    if(historyList.size()== 0){
                        pullToRefresh_groupHistory.setVisibility(View.GONE);
                        layout_noItem.setVisibility(View.VISIBLE);
                    }
                    else {
                        pullToRefresh_groupHistory.setVisibility(View.VISIBLE);
                        layout_noItem.setVisibility(View.GONE);
                        groupHistoryAdapter.addItems(historyList);
                    }
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                alertDialog.cancel();
            }
        });
    }

    private  void setUpLotteryGroupHistoryAdapter(){
         groupHistoryAdapter = new GroupHistoryAdapter(new ArrayList<>(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listview_groupLotteryHistory.setLayoutManager(mLayoutManager);
        listview_groupLotteryHistory.setItemAnimator(new DefaultItemAnimator());
        listview_groupLotteryHistory.setAdapter(groupHistoryAdapter);
        groupHistoryAdapter.notifyDataSetChanged();
    }
}
