package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class EventUserBill {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fk_event")
    @Expose
    private String fkEvent;
    @SerializedName("fk_user")
    @Expose
    private String fkUser;
    @SerializedName("fk_bill")
    @Expose
    private String fkBill;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;


    public EventUserBill(){}


    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The fkEvent
     */
    public String getFkEvent() {
        return fkEvent;
    }

    /**
     *
     * @param fkEvent
     * The fk_event
     */
    public void setFkEvent(String fkEvent) {
        this.fkEvent = fkEvent;
    }

    /**
     *
     * @return
     * The fkUser
     */
    public String getFkUser() {
        return fkUser;
    }

    /**
     *
     * @param fkUser
     * The fk_user
     */
    public void setFkUser(String fkUser) {
        this.fkUser = fkUser;
    }

    /**
     *
     * @return
     * The fkBill
     */
    public String getFkBill() {
        return fkBill;
    }

    /**
     *
     * @param fkBill
     * The fk_bill
     */
    public void setFkBill(String fkBill) {
        this.fkBill = fkBill;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}