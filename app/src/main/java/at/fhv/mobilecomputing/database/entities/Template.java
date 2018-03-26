package at.fhv.mobilecomputing.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by timorzipa on 26.03.18.
 */

@Data
@NoArgsConstructor
@Entity(tableName = "templates")
public class Template {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @lombok.NonNull
    @ColumnInfo(name = "name")
    private String name;
}
