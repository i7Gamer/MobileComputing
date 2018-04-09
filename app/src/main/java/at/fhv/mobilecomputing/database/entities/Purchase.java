package at.fhv.mobilecomputing.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import at.fhv.mobilecomputing.database.converters.TimestampConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by timorzipa on 26.03.18.
 */
@Data
@NoArgsConstructor
@Entity(tableName = "purchases")
public class Purchase {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @lombok.NonNull
    @ColumnInfo(name = "date")
    @TypeConverters({TimestampConverter.class})
    private Date date;

    @ColumnInfo(name = "total")
    private double total;
}