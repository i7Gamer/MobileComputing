package at.fhv.mobilecomputing.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.fhv.mobilecomputing.database.entities.Setting;

/**
 * Created by timorzipa on 26.03.18.
 */

@Dao
public interface SettingsDAO {
    @Query("SELECT * FROM settings")
    List<Setting> getAll();

    @Query("SELECT * FROM settings WHERE settings.`key` = :key")
    List<Setting> loadAllByKeys(String key);

    @Insert
    void insertAll(Setting... settings);

    @Update
    void updateAll(Setting... settings);

    @Delete
    void delete(Setting setting);
}
