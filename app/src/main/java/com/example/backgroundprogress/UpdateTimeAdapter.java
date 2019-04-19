package com.example.backgroundprogress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateTimeAdapter extends RecyclerView.Adapter<UpdateTimeAdapter.MyViewHolder> {

    private static final String TAG = UpdateTimeAdapter.class.getSimpleName();
    Context context;
    ArrayList<String> updateTimeList = new ArrayList<>();

    public UpdateTimeAdapter(Context context, ArrayList<String> updateTimeList) {
        this.context = context;
        this.updateTimeList = updateTimeList;
    }

    @Override
    public UpdateTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_adapter,parent,false);
        return new UpdateTimeAdapter.MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(UpdateTimeAdapter.MyViewHolder holder, final int position) {

      holder.updateTime.setText(updateTimeList.get(position));

    }





    @Override
    public int getItemCount() {
        return updateTimeList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.updateTime)
        TextView updateTime;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
