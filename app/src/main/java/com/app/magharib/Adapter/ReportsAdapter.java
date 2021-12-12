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

import com.app.magharib.Admin.ReportsActivity;
import com.app.magharib.Model.Data;
import com.app.magharib.Model.Report;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;

import java.util.List;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {
    private Context context;
    private List<Report> objects;


    public ReportsAdapter(Context context, List<Report> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reports, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Report data = objects.get(i);

        myViewHolder.tv_email_reports.setText(data.getEmail_user());
        myViewHolder.tv_txt_reports.setText("This User Has Written Hateful Comment "+data.getEmail_user_report());

        myViewHolder.btn_view.setOnClickListener(v -> {
            // الانتقال الى واجهة تفاصيل الابلاغ و نقل الاوبجكت الخاص في ال reports
            Intent intent = new Intent(context, ReportsActivity.class);
            intent.putExtra("data_reports",data);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_email_reports, tv_txt_reports;
        private Button btn_view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_email_reports = itemView.findViewById(R.id.tv_email_reports);
            tv_txt_reports = itemView.findViewById(R.id.tv_txt_reports);
            btn_view = itemView.findViewById(R.id.btn_view);


        }
    }

}
