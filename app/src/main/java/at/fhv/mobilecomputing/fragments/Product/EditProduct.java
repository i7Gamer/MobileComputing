package at.fhv.mobilecomputing.fragments.Product;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Shop;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProduct.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProduct extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Item currentItem;

    private OnFragmentInteractionListener mListener;

    public EditProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProduct newInstance(String param1, String param2) {
        EditProduct fragment = new EditProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        List<Shop> spinnerArray = new ArrayList<>();
        spinnerArray = appDatabase.shopDAO().getAll();
        List<String> spinnerArrayStrings = new ArrayList<>();
        for (Shop shop : spinnerArray) {
            spinnerArrayStrings.add(shop.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArrayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) getActivity().findViewById(R.id.spinner);
        sItems.setAdapter(adapter);
        EditText productName = getActivity().findViewById(R.id.editTextProductName);
        productName.setText(currentItem.getName(), TextView.BufferType.EDITABLE);
        EditText description = getActivity().findViewById(R.id.editTextDescription);
        description.setText(currentItem.getDescription(), TextView.BufferType.EDITABLE);
        EditText amount = getActivity().findViewById(R.id.editTextAmount);
        amount.setText(currentItem.getAmount(), TextView.BufferType.EDITABLE);
        EditText dueDate = getActivity().findViewById(R.id.editTextDueDate);
        dueDate.setText(currentItem.getDueDate(), TextView.BufferType.EDITABLE);
        Spinner shop = getActivity().findViewById(R.id.spinner);
        Shop selShop = new Shop();
        for (Shop shopinList : spinnerArray) {
            if (currentItem.getId() == shopinList.getId()) {
                selShop = shopinList;
                return;
            }
        }
        shop.setSelection(adapter.getPosition(selShop.getName()));

        Button editProduct = getActivity().findViewById(R.id.buttonEditProduct);
        List<Shop> finalSpinnerArray = spinnerArray;
        editProduct.setOnClickListener(v -> {
            currentItem.setName(productName.getText().toString());
            currentItem.setDescription(description.getText().toString());
            currentItem.setAmount(amount.getText().toString());
            currentItem.setDueDate(dueDate.getText().toString());
            String selectedShop = shop.getSelectedItem().toString();
            Shop selShop1 = finalSpinnerArray.stream().filter(shopName -> selectedShop.equals(shopName.getName())).findFirst().orElse(null);
            currentItem.setShopId(selShop1.getId());
            appDatabase.itemDAO().updateAll(currentItem);
            getActivity().onBackPressed();
        });

        Calendar myCalendar = Calendar.getInstance();

        EditText edittext = getActivity().findViewById(R.id.editTextDueDate);
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            edittext.setText(sdf.format(myCalendar.getTime()));
        };
        edittext.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

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
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
}