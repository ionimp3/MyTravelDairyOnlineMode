package com.lmh.mytraveldairyjava;

public class BaiscinfoHelper {

    String aplanname;
    String aplandepartday;
    String aplandays;
    String update_timestamp;
    String create_timestamp;
    String aloginemail ;

    public BaiscinfoHelper() {

    }

    public BaiscinfoHelper(String aplanname, String aplandepartday, String aplandays, String update_timestamp, String create_timestamp) {
        this.aplanname = aplanname;
        this.aplandepartday = aplandepartday;
        this.aplandays = aplandays;
        this.update_timestamp = update_timestamp;
        this.create_timestamp = create_timestamp;
        this.aloginemail = aloginemail;
    }

    public String getAplanname() {
        return aplanname;
    }

    public void setAplanname(String aplanname) {
        this.aplanname = aplanname;
    }

    public String getAplandepartday() {
        return aplandepartday;
    }

    public void setAplandepartday(String aplandepartday) {
        this.aplandepartday = aplandepartday;
    }

    public String getAplandays() {
        return aplandays;
    }

    public void setAplandays(String aplandays) {
        this.aplandays = aplandays;
    }

    public String getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(String update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getCreate_timestamp() {
        return create_timestamp;
    }

    public void setCreate_timestamp(String create_timestamp) {
        this.create_timestamp = create_timestamp;
    }

    public String getAloginemail() {
        return aloginemail;
    }

    public void setAloginemail(String aloginemail) {
        this.aloginemail = aloginemail;
    }
}
