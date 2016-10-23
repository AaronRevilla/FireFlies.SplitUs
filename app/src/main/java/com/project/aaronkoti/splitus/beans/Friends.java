package com.project.aaronkoti.splitus.beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Friends {

    @SerializedName("Friends")
    @Expose
    private List<Friend> friends = new ArrayList<Friend>();

    /**
     *
     * @return
     * The friends
     */
    public List<Friend> getFriends() {
        return friends;
    }

    /**
     *
     * @param friends
     * The Friends
     */
    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

}