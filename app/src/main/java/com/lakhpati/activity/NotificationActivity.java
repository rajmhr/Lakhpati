package com.lakhpati.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.UserMessageApiInterface;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.adapters.NotificationAdapter;
import com.lakhpati.models.NotificationSpModel;
import com.lakhpati.models.Pagination.NotificationPaginationRequestModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.notification_recycleView)
    RecyclerView notification_recycleView;

    @BindView(R.id.layout_noItem)
    LinearLayout layout_noItem;

    @BindView(R.id.pullToRefresh_notification)
    SwipeRefreshLayout pullToRefresh_notification;



    NotificationAdapter allUserTicketsAdapter;

    private static final int limit = 10;
    private int offsetVar = 0;
    private static final int offsetConst = 10;
    private static final String title = "User Notification";
    boolean isLoading = true;
    private int loadLoop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);
        ButterKnife.bind(this);

        setUpNotificationAdapter();
        getAllNotification();

        pullToRefresh_notification.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetNotificationList();
                getAllNotification();
                pullToRefresh_notification.setRefreshing(false);
            }
        });

        notification_recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == allUserTicketsAdapter.getItemCount() - 1) {
                        //bottom of list!
                        if (loadLoop != 1) {
                            getAllNotification();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void resetNotificationList() {
        offsetVar = 0;
        allUserTicketsAdapter.clearItems();
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

    private void setUpNotificationAdapter() {
        allUserTicketsAdapter = new NotificationAdapter(new ArrayList<>(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        notification_recycleView.setLayoutManager(mLayoutManager);
        notification_recycleView.setItemAnimator(new DefaultItemAnimator());
        notification_recycleView.setAdapter(allUserTicketsAdapter);
    }

    private void displayNotification(List<NotificationSpModel> allNotification) {
        allUserTicketsAdapter.addItems(allNotification);
        allUserTicketsAdapter.notifyDataSetChanged();
    }

    private void getAllNotification() {
        UserMessageApiInterface userMessageApiInterface = RetrofitClientInstance.getRetrofitInstance().create(UserMessageApiInterface.class);

        NotificationPaginationRequestModel model = new NotificationPaginationRequestModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setOffset(offsetVar);
        model.setLimit(limit);

        Call<ReturnModel> callValue = userMessageApiInterface.getAllNotification(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String returnData = response.body().getReturnData();
                if (response.body().isSuccess()) {
                    List<NotificationSpModel> allNotification = HelperClass.getListModelFromJson(new TypeToken<List<NotificationSpModel>>() {
                    }.getType(), returnData);
                    if (allNotification.size() > 0) {
                        displayNotification(allNotification);
                        notification_recycleView.setVisibility(View.VISIBLE);
                        layout_noItem.setVisibility(View.GONE);
                        offsetVar += offsetConst;
                        isLoading =true;
                    } else {
                        notification_recycleView.setVisibility(View.GONE);
                        layout_noItem.setVisibility(View.VISIBLE);
                        isLoading = false;
                    }
                }
                else {
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
            }
        });
    }

}
