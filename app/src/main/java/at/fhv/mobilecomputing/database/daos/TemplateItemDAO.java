package at.fhv.mobilecomputing.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.fhv.mobilecomputing.database.entities.TemplateItem;

/**
 * Created by timorzipa on 26.03.18.
 */

@Dao
public interface TemplateItemDAO {
    @Query("SELECT * FROM templateItems")
    List<TemplateItem> getAll();

    @Query("SELECT * FROM templateItems WHERE id IN (:templateItemIds)")
    List<TemplateItem> loadAllByIds(int[] templateItemIds);

    @Query("SELECT * FROM templateItems WHERE name = :name")
    TemplateItem findByName(String name);

    @Insert
    void insertAll(TemplateItem... templateItems);

    @Update
    void updateAll(TemplateItem... templateItems);

    @Delete
    void delete(TemplateItem templateItem);
}
