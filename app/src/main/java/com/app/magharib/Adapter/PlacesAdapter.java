package com.app.magharib.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.app.magharib.interfaces.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.List;


public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    private Context context;
    private List<Experience> objects;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public PlacesAdapter(Context context, List<Experience> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_places, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Experience data = objects.get(i);

        myViewHolder.tv_name_places.setText(data.getName_experience());
        myViewHolder.tv_price_places.setText(data.getPrice_experience() + " SAR");


        Glide.with(context)
                .load(data.getImage_uri_experience())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        myViewHolder.mAviLoading.setVisibility(View.GONE);
                        return false;
                    }
                }).into(myViewHolder.image_experience);


        if (i == 0) {
            myViewHolder.view_places.setVisibility(View.GONE);
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
        private TextView tv_name_places, tv_price_places;
        private AVLoadingIndicatorView mAviLoading;
        private ImageView image_experience;
        private View view_places;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_experience = itemView.findViewById(R.id.image_experience);
            mAviLoading = itemView.findViewById(R.id.avi_Loading);
            tv_name_places = itemView.findViewById(R.id.tv_name_places);
            tv_price_places = itemView.findViewById(R.id.tv_price_places);
            view_places = itemView.findViewById(R.id.view_places);


        }
    }

}
