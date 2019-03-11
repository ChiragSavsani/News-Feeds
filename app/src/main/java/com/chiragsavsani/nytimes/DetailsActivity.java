package com.chiragsavsani.nytimes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chiragsavsani.nytimes.R;
import com.chiragsavsani.nytimes.entities.Feeds;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    TextView txtTitle, txtAbstract, imgSource, imageDetails, txtByline, txtPublishedDate;
    ImageView largeImage;
    ArrayList<Feeds> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        txtTitle = findViewById(R.id.txtTitle);
        txtAbstract = findViewById(R.id.txtAbstract);
        imgSource = findViewById(R.id.imgSource);
        imageDetails = findViewById(R.id.imageDetails);
        txtByline = findViewById(R.id.txtByline);
        txtPublishedDate = findViewById(R.id.txtPublishedDate);

        largeImage = findViewById(R.id.largeImage);

        detailList = getIntent().getParcelableArrayListExtra("DETAILS");

        Feeds feeds = detailList.get(0);

        String title = feeds.getTitle();
        String feedAbstract = feeds.getFeedAbstract();
        String copyright = feeds.getCopyright();
        String caption = feeds.getCaption();
        String byLine = feeds.getByline();
        String publishDate = feeds.getPublished_date();

        if(title.trim().isEmpty()) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setText(title);
        }

        if(feedAbstract.trim().isEmpty()) {
            txtAbstract.setVisibility(View.GONE);
        } else {
            txtAbstract.setText(feedAbstract);
        }

        if(copyright.trim().isEmpty()) {
            imgSource.setVisibility(View.GONE);
        } else {
            imgSource.setText(copyright);
        }

        if(caption.trim().isEmpty()) {
            imageDetails.setVisibility(View.GONE);
        } else {
            imageDetails.setText(caption);
        }

        if(byLine.trim().isEmpty()) {
            txtByline.setVisibility(View.GONE);
        } else {
            txtByline.setText(byLine);
        }

        if(publishDate.trim().isEmpty()) {
            txtPublishedDate.setVisibility(View.GONE);
        } else {
            txtPublishedDate.setText(publishDate);
        }
        displayImageOriginal(this, largeImage, feeds.getImages().get(feeds.images.size() -1).getUrl());
    }

     private void displayImageOriginal(Context ctx, ImageView img, String url) {
        try {
            Glide.with(ctx).load(url)
                    .crossFade().placeholder(R.drawable.ic_loading_jpg).error(R.drawable.ic_no_image_available)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
