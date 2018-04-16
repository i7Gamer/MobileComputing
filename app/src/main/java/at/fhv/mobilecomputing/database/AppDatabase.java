package at.fhv.mobilecomputing.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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

@Database(entities = {Item.class, Purchase.class, Setting.class, Shop.class, Template.class, TemplateItem.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "shoppingListDatabase")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract ItemDAO itemDAO();

    public abstract PurchaseDAO purchaseDAO();

    public abstract SettingsDAO settingsDAO();

    public abstract ShopDAO shopDAO();

    public abstract TemplateItemDAO templateItemDAO();

    public abstract TemplateDAO templateDAO();
}
