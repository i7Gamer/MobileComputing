package at.fhv.mobilecomputing.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.fhv.mobilecomputing.database.entities.Purchase;

/**
 * Created by timorzipa on 26.03.18.
 */

@Dao
public interface PurchaseDAO {
    @Query("SELECT * FROM purchases")
    List<Purchase> getAll();

    @Query("SELECT * FROM purchases WHERE id IN (:purchaseId)")
    List<Purchase> loadAllByIds(int[] purchaseId);

    @Insert
    long[] insertAll(Purchase... purchases);

    @Insert
    long insert(Purchase purchase);

    @Update
    void updateAll(Purchase... purchases);

    @Delete
    void delete(Purchase purchase);
}
