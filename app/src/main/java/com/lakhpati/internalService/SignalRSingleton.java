package com.lakhpati.internalService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lakhpati.R;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.activity.LoginActivity;
import com.lakhpati.fragments.GroupLuckyDrawFragment;
import com.lakhpati.models.GroupChatViewModel;
import com.lakhpati.retrofit.RetrofitClientInstance;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

public class SignalRSingleton {
    public HubConnection hubConnection;
    private static SignalRSingleton mInstance = null;

    public static SignalRSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new SignalRSingleton();
        }
        return mInstance;
    }
    public void setUpHubConnection(){
        String url = RetrofitClientInstance.BASE_HUB_URL+"chatGroups?userId=" + DrawerActivity.userCommonModel.getUserDetailId();
        hubConnection = HubConnectionBuilder.create(url).build();
    }

    public void sendChatMessage(int userDetailId, int groupId, String message, String displayName, String groupName, String emailId) {
        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            hubConnection.start().blockingAwait();
        }
        hubConnection.send("SendChatMessage", userDetailId, groupId, message, displayName, groupName, emailId);
    }
}
