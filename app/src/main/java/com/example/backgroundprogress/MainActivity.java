package com.example.backgroundprogress;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UpdateTimeAdapter mAdapter;
    ArrayList<String> updateTimeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateTimeService.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        Intent intent = new Intent(MainActivity.this, UpdateTimeService.class);
        startService(intent);

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s1 = intent.getStringExtra("time");
            updateTimeList.add(s1);
            mAdapter = new UpdateTimeAdapter(MainActivity.this, updateTimeList);
            mRecyclerView.setAdapter(mAdapter);
        }
    };


    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();

    }





}
