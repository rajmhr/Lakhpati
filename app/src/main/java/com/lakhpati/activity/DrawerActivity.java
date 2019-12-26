package com.lakhpati.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.infideap.drawerbehavior.BuildConfig;
import com.lakhpati.R;
import com.lakhpati.Services.InternetConnectionListener;
import com.lakhpati.Services.MyGroupApiInterface;
import com.lakhpati.Services.UserApiInterface;
import com.lakhpati.Services.UserMessageApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.LoginPreference;
import com.lakhpati.Utilities.MyGroupPreference;
import com.lakhpati.adapters.AllUserTicketsAdapter;
import com.lakhpati.adapters.MyGroupRecyclerAdapter;
import com.lakhpati.adapters.NotificationAdapter;
import com.lakhpati.internalService.SignalRChatService;
import com.lakhpati.models.AllUserTicketViewModel;
import com.lakhpati.models.CoinHistoryModel;
import com.lakhpati.models.LoginModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.NotificationModel;
import com.lakhpati.models.NotificationSpModel;
import com.lakhpati.models.RelatedLotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserDetailViewModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InternetConnectionListener {

    //region Fields and Declaration
    View popupInputDialogView;
    EditText groupName;

    @BindView(R.id.notification_recycleView)
    RecyclerView notification_recycleView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_addGroup)
    FloatingActionButton btn_floatAddGroup;
    @BindView(R.id.drawer_layout)
    AdvanceDrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.myGroupSearchView)
    SearchView myGroupSearchView;

    @BindView(R.id.myGroup_recycler_view)
    RecyclerView myGroup_recycler_view;

    @BindView(R.id.layout_noItem)
    LinearLayout layout_noItem;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.pullToRefresh_noItem)
    SwipeRefreshLayout pullToRefresh_noItem;

    MyGroupRecyclerAdapter myGroupRecycleAdapter;

    List<RelatedLotteryGroupModel> myGroupModelList = new ArrayList<RelatedLotteryGroupModel>();

    public static DrawerActivity drawerActivity;
    public static UserDetailViewModel userCommonModel;
    public static RelatedLotteryGroupModel activeLotteryGroup;

    AlertDialog alertDialog;
    AlertDialog addGroupAlertDialog;

    MaterialButton btnGroupCreate;
    MaterialButton btnGroupCancel;
    NotificationAdapter allUserTicketsAdapter;
