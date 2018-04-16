package at.fhv.mobilecomputing.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * {@link AddProduct.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProduct extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int LOAD_IMAGE_RESULTS = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Shop> spinnerArray =  new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public AddProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProduct newInstance(String param1, String param2) {
        AddProduct fragment = new AddProduct();
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
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        view.findViewById(R.id.buttonAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGallery();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == -1) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);

                    ImageView imageView = getActivity().findViewById(R.id.imageViewProduct);
                    imageView.setImageBitmap(bitmapImage);
                    imageView.setMaxHeight(300);
                } catch (IOException e) {

                }
            }
        }
    }
    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
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
        Calendar myCalendar = Calendar.getInstance();

        EditText edittext = (EditText) getActivity().findViewById(R.id.editTextDueDate);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edittext.setText(sdf.format(myCalendar.getTime()));
            }

        };
        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        spinnerArray = new ArrayList<>();
        spinnerArray = appDatabase.shopDAO().getAll();
        List<String> spinnerArrayStrings = new ArrayList<>();
        for(Shop shop : spinnerArray) {
            spinnerArrayStrings.add(shop.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArrayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) getActivity().findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        Button addProduct = getActivity().findViewById(R.id.buttonAddProduct);
        addProduct.setOnClickListener(v -> {
            EditText productName = getActivity().findViewById(R.id.editTextProductName);
            EditText description = getActivity().findViewById(R.id.editTextDescription);
            EditText amount = getActivity().findViewById(R.id.editTextAmount);
            EditText dueDate = getActivity().findViewById(R.id.editTextDueDate);
            Spinner shop = getActivity().findViewById(R.id.spinner);
            ImageView imageView = getView().findViewById(R.id.imageViewProduct);

            Item newProduct = new Item();
            newProduct.setName(productName.getText().toString());
            newProduct.setDescription(description.getText().toString());
            newProduct.setAmount(amount.getText().toString());
            newProduct.setDueDate(dueDate.getText().toString());
            String selectedShop = shop.getSelectedItem().toString();
            Shop selShop = spinnerArray.stream().filter(shopName -> selectedShop.equals(shopName.getName())).findFirst().orElse(null);
            newProduct.setShopId(selShop.getId());
            Bitmap bm =((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            newProduct.setPicture(bos.toByteArray());
            appDatabase.itemDAO().insertAll(newProduct);
            getActivity().onBackPressed();
        });
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
