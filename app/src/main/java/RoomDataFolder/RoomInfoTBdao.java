package RoomDataFolder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomInfoTBdao {
    @Insert
    void insert(RoomInfoTB roomInfoTB);

    @Update
    void update(RoomInfoTB roomInfoTB);
    @Delete
    void delete(RoomInfoTB roomInfoTB);

    @Query("SELECT * FROM RoomInfoTB")
    LiveData<List<RoomInfoTB>> getAllInfoTB();

    @Query("SELECT * FROM RoomInfoTB")
    LiveData<List<RoomInfoTB>> getAllInfoTBLiveData();

    @Query("SELECT * FROM RoomInfoTB")
        List<RoomInfoTB> getAllInfoTBList();

    @Query("SELECT * FROM RoomInfoTB WHERE InfoTB_Id LIKE :infoTBId")
    RoomInfoTB findRoomInfoTBById(int infoTBId);
}
