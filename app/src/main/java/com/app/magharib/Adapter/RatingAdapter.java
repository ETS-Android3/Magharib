package com.app.magharib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Model.Order;
import com.app.magharib.Model.Rating;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;

import java.util.List;


public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {
    private Context context;
    private List<Rating> objects;


    public RatingAdapter(Context context, List<Rating> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rating, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Rating data = objects.get(i);

        myViewHolder.tv_date.setText(data.getDate());
        myViewHolder.tv_rating.setText(data.getRating()+" / "+"5");
        myViewHolder.tv_comment.setText(data.getComment());


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date , tv_rating , tv_comment;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_comment = itemView.findViewById(R.id.tv_comment);

        }
    }

}