//endregion

    //region Native Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        ButterKnife.bind(this);
        initMyGroupAdapter();
        setUpNotificationAdapter();

        drawerActivity = this;
        alertDialog = Dialogs.getInstance().initLoaderDialog(this);

        ((RetrofitClientInstance) getApplication()).setInternetConnectionListener(this);

        Bundle b = this.getIntent().getExtras();
        userCommonModel = (UserDetailViewModel) b.getSerializable("userModel");
        if (userCommonModel == null) {
            clearLoginPreferences();
            return;
        }

        startService(new Intent(DrawerActivity.this, SignalRChatService.class));
        //startService(new Intent(DrawerActivity.this, SignalRLuckyDrawService.class));
        setNavHeaderText();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        btn_floatAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLotteryGroup();
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMyGroups(true); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        pullToRefresh_noItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMyGroups(true); // your code
                pullToRefresh_noItem.setRefreshing(false);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_userprofile:
                        Intent userProfileIntent = new Intent(DrawerActivity.this, UserProfileActivity.class);
                        DrawerActivity.this.startActivity(userProfileIntent);
                        break;
                    case R.id.nav_transactionHistory:
                        Intent coinTransaction = new Intent(DrawerActivity.this, CoinTransactionActivity.class);
                        DrawerActivity.this.startActivity(coinTransaction);
                        break;
                    case R.id.nav_lakhpatilogout:
                        clearLoginPreferences();
                        break;
                    case R.id.nav_buySellCoins:
                        Intent loadCoinIntent = new Intent(DrawerActivity.this, LoadCoinActivity.class);
                        DrawerActivity.this.startActivity(loadCoinIntent);
                        break;
                    case R.id.nav_return:
                        Intent returnPayment = new Intent(DrawerActivity.this, ReturnActivity.class);
                        DrawerActivity.this.startActivity(returnPayment);
                        break;
                    case R.id.nav_send:
                        shareApp();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        drawer.setViewScale(Gravity.END, 0.9f);
        drawer.setViewElevation(Gravity.END, 20);

        setupSearchView();
        loadMyGroups(false);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_right_drawer);

        View actionView = menuItem.getActionView();
        ImageView notification_badge = (ImageView) actionView.findViewById(R.id.notification_image);

        //setupBadge();
        //getAllNotification();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_right_drawer:
                //drawer.openDrawer(GravityCompat.END);
                Intent notiIntent = new Intent(DrawerActivity.this, NotificationActivity.class);
                DrawerActivity.this.startActivity(notiIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInternetUnavailable() {
        Snackbar snackbar = Snackbar
                .make(drawer, "No internet connection. Try later.", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        snackbar.setBackgroundTint(getResources().getColor(R.color.yellow_ring_color));
        snackbar.show();
        // MessageDisplay.getInstance().showErrorToast("Internet connection not available. Loading cache data.", this);
    }
    //endregion

    //region Private Methods
    private void initMyGroupAdapter() {
        myGroupRecycleAdapter = new MyGroupRecyclerAdapter(myGroupModelList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        myGroup_recycler_view.setLayoutManager(mLayoutManager);
        myGroup_recycler_view.setItemAnimator(new DefaultItemAnimator());
        myGroup_recycler_view.setAdapter(myGroupRecycleAdapter);
    }

    private void setNavHeaderText() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txt_navHeaderUserName);
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.txt_navHeaderUserEmail);
        navUsername.setText(userCommonModel.getDisplayName());
        navUserEmail.setText(userCommonModel.getEmailId());
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "NEP-LAKHPATI");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Please choose one.."));
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMyGroups(boolean isRefresh) {
        alertDialog.show();

        MyGroupPreference preference = new MyGroupPreference(getApplicationContext());

        MyGroupApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(MyGroupApiInterface.class);
        Call<ReturnModel> callValue = userApiService.getAllMyGroup(userCommonModel.getUserDetailId());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    preference.setMyGroupPreference(returnData);

                    populateRecycleView(returnData);
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {

                String prefData = preference.getMyGroupPreferences();
                if (prefData != null && !prefData.trim().equals("")) {
                    populateRecycleView(prefData);
                }
                alertDialog.cancel();
            }
        });
    }

    private void clearLoginPreferences() {
        LoginPreference preference = new LoginPreference(getApplicationContext());
        preference.clearLoginPreferences();
        Toast.makeText(getBaseContext(), "User logged out.", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(DrawerActivity.this, LoginActivity.class);
        DrawerActivity.this.startActivity(loginIntent);
    }

    private void addLotteryGroup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Create Group !!");
        alertDialogBuilder.setIcon(R.drawable.add);
        alertDialogBuilder.setCancelable(false);

        // Init popup dialog view and it's ui controls.
        initPopupViewControls();

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);

        addGroupAlertDialog = alertDialogBuilder.create();
        addGroupAlertDialog.show();

        btnGroupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroupAlertDialog.dismiss();
            }
        });
        btnGroupCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCreateGroup())
                    createMyGroup();
            }
        });
    }

    private void initPopupViewControls() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popupInputDialogView = layoutInflater.inflate(R.layout.activity_creategroup, null);
        groupName = (TextInputEditText) popupInputDialogView.findViewById(R.id.createGroup_txtGroupName);
        btnGroupCreate = (MaterialButton) popupInputDialogView.findViewById(R.id.btnGroupCreate);
        btnGroupCancel = (MaterialButton) popupInputDialogView.findViewById(R.id.btnGroupCancel);
    }

    private boolean validateCreateGroup() {
        if (groupName.getText().toString() == "") {
            groupName.setError("Please enter group name.");
            return false;
        }
        if (groupName.length() < 4) {
            groupName.setError("Group name should be more than 3 character.");
            return false;
        }
        return true;
    }

    private void populateRecycleView(String returnData) {
        myGroupModelList = HelperClass.getListModelFromJson(new TypeToken<List<RelatedLotteryGroupModel>>() {
        }.getType(), returnData);

        myGroupRecycleAdapter.addItems(myGroupModelList);
        noItemVisible(myGroupRecycleAdapter.getItemCount() == 0);
        myGroupRecycleAdapter.notifyDataSetChanged();
    }

    public void noItemVisible(boolean isVisible) {
        if (isVisible) {
            layout_noItem.setVisibility(View.VISIBLE);
            pullToRefresh.setVisibility(View.GONE);
        } else {
            layout_noItem.setVisibility(View.GONE);
            pullToRefresh.setVisibility(View.VISIBLE);
        }
    }

    private void addLotteryGroupToView(String returnData) {
        RelatedLotteryGroupModel myGroupModel = HelperClass.getSingleModelFromJson(RelatedLotteryGroupModel.class, returnData);
        myGroupModel.setAdmin(true);
        myGroupModel.setCampaignStatus("");
        myGroupModel.setLotteryGroupCampaignId(-1);

        myGroupRecycleAdapter.addItem(myGroupModel);
        myGroupRecycleAdapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        SearchView searchView = this.findViewById(R.id.myGroupSearchView);
        EditText et = searchView.findViewById(R.id.search_src_text);
        et.setTextColor(getResources().getColor(R.color.black));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        myGroupSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        myGroupSearchView.setIconifiedByDefault(false);
        myGroupSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myGroupRecycleAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (myGroupRecycleAdapter == null)
                    return true;
                myGroupRecycleAdapter.getFilter().filter(query);
                return false;
            }
        });
        myGroupSearchView.setSubmitButtonEnabled(true);
    }

    private void createMyGroup() {
        alertDialog.show();
        MyGroupApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(MyGroupApiInterface.class);

        LoginPreference loginPreference = new LoginPreference(this);
        LoginModel lModel = loginPreference.getLoginPreferences();

        LotteryGroupModel model = new LotteryGroupModel();
        model.setName(groupName.getText().toString());
        model.setUserDetailId(lModel.getUserDetailId());


        Call<ReturnModel> callValue = userApiService.createMyGroup(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String returnData = response.body().getReturnData();
                if (response.body().isSuccess()) {
                    addLotteryGroupToView(returnData);
                    alertDialog.cancel();
                    addGroupAlertDialog.cancel();
                } else {
                    String message = response.body().getMessage();
                    Toast toast = Toast.makeText(getApplication(), message, Toast.LENGTH_LONG);
                    View view = toast.getView();

                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(getResources().getColor(R.color.white));
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    toast.show();
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                alertDialog.cancel();
            }
        });
    }

    private void setUpNotificationAdapter() {
        allUserTicketsAdapter = new NotificationAdapter(new ArrayList<>(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        notification_recycleView.setLayoutManager(mLayoutManager);
        notification_recycleView.setItemAnimator(new DefaultItemAnimator());
        notification_recycleView.setAdapter(allUserTicketsAdapter);
    }

    //endregion
}
