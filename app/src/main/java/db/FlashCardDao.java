package db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Dad on 11/15/2017.
 */
@Dao
public interface FlashCardDao {
    @Query("SELECT * FROM flashcard WHERE set_name = :id")
    public List<FlashCard> getAll(String id);


    @Insert
    public long insert(FlashCard flashcard);

    @Update
    public int update(FlashCard flashcard);

}
