package at.fhv.mobilecomputing.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import at.fhv.mobilecomputing.database.daos.ItemDAO;
import at.fhv.mobilecomputing.database.daos.PurchaseDAO;
import at.fhv.mobilecomputing.database.daos.SettingsDAO;
import at.fhv.mobilecomputing.database.daos.ShopDAO;
import at.fhv.mobilecomputing.database.daos.TemplateDAO;
import at.fhv.mobilecomputing.database.daos.TemplateItemDAO;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Purchase;
import at.fhv.mobilecomputing.database.entities.Setting;
import at.fhv.mobilecomputing.database.entities.Shop;
import at.fhv.mobilecomputing.database.entities.Template;
import at.fhv.mobilecomputing.database.entities.TemplateItem;

/**
 * Created by timorzipa on 26.03.18.
 */

@Database(entities = {Item.class, Purchase.class, Setting.class, Shop.class, Template.class, TemplateItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDAO itemDAO();

    public abstract PurchaseDAO purchaseDAO();

    public abstract SettingsDAO settingsDAO();

    public abstract ShopDAO shopDAO();

    public abstract TemplateItemDAO templateItemDAO();

    public abstract TemplateDAO templateDAO();
}
