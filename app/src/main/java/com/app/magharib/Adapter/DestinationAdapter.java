package com.app.magharib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Model.Data;
import com.app.magharib.Model.DataLocation;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;

import java.util.List;


public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.MyViewHolder> {
    private Context context;
    private List<DataLocation> objects;

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public DestinationAdapter(Context context, List<DataLocation> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        DataLocation data = objects.get(i);

        myViewHolder.tv_city.setText(data.getCity_name());

        myViewHolder.itemView.setOnClickListener(v -> {
            mOnItemClickListener.onClick(data, v, i);
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_city;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);

        }
    }

}
