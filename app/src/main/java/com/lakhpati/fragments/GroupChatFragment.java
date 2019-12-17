package com.lakhpati.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.LotteryGroupApiInterface;
import com.lakhpati.Utilities.CheckConnection;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.adapters.MessageAdapter;
import com.lakhpati.customControl.ProgressLoader;
import com.lakhpati.internalService.SignalRSingleton;
import com.lakhpati.models.GroupChatModel;
import com.lakhpati.models.GroupMembersViewModel;
import com.lakhpati.models.LotteryUserGroupViewModel;
import com.lakhpati.models.MemberData;
import com.lakhpati.models.Message;
import com.lakhpati.models.Pagination.ChatGroupPaginationRequestModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupChatFragment extends Fragment {

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.messages_view)
    ListView messageListView;

    @BindView(R.id.btnSendMessage)
    ImageButton btnSendMessage;

    @BindView(R.id.pullToRefresh_chatMessage)
    SwipeRefreshLayout pullToRefresh_chatMessage;

    int offSet = 0;
    int limit = 10;
    private MessageAdapter messageAdapter;

    public static GroupChatFragment grpChatFragment;

    AlertDialog alertDialog;

    public static GroupChatFragment newInstance() {
        GroupChatFragment fragment = new GroupChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupchat, container, false);
        ButterKnife.bind(this, view);

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
        offSet = 0;
        grpChatFragment = this;
        messageAdapter = new MessageAdapter(getContext());
        messageListView = (ListView) view.findViewById(R.id.messages_view);
        messageListView.setAdapter(messageAdapter);
        messageListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    pullToRefresh_chatMessage.setEnabled(true);
                } else pullToRefresh_chatMessage.setEnabled(false);
            }
        });

        MemberData data = new MemberData(getRandomName(), getRandomColor());

        pullToRefresh_chatMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreMessage();
                pullToRefresh_chatMessage.setRefreshing(false);
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message message = new Message(editText.getText().toString(), data, true);
                messageAdapter.add(message);
                sendMessage(editText.getText().toString());
                editText.getText().clear();
            }
        });
        loadMoreMessage();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        grpChatFragment = null;
    }

    private void reloadFragment() {

        Fragment frg = getFragmentManager().findFragmentById(R.id.frame_layout);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void loadMoreMessage() {
        alertDialog.show();

        LotteryGroupApiInterface lotteryGroupApi = RetrofitClientInstance.getRetrofitInstance().create(LotteryGroupApiInterface.class);

        ChatGroupPaginationRequestModel model = new ChatGroupPaginationRequestModel();
        model.setGroupId(GroupDetailActivity.commonFragmentModel.getGroupId());

        //DrawerActivity.activeLotteryGroup.getGroupId()
        model.setLimit(limit);
        model.setOffset(offSet);

        Call<ReturnModel> callValue = lotteryGroupApi.getGroupChatMessage(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {

                    List<GroupChatModel> chats = HelperClass.getListModelFromJson(new TypeToken<List<GroupChatModel>>() {
                    }.getType(), response.body().getReturnData());

                    loadToListView(chats);
                    if (chats.size() == 0 && offSet != 0)
                        MessageDisplay.getInstance().showSuccessToast("No more message to load.", getContext());
                    else if (offSet == 0) {
                        scrollMyListViewToBottom();
                    }
                    offSet += 10;
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

    private void loadToListView(List<GroupChatModel> chats) {
        boolean isBeloingToUser;
        List<Message> listMessage = new ArrayList<>();
        Collections.reverse(chats);
        for (int i = 0; i < chats.size(); i++) {
            isBeloingToUser = chats.get(i).getUserDetailId() == DrawerActivity.userCommonModel.getUserDetailId();
            MemberData memberData = new MemberData(chats.get(i).getUserDisplayName(), getRandomColor());
            Message msg = new Message(chats.get(i).getMessage(), memberData, isBeloingToUser);
            listMessage.add(msg);
        }
        if (listMessage.size() > 0) {
            messageAdapter.addMore(listMessage);
        }
    }

    private void scrollMyListViewToBottom() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                messageListView.smoothScrollToPosition(messageAdapter.getCount() - 1);
            }
        });
    }

    private void sendMessage(String message) {
        if (message.length() > 0) {
            SignalRSingleton.getInstance().sendChatMessage(DrawerActivity.userCommonModel.getUserDetailId(),
                    GroupDetailActivity.commonFragmentModel.getGroupId(), message, DrawerActivity.userCommonModel.getDisplayName(),
                    GroupDetailActivity.commonFragmentModel.getGroupName(), DrawerActivity.userCommonModel.getEmailId()
            );
            scrollMyListViewToBottom();
        }
    }

    public void receiveMessage(String incomingMessage, String displayName) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MemberData data = new MemberData(displayName, getRandomColor());
                final Message message = new Message(incomingMessage, data, false);
                messageAdapter.add(message);
                scrollMyListViewToBottom();
            }
        });

    }


    private String getRandomName() {
        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};
        return (
                adjs[(int) Math.floor(Math.random() * adjs.length)] +
                        "_" +
                        nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
