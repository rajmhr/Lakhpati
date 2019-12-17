package com.lakhpati.Utilities;

public class EnumCollection {
    public enum CampaignStatus {
        New,
        InProgress,
        DrawStarted,
        Stopped,
        Completed
    }
    public enum NotificationType
    {
        UserAdded,

    }
    public enum UserStatus{
        Active,
        NotActive,
        Blocked,
        Terminated
    }
    public enum HubInvokeMethod{
        ReceiveChatMessage,
        LuckyDrawReceiveMessage,
        NotificationReceivedMessage
    }
    public enum MessageType{
        Initializing,
        Starting,
        Completed,
        InProgress
    }
}
