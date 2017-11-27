package db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Dad on 11/26/2017.
 */
@Dao
public interface SetCardDao {

    @Query("SELECT * FROM setcard")
    public List<SetCard> getAll();

    @Insert
    public long insert(SetCard setCard);

//    @Query("SELECT * FROM flashcard WHERE set_name = :id")
//    public List<FlashCard> getAll(int id);

}
