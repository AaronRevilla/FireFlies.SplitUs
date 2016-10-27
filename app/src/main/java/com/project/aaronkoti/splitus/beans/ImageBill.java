package com.project.aaronkoti.splitus.beans;

import android.graphics.Bitmap;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("org.jsonschema2pojo")
public class ImageBill implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("usrUid")
    @Expose
    private String usrUid;
    @SerializedName("billId")
    @Expose
    private String billId;
    @SerializedName("img")
    @Expose
    private String img;



    public ImageBill(){}

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
     * The status
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     *
     * @param imgUrl
     * The imgUrl
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getUsrUid() {
        return usrUid;
    }

    public void setUsrUid(String usrUid) {
        this.usrUid = usrUid;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "ImageBill{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}