package com.lmh.mytraveldairyjava;

public class SettingHelperClass {

    Integer base_CURR_CD, login_MAT_ID, now_USER_ST;
    String  disp_MAIL_ID, cover_PIC, nic_NAME_NM, profile_PIC, push_ALAR_ST, sele_MAIL_PK, tstamp_UP_DT, tstamp_CR_DT;

    public SettingHelperClass() {

    }

    public SettingHelperClass(Integer base_CURR_CD, String cover_PIC, Integer login_MAT_ID, Integer now_USER_ST, String disp_MAIL_ID, String nic_NAME_NM, String profile_PIC, String push_ALAR_ST, String sele_MAIL_PK, String tstamp_UP_DT, String tstamp_CR_DT) {
        this.base_CURR_CD = base_CURR_CD;
        this.cover_PIC = cover_PIC;
        this.login_MAT_ID = login_MAT_ID;
        this.now_USER_ST = now_USER_ST;
        this.disp_MAIL_ID = disp_MAIL_ID;
        this.nic_NAME_NM = nic_NAME_NM;
        this.profile_PIC = profile_PIC;
        this.push_ALAR_ST = push_ALAR_ST;
        this.sele_MAIL_PK = sele_MAIL_PK;
        this.tstamp_UP_DT = tstamp_UP_DT;
        this.tstamp_CR_DT = tstamp_CR_DT;
    }

    public Integer getBase_CURR_CD() {
        return base_CURR_CD;
    }

    public void setBase_CURR_CD(Integer base_CURR_CD) {
        this.base_CURR_CD = base_CURR_CD;
    }

    public Integer getLogin_MAT_ID() {
        return login_MAT_ID;
    }

    public void setLogin_MAT_ID(Integer login_MAT_ID) {
        this.login_MAT_ID = login_MAT_ID;
    }

    public Integer getNow_USER_ST() {
        return now_USER_ST;
    }

    public void setNow_USER_ST(Integer now_USER_ST) {
        this.now_USER_ST = now_USER_ST;
    }

    public String getDisp_MAIL_ID() {
        return disp_MAIL_ID;
    }

    public void setDisp_MAIL_ID(String disp_MAIL_ID) {
        this.disp_MAIL_ID = disp_MAIL_ID;
    }

    public String getNic_NAME_NM() {
        return nic_NAME_NM;
    }

    public void setNic_NAME_NM(String nic_NAME_NM) {
        this.nic_NAME_NM = nic_NAME_NM;
    }

    public String getProfile_PIC() {
        return profile_PIC;
    }

    public void setProfile_PIC(String profile_PIC) {
        this.profile_PIC = profile_PIC;
    }

    public String getPush_ALAR_ST() {
        return push_ALAR_ST;
    }

    public void setPush_ALAR_ST(String push_ALAR_ST) {
        this.push_ALAR_ST = push_ALAR_ST;
    }

    public String getSele_MAIL_PK() {
        return sele_MAIL_PK;
    }

    public void setSele_MAIL_PK(String sele_MAIL_PK) {
        this.sele_MAIL_PK = sele_MAIL_PK;
    }

    public String getTstamp_UP_DT() {
        return tstamp_UP_DT;
    }

    public void setTstamp_UP_DT(String tstamp_UP_DT) {
        this.tstamp_UP_DT = tstamp_UP_DT;
    }

    public String getTstamp_CR_DT() {
        return tstamp_CR_DT;
    }

    public void setTstamp_CR_DT(String tstamp_CR_DT) {
        this.tstamp_CR_DT = tstamp_CR_DT;
    }

    public String getCover_PIC() {
        return cover_PIC;
    }

    public void setCover_PIC(String cover_PIC) {
        this.cover_PIC = cover_PIC;
    }
}
