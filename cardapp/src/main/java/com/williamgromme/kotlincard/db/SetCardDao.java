package com.williamgromme.kotlincard.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Dad on 11/26/2017.
 */
@Dao
public interface SetCardDao {

    @Query("SELECT * FROM setcard")
    public Flowable<List<SetCard>> getAllSets();

    @Query("SELECT * FROM setcard ORDER BY section")
    public Flowable<List<SetCard>> getAllSetsSorted();

    @Query("SELECT * FROM setcard WHERE uid = :id")
    public SetCard getSet(Integer id);

    @Query("SELECT * FROM setcard WHERE name = :nameID")
    public SetCard getSetUniqueName(String nameID);

    @Insert
    public long insert(SetCard setCard);

    @Delete
    public void delete( SetCard setCard);

    @Update
    public int update(SetCard setCard);

//    @Query("UPDATE setcard SET count = :num  WHERE uid = :id")
//    public int updateCount(Integer num ,  Integer id);

}
