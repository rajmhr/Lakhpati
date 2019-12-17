package com.lakhpati.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lakhpati.R;

public class Dialogs {
    MaterialAlertDialogBuilder _mDialog;
    private static Dialogs _dialogs = new Dialogs();

    private Dialogs(){

    }

    public static Dialogs getInstance(){
        if(_dialogs == null){
            _dialogs = new Dialogs();
        }
        return _dialogs;
    }

    public AlertDialog initLoaderDialog(Activity context) {
        _mDialog = new MaterialAlertDialogBuilder(context, R.style.AlertDialogCustom);
        _mDialog.setView(R.layout.progress_layout);
        return _mDialog.create();
    }
}
