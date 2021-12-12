package com.app.magharib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Model.Data;
import com.app.magharib.Model.Order;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;

import java.util.List;


public class BookingRequestAdapter extends RecyclerView.Adapter<BookingRequestAdapter.MyViewHolder> {
    private Context context;
    private List<Order> objects;

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public BookingRequestAdapter(Context context, List<Order> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_request, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Order data = objects.get(i);

        if (i == 0){
            myViewHolder.view_booking_request.setVisibility(View.GONE);
        }

        myViewHolder.tv_name_booking_request.setText(data.getFirst_name()+" "+data.getLast_name());

        myViewHolder.itemView.setOnClickListener(v -> {
            mOnItemClickListener.onClick(data, v, i);

        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name_booking_request;
        private View view_booking_request ;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_booking_request = itemView.findViewById(R.id.tv_name_booking_request);
            view_booking_request = itemView.findViewById(R.id.view_booking_request);

        }
    }

}
