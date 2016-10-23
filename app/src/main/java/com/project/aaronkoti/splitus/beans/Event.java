package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Generated("org.jsonschema2pojo")
public class Event {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bill")
    @Expose
    private Bill bill;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("ownerEvent")
    @Expose
    private User ownerEvent;

    public Event(){}

    public User getOwnerEvent() {
        return ownerEvent;
    }

    public void setOwnerEvent(User ownerEvent) {
        this.ownerEvent = ownerEvent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}