package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Friend {

    @SerializedName("myId")
    @Expose
    private String myId;
    @SerializedName("friendId")
    @Expose
    private String friendId;

    /**
     *
     * @return
     * The myId
     */
    public String getMyId() {
        return myId;
    }

    /**
     *
     * @param myId
     * The myId
     */
    public void setMyId(String myId) {
        this.myId = myId;
    }

    /**
     *
     * @return
     * The friendId
     */
    public String getFriendId() {
        return friendId;
    }

    /**
     *
     * @param friendId
     * The friendId
     */
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "myId='" + myId + '\'' +
                ", friendId='" + friendId + '\'' +
                '}';
    }
}