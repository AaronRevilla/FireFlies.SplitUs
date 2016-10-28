package com.project.aaronkoti.splitus.beans;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Result {

    @SerializedName("message_id")
    @Expose
    private String messageId;

    /**
     *
     * @return
     * The messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     *
     * @param messageId
     * The message_id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}