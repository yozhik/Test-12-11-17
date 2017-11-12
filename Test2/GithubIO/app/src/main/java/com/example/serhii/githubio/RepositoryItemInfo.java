package com.example.serhii.githubio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Serhii on 11/10/2017.
 */

public class RepositoryItemInfo implements Parcelable {
    private String name;
    private String description;
    private String language;
    private int starsCount;
    private int forksCount;
    private int issuesCount;
    private int watchersCount;
    private String updatedDate;

    public RepositoryItemInfo(String name, String description, String language, int starsCount,
                              int forksCount, int issuesCount, int watchersCount, String updatedDate) {
        this.name = name;
        this.description = description;
        this.language = language;
        this.starsCount = starsCount;
        this.forksCount = forksCount;
        this.issuesCount = issuesCount;
        this.watchersCount = watchersCount;
        this.updatedDate = updatedDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public int getIssuesCount() {
        return issuesCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeString(this.language);
        parcel.writeInt(this.starsCount);
        parcel.writeInt(this.forksCount);
        parcel.writeInt(this.issuesCount);
        parcel.writeInt(this.watchersCount);
        parcel.writeString(this.updatedDate);
    }

    public static final Parcelable.Creator<RepositoryItemInfo> CREATOR
            = new Parcelable.Creator<RepositoryItemInfo>() {
        public RepositoryItemInfo createFromParcel(Parcel in) {
            return new RepositoryItemInfo(in);
        }

        public RepositoryItemInfo[] newArray(int size) {
            return new RepositoryItemInfo[size];
        }
    };

    private RepositoryItemInfo(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.language = in.readString();
        this.starsCount = in.readInt();
        this.forksCount = in.readInt();
        this.issuesCount = in.readInt();
        this.watchersCount = in.readInt();
        this.updatedDate = in.readString();
    }
}