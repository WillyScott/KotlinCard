package com.williamgromme.kotlincard.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Dad on 11/15/2017.
 */
@Dao
public interface FlashCardDao {
    @Query("SELECT * FROM flashcard WHERE set_uid = :id")
    public Flowable<List<FlashCard>> getAll(Integer id);

    @Query("SELECT * FROM flashcard WHERE set_uid = :id AND show = 1")
    public Flowable<List<FlashCard>> getAllStartFlowable(Integer id);

    @Query("SELECT * FROM flashcard WHERE set_uid = :id")
    public List<FlashCard> getAllNotFlowable(Integer id);

    @Query("SELECT * FROM flashcard WHERE set_uid = :id AND show = 1")
    public List<FlashCard> getAllStart(Integer id);

    //Flashcards sorted by randomize
    @Query("SELECT * FROM flashcard WHERE set_uid = :id AND show = 1 ORDER BY random_num")
    public List<FlashCard> getAllStartRandom(Integer id);

    @Query("SELECT * FROM flashcard WHERE uid = :id")
    public FlashCard get(Integer id);

    @Query("Update flashcard set show = 1 WHERE set_uid = :id")
    public int updateShowCardTrue(Integer id);

    @Query("SELECT COUNT(show) FROM flashcard WHERE set_uid = :id AND show = 1")
    public int countShowCardTrue(Integer id);

    @Insert
    public long insert(FlashCard flashcard);

    @Insert
    public void insertAll(List<FlashCard> flashCards);

    @Update
    public int updateAll(List<FlashCard> flashcard);

   @Update
    public int update(FlashCard flashCard);

//test
//    @Query("Update * FROM flashcard WHERE set_uid = :id and ")
//    public int updateID(flashcard flashCard, Integer id);

    @Delete
    void delete(FlashCard flashCard);

}
