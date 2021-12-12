package com.app.magharib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;

import com.app.magharib.Model.Welcome;
import com.app.magharib.R;

import java.util.List;


public class WelcomeAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<Welcome> welcomeList;


    public WelcomeAdapter(List<Welcome> welcomeList, Context context) {
        this.context = context;
        this.welcomeList = welcomeList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return welcomeList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_welcome, container, false);

        Welcome data = welcomeList.get(position);


        ImageView image_welcome = view.findViewById(R.id.image_welcome);
        image_welcome.setImageResource(data.getImage_welcome());

        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(data.getTitle());

        TextView tv_description = view.findViewById(R.id.tv_description);
        tv_description.setText(data.getDescription());

        container.addView(view);

        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}







