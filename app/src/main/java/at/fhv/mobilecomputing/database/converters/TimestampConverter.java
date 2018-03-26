package at.fhv.mobilecomputing.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by timorzipa on 26.03.18.
 */

public class TimestampConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
