package com.app.magharib.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Admin.ExperienceRequestsActivity;
import com.app.magharib.Model.Data;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;

import java.util.List;


public class ExperienceRequestsAdapter extends RecyclerView.Adapter<ExperienceRequestsAdapter.MyViewHolder> {
    private Context context;
    private List<Experience> objects;

    public ExperienceRequestsAdapter(Context context, List<Experience> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_experience, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Experience data = objects.get(i);

        myViewHolder.tv_name_request.setText(data.getFirst_name()+" "+data.getLast_name());

        myViewHolder.btn_view.setOnClickListener(v -> {
            Intent i2 = new Intent(context, ExperienceRequestsActivity.class);
            i2.putExtra("experience_data",data);
            context.startActivity(i2);
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name_request;
        private Button btn_view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_request = itemView.findViewById(R.id.tv_name_request);
            btn_view = itemView.findViewById(R.id.btn_view);

        }
    }

}
