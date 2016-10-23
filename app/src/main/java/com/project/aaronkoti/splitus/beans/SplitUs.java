package com.project.aaronkoti.splitus.beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SplitUs {

    @SerializedName("Users")
    @Expose
    private List<User> users = new ArrayList<User>();
    @SerializedName("Bills")
    @Expose
    private List<Bill> bills = new ArrayList<Bill>();
    @SerializedName("ImageBills")
    @Expose
    private List<ImageBill> imageBills = new ArrayList<ImageBill>();
    @SerializedName("Events")
    @Expose
    private List<Event> events = new ArrayList<Event>();
    @SerializedName("Event_User_Bills")
    @Expose
    private List<EventUserBill> eventUserBills = new ArrayList<EventUserBill>();

    public SplitUs(){}

    /**
     *
     * @return
     * The users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     *
     * @param users
     * The Users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     *
     * @return
     * The bills
     */
    public List<Bill> getBills() {
        return bills;
    }

    /**
     *
     * @param bills
     * The Bills
     */
    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    /**
     *
     * @return
     * The imageBills
     */
    public List<ImageBill> getImageBills() {
        return imageBills;
    }

    /**
     *
     * @param imageBills
     * The ImageBills
     */
    public void setImageBills(List<ImageBill> imageBills) {
        this.imageBills = imageBills;
    }

    /**
     *
     * @return
     * The events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     *
     * @param events
     * The Events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     *
     * @return
     * The eventUserBills
     */
    public List<EventUserBill> getEventUserBills() {
        return eventUserBills;
    }

    /**
     *
     * @param eventUserBills
     * The Event_User_Bills
     */
    public void setEventUserBills(List<EventUserBill> eventUserBills) {
        this.eventUserBills = eventUserBills;
    }

}