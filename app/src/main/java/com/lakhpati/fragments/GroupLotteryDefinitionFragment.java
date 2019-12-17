package com.lakhpati.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.lakhpati.R;
import com.lakhpati.Services.GroupCampaignApiInterface;
import com.lakhpati.Services.MyTicketsApiInterface;
import com.lakhpati.Utilities.CheckConnection;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.EnumCollection;
import com.lakhpati.Utilities.HelperAsyncClass;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.adapters.LotteryDefinitionTabAdapter;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupLotteryDefinitionFragment extends Fragment {
    @BindView(R.id.txt_period)
    TextView txt_period;

    @BindView(R.id.txt_campaignStatus)
    TextView txt_campaignStatus;

    @BindView(R.id.txt_betCoins)
    TextView txt_betCoins;

    @BindView(R.id.txt_prizeCoins)
    TextView txt_prizeCoins;

    @BindView(R.id.txt_campaignName)
    TextView txt_campaignName;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    //create lottery declaration
    private static final int Date_id = 0;
    private static final int Time_id = 1;

    private MaterialButton btn_create_createLottery;
    private TextView txt_endTime;
    private TextView txt_endDate;
    private MaterialButton btn_endtime;
    private MaterialButton btn_enddate;

    private Button txt_lotteryTitle;
    private TextInputEditText edittxt_betCoins;

    @BindView(R.id.btn_stopLottery)
    MaterialButton btn_stopLottery;

    @BindView(R.id.pullToRefresh_definition)
    public SwipeRefreshLayout pullToRefresh_definition;

    @BindView(R.id.btn_deleteLottery)
    MaterialButton btn_deleteLottery;


    private LotteryGroupCampaignDetailModel myGroupDetailModel;
    static GroupLotteryDefinitionFragment groupLotteryDefinitionFragment;

    private LotteryDefinitionTabAdapter tabsAdapter;
    private AlertDialog alertDialog;

    public static GroupLotteryDefinitionFragment newInstance() {
        return new GroupLotteryDefinitionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupLotteryDefinitionFragment = this;
        View view;

        boolean result = CheckConnection.isNetworkConnected(getContext());
        if (!result) {
            view = inflater.inflate(R.layout.no_internet_layout, container, false);
            MaterialButton btn_reTry = (MaterialButton) view.findViewById(R.id.btn_retry);
            btn_reTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadFragment();
                }
            });
            return view;
        }

        alertDialog = Dialogs.getInstance().initLoaderDialog(getActivity());
        myGroupDetailModel = GroupDetailActivity.commonFragmentModel;

        if (myGroupDetailModel.getGroupCampaignId() <= 0) {
            view = loadNewLotteryView(inflater, container, myGroupDetailModel.isAdmin());
            return view;
        }

        view = initFragmentView(inflater, container);
        return view;
    }

    private View loadNewLotteryView(LayoutInflater inflater, ViewGroup container, boolean isAdmin) {
        View view;
        if (isAdmin) {
            view = inflater.inflate(R.layout.fragment_grouplotterydefination_create, container, false);
            initCreateLottery(view);
        } else {
            view = inflater.inflate(R.layout.fragment_grouplotterydefination_nocampaign, container, false);
            SwipeRefreshLayout pullToRefresh_noCampaign = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_noCampaign);
            pullToRefresh_noCampaign.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reloadFragment();
                    pullToRefresh_noCampaign.setRefreshing(false);
                }
            });

        }
        return view;
    }

    private View initFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_grouplotterydefination, container, false);

        if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.Completed.toString()) || myGroupDetailModel.getCampaignStatus().trim().equals("")) {
            view = loadNewLotteryView(inflater, container, myGroupDetailModel.isAdmin());
            return view;
        } else if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.Stopped.toString())) {
            view = inflater.inflate(R.layout.fragment_grouplotterydefination_stopped, container, false);
            MaterialButton btn_liveDraw = (MaterialButton) view.findViewById(R.id.btn_liveDraw);
            SwipeRefreshLayout pullToRefresh_stopped = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_stopped);
            pullToRefresh_stopped.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reloadFragment();
                    pullToRefresh_stopped.setRefreshing(false);
                }
            });
            btn_liveDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToLiveDraw();
                }
            });
            return view;
        } else if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.DrawStarted.toString())) {
            view = inflater.inflate(R.layout.fragment_grouplotterydefination_drawstarted, container, false);
            MaterialButton btn_gotoLiveDraw = (MaterialButton) view.findViewById(R.id.btn_gotoLiveDraw);
            btn_gotoLiveDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToLiveDraw();
                }
            });
            SwipeRefreshLayout pullToRefresh_drawStarted = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_drawStarted);
            pullToRefresh_drawStarted.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh_drawStarted.setRefreshing(false);
                    reloadFragment();
                }
            });
            return view;
        }
        //main view definition
        view = inflater.inflate(R.layout.fragment_grouplotterydefination, container, false);
        ButterKnife.bind(this, view);

        initLotteryDefinition();
        setUpActionButtons(false);

        btn_deleteLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroupLottery();
            }
        });

        btn_stopLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGroupLottery();
            }
        });

        pullToRefresh_definition.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadFragment();
                pullToRefresh_definition.setRefreshing(false);
            }
        });
        tabsAdapter = new LotteryDefinitionTabAdapter(getFragmentManager(), tab_layout.getTabCount(), getActivity());
        view_pager.setAdapter(tabsAdapter);
        view_pager.setOffscreenPageLimit(0);
        //tab_layout.setupWithViewPager(view_pager);

        tab_layout.setTabTextColors(
                ContextCompat.getColor(getContext(), R.color.white),
                ContextCompat.getColor(getContext(), R.color.black)
        );

        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private void navigateToLiveDraw() {
        GroupDetailActivity.groupDetailActivity.updateBottomNavigation(R.id.action_groupLuckdraw);
    }

    private View initNewStatus(View view) {

        SwipeRefreshLayout pullToRefresh_fresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_fresh);
        pullToRefresh_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                reloadFragment();
                pullToRefresh_fresh.setRefreshing(false);
            }
        });

        Button btn_deleteLottery = (Button) view.findViewById(R.id.btn_deleteLottery);
        btn_deleteLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroupLottery();
            }
        });

        Button btn_startLottery = (Button) view.findViewById(R.id.btn_startLottery);
        btn_startLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGroupLottery();
            }
        });
        TextView txt_campaignName = (TextView) view.findViewById(R.id.txt_campaignName);
        txt_campaignName.setText(myGroupDetailModel.getCampaignTitle());

        TextView txt_period = (TextView) view.findViewById(R.id.txt_period);
        txt_period.setText("Lottery will close on " + myGroupDetailModel.getPeriodEnd().toString().substring(0, 10));

        TextView txt_betCoins = (TextView) view.findViewById(R.id.txt_betCoins);
        txt_betCoins.setText("Bet coins : " + Integer.toString(myGroupDetailModel.getBetCoin()) + " coins.");

        TextView txt_campaignStatus = (TextView) view.findViewById(R.id.txt_campaignStatus);
        txt_campaignStatus.setText("Lottery status : " + myGroupDetailModel.getCampaignStatus());
        return view;
    }

    private void startGroupLottery() {
        alertDialog.show();

        GroupCampaignApiInterface lotteryGroupCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(GroupCampaignApiInterface.class);

        Call<ReturnModel> callValue = lotteryGroupCampaignApi.startGroupCampaign(myGroupDetailModel.getGroupCampaignId());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(message, getContext());
                    reloadFragment();
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getContext());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getActivity());
                alertDialog.cancel();
            }
        });
    }

    private void deleteGroupLottery() {
        alertDialog.show();

        GroupCampaignApiInterface lotteryGroupCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(GroupCampaignApiInterface.class);

        LotteryGroupCampaignModel model = new LotteryGroupCampaignModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setGroupId(myGroupDetailModel.getGroupId());
        model.setGroupCampaignId(myGroupDetailModel.getGroupCampaignId());
        model.setGroupName(myGroupDetailModel.getGroupName());

        Call<ReturnModel> callValue = lotteryGroupCampaignApi.deleteGroupCampaign(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(message, getContext());
                    GroupDetailActivity.commonFragmentModel.setLotteryGroupCampaignId(-1);
                    DrawerActivity.activeLotteryGroup.setLotteryGroupCampaignId(-1);
                    reloadFragment();
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getContext());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getActivity());
                alertDialog.cancel();
            }
        });
    }

    private void reloadFragment() {
        LotteryGroupModel model = new LotteryGroupModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setGroupId(GroupDetailActivity.commonFragmentModel.getGroupId());

        GroupDetailActivity.commonFragmentModel = HelperAsyncClass.loadGroupInfoByGroupId(model, getActivity());
        Fragment frg = getFragmentManager().findFragmentById(R.id.frame_layout);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private View initCreateLottery(View view) {
        {
            SwipeRefreshLayout pullToRefresh_createLottery = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_createLottery);
            pullToRefresh_createLottery.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reloadFragment();
                    pullToRefresh_createLottery.setRefreshing(false);
                }
            });

            btn_create_createLottery = (MaterialButton) view.findViewById(R.id.btn_create_createLottery);

            btn_create_createLottery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (validateCreateLottery())
                            saveLottery();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_endTime = view.findViewById(R.id.txt_endTime);
            txt_endDate = view.findViewById(R.id.txt_endDate);

            btn_enddate = view.findViewById(R.id.btn_enddate);
            btn_endtime = view.findViewById(R.id.btn_endtime);


            edittxt_betCoins = view.findViewById(R.id.edittxt_betCoins);
            txt_lotteryTitle = view.findViewById(R.id.edittxt_lotteryTitle);
            dateTimePickerSetting();
            initDefaultFields();

            String description = "Lottery round - " + (myGroupDetailModel.getRoundNumber() + 1);
            txt_lotteryTitle.setText(description);
            return view;
        }
    }

    private void initDefaultFields() {

        SimpleDateFormat df = new SimpleDateFormat("E, MMM d, yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date newDate = calendar.getTime();
        String weekLaterDate = df.format(newDate);

        txt_endDate.setText(weekLaterDate);
        txt_endTime.setText("12:00 AM");
        edittxt_betCoins.setText("10");
    }

    private boolean validateCreateLottery() {
        boolean isValid = true;
        if (edittxt_betCoins.getText().toString().equals("")) {
            edittxt_betCoins.setError("Bet coin is required.");
            isValid = false;
        } else if (txt_endDate.getText().toString().equals("")) {
            txt_endDate.setError("Lottery end date is required.");
            isValid = false;
        } else if (txt_endTime.getText().toString().equals("")) {
            txt_endTime.setError("Lottery end time is required.");
            isValid = false;
        }
        return isValid;
    }

    private void saveLottery() throws ParseException {
        alertDialog.show();

        GroupCampaignApiInterface lotteryGroupCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(GroupCampaignApiInterface.class);

        LotteryGroupCampaignModel rModel = getSaveLotteryModel();

        Call<ReturnModel> callValue = lotteryGroupCampaignApi.saveGroupCampaign(rModel);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(message, getContext());
                    LotteryGroupCampaignModel retModel = HelperClass.getSingleModelFromJson(LotteryGroupCampaignModel.class, response.body().getReturnData());
                    DrawerActivity.activeLotteryGroup.setLotteryGroupCampaignId(retModel.getGroupCampaignId());
                    DrawerActivity.activeLotteryGroup.setCampaignStatus(EnumCollection.CampaignStatus.InProgress.toString());
                    reloadFragment();
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getContext());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getActivity());
                alertDialog.cancel();
            }
        });
    }

    private LotteryGroupCampaignModel getSaveLotteryModel() throws ParseException {
        LotteryGroupCampaignModel model = new LotteryGroupCampaignModel();
        model.setBetCoin(Integer.parseInt(edittxt_betCoins.getText().toString()));
        model.setDescription(txt_lotteryTitle.getText().toString());
        model.setGroupId(myGroupDetailModel.getGroupId());
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("E, MMM d, yyyy HH:mm");

        String startDateText = df.format(c);
        Date sDate = df.parse(startDateText);

        String endDateText = txt_endDate.getText().toString() + " " + txt_endTime.getText().toString();
        Date eDate = df.parse(endDateText);

        model.setPeriodStart(sDate);
        model.setPeriodEnd(eDate);
        model.setRoundNumber(myGroupDetailModel.getRoundNumber() + 1);
        return model;
    }

    private void dateTimePickerSetting() {

        btn_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(Time_id).show();
            }
        });

        btn_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(Date_id).show();
            }
        });
    }

    private void initLotteryDefinition() {
        txt_campaignName.setText(myGroupDetailModel.getCampaignTitle());
        txt_period.setText("Lottery will close on " + DateFormat.format("yyyy-MM-dd", myGroupDetailModel.getPeriodEnd()));
        txt_betCoins.setText(String.valueOf("Bet coins : " + myGroupDetailModel.getBetCoin()));
        txt_prizeCoins.setText("Coin Prize : " + myGroupDetailModel.getTotalTicketSold() * myGroupDetailModel.getBetCoin() + " coins.");
        txt_campaignStatus.setText("Lottery status : " + myGroupDetailModel.getCampaignStatus());


    }

    public void setUpActionButtons(boolean isPurchase) {
        if (myGroupDetailModel.isAdmin()) {
            if (myGroupDetailModel.getCampaignStatus().trim().equals(EnumCollection.CampaignStatus.InProgress.toString()) && myGroupDetailModel.getTotalTicketSold() > 0) {
                btn_stopLottery.setVisibility(View.VISIBLE);
                btn_deleteLottery.setVisibility(View.GONE);
            }
        } else {
            btn_stopLottery.setVisibility(View.GONE);
            btn_deleteLottery.setVisibility(View.GONE);
        }

        if (isPurchase) {
            btn_deleteLottery.setVisibility(View.GONE);
        }
    }

    private void stopGroupLottery() {
        alertDialog.show();

        GroupCampaignApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(GroupCampaignApiInterface.class);

        LotteryGroupCampaignModel model = new LotteryGroupCampaignModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setGroupId(myGroupDetailModel.getGroupId());
        model.setGroupCampaignId(myGroupDetailModel.getGroupCampaignId());
        model.setGroupName(myGroupDetailModel.getGroupName());

        Call<ReturnModel> callValue = grpCampaignApi.stopGroupCampaign(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    reloadFragment();
                    MessageDisplay.getInstance().showSuccessToast(message, getActivity());
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getActivity());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getActivity());
                alertDialog.cancel();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);

            String formatted = new SimpleDateFormat("E, MMM d, yyyy").format(cal.getTime());
            txt_endDate.setText(formatted);
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String amPm = "AM";
            if (hour > 12) {
                hour = hour - 12;
                amPm = "PM";
            }
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute) + " " + amPm;
            txt_endTime.setText(time1);
        }
    };

    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        switch (id) {
            case Date_id:

                // Open the datepicker dialog
                DatePickerDialog d = new DatePickerDialog(getActivity(), date_listener, year,
                        month, day);
                d.setCancelable(false);
                d.getDatePicker().setMinDate(System.currentTimeMillis());
                return d;
            case Time_id:

                // Open the timepicker dialog
                Dialog td = new TimePickerDialog(getActivity(), time_listener, hour,
                        minute, false);
                td.setCancelable(false);
                return td;
        }
        return null;
    }


}
