package com.app.magharib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Model.Data;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;

import java.util.List;


public class ExperiencesAdapter extends RecyclerView.Adapter<ExperiencesAdapter.MyViewHolder> {
    private Context context;
    private List<Experience> objects;


    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public ExperiencesAdapter(Context context, List<Experience> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Experience data = objects.get(i);

        myViewHolder.tv_place.setText(data.getName_experience());

        if (i == 0){
            myViewHolder.view_history.setVisibility(View.GONE);
        }

        myViewHolder.itemView.setOnClickListener(v -> {
            mOnItemClickListener.onClick(data, v, i);

        });


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_place;
        private View view_history ;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_place = itemView.findViewById(R.id.tv_place);
            view_history = itemView.findViewById(R.id.view_history);


        }
    }

}
