package db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Dad on 11/23/2017.
 */
@Entity()
public class SetCard {
    @PrimaryKey
    @NonNull
    public String name;

    @ColumnInfo
    public String section;

    @ColumnInfo
    public String description;

    @ColumnInfo
    public Boolean randomize;

    @ColumnInfo
    public String urlString;

    public SetCard (String name, String section, String description, String urlString) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.urlString = urlString;
    }

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
}
