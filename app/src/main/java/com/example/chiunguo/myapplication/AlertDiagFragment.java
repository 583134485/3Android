package com.example.chiunguo.myapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

public class AlertDiagFragment extends DialogFragment {
    public ArrayList mSelectedItems;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       mSelectedItems = new ArrayList();

        builder.setTitle("picker")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.demo, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                    toast("click");
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                    toast("remove");
                                }
                            }
                        });

// Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                toast("ok");
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                toast("cancel");
            }
        });
// 2. Chain together various setter methods to set the dialog characteristics
//        builder.setMessage(R.string.dialog_message)
//                .setTitle(R.string.dialog_title);        builder.setMessage(R.string.dialog_message)
//                .setTitle(R.string.dialog_title);

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        return  dialog;

    }
    public void toast(String m){
        Context context = getContext();
        CharSequence text = m;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
