package com.chiragsavsani.nytimes.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chiragsavsani.nytimes.R;
import com.chiragsavsani.nytimes.entities.Feeds;
import com.chiragsavsani.nytimes.entities.ImageData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Feeds> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Feeds obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public FeedAdapter(Context context, List<Feeds> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView txtTitle, txtByline, txtPublishedDate, txtSection;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.image);

            txtTitle = v.findViewById(R.id.txtTitle);
            txtByline = v.findViewById(R.id.txtByline);
            txtPublishedDate = v.findViewById(R.id.txtPublishedDate);
            txtSection = v.findViewById(R.id.txtSection);

            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_item_raw, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Feeds feeds = items.get(position);

            view.txtTitle.setText(feeds.getTitle());
            view.txtByline.setText(feeds.getByline());
            view.txtPublishedDate.setText(feeds.getPublished_date());
            view.txtSection.setText(feeds.getSection());

            String url = "";

            ArrayList<ImageData> imageArray = feeds.getImages();
            //Get first image for Display in icon which is low size
            ImageData imgData = imageArray.get(0);

            displayImageRound(ctx, view.image, imgData.getUrl());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void displayImageRound(final Context ctx, final ImageView img, String imgUrl) {
        try {
            /*Glide.with(ctx).load(imgUrl)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);*/

            Glide.with(ctx).load(imgUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
