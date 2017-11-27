package db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Random;

/**
 * Created by Dad on 11/15/2017.
 */
@Entity(foreignKeys = @ForeignKey(entity = SetCard.class, parentColumns = "name",
                                                        childColumns = "set_name", onDelete = ForeignKey.CASCADE),
         indices = { @Index(value = {"set_name"})})

public class FlashCard {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo ( name = "front_card")
    public String frontcard;

    @ColumnInfo (name = "back_card")
    public String backcard;

    @ColumnInfo
    public int random_num;

    @ColumnInfo
    @NonNull
    public String set_name;

    public FlashCard( String frontcard, String backcard, String set_name){

        this.frontcard = frontcard;
        this.backcard = backcard;
        this.set_name = set_name;
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

    public String getSet_name() {
        return set_name;
    }

    public void setSet_name(String set_name) {
        this.set_name = set_name;
    }
}
