package com.example.dad.kotlincard.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Dad on 11/15/2017.
 */
@Dao
public interface FlashCardDao {
    @Query("SELECT * FROM flashcard WHERE set_uid = :id")
    public Flowable<List<FlashCard>> getAll(Integer id);

    @Query("SELECT * FROM flashcard WHERE set_uid = :id AND show = 'true'")
    public Flowable<List<FlashCard>> getAllStart(Integer id);

    @Query("SELECT * FROM flashcard WHERE uid = :id")
    public FlashCard get(Integer id);

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
//    public int updateID(FlashCard flashCard, Integer id);

    @Delete
    void delete(FlashCard flashCard);

}
