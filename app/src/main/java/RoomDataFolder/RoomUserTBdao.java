package RoomDataFolder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomUserTBdao {

    @Insert
    void insert(RoomUserTB roomUserTB);

    @Update
    void update(RoomUserTB roomUserTB);

    @Delete
    void delete(RoomUserTB roomUserTB);

    @Query("SELECT * FROM RoomUserTB")
    LiveData<List<RoomUserTB>> getAllLiveData();

    @Query("SELECT * FROM RoomUserTB")
    List<RoomUserTB> getAllUserTBList();

    @Query("SELECT * FROM RoomUserTB WHERE UserTB_Id LIKE :userTBId")
    RoomUserTB findRoomUserTBById(int userTBId);

}
