package com.example.dad.kotlincard.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Dad on 11/23/2017.
 */
@Entity()
public class SetCard {
    @PrimaryKey(autoGenerate = true)
    private Integer uid;

    @ColumnInfo()
    private String name;

    @ColumnInfo()
    private String section;

    @ColumnInfo()
    public String description;

    @ColumnInfo()
    public Boolean randomize = true;

    @ColumnInfo()
    public Boolean reverse = false;

    @ColumnInfo()
    public String urlString = "https://raw.githubusercontent.com/WillyScott/FlashCardsData/master/Swift_KeywordsV3_0_1.json";

    @ColumnInfo()
    public Integer count = 0;

    public SetCard (String name, String section, String description, String urlString) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.urlString = urlString;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) { this.uid = uid;}

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRandomize() {
        return randomize;
    }

    public void setRandomize(Boolean randomize) {
        this.randomize = randomize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}