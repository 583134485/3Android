package com.example.chiunguo.myapplication.Summary;

import com.example.chiunguo.myapplication.core.BasePresenter;
import com.example.chiunguo.myapplication.core.BaseView;

public interface SummaryContract {

    interface View extends BaseView<Presenter>{

        boolean isActive();

        void showcategories();

        void showDialog(String message);
    }

    interface  Presenter extends BasePresenter{

    }
}
