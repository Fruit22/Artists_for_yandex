package com.fruit.foryandex.ErrDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import com.fruit.foryandex.R;

public class ErrUnknown extends DialogFragment implements OnClickListener {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.err_unknown_title)
                .setNegativeButton(R.string.err_exit, this)
                .setMessage(R.string.err_unknown_massage);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_NEGATIVE:
                getActivity().finish();
                break;
        }
    }
}