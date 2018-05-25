package com.example.chiunguo.myapplication.Summary;

import com.example.chiunguo.myapplication.model.Employee;

public class SummaryPresenter implements SummaryContract.Presenter{

    private  SummaryContract.View view;
    SummaryPresenter(SummaryContract.View view){
        //come a view
        this.view=view;
        //send this presenter to view
        view.setPresenter(this);
    }

    @Override
    public void start() {
        Employee employee=GetEmployeeData();
        if(employee==null){
            view.showDialog("nothing");
        }
        else {

        }
       view.showcategories();
    }

    private Employee GetEmployeeData (){
     return null;
    }
}
