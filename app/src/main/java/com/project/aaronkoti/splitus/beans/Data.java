package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("time")
    @Expose
    private String time;

    /**
     *
     * @return
     * The score
     */
    public String getScore() {
        return score;
    }

    /**
     *
     * @param score
     * The score
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     *
     * @return
     * The time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(String time) {
        this.time = time;
    }

}