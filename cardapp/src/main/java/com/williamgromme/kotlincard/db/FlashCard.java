package com.williamgromme.kotlincard.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.Random;

/**
 * Created by Dad on 11/15/2017.
 */
@Entity(foreignKeys = @ForeignKey(entity = SetCard.class, parentColumns = "uid",
                                                        childColumns = "set_uid", onDelete = ForeignKey.CASCADE),
         indices = { @Index(value = {"set_uid"})})

public class FlashCard {

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false)
    public int uid;

    @ColumnInfo ( name = "front_card")
    @SerializedName ("front") @Expose
    public String frontcard;

    @ColumnInfo (name = "back_card")
    @SerializedName("back") @Expose
    public String backcard;

    @ColumnInfo
    @Expose(serialize = false)
    public int random_num;

    @ColumnInfo
    @Expose(serialize = false)
    public Boolean show = true;

    @ColumnInfo
    @NonNull
    @Expose(serialize = false)
    public Integer set_uid;

    public FlashCard( String frontcard, String backcard, Integer set_uid, Boolean show ){
        this.frontcard = frontcard;
        this.backcard = backcard;
        this.set_uid = set_uid;
        this.show = show;
        this.random_num = new Random().nextInt();

    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFrontcard() {
        return frontcard;
    }

    public void setFrontcard(String frontcard) {
        this.frontcard = frontcard;
    }

    public String getBackcard() {
        return backcard;
    }

    public void setBackcard(String backcard) {
        this.backcard = backcard;
    }

    public int getRandom_num() {
        return random_num;
    }

    public void setRandom_num(int random_num) {
        this.random_num = random_num;
    }

    public Integer getSet_uid() {
        return set_uid;
    }

    public void setSet_name(Integer set_uid) {
        this.set_uid = set_uid;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

}
