package com.lakhpati.customControl;

import android.app.ProgressDialog;
import android.content.Context;

import com.lakhpati.R;
import com.lakhpati.activity.LoginActivity;

public class ProgressLoader {
    private ProgressDialog progressDialog = null;
    private Context _context;

    public ProgressLoader(Context context) {
        this._context = context;
        progressDialog = new ProgressDialog(_context, R.style.AppTheme_Dark_Dialog);
    }

    public void setProgressDialog(String message) {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
