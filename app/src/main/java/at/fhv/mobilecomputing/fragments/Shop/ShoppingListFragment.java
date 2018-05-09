package at.fhv.mobilecomputing.fragments.Shop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Shop;
import at.fhv.mobilecomputing.fragments.Dialogs.DeleteDialog;
import at.fhv.mobilecomputing.fragments.Product.ShopDetailViewFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView shoppingList;
    List<Shop> shops;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shoppingList = view.findViewById(R.id.ShoppingList);

        updateData();

        shoppingList.setOnItemClickListener((parent, view1, position, id) -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            Map<String, String> selectedItem = (Map<String, String>) shoppingList.getAdapter().getItem(position);
            Shop shop = AppDatabase.getAppDatabase(getContext()).shopDAO().findByName(selectedItem.get("name"));

            ShopDetailViewFragment shopDetailViewFragment = ShopDetailViewFragment.newInstance(shop.getId());
            fragmentTransaction.replace(R.id.nav_content, shopDetailViewFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        shoppingList.setOnItemLongClickListener((parent, view1, arg2, arg3) -> {

            Shop shopToDelete = shops.get(arg2);
            List<Item> toDeleteShopItems = AppDatabase.getAppDatabase(getContext())
                    .itemDAO().getAll().stream().filter(i -> i.getPurchaseId() == null &&
                            i.getShopId() == shopToDelete.getId()).collect(Collectors.toList());

            DeleteDialog deleteDialog;
            if (toDeleteShopItems.size() > 0) {

                deleteDialog = DeleteDialog.newInstance(getResources().getString(R.string.deleteShopMessageContainingItems1)
                        + toDeleteShopItems.size() + getResources().getString(R.string.deleteShopMessageContainingItems2));
            } else {
                deleteDialog = DeleteDialog.newInstance(getResources().getString(R.string.deleteShopMessage));
            }

            deleteDialog.setShopItemsToDelete(toDeleteShopItems);
            deleteDialog.setShopToDelete(shopToDelete);
            deleteDialog.setShoppinglistFragment(this);

            assert getFragmentManager() != null;
            deleteDialog.show(getFragmentManager(), "DeleteDialogFragment");

            return true;
        });

        sendNotification();
    }

    public void updateData() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        shops = db.shopDAO().getAll();

        final ListAdapter listAdapter = createListAdapter(shops);
        shoppingList.setAdapter(listAdapter);
    }

    private ListAdapter createListAdapter(final List<Shop> shops) {
        final String[] fromMapKey = new String[] {"name", "address"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems(shops);

        return new SimpleAdapter(getContext(), list,
                android.R.layout.simple_list_item_2,
                fromMapKey, toLayoutId);
    }

    private List<Map<String, String>> convertToListItems(final List<Shop> shops) {
        final List<Map<String, String>> listItem =
                new ArrayList<Map<String, String>>(shops.size());

        for (final Shop shop: shops) {
            final Map<String, String> listItemMap = new HashMap<>();
            listItemMap.put("name", shop.getName());
            listItemMap.put("address", shop.getAddress());
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }
        return Collections.unmodifiableList(listItem);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void sendNotification() {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yy");
        List<Item> itemsToBuyToday = AppDatabase.getAppDatabase(getContext()).itemDAO().getAll()
                .stream().filter(i -> i.getPurchaseId() == null && (dt1.format(new Date()).toString()).equals(i.getDueDate())).collect(Collectors.toList());

        Log.i("Date:", dt1.format(new Date()).toString());

        if(itemsToBuyToday.size() > 0) {

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getContext().getApplicationContext(), "notify_001");
            Intent ii = new Intent(getContext(), ShoppingListFragment.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, ii, 0);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

            String textToDisplay = "";
            for(Item itemToBuy : itemsToBuyToday) {
                if(textToDisplay != "") {
                    textToDisplay +=  System.lineSeparator();
                }
                textToDisplay += itemToBuy.getAmount() != null ? itemToBuy.getAmount() + " " + itemToBuy.getName() : itemToBuy.getName();
            }
            bigText.bigText(textToDisplay);

            bigText.setBigContentTitle("Today:");
            bigText.setSummaryText("Reminder");

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.drawable.ic_shopping_cart_black_24dp);
            mBuilder.setContentTitle("Your Title");
            mBuilder.setContentText("Your text");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("notify_001",
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
            }

            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}
