package at.fhv.mobilecomputing.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.fhv.mobilecomputing.database.entities.Item;

/**
 * Created by timorzipa on 26.03.18.
 */

@Dao
public interface ItemDAO {
    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Query("SELECT * FROM items WHERE id IN (:itemIds)")
    List<Item> loadAllByIds(int[] itemIds);

    @Query("SELECT * FROM items WHERE name LIKE '%' + :name + '%'")
    Item findByName(String name);

    @Insert
    void insertAll(Item... items);

    @Update
    void updateAll(Item... items);

    @Delete
    void delete(Item item);
}
