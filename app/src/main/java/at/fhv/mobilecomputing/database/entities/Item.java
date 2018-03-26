package at.fhv.mobilecomputing.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by timorzipa on 26.03.18.
 */

@Entity(tableName = "items",
        foreignKeys = {
                @ForeignKey(entity = Shop.class, parentColumns = "id", childColumns = "shopId"),
                @ForeignKey(entity = Purchase.class, parentColumns = "id", childColumns = "purchaseId")
        }
)
@Data
@NoArgsConstructor
public class Item {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @lombok.NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "shopId")
    private String shopId;

    @ColumnInfo(name = "purchaseId")
    private String purchaseId;

    @ColumnInfo(name = "dueDate")
    private String dueDate;

    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    private byte[] picture;
}
