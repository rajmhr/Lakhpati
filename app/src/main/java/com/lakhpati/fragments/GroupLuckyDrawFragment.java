package com.lakhpati.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.GroupCampaignApiInterface;
import com.lakhpati.Services.MyTicketsApiInterface;
import com.lakhpati.Utilities.CheckConnection;
import com.lakhpati.Utilities.EnumCollection;
import com.lakhpati.Utilities.HelperAsyncClass;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.adapters.LiveDrawListTicketsAdapter;
import com.lakhpati.internalService.SignalRChatService;
import com.lakhpati.internalService.SignalRSingleton;
import com.lakhpati.models.AllUserTicketViewModel;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.LuckyDrawHubResultModel;
import com.lakhpati.models.LuckyDrawViewModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupLuckyDrawFragment extends Fragment {

    @BindView(R.id.txt_intro)
    TextView txt_intro;

    @BindView(R.id.txt_description)
    TextView txt_description;

    @BindView(R.id.btn_takebackto_defination)
    MaterialButton btn_backto_create_lottery;

    private CardView card_view_liveDraw;

    private RecyclerView liveTickets_recycler_view;
    private TextView txt_live_ticket;
    private GifImageView img_loader;

    private TextView txt_ticketNo;
    private TextView txt_purchasedDate;
    private TextView txt_buyerInfo;

    private LiveDrawListTicketsAdapter listTicketsAdapter;
    private LotteryGroupCampaignDetailModel myGroupDetailModel;

    public static GroupLuckyDrawFragment groupLuckyDrawFragment;

    public static GroupLuckyDrawFragment newInstance() {
        GroupLuckyDrawFragment fragment = new GroupLuckyDrawFragment();
        return fragment;
    }

    @Override
    public void onStop() {
        groupLuckyDrawFragment = null;
        super.onStop();
    }

    @Override
    public void onPause() {
        groupLuckyDrawFragment = null;
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupLuckyDrawFragment = this;
        boolean result = CheckConnection.isNetworkConnected(getContext());
        if (!result) {
            View view = inflater.inflate(R.layout.no_internet_layout, container, false);
            return view;
        }

        View view = inflater.inflate(R.layout.fragment_groupluckydraw, container, false);
        ButterKnife.bind(this, view);

        myGroupDetailModel = GroupDetailActivity.commonFragmentModel;

        if (myGroupDetailModel.getCampaignStatus().trim().equals("")) {
            txt_intro.setText("Welcome!! To live draw.");
            txt_description.setText("Please create lottery to proceed.");
        } else if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.Completed.toString())) {
            txt_intro.setText("Congratulation!! You have recently completed lottery. ");
            txt_description.setText("Please create another one to proceed.");
        } else if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.InProgress.toString())) {
            txt_intro.setText("Lottery is in progress!!");
            txt_description.setText("Change lottery status to 'Stopped', live lottery will be available. ");
            return view;
        } else if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.Stopped.toString())) {
            initStoppedLayout();
        } else if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.DrawStarted.toString())) {
            view = inflater.inflate(R.layout.fragment_groupluckydraw_drawstarted, container, false);
            initDrawStarted(view);
            return view;
        }
        initActionButton();
        return view;
    }

    private void initActionButton() {

        if (!myGroupDetailModel.getCampaignStatus().equals(EnumCollection.CampaignStatus.DrawStarted.toString())) {
            if (myGroupDetailModel.getCampaignStatus() != null &&
                    myGroupDetailModel.getCampaignStatus().equals(EnumCollection.CampaignStatus.Stopped.toString()) &&
                    myGroupDetailModel.isAdmin()) {
                btn_backto_create_lottery.setText("Hit me to start live draw.");
            } else {
                btn_backto_create_lottery.setText("Group admin will start lucky draw.");
                btn_backto_create_lottery.setEnabled(false);
            }
            btn_backto_create_lottery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myGroupDetailModel.getCampaignStatus().equals(EnumCollection.CampaignStatus.Stopped.toString()))
                        startLuckyDraw();
                    else
                        navigateToDefinition();
                }
            });
        }
    }

    private void setDrawTicket(final String ticketNo) {
        if (ticketNo == null || ticketNo.equals(""))
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_live_ticket.setText(ticketNo);
                if (listTicketsAdapter != null && ticketNo != null) {
                    AllUserTicketViewModel model = listTicketsAdapter.getTicketItemByTicket(ticketNo.trim());
                    if (model != null) {
                        txt_ticketNo.setText(model.getTicketNo());
                        txt_purchasedDate.setText(model.getPurchasedDate().toString());
                        txt_buyerInfo.setText(model.getDisplayName() + " ( " + model.getEmailId() + " ) ");
                        listTicketsAdapter.swapItem(ticketNo.trim(), 0);
                        if (((LinearLayoutManager) (liveTickets_recycler_view.getLayoutManager())).findFirstVisibleItemPosition() != 0) {
                            liveTickets_recycler_view.smoothScrollToPosition(0);
                        }
                    }
                }
            }
        });
    }

    private void setCollapseImageLoader() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img_loader.setVisibility(View.GONE);
                txt_live_ticket.setText("Congratulation !!!");
            }
        });
    }

    private void initDrawStarted(View view) {
        liveTickets_recycler_view = view.findViewById(R.id.liveTickets_recycler_view);
        txt_live_ticket = view.findViewById(R.id.txt_live_ticket);
        img_loader = view.findViewById(R.id.img_loader);
        txt_ticketNo = view.findViewById(R.id.txt_ticketNo);
        txt_purchasedDate = view.findViewById(R.id.txt_purchasedDate);
        txt_buyerInfo = view.findViewById(R.id.txt_buyerInfo);
        card_view_liveDraw = view.findViewById(R.id.card_view_liveDraw);
        card_view_liveDraw.setCardBackgroundColor(getResources().getColor(R.color.light_green));
        initParticipatingList();
        getParticipatingTickets();
    }

    private void initParticipatingList() {
        listTicketsAdapter = new LiveDrawListTicketsAdapter(new ArrayList<AllUserTicketViewModel>(), getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        liveTickets_recycler_view.setLayoutManager(mLayoutManager);
        liveTickets_recycler_view.setItemAnimator(new DefaultItemAnimator());
        liveTickets_recycler_view.setAdapter(listTicketsAdapter);
    }

    private void initStoppedLayout() {
        txt_intro.setText("Wow!! You are all set to live lucky draw.");
        if (myGroupDetailModel.isAdmin()) {
            txt_intro.setText("Just one step away from winning!!");
            txt_description.setText("Please use button below to start live lucky draw!");

        } else {
            txt_description.setText("Group admin will start live lucky draw!!");
        }
    }

    private void navigateToDefinition() {
        GroupDetailActivity.groupDetailActivity.updateBottomNavigation(R.id.action_groupLotteryDefination);
    }

    private void startSignalR() {
        if (SignalRSingleton.getInstance().hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            SignalRSingleton.getInstance().hubConnection.start();
        }
    }

    public void receiveLuckDrawMessage(LuckyDrawHubResultModel model) {
        if (model.getMessageType().equals(EnumCollection.MessageType.Starting.toString())) {
            reloadFragment();
        } else if (model.getMessageType().equals(EnumCollection.MessageType.Completed.toString())) {
            setCollapseImageLoader();
        } else if (model.getMessageType().equals(EnumCollection.MessageType.InProgress.toString())) {
            setDrawTicket(model.getMessage());
        }
    }

    private void getParticipatingTickets() {
        MyTicketsApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(MyTicketsApiInterface.class);

        Call<ReturnModel> callValue = grpCampaignApi.getAllUserTicketsByGroupCampaignId(myGroupDetailModel.getGroupCampaignId());

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
                callValue.cancel();
            }
        });
    }

    private void startLuckyDraw() {
        startSignalR();
        GroupCampaignApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(GroupCampaignApiInterface.class);

        LuckyDrawViewModel model = new LuckyDrawViewModel();
        model.setGroupId(myGroupDetailModel.getGroupId());
        model.setGroupCampaignId(myGroupDetailModel.getGroupCampaignId());
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setGroupName(GroupDetailActivity.commonFragmentModel.getGroupName());

        Call<ReturnModel> callValue = grpCampaignApi.startDraw(model);

        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                }
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                if (!t.getMessage().contains("timeout"))
                    MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getActivity());
            }
        });
    }

    private void populateRecycleView(String returnData) {
        List<AllUserTicketViewModel> models = HelperClass.getListModelFromJson(new TypeToken<List<AllUserTicketViewModel>>() {
        }.getType(), returnData);

        listTicketsAdapter.addItems(models);
        listTicketsAdapter.notifyDataSetChanged();
    }

    private void reloadFragment() {
        GroupDetailActivity.commonFragmentModel.setCampaignStatus(EnumCollection.CampaignStatus.DrawStarted.toString());
        Fragment frg = getFragmentManager().findFragmentById(R.id.frame_layout);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}
