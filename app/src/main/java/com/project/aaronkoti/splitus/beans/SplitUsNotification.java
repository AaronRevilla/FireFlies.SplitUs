package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SplitUsNotification {

    @SerializedName("notification")
    @Expose
    private Notification notification;
    @SerializedName("to")
    @Expose
    private String to;

    /**
     *
     * @return
     * The notification
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     *
     * @param notification
     * The notification
     */
    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    /**
     *
     * @return
     * The to
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to
     * The to
     */
    public void setTo(String to) {
        this.to = to;
    }

}
