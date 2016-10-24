package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Bill {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bill_name")
    @Expose
    private String billName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("status")
    @Expose
    private String status;
//    @SerializedName("imgList")
//    @Expose
//    private List<ImageBill> imgList;


    public Bill(){}

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
     * The billName
     */
    public String getBillName() {
        return billName;
    }

    /**
     *
     * @param billName
     * The bill_name
     */
    public void setBillName(String billName) {
        this.billName = billName;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The date
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(Date date) {
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

//    public List<ImageBill> getImgList() {
//        return imgList;
//    }
//
//    public void setImgList(List<ImageBill> imgList) {
//        this.imgList = imgList;
//    }
}