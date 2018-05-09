package at.fhv.mobilecomputing.fragments.Product;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Shop;
import at.fhv.mobilecomputing.fragments.Dialogs.EditDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopDetailViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopDetailViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopDetailViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOP = "shop";
    ListView productList;
    List<Item> items;
    List<Item> selectedItems = new ArrayList<>();
    boolean itemChecked = false;
    // TODO: Rename and change types of parameters
    private int shopId;
    private Shop shop;
    private OnFragmentInteractionListener mListener;

    public ShopDetailViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param shopId Parameter 1.
     * @return A new instance of fragment ShopDetailViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopDetailViewFragment newInstance(int shopId) {
        ShopDetailViewFragment fragment = new ShopDetailViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SHOP, shopId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shopId = getArguments().getInt(ARG_SHOP);
        }
        shop = AppDatabase.getAppDatabase(getContext()).shopDAO().getAll().stream().filter(s -> s.getId() == shopId).findFirst().get();
        this.getActivity().setTitle(getResources().getString(R.string.details_for_shop) + " " + shop.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_detail_view, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.finishPurchase);

        btn.setOnClickListener(v -> {
            finishPurchase(view);
        });

        productList = view.findViewById(R.id.ProductList);

        updateData();

        productList.setOnItemClickListener((parent, view12, position, id) -> {
            if (productList.getCheckedItemCount() > 0) {
                btn.setVisibility(View.VISIBLE);
                selectedItems.add(items.get(position));
            } else {
                btn.setVisibility(View.INVISIBLE);
                selectedItems.remove(items.get(position));
            }

            //productList.setItemChecked(position, productList.isItemChecked(position));
        });

        productList.setOnItemLongClickListener((parent, view1, arg2, arg3) -> {

            Item itemToDelete = items.get(arg2);

            EditDialog editDialog;
            editDialog = EditDialog.newInstance(getResources().getString(R.string.editProductMessage));

            editDialog.setItemToDelete(itemToDelete);
            editDialog.setShopDetailViewFragment(this);

            assert getFragmentManager() != null;
            editDialog.show(getFragmentManager(), "EditDialogFragment");

            return true;
        });
    }

    public void updateData() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        items = db.itemDAO().getAll().stream().filter(s -> s.getShopId() == shopId && s.getPurchaseId() == null).collect(Collectors.toList());
        productList.setAdapter(createListAdapter(items));
    }

    public void finishPurchase(View view) {
        for (int i = 0; i < productList.getCount() - 1; i++) {
            productList.setItemChecked(i, false);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        FinishPurchaseFragment finishPurchaseFragment = new FinishPurchaseFragment();
        finishPurchaseFragment.selectedItems = selectedItems;
        fragmentTransaction.replace(R.id.nav_content, finishPurchaseFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private ListAdapter createListAdapter(final List<Item> items) {
        final String[] fromMapKey = new String[] {"name", "description"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems(items);

        return new SimpleAdapter(getContext(), list,
                android.R.layout.simple_list_item_checked,
                fromMapKey, toLayoutId);
    }

    private List<Map<String, String>> convertToListItems(final List<Item> items) {
        final List<Map<String, String>> listItem =
                new ArrayList<>(items.size());

        for (final Item item: items) {
            String firstLine = item.getAmount() != null ? item.getAmount() + " " + item.getName() : item.getName();
            String secondLine = item.getDueDate() != null ? "@" + item.getDueDate() + " " + item.getDescription() : item.getDescription();

            final Map<String, String> listItemMap = new HashMap<>();
            listItemMap.put("name", firstLine);
            listItemMap.put("description", secondLine);
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }
        return Collections.unmodifiableList(listItem);
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
        void onFragmentInteraction(Uri uri);
    }
}
