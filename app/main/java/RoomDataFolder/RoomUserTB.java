package RoomDataFolder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomUserTB {
    @PrimaryKey(autoGenerate = true)
    public int UserTB_Id;
    public String UserTB_coverImage;
    public String UserTB_coverImageKey;
    public String UserTB_nicName;
    public String UserTB_nowUserStatus;
    public String UserTB_profileImage;
    public String UserTB_profileImageKey;
    public String UserTB_pushAlarmSelected;
    public String UserTB_timeStampCreateTime;
    public String UserTB_timeStampUpdateTime;

    public RoomUserTB() {
    }

    public RoomUserTB( String userTB_coverImage, String userTB_coverImageKey, String userTB_nicName, String userTB_nowUserStatus, String userTB_profileImage, String userTB_profileImageKey, String userTB_pushAlarmSelected, String userTB_timeStampCreateTime, String userTB_timeStampUpdateTime) {
        UserTB_coverImage = userTB_coverImage;
        UserTB_coverImageKey = userTB_coverImageKey;
        UserTB_nicName = userTB_nicName;
        UserTB_nowUserStatus = userTB_nowUserStatus;
        UserTB_profileImage = userTB_profileImage;
        UserTB_profileImageKey = userTB_profileImageKey;
        UserTB_pushAlarmSelected = userTB_pushAlarmSelected;
        UserTB_timeStampCreateTime = userTB_timeStampCreateTime;
        UserTB_timeStampUpdateTime = userTB_timeStampUpdateTime;
    }

    public int getUserTB_Id() {
        return UserTB_Id;
    }

    public void setUserTB_Id(int userTB_Id) {
        UserTB_Id = userTB_Id;
    }

    public String getUserTB_coverImage() {
        return UserTB_coverImage;
    }

    public void setUserTB_coverImage(String userTB_coverImage) {
        UserTB_coverImage = userTB_coverImage;
    }

    public String getUserTB_coverImageKey() {
        return UserTB_coverImageKey;
    }

    public void setUserTB_coverImageKey(String userTB_coverImageKey) {
        UserTB_coverImageKey = userTB_coverImageKey;
    }

    public String getUserTB_nicName() {
        return UserTB_nicName;
    }

    public void setUserTB_nicName(String userTB_nicName) {
        UserTB_nicName = userTB_nicName;
    }

    public String getUserTB_nowUserStatus() {
        return UserTB_nowUserStatus;
    }

    public void setUserTB_nowUserStatus(String userTB_nowUserStatus) {
        UserTB_nowUserStatus = userTB_nowUserStatus;
    }

    public String getUserTB_profileImage() {
        return UserTB_profileImage;
    }

    public void setUserTB_profileImage(String userTB_profileImage) {
        UserTB_profileImage = userTB_profileImage;
    }

    public String getUserTB_profileImageKey() {
        return UserTB_profileImageKey;
    }

    public void setUserTB_profileImageKey(String userTB_profileImageKey) {
        UserTB_profileImageKey = userTB_profileImageKey;
    }

    public String getUserTB_pushAlarmSelected() {
        return UserTB_pushAlarmSelected;
    }

    public void setUserTB_pushAlarmSelected(String userTB_pushAlarmSelected) {
        UserTB_pushAlarmSelected = userTB_pushAlarmSelected;
    }

    public String getUserTB_timeStampCreateTime() {
        return UserTB_timeStampCreateTime;
    }

    public void setUserTB_timeStampCreateTime(String userTB_timeStampCreateTime) {
        UserTB_timeStampCreateTime = userTB_timeStampCreateTime;
    }

    public String getUserTB_timeStampUpdateTime() {
        return UserTB_timeStampUpdateTime;
    }

    public void setUserTB_timeStampUpdateTime(String userTB_timeStampUpdateTime) {
        UserTB_timeStampUpdateTime = userTB_timeStampUpdateTime;
    }

    @Override
    public String toString() {
        return "RoomUserTB{" +
                "UserTB_Id=" + UserTB_Id +
                ", UserTB_coverImage='" + UserTB_coverImage + '\'' +
                ", UserTB_coverImageKey='" + UserTB_coverImageKey + '\'' +
                ", UserTB_nicName='" + UserTB_nicName + '\'' +
                ", UserTB_nowUserStatus='" + UserTB_nowUserStatus + '\'' +
                ", UserTB_profileImage='" + UserTB_profileImage + '\'' +
                ", UserTB_profileImageKey='" + UserTB_profileImageKey + '\'' +
                ", UserTB_pushAlarmSelected='" + UserTB_pushAlarmSelected + '\'' +
                ", UserTB_timeStampCreateTime='" + UserTB_timeStampCreateTime + '\'' +
                ", UserTB_timeStampUpdateTime='" + UserTB_timeStampUpdateTime + '\'' +
                '}';
    }
}
