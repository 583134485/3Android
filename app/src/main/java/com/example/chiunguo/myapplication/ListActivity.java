package com.example.chiunguo.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chiunguo.myapplication.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<DummyItem> myDataset= new ArrayList<DummyItem>();
        // specify an adapter (see also next example
        // specify an adapter (see also next example
        mAdapter = new MyItemRecyclerViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
}
