package com.lakhpati.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.LotteryGroupApiInterface;
import com.lakhpati.Services.UserTransactionApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.adapters.CoinTransactionRecyclerAdapter;
import com.lakhpati.adapters.MyGroupRecyclerAdapter;
import com.lakhpati.models.CoinHistoryModel;
import com.lakhpati.models.GroupChatModel;
import com.lakhpati.models.Pagination.CoinHistoryPaginationModel;
import com.lakhpati.models.Pagination.PrizeMoneyPaginationRequestModel;
import com.lakhpati.models.RelatedLotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserCoinsModel;
import com.lakhpati.models.UserLotterySummaryModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinTransactionActivity extends AppCompatActivity {

    @BindView(R.id.recycleView_coinTransaction)
    RecyclerView recycleView_coinTransaction;

    @BindView(R.id.layout_noItem)
    LinearLayout layout_noItem;

    AlertDialog alertDialog;
    CoinTransactionRecyclerAdapter adapter;

    int limit = 10;
    int offset = 0;
    private static final int offsetConst = 10;
    boolean isLoading = true;
    int loadLoop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_transaction);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction summary");
        initListAdapter();
        offset = 0;
        alertDialog = Dialogs.getInstance().initLoaderDialog(this);
        loadTransactionHistory();
        recycleView_coinTransaction.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                        //bottom of list!
                        if (loadLoop != 1) {
                            loadTransactionHistory();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTransactionHistory() {
        alertDialog.show();
        UserTransactionApiInterface userTransactionApi = RetrofitClientInstance.getRetrofitInstance().create(UserTransactionApiInterface.class);

        CoinHistoryPaginationModel model = new CoinHistoryPaginationModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setOffset(offset);
        model.setLimit(limit);

        Call<ReturnModel> callValue = userTransactionApi.getCoinTransactionHistory(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    List<CoinHistoryModel> coinList = HelperClass.getListModelFromJson(new TypeToken<List<CoinHistoryModel>>() {
                    }.getType(), returnData);
                    if (isHistoryFirstEmptyLoaded(coinList)) {
                        layout_noItem.setVisibility(View.VISIBLE);
                        recycleView_coinTransaction.setVisibility(View.GONE);
                        isLoading = false;
                    } else {
                        layout_noItem.setVisibility(View.GONE);
                        recycleView_coinTransaction.setVisibility(View.VISIBLE);
                        if (coinList.size() > 0) {
                            populateRecycleView(coinList);
                            offset += offsetConst;
                            isLoading = false;
                        } else {
                            MessageDisplay.getInstance().showErrorToast("No more data to load.", getApplication());
                            isLoading = true;
                        }
                    }
                } else
                    isLoading = false;
                alertDialog.cancel();
                loadLoop++;
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
                isLoading = false;
            }
        });
    }
    private boolean isHistoryFirstEmptyLoaded(List<CoinHistoryModel> coinList){
        return (loadLoop == 0 && coinList.size() == 0);
    }

    private void populateRecycleView(List<CoinHistoryModel> coinList) {
        adapter.addListItem(coinList);
    }

    void initListAdapter() {
        adapter = new CoinTransactionRecyclerAdapter(new ArrayList<CoinHistoryModel>(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleView_coinTransaction.setLayoutManager(mLayoutManager);
        // recycleView_coinTransaction.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycleView_coinTransaction.setItemAnimator(new DefaultItemAnimator());
        recycleView_coinTransaction.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
