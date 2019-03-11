package com.chiragsavsani.nytimes.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Feeds implements Parcelable {

    private String title, type, byline, feedAbstract, published_date, source, section,
            caption, mediaType, subtype, copyright;
    double id;

    public ArrayList<ImageData> images;

    public Feeds() {

    }

    protected Feeds(Parcel in) {
        id = in.readDouble();
        title = in.readString();
        type = in.readString();
        byline = in.readString();
        feedAbstract = in.readString();
        published_date = in.readString();
        source = in.readString();
        section = in.readString();
        caption = in.readString();
        mediaType = in.readString();
        subtype = in.readString();
        copyright = in.readString();

        if (in.readByte() == 0x01) {
            images = new ArrayList<ImageData>();
            in.readList(images, ImageData.class.getClassLoader());
        } else {
            images = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(byline);
        dest.writeString(feedAbstract);
        dest.writeString(published_date);
        dest.writeString(source);
        dest.writeString(section);

        dest.writeString(caption);
        dest.writeString(mediaType);
        dest.writeString(subtype);
        dest.writeString(copyright);
        if (images == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(images);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Feeds> CREATOR = new Parcelable.Creator<Feeds>() {
        @Override
        public Feeds createFromParcel(Parcel in) {
            return new Feeds(in);
        }

        @Override
        public Feeds[] newArray(int size) {
            return new Feeds[size];
        }
    };

    public Feeds(double id, String title, String type, String byline, String feedAbstract,
                 String published_date, String source, String section,
                 String caption, String mediaType, String subtype, String copyright, ArrayList<ImageData> images) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.byline = byline;
        this.feedAbstract = feedAbstract;
        this.published_date = published_date;
        this.source = source;
        this.section = section;
        this.caption = caption;
        this.mediaType = mediaType;
        this.subtype = subtype;
        this.images = images;
        this.copyright = copyright;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getFeedAbstract() {
        return feedAbstract;
    }

    public void setFeedAbstract(String feedAbstract) {
        this.feedAbstract = feedAbstract;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public ArrayList<ImageData> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageData> images) {
        this.images = images;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
