package com.activity.entity;

import java.util.Date;

public class Feedback {
    private Long feedbackId;
    private Long activityId;
    private Long userId;
    private Integer rating;
    private String content;
    private Date feedbackTime;
    private String userName;
    private String activityName;

    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getFeedbackTime() { return feedbackTime; }
    public void setFeedbackTime(Date feedbackTime) { this.feedbackTime = feedbackTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
}
