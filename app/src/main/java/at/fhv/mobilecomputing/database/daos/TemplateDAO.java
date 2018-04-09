package at.fhv.mobilecomputing.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.fhv.mobilecomputing.database.entities.Template;

/**
 * Created by timorzipa on 26.03.18.
 */

@Dao
public interface TemplateDAO {
    @Query("SELECT * FROM templates")
    List<Template> getAll();

    @Query("SELECT * FROM templates WHERE id IN (:templateIds)")
    List<Template> loadAllByIds(int[] templateIds);

    @Query("SELECT * FROM templates WHERE name = :name")
    Template findByName(String name);

    @Insert
    void insertAll(Template... templates);

    @Update
    void updateAll(Template... templates);

    @Delete
    void delete(Template template);
}
