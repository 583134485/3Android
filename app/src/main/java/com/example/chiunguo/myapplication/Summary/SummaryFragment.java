package com.example.chiunguo.myapplication.Summary;

import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

import com.example.chiunguo.myapplication.Summary.SummaryContract.Presenter;

public class SummaryFragment  extends Fragment implements SummaryContract.View{

private  SummaryContract.Presenter presenter;

    @Override
    public void setPresenter(Presenter presenter) {
       this.presenter=presenter;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void showcategories() {

    }

    @Override
    public void showDialog(String message) {
        Context context = getContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
