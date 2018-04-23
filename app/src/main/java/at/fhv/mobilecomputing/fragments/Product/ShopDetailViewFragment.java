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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.stream.Collectors;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Shop;

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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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

        // TODO get string from resources
        this.getActivity().setTitle(getResources().getString(R.string.details_for_shop) + shop.getName());
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

        productList = view.findViewById(R.id.ProductList);

        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        List<Item> items = db.itemDAO().getAll().stream().filter(s -> s.getShopId() == shopId).collect(Collectors.toList());

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1);
        productList.setAdapter(adapter);

        for (Item item : items) {
            adapter.add(item.getName());
        }

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    EditProduct fragment = new EditProduct();
                    String selectedItemName = (String)productList.getItemAtPosition(position);
                    Item selectedItem = items.stream().filter(itemName -> selectedItemName.equals(itemName.getName())).findFirst().orElse(null);
                    fragment.setCurrentItem(selectedItem);
                    fragmentTransaction.replace(R.id.nav_content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
            }
        });
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
