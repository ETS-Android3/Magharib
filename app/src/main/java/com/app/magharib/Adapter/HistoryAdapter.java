package com.app.magharib.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Model.Data;
import com.app.magharib.Model.Order;
import com.app.magharib.R;
import com.app.magharib.Traveler.HistoryDetailsActivity;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private Context context;
    private List<Order> objects;


    public HistoryAdapter(Context context, List<Order> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history2, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Order data = objects.get(i);

        myViewHolder.tv_date.setText(data.getDate_order());
        myViewHolder.tv_place.setText(data.getName_experience());

        if (i == 0){
            myViewHolder.view_history.setVisibility(View.GONE);
        }

        myViewHolder.itemView.setOnClickListener(view -> {
            // الانتقال الى واجهة تفاصيل الحجز و نقل اوبجت الحجز او الطلب
            Intent intent = new Intent(context , HistoryDetailsActivity.class);
            intent.putExtra("order_details",data);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date , tv_place;
        private View view_history ;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_place = itemView.findViewById(R.id.tv_place);
            view_history = itemView.findViewById(R.id.view_history);


        }
    }

}
