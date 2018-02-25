package com.example.android.bounty;



import android.graphics.PointF;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by user on 21/02/2018.
 */
@IgnoreExtraProperties
public class Job {
    private String giverId;
    private String jobTitle;
    private int price;
    private String description;
    private PointF startPoint;
    private PointF endPoint;
    private boolean status;
    private String takerId;

    public Job() {}
    public Job (String jobTitle, int price,
                String description, PointF startPoint, PointF endPoint,boolean status) {
        this.jobTitle = jobTitle;
        this.price = price;
        this.description = description;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.status = status;
        this.takerId = "";
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public int getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public PointF getStartPoint() {
        return this.startPoint;
    }
    public PointF getEndPoint() {
        return this.endPoint;
    }

    public boolean isActive() {
        return this.status;
    }

    public String getTakerId() {
        return this.takerId;
    }

}
