package RoomDataFolder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities = {RoomUserTB.class,RoomInfoTB.class,RoomToDoRawTB.class,RoomRouteTB.class,RoomCostRawTB.class,RoomCostSumTB.class
        ,RoomMemoRawTB.class,RoomTagsRawTB.class, RoomPlaceRawTB.class,RoomLinkRawTB.class,RoomPushAlarmTB.class,RoomUploadFileListTB.class}
        , version = 1)
public abstract class RoomMyTravelDairyDatabase extends RoomDatabase {

    public abstract  RoomUserTBdao getRoomUserTBdao();

    public abstract RoomInfoTBdao getRoomInfoTBdao();

    private static volatile   RoomMyTravelDairyDatabase INSTANCE;

    public static RoomMyTravelDairyDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized ( RoomMyTravelDairyDatabase.class){
                INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext(),RoomMyTravelDairyDatabase.class,"MyTravelDairyDatabase")
                        .build();
            }
        }
        return INSTANCE;
    }
}
