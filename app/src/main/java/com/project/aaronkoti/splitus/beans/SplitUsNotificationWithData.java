package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SplitUsNotificationWithData {

    @SerializedName("collapse_key")
    @Expose
    private String collapseKey;
    @SerializedName("time_to_live")
    @Expose
    private Integer timeToLive;
    @SerializedName("delay_while_idle")
    @Expose
    private Boolean delayWhileIdle;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("to")
    @Expose
    private String to;

    /**
     *
     * @return
     * The collapseKey
     */
    public String getCollapseKey() {
        return collapseKey;
    }

    /**
     *
     * @param collapseKey
     * The collapse_key
     */
    public void setCollapseKey(String collapseKey) {
        this.collapseKey = collapseKey;
    }

    /**
     *
     * @return
     * The timeToLive
     */
    public Integer getTimeToLive() {
        return timeToLive;
    }

    /**
     *
     * @param timeToLive
     * The time_to_live
     */
    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     *
     * @return
     * The delayWhileIdle
     */
    public Boolean getDelayWhileIdle() {
        return delayWhileIdle;
    }

    /**
     *
     * @param delayWhileIdle
     * The delay_while_idle
     */
    public void setDelayWhileIdle(Boolean delayWhileIdle) {
        this.delayWhileIdle = delayWhileIdle;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
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