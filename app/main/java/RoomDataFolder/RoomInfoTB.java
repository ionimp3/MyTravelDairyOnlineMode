package RoomDataFolder;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomInfoTB {
    @PrimaryKey(autoGenerate = true)
    public int InfoTB_Id;
    public String InfoTB_planName;
    public String InfoTB_planDepartDate;
    public String InfoTB_planEndDate;
    public String InfoTB_budget;
    public String InfoTB_planCountry;
    public String InfoTB_subCurrency;
    public String InfoTB_nowUserStatus;
    public String InfoTB_planTags;
    public String InfoTB_titleImage;
    public int InfoTB_viewCount;
    public String InfoTB_timeStampUpdateTime;
    public String InfoTB_timeStampCreateTime;
    public String InfoTB_UserTB_nicName;
    public String InfoTB_UserTB_nowUserStatus;
    public String InfoTB_UserTB_profileImage;

    public RoomInfoTB() {
    }

    public RoomInfoTB(String infoTB_planName, String infoTB_planDepartDate, String infoTB_planEndDate, String infoTB_budget, String infoTB_planCountry, String infoTB_subCurrency, String infoTB_nowUserStatus, String infoTB_planTags, String infoTB_titleImage, int infoTB_viewCount, String infoTB_timeStampUpdateTime, String infoTB_timeStampCreateTime, String infoTB_UserTB_nicName, String infoTB_UserTB_nowUserStatus, String infoTB_UserTB_profileImage) {

        InfoTB_planName = infoTB_planName;
        InfoTB_planDepartDate = infoTB_planDepartDate;
        InfoTB_planEndDate = infoTB_planEndDate;
        InfoTB_budget = infoTB_budget;
        InfoTB_planCountry = infoTB_planCountry;
        InfoTB_subCurrency = infoTB_subCurrency;
        InfoTB_nowUserStatus = infoTB_nowUserStatus;
        InfoTB_planTags = infoTB_planTags;
        InfoTB_titleImage = infoTB_titleImage;
        InfoTB_viewCount = infoTB_viewCount;
        InfoTB_timeStampUpdateTime = infoTB_timeStampUpdateTime;
        InfoTB_timeStampCreateTime = infoTB_timeStampCreateTime;
        InfoTB_UserTB_nicName = infoTB_UserTB_nicName;
        InfoTB_UserTB_nowUserStatus = infoTB_UserTB_nowUserStatus;
        InfoTB_UserTB_profileImage = infoTB_UserTB_profileImage;
    }

    public int getInfoTB_Id() {
        return InfoTB_Id;
    }

    public void setInfoTB_Id(int infoTB_Id) {
        InfoTB_Id = infoTB_Id;
    }

    public String getInfoTB_planName() {
        return InfoTB_planName;
    }

    public void setInfoTB_planName(String infoTB_planName) {
        InfoTB_planName = infoTB_planName;
    }

    public String getInfoTB_planDepartDate() {
        return InfoTB_planDepartDate;
    }

    public void setInfoTB_planDepartDate(String infoTB_planDepartDate) {
        InfoTB_planDepartDate = infoTB_planDepartDate;
    }

    public String getInfoTB_planEndDate() {
        return InfoTB_planEndDate;
    }

    public void setInfoTB_planEndDate(String infoTB_planEndDate) {
        InfoTB_planEndDate = infoTB_planEndDate;
    }

    public String getInfoTB_budget() {
        return InfoTB_budget;
    }

    public void setInfoTB_budget(String infoTB_budget) {
        InfoTB_budget = infoTB_budget;
    }

    public String getInfoTB_planCountry() {
        return InfoTB_planCountry;
    }

    public void setInfoTB_planCountry(String infoTB_planCountry) {
        InfoTB_planCountry = infoTB_planCountry;
    }

    public String getInfoTB_subCurrency() {
        return InfoTB_subCurrency;
    }

    public void setInfoTB_subCurrency(String infoTB_subCurrency) {
        InfoTB_subCurrency = infoTB_subCurrency;
    }

    public String getInfoTB_nowUserStatus() {
        return InfoTB_nowUserStatus;
    }

    public void setInfoTB_nowUserStatus(String infoTB_nowUserStatus) {
        InfoTB_nowUserStatus = infoTB_nowUserStatus;
    }

    public String getInfoTB_planTags() {
        return InfoTB_planTags;
    }

    public void setInfoTB_planTags(String infoTB_planTags) {
        InfoTB_planTags = infoTB_planTags;
    }

    public String getInfoTB_titleImage() {
        return InfoTB_titleImage;
    }

    public void setInfoTB_titleImage(String infoTB_titleImage) {
        InfoTB_titleImage = infoTB_titleImage;
    }

    public int getInfoTB_viewCount() {
        return InfoTB_viewCount;
    }

    public void setInfoTB_viewCount(int infoTB_viewCount) {
        InfoTB_viewCount = infoTB_viewCount;
    }

    public String getInfoTB_timeStampUpdateTime() {
        return InfoTB_timeStampUpdateTime;
    }

    public void setInfoTB_timeStampUpdateTime(String infoTB_timeStampUpdateTime) {
        InfoTB_timeStampUpdateTime = infoTB_timeStampUpdateTime;
    }

    public String getInfoTB_timeStampCreateTime() {
        return InfoTB_timeStampCreateTime;
    }

    public void setInfoTB_timeStampCreateTime(String infoTB_timeStampCreateTime) {
        InfoTB_timeStampCreateTime = infoTB_timeStampCreateTime;
    }

    public String getInfoTB_UserTB_nicName() {
        return InfoTB_UserTB_nicName;
    }

    public void setInfoTB_UserTB_nicName(String infoTB_UserTB_nicName) {
        InfoTB_UserTB_nicName = infoTB_UserTB_nicName;
    }

    public String getInfoTB_UserTB_nowUserStatus() {
        return InfoTB_UserTB_nowUserStatus;
    }

    public void setInfoTB_UserTB_nowUserStatus(String infoTB_UserTB_nowUserStatus) {
        InfoTB_UserTB_nowUserStatus = infoTB_UserTB_nowUserStatus;
    }

    public String getInfoTB_UserTB_profileImage() {
        return InfoTB_UserTB_profileImage;
    }

    public void setInfoTB_UserTB_profileImage(String infoTB_UserTB_profileImage) {
        InfoTB_UserTB_profileImage = infoTB_UserTB_profileImage;
    }

    @Override
    public String toString() {
        return "RoomInfoTB{" +
                "InfoTB_Id=" + InfoTB_Id +
                ", InfoTB_planName='" + InfoTB_planName + '\'' +
                ", InfoTB_planDepartDate='" + InfoTB_planDepartDate + '\'' +
                ", InfoTB_planEndDate='" + InfoTB_planEndDate + '\'' +
                ", InfoTB_budget='" + InfoTB_budget + '\'' +
                ", InfoTB_planCountry='" + InfoTB_planCountry + '\'' +
                ", InfoTB_subCurrency='" + InfoTB_subCurrency + '\'' +
                ", InfoTB_nowUserStatus='" + InfoTB_nowUserStatus + '\'' +
                ", InfoTB_planTags='" + InfoTB_planTags + '\'' +
                ", InfoTB_titleImage='" + InfoTB_titleImage + '\'' +
                ", InfoTB_viewCount=" + InfoTB_viewCount +
                ", InfoTB_timeStampUpdateTime='" + InfoTB_timeStampUpdateTime + '\'' +
                ", InfoTB_timeStampCreateTime='" + InfoTB_timeStampCreateTime + '\'' +
                ", InfoTB_UserTB_nicName='" + InfoTB_UserTB_nicName + '\'' +
                ", InfoTB_UserTB_nowUserStatus='" + InfoTB_UserTB_nowUserStatus + '\'' +
                ", InfoTB_UserTB_profileImage='" + InfoTB_UserTB_profileImage + '\'' +
                '}';
    }
}
