package com.lakhpati.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.GroupCampaignApiInterface;
import com.lakhpati.Services.MyTicketsApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.ListViewUtil;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.customControl.ProgressLoader;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryTicketViewModel;
import com.lakhpati.models.PurchaseTicketViewModel;
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

public class BuyTicketFragment extends Fragment {

    @BindView(R.id.txt_noOfTicket)
    TextInputEditText txt_noOfTicket;

    @BindView(R.id.btnGenerateTicket)
    AppCompatButton btnGenerateTicket;

    @BindView(R.id.generatedTicket_cardView)
    CardView generatedTicket_cardView;

    @BindView(R.id.listViewGenerateTicket)
    ListView listViewGenerateTicket;

    @BindView(R.id.layout_purchaseSummary_Card)
    CardView layout_purchaseSummary_Card;

    @BindView(R.id.txt_recentPurchaseTicket)
    TextView txt_recentPurchaseTicket;

    @BindView(R.id.txt_buyTotalTicket)
    TextView txt_buyTotalTicket;

    @BindView(R.id.txt_TotalCoins)
    TextView txt_TotalCoins;

    @BindView(R.id.btn_BuyTickets)
    MaterialButton btn_BuyTickets;

    @BindView(R.id.txt_buyTicketTotalCoins)
    TextView txt_buyTicketTotalCoins;

    private LotteryGroupCampaignDetailModel myGroupModel;
    private AlertDialog alertDialog;

    public  static BuyTicketFragment newInstance() {
        BuyTicketFragment fragment = new BuyTicketFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buyticket, container, false);
        ButterKnife.bind(this, view);
        Bundle b = getActivity().getIntent().getExtras();

        myGroupModel = GroupDetailActivity.commonFragmentModel;

        alertDialog = Dialogs.getInstance().initLoaderDialog(getActivity());

        btnGenerateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedTicket_cardView.setVisibility(View.GONE);
                getGeneratedTicket();
            }
        });
        btn_BuyTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseTicket();
            }
        });
        return view;
    }

    private void purchaseTicket() {
        alertDialog.show();

        MyTicketsApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(MyTicketsApiInterface.class);

        PurchaseTicketViewModel model = new PurchaseTicketViewModel();
        model.setLotteryGroupCampaignId(myGroupModel.getGroupCampaignId());
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());

        int totalTicketItems = listViewGenerateTicket.getAdapter().getCount();
        ArrayList<String> ticketItem = new ArrayList<String>();
        for (int i = 0; i < totalTicketItems; i++) {
            ticketItem.add(listViewGenerateTicket.getAdapter().getItem(i).toString());
        }
        model.setTicketNos(ticketItem);

        Call<ReturnModel> callValue = grpCampaignApi.buyLotteryGroupTickets(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(response.body().getMessage(), getActivity());
                    layout_purchaseSummary_Card.setVisibility(View.GONE);
                    txt_recentPurchaseTicket.setText("Your recent purchase tickets..");
                    GroupLotteryDefinitionFragment.groupLotteryDefinitionFragment.setUpActionButtons(true);
                }
                else {
                    MessageDisplay.getInstance().showErrorToast(response.body().getMessage(), getActivity());
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

    private void getGeneratedTicket() {
        alertDialog.show();

        MyTicketsApiInterface grpCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(MyTicketsApiInterface.class);
        Call<ReturnModel> callValue = grpCampaignApi.getGeneratedTickets(txt_noOfTicket.getText().toString());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    Gson gson = new GsonBuilder().create();
                    ArrayList<LotteryTicketViewModel> ticketList = gson.fromJson(returnData, new TypeToken<List<LotteryTicketViewModel>>() {
                    }.getType());

                    List<String> myGroups = new ArrayList<String>();
                    for (LotteryTicketViewModel model : ticketList) {
                        myGroups.add(model.getTicketNo());
                    }
                    if (myGroups.size() > 0) {
                        generatedTicket_cardView.setVisibility(View.VISIBLE);
                        listViewGenerateTicket.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listitem_generateticket, R.id.ticketItem, myGroups));
                        ListViewUtil.setListViewHeightBasedOnChildren(listViewGenerateTicket);
                        txt_buyTotalTicket.setText("Total buying tickets : " + myGroups.size());
                        txt_buyTicketTotalCoins.setText("Coin required : " + myGroups.size() * myGroupModel.getBetCoin());
                        //need to correct this
                       // txt_TotalCoins.setText("Coins you have : " + DrawerActivity.userCommonModel.getCoins());
                        btn_BuyTickets.setEnabled(true);
                        txt_recentPurchaseTicket.setText("Here are your generated tickets..");
                        layout_purchaseSummary_Card.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    MessageDisplay.getInstance().showErrorToast(response.body().getMessage(),getActivity());
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
}