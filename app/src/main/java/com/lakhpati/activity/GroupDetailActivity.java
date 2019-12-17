package com.lakhpati.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.InternetConnectionListener;
import com.lakhpati.Services.LotteryGroupApiInterface;
import com.lakhpati.Services.MyGroupApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperAsyncClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.adapters.AssignAdminRecyclerAdapter;
import com.lakhpati.fragments.GroupChatFragment;
import com.lakhpati.fragments.GroupLotteryDefinitionFragment;
import com.lakhpati.fragments.GroupLotteryHistoryFragment;
import com.lakhpati.fragments.GroupLuckyDrawFragment;
import com.lakhpati.models.GroupMembersViewModel;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.LotteryUserGroupViewModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity implements InternetConnectionListener {

    Fragment currentFragment;
    int itemId;
    BottomNavigationView bottomNavigationView;
    RelativeLayout activity_main;
    View assignAdmin_dialogView;
    View addUserToGroup_dialogView;
    RecyclerView listView_assignAdmin;
    AssignAdminRecyclerAdapter assignAdminRecyclerAdapter;
    AlertDialog assignAdmin_alertDialog;
    AlertDialog addUserToGroup_alertDialog;
    Button btn_cancel_assignAdmin;
    Button btn_create_assignAdmin;

    //MaterialButton btn_findUser;
    MaterialButton btn_cancel_addUserToGroup;
    MaterialButton btn_save_addUserToGroup;
    TextInputEditText txt_userName;
    public static GroupDetailActivity groupDetailActivity;

    public static LotteryGroupCampaignDetailModel commonFragmentModel;

    AlertDialog alertDialog;
    public Map<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        groupDetailActivity = this;
        setIntent(intent);
        initGroupDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        groupDetailActivity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initGroupDetail();
    }

    void initGroupDetail() {
        setContentView(R.layout.activity_groupdetail);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        ((RetrofitClientInstance) getApplication()).setInternetConnectionListener(this);

        alertDialog = Dialogs.getInstance().initLoaderDialog(this);
        Fragment frameToLoad = null;

        Intent i = getIntent();
        Integer groupId = i.getIntExtra("groupId", 0);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Fragment selectedFragment = null;
                        if (currentFragment != null && item.getItemId() == itemId) {
                            selectedFragment = currentFragment;
                        } else {
                            selectedFragment = getRelatedFragment(item.getItemId());
                            currentFragment = selectedFragment;
                            itemId = item.getItemId();
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        //check if it is coming from notification
        if (groupId != 0) {
            frameToLoad = getRelatedFragment(R.id.action_groupchat);
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        } else {
            groupId = DrawerActivity.activeLotteryGroup.getGroupId();
            frameToLoad = GroupLotteryDefinitionFragment.newInstance();

        }
        LotteryGroupModel groupModel = new LotteryGroupModel();
        groupModel.setGroupId(groupId);
        groupModel.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        commonFragmentModel = HelperAsyncClass.loadGroupInfoByGroupId(groupModel, this);
        if (commonFragmentModel == null) {
            finish();
            return;
        } else {
            setTitle(commonFragmentModel.getGroupName());
        }

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, frameToLoad);
        transaction.commit();
    }

    public void updateBottomNavigation(int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bottomNavigationView.setSelectedItemId(id);
            }
        });

    }

    private Fragment getRelatedFragment(int itemId) {
        Fragment retFragment = null;
        switch (itemId) {
            case R.id.action_groupLotteryDefination:
                retFragment = GroupLotteryDefinitionFragment.newInstance();
                break;
            case R.id.action_groupchat:
                retFragment = GroupChatFragment.newInstance();
                break;
            case R.id.action_groupLuckdraw:
                retFragment = GroupLuckyDrawFragment.newInstance();
                break;
            case R.id.action_groupLotteryHistory:
                retFragment = GroupLotteryHistoryFragment.newInstance();
                break;
        }
        return retFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                DrawerActivity.activeLotteryGroup = null;
                GroupDetailActivity.commonFragmentModel = null;
                break;
            case R.id.menuItem_deleteGroup:
                deleteGroup();
                break;
            case R.id.menuItem_leaveGroup:
                leaveGroup();
                break;
            case R.id.menuItem_assignAdmin:
                assignAdminDialog();
                break;
            case R.id.menuItem_invite:
                addUserToGroupDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (commonFragmentModel == null)
            return true;
        if (commonFragmentModel.isAdmin())
            getMenuInflater().inflate(R.menu.menu_groupdetail_admin, menu);
        else
            getMenuInflater().inflate(R.menu.menu_groupdetail, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    //assign admin methods
    private void assignAdminDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Assign Admin..");
        alertDialogBuilder.setIcon(R.drawable.img_admin);
        alertDialogBuilder.setCancelable(true);

        // Init popup dialog view and it's ui controls.
        initAssignAdminControl();
        loadAllUsersInGroup();

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(assignAdmin_dialogView);

        // Create AlertDialog and show.
        assignAdmin_alertDialog = alertDialogBuilder.create();
        assignAdmin_alertDialog.show();

        btn_cancel_assignAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignAdmin_alertDialog.cancel();
            }
        });
        btn_create_assignAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAssignAdmin();
            }
        });
    }

    private void initAssignAdminControl() {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        assignAdmin_dialogView = layoutInflater.inflate(R.layout.activity_assign_admin, null);
        listView_assignAdmin = (RecyclerView) assignAdmin_dialogView.findViewById(R.id.listview_assignAdmin);
        btn_cancel_assignAdmin = (Button) assignAdmin_dialogView.findViewById(R.id.btn_cancel_assignAdmin);
        btn_create_assignAdmin = (Button) assignAdmin_dialogView.findViewById(R.id.btn_create_assignAdmin);
    }

    private void saveAssignAdmin() {
        alertDialog.show();

        LotteryGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(LotteryGroupApiInterface.class);
        List<GroupMembersViewModel> models = new ArrayList<GroupMembersViewModel>();

        for (int i = 0; i < assignAdminRecyclerAdapter.getItemCount(); i++) {
            GroupMembersViewModel data = assignAdminRecyclerAdapter.getItem(i);
            if (data.isAdmin())
                models.add(assignAdminRecyclerAdapter.getItem(i));
        }

        Call<ReturnModel> callValue = lotteryGroupApi.assignAdmin(models);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(message, getApplication());
                    assignAdmin_alertDialog.cancel();
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getApplication());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }

    private void loadAllUsersInGroup() {
        alertDialog.show();

        LotteryGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(LotteryGroupApiInterface.class);
        Call<ReturnModel> callValue = lotteryGroupApi.getGroupMembers(commonFragmentModel.getGroupId());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    populateListUsersInGroup(returnData);
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                alertDialog.cancel();
            }
        });
    }
    //--------------Assign Admin-----------------------------

    //add user to group methods
    private void initAddUserToGroupControl() {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        addUserToGroup_dialogView = layoutInflater.inflate(R.layout.activity_add_user_togroup, null);
        txt_userName = (TextInputEditText) addUserToGroup_dialogView.findViewById(R.id.txt_userName);
        //btn_findUser = (MaterialButton) addUserToGroup_dialogView.findViewById(R.id.btn_findUser);
        btn_save_addUserToGroup = (MaterialButton) addUserToGroup_dialogView.findViewById(R.id.btn_save_addUserToGroup);
        btn_cancel_addUserToGroup = (MaterialButton) addUserToGroup_dialogView.findViewById(R.id.btn_cancel_addUserToGroup);
    }

    private void addUserToGroup() {
        alertDialog.show();
        String helperText = "@gmail.com";
        LotteryGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(LotteryGroupApiInterface.class);

        LotteryUserGroupViewModel model = new LotteryUserGroupViewModel();
        model.setLotteryUserGroupId(commonFragmentModel.getLotteryUserGroupId());
        model.setEmailId(txt_userName.getText().toString() + helperText);
        model.setUserDisplayName(DrawerActivity.userCommonModel.getDisplayName());
        model.setGroupId(commonFragmentModel.getGroupId());
        model.setGroupName(commonFragmentModel.getGroupName());
        //model.setUserDetailId(myGroupModel.getUserDetailId());
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());

        Call<ReturnModel> callValue = lotteryGroupApi.addUserToGroup(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    addUserToGroup_alertDialog.cancel();
                    Gson gson = new GsonBuilder().create();
                    GroupMembersViewModel findUser = gson.fromJson(response.body().getReturnData(), new TypeToken<GroupMembersViewModel>() {
                    }.getType());
                    MessageDisplay.getInstance().showSuccessToast(message, getApplication());
                } else {
                    txt_userName.setError(message);
                    MessageDisplay.getInstance().showErrorToast(message, getApplication());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }

    /*private void findUser() {
        alertDialog.show();
        String helperText = "@google.com";
        LotteryGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(LotteryGroupApiInterface.class);

        LotteryUserGroupViewModel model = new LotteryUserGroupViewModel();
        model.setLotteryUserGroupId(commonFragmentModel.getLotteryUserGroupId());
        model.setEmailId(txt_userName.getText().toString() + helperText);
        model.setGroupId(commonFragmentModel.getGroupId());


        Call<ReturnModel> callValue = lotteryGroupApi.findUserByEmailId(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    Gson gson = new GsonBuilder().create();
                    GroupMembersViewModel findUser = gson.fromJson(response.body().getReturnData(), new TypeToken<GroupMembersViewModel>() {
                    }.getType());
                    message = String.format("User found: '%s' ( '%s' )", findUser.getDisplayName(), findUser.getEmail());
                    txt_userFindResult.setTextColor(getResources().getColor(R.color.colorPrimary));
                    setFineResultText(message, getResources().getColor(R.color.colorPrimary));
                    btn_save_addUserToGroup.setEnabled(true);
                } else {
                    setFineResultText(message, getResources().getColor(R.color.primary_dark));
                    btn_save_addUserToGroup.setEnabled(false);
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
                btn_save_addUserToGroup.setEnabled(false);
            }
        });
    }*/

    private void addUserToGroupDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Add user to group..");
        alertDialogBuilder.setIcon(R.drawable.img_user);
        alertDialogBuilder.setCancelable(false);

        // Init popup dialog view and it's ui controls.
        initAddUserToGroupControl();

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(addUserToGroup_dialogView);

        // Create AlertDialog and show.
        addUserToGroup_alertDialog = alertDialogBuilder.create();
        addUserToGroup_alertDialog.show();

        btn_cancel_addUserToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToGroup_alertDialog.cancel();
            }
        });
        btn_save_addUserToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAddUserToGroup())
                    addUserToGroup();
            }
        });
       /* btn_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = new HelperClass().isEmailValid(txt_userName.getText().toString().trim());
                if (!result) {
                    setFineResultText("Please enter valid email id.", getResources().getColor(R.color.primary_dark));
                } else {
                    findUser();
                }
            }
        });*/
    }

    private boolean validateAddUserToGroup() {
        if (txt_userName.getText().toString().equals("")) {
            txt_userName.setError("Please enter email id to add to group.");
            return false;
        }
        return true;
    }

    private void populateListUsersInGroup(String returnData) {
        Gson gson = new GsonBuilder().create();
        List<GroupMembersViewModel> allUserList = gson.fromJson(returnData, new TypeToken<List<GroupMembersViewModel>>() {
        }.getType());

        assignAdminRecyclerAdapter = new AssignAdminRecyclerAdapter(allUserList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView_assignAdmin.setLayoutManager(mLayoutManager);
        listView_assignAdmin.setItemAnimator(new DefaultItemAnimator());
        listView_assignAdmin.setAdapter(assignAdminRecyclerAdapter);
        assignAdminRecyclerAdapter.notifyDataSetChanged();
    }
    //-------------Add user to group-----------------------------


    //delete group
    private void deleteGroup() {
        alertDialog.show();
        MyGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(MyGroupApiInterface.class);

        Call<ReturnModel> callValue = lotteryGroupApi.deleteGroup(commonFragmentModel.getGroupId());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                View view = toast.getView();

                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.white));

                if (response.body().isSuccess()) {
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                } else {
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                }
                toast.show();
                alertDialog.cancel();
                DrawerActivity.drawerActivity.myGroupRecycleAdapter.removeItem(commonFragmentModel.getGroupId());
                finish();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }

    //leave group
    private void leaveGroup() {
        alertDialog.show();
        MyGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(MyGroupApiInterface.class);

        Call<ReturnModel> callValue = lotteryGroupApi.leaveGroup(commonFragmentModel.getLotteryUserGroupId());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                View view = toast.getView();

                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.white));

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(message, getApplication());
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getApplication());
                }
                alertDialog.cancel();
                DrawerActivity.drawerActivity.myGroupRecycleAdapter.removeItem(commonFragmentModel.getGroupId());
                finish();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
            }
        });
    }
    //----------------Menu item actions--------------------

    @Override
    public void onInternetUnavailable() {
       /* Snackbar snackbar = Snackbar
                .make(activity_main, "No internet connection. Try later.", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        snackbar.setBackgroundTint(getResources().getColor(R.color.yellow_ring_color));
        snackbar.show();*/
    }
}
