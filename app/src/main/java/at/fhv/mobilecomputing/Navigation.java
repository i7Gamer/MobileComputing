package at.fhv.mobilecomputing;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Shop;
import at.fhv.mobilecomputing.database.entities.Template;
import at.fhv.mobilecomputing.database.entities.TemplateItem;
import at.fhv.mobilecomputing.fragments.DeleteDialog;
import at.fhv.mobilecomputing.fragments.History.PurchaseHistoryFragment;
import at.fhv.mobilecomputing.fragments.Product.AddProduct;
import at.fhv.mobilecomputing.fragments.Product.EditProduct;
import at.fhv.mobilecomputing.fragments.Product.ShopDetailViewFragment;
import at.fhv.mobilecomputing.fragments.Settings.SettingsFragment;
import at.fhv.mobilecomputing.fragments.Shop.AddShop;
import at.fhv.mobilecomputing.fragments.Shop.ShoppingListFragment;
import at.fhv.mobilecomputing.fragments.Template.AddTemplate;
import at.fhv.mobilecomputing.fragments.Template.TemplateItemsFragment;
import at.fhv.mobilecomputing.fragments.Template.TemplateListFragment;

public class Navigation extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnFragmentInteractionListener,
        ShoppingListFragment.OnFragmentInteractionListener,
        PurchaseHistoryFragment.OnFragmentInteractionListener,
        AddShop.OnFragmentInteractionListener,
        AddProduct.OnFragmentInteractionListener,
        ShopDetailViewFragment.OnFragmentInteractionListener,
        DeleteDialog.DeleteDialogListener,
        AddTemplate.OnFragmentInteractionListener,
        TemplateListFragment.OnFragmentInteractionListener,
        TemplateItemsFragment.OnFragmentInteractionListener,
        EditProduct.OnFragmentInteractionListener
{

    String lastTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionMenu menu = findViewById(R.id.floatingMenu);
        menu.showMenuButton(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // insert test data

        AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());

        for (Shop s : appDatabase.shopDAO().getAll()) {
            appDatabase.shopDAO().delete(s);
        }
        for (Item i : appDatabase.itemDAO().getAll()) {
            appDatabase.itemDAO().delete(i);
        }

        if (appDatabase.shopDAO().findByName("Spar") == null) {
            Shop shop = new Shop();
            shop.setName("Spar");
            shop.setAddress("Bregenz");
            appDatabase.shopDAO().insertAll(shop);
        }

        if (appDatabase.itemDAO().findByName("Mohren") == null) {
            Item item = new Item();
            item.setName("Mohren");
            item.setDescription("Bier");
            item.setAmount("999");
            Shop shop = appDatabase.shopDAO().getAll().get(0);
            item.setShopId(shop.getId());
            appDatabase.itemDAO().insertAll(item);

            item = new Item();
            item.setName("Fohren");
            item.setDescription("Bier");
            item.setAmount("999");
            item.setShopId(shop.getId());
            appDatabase.itemDAO().insertAll(item);
        }

        if (savedInstanceState == null) {
            Fragment fragment = new ShoppingListFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.commit();
            lastTitle = getResources().getString(R.string.title_activity_shopping_list);

            setTitle(getResources().getString(R.string.navigation_shoppinglist));

            drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        //Listen to Add Product
        final FloatingActionButton addProduct = findViewById(R.id.fabAddProduct);
        addProduct.setOnClickListener(view -> {
            FloatingActionMenu floatingActionMenu = findViewById(R.id.floatingMenu);
            floatingActionMenu.hideMenuButton(true);

            setTitle(getResources().getString(R.string.add_product));
            Fragment fragment = new AddProduct();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        //Listen to Add Shop
        FloatingActionButton addShop = findViewById(R.id.fabAddShop);
        addShop.setOnClickListener(view -> {
            FloatingActionMenu floatingActionMenu = findViewById(R.id.floatingMenu);
            floatingActionMenu.hideMenuButton(true);

            setTitle(getResources().getString(R.string.add_shop));
            Fragment fragment = new AddShop();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        //Listen to Add Template
        FloatingActionButton addTemplate = findViewById(R.id.fabAddTemplate);
        addTemplate.setOnClickListener(view -> {
            FloatingActionMenu floatingActionMenu = findViewById(R.id.floatingMenu);
            floatingActionMenu.hideMenuButton(true);

            setTitle(getResources().getString(R.string.add_template));
            Fragment fragment = new AddTemplate();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.addToBackStack(null);
            ft.commit();
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        FloatingActionMenu floatingActionMenu = findViewById(R.id.floatingMenu);
        floatingActionMenu.showMenuButton(true);

        setTitle(lastTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_shoppinglist) {
            fragment = new ShoppingListFragment();
        } else if (id == R.id.nav_standardlist) {
            fragment = new TemplateListFragment();
        } else if (id == R.id.nav_history) {
            fragment = new PurchaseHistoryFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.commit();
        }

        lastTitle = item.getTitle().toString();
        setTitle(item.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onDialogDeleteClick(DeleteDialog dialog) {
        Shop shop = dialog.getShopToDelete();
        List<Item> shopItems = dialog.getShopItemsToDelete();

        Template template = dialog.getTemplateToDelete();
        List<TemplateItem> templateItems = dialog.getTemplateItemsToDelete();

        if (shop != null) {
            if (shopItems != null && !shopItems.isEmpty()) {
                for (Item i : shopItems) {
                    AppDatabase.getAppDatabase(getApplicationContext()).itemDAO().delete(i);
                }
            }

            shop.setDeleted(true);
            AppDatabase.getAppDatabase(getApplicationContext()).shopDAO().updateAll(shop);

            ShoppingListFragment shoppingListFragment = dialog.getShoppinglistFragment();
            // Check if the tab fragment is available
            if (shoppingListFragment != null) {
                // Call your method in the TabFragment
                shoppingListFragment.updateData();
            }
        }
        if (template != null) {
            if (templateItems != null && !templateItems.isEmpty()) {
                for (TemplateItem t : templateItems) {
                    AppDatabase.getAppDatabase(getApplicationContext()).templateItemDAO().delete(t);
                }
            }

            AppDatabase.getAppDatabase(getApplicationContext()).templateDAO().delete(template);

            TemplateListFragment templateListFragment = dialog.getTemplateListFragment();
            // Check if the tab fragment is available
            if (templateListFragment != null) {
                // Call your method in the TabFragment
                templateListFragment.updateData();
            }
        }
    }

    @Override
    public void onDialogCancelClick(DeleteDialog dialog) {
        Log.i("deleteDialog", "cancel pressed");
    }
}
