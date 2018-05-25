package com.example.chiunguo.myapplication.Summary;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SummaryPresenterTest {

    @Mock
   private SummaryContract.View mView;

    private  SummaryPresenter summaryPresenter;


    @Before
    public void setupSummaryPresenter(){
        MockitoAnnotations.initMocks(this);
        //Get a reference  about mVIew to the class
        summaryPresenter=new SummaryPresenter(mView);

        when(mView.isActive()).thenReturn(true);
    }
    @Test
    public void createPresenter_and_setsPresenterToView(){

        summaryPresenter=new SummaryPresenter(mView);

        verify(mView).setPresenter(summaryPresenter);
    }


    @Test
    public void  getNullDataFromApi_andDisplaynothing(){
        summaryPresenter.start();
        verify(mView).showDialog("nothing");
        verify(mView).showcategories();
    }

    @Test
    public void loadDataFromApi_toDisplay(){
    summaryPresenter.start();
    verify(mView).showcategories();
    }




}
