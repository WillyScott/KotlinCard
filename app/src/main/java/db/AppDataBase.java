package db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Dad on 11/15/2017.
 */
@Database(entities = {FlashCard.class, SetCard.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase{
    private static AppDataBase INSTANCE;
    public abstract FlashCardDao flashCardDao();
    public abstract SetCardDao setCardDao();

    public static AppDataBase getINSTANCE(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,AppDataBase.class,"my-database").build();
        }
        return INSTANCE;
    }
}
