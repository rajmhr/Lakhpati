package com.lakhpati.internalService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lakhpati.R;
import com.lakhpati.Services.NetworkStateReceiver;
import com.lakhpati.Utilities.CheckConnection;
import com.lakhpati.Utilities.EnumCollection;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.GroupDetailActivity;
import com.lakhpati.fragments.GroupChatFragment;
import com.lakhpati.fragments.GroupLotteryDefinitionFragment;
import com.lakhpati.fragments.GroupLuckyDrawFragment;
import com.lakhpati.models.GroupChatViewModel;
import com.lakhpati.models.LuckyDrawHubResultModel;
import com.lakhpati.models.NotificationModel;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import okhttp3.internal.platform.Platform;

public class SignalRChatService extends Service implements NetworkStateReceiver.NetworkStateReceiverListener {
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    private SignalRSingleton mInstance;
    private NetworkStateReceiver networkStateReceiver;

    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) currentContext);
        registerNetworkBroadcastReceiver(currentContext);
    }

    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = SignalRSingleton.getInstance();
        mHandler = new Handler(Looper.getMainLooper());
        startNetworkBroadcastReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        return result;
    }


    @Override
    public void onDestroy() {
        stopSignalR();
        unregisterNetworkBroadcastReceiver(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        startSignalR();
        return mBinder;
    }

    @Override
    public void networkAvailable() {
        startSignalR();
    }

    @Override
    public void networkUnavailable() {
        stopSignalR();
    }

    private void stopSignalR() {
        if (mInstance.hubConnection != null)
            mInstance.hubConnection.stop();
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRChatService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRChatService.this;
        }
    }

    public void displayChatNotification(GroupChatViewModel model) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "xx")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Group : " + model.getGroupName() + " ( " + model.getUserDisplayName() + " 'says' )")
                .setContentText(model.getMessage())
                .setColor(Color.parseColor("#009add"))
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(
                NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("xx", "xxyy", NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }
        if (GroupDetailActivity.commonFragmentModel != null && model.getGroupId() == GroupDetailActivity.commonFragmentModel.getGroupId()) {
            GroupDetailActivity.groupDetailActivity.updateBottomNavigation(R.id.action_groupchat);

            if (GroupChatFragment.grpChatFragment != null)
                GroupChatFragment.grpChatFragment.receiveMessage(model.getMessage(), model.getUserDisplayName());
        } else {
            Intent intent = new Intent(this, GroupDetailActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Send data to NotificationView Class
            intent.putExtra("groupId", model.getGroupId());
            intent.putExtra("fragmentToLoad", "ChatFragment");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pIntent);
        }
        notificationManager.notify(0, mBuilder.build());
    }

    public void startSignalR() {
        if (!CheckConnection.isNetworkConnected(getApplication()))
            return;
        if (DrawerActivity.userCommonModel != null) {
            if (mInstance.hubConnection == null) {
                mInstance.setUpHubConnection();

                mInstance.hubConnection.onClosed(exception -> new HubConnectionTask().execute(mInstance.hubConnection));

                mInstance.hubConnection.on(EnumCollection.HubInvokeMethod.ReceiveChatMessage.toString(), (model) -> {
                    Log.d("Received", model.getGroupName());
                    displayChatNotification(model);
                }, GroupChatViewModel.class);

                mInstance.hubConnection.on(EnumCollection.HubInvokeMethod.LuckyDrawReceiveMessage.toString(), (model) -> {
                    Log.d("LuckyDraw", "test received");
                    displayLuckDrawResult(model);
                }, LuckyDrawHubResultModel.class);

                mInstance.hubConnection.on(EnumCollection.HubInvokeMethod.NotificationReceivedMessage.toString(), (model) -> {
                    Log.d("Notification", "Noti received");
                    displayNotification(model);
                }, NotificationModel.class);


                new HubConnectionTask().execute(mInstance.hubConnection);
            } else if (mInstance.hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
                new HubConnectionTask().execute(mInstance.hubConnection);
            }
        }
    }

    public void displayNotification(NotificationModel model) {
        String msg = model.getMessage();
        if (model.getMessageType().equals(EnumCollection.NotificationType.UserAdded.toString())) {
            if (model.getTargetUserDetailId() == DrawerActivity.userCommonModel.getUserDetailId()) {
                msg = String.format("You are added to group '%s' by '%s'", model.getGroupName(), model.getFromUserName());
            } else
                msg = String.format("User '%s' is added to group '%s'", model.getTargetUserName(), model.getGroupName());
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "xx")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Group : " + model.getGroupName())
                .setContentText(msg)
                .setColor(Color.parseColor("#009add"))
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(
                NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("xx", "xxyy", NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }

       /* Intent intent = new Intent(this, GroupDetailActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Send data to NotificationView Class
        intent.putExtra("groupId", model.getGroupId());
        intent.putExtra("fragmentToLoad", "ChatFragment");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/

        //mBuilder.setContentIntent(pIntent);
        notificationManager.notify(0, mBuilder.build());
    }


    private void displayLuckDrawResult(LuckyDrawHubResultModel model) {
        if (GroupDetailActivity.commonFragmentModel != null &&
                GroupDetailActivity.commonFragmentModel.getGroupId() == model.getGroupId() &&
                GroupLuckyDrawFragment.groupLuckyDrawFragment != null) {
            GroupLuckyDrawFragment.groupLuckyDrawFragment.receiveLuckDrawMessage(model);

        }
    }

    static class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            hubConnection.start().blockingAwait();
            return null;
        }
    }
}