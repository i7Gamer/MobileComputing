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
@Entity(tableName = "templateItems")
@Data
@NoArgsConstructor
public class TemplateItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ForeignKey(entity = Template.class, parentColumns = "id", childColumns = "templateId")
    @ColumnInfo(name = "templateId")
    private int templateId;

    @ForeignKey(entity = Shop.class, parentColumns = "id", childColumns = "shopId")
    @ColumnInfo(name = "shopId")
    private Integer shopId;

    @NonNull
    @lombok.NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    private byte[] picture;
}
