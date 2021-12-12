package com.app.magharib.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.magharib.Model.DataLocation;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;

import java.util.List;


public class ListPopupAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<DataLocation> dataLocations;
    private LayoutInflater layoutInflater;


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public ListPopupAdapter(Activity activity, List<DataLocation> cities) {
        this.mActivity = activity;
        this.dataLocations = cities;
        layoutInflater = mActivity.getLayoutInflater();
    }



    @Override
    public int getCount() {
        return dataLocations.size();
    }

    @Override
    public Object getItem(int i) {
        return dataLocations.get(i);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_cities, null);
            holder.tv_name_cities = convertView.findViewById(R.id.tv_name_cities);
            holder.line_popup = convertView.findViewById(R.id.line_popup);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DataLocation data = dataLocations.get(position);

        holder.tv_name_cities.setText(data.getCity_name());

        holder.line_popup.setOnClickListener(v -> {
            mOnItemClickListener.onClick(data, v, position);
        });


        return convertView;
    }

    public class ViewHolder {
        private TextView tv_name_cities;
        private LinearLayout line_popup ;

    }


}