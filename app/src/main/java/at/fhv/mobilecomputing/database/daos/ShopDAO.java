package at.fhv.mobilecomputing.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.fhv.mobilecomputing.database.entities.Shop;

/**
 * Created by timorzipa on 26.03.18.
 */

@Dao
public interface ShopDAO {
    @Query("SELECT * FROM shops")
    List<Shop> getAll();

    @Query("SELECT * FROM shops WHERE id IN (:shopId)")
    List<Shop> loadAllByIds(int[] shopId);

    @Query("SELECT * FROM shops WHERE shops.name = :name")
    Shop findByName(String name);

    @Query("SELECT * FROM shops WHERE shops.id = :id")
    Shop findById(int id);

    @Insert
    void insertAll(Shop... shops);

    @Update
    void updateAll(Shop... shops);

    @Delete
    void delete(Shop shop);
}
