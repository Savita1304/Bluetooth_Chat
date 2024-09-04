package com.goo.vapps.bluetooth.chat.Model;

/**
 * Created by compind-pc8 on 18/2/17.
 */
public class ChatData {


    int id;
    String message,fid;




    public ChatData() {
    }




    public ChatData(String message, String fid) {
        this.message = message;
        this.fid = fid;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
