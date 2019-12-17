package com.lakhpati.Utilities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lakhpati.R;

public class MessageDisplay {

    private static MessageDisplay msgDisplay = new MessageDisplay();

    private MessageDisplay() {

    }

    public static MessageDisplay getInstance() {
        if (msgDisplay == null) {
            msgDisplay = new MessageDisplay();
        }
        return msgDisplay;
    }

    public void showSuccessToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();

        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.white));

        view.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    public void showErrorToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();

        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.white));

        view.getBackground().setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
