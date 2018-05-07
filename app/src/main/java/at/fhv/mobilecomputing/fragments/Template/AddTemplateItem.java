package at.fhv.mobilecomputing.fragments.Template;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.TemplateItem;
import at.fhv.mobilecomputing.fragments.Product.AddProduct;

public class AddTemplateItem extends Fragment {
    private static final String ARGUMENT_TEMPLATE = "template_id";

    private OnFragmentInteractionListener mListener;
    private int selectedTemplateId;

    public AddTemplateItem() {
        // Required empty public constructor
    }

    public static AddTemplateItem newInstance(int selectedTemplateId) {
        AddTemplateItem fragment = new AddTemplateItem();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TEMPLATE, selectedTemplateId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.selectedTemplateId = getArguments().getInt(ARGUMENT_TEMPLATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_template_item, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddProduct.OnFragmentInteractionListener) {
            mListener = (AddTemplateItem.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button addTemplateItemBtn = getActivity().findViewById(R.id.buttonAddTemplateItem);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());

        addTemplateItemBtn.setOnClickListener(v -> {
            EditText name = getActivity().findViewById(R.id.editTextTemplateItemName);
            EditText description = getActivity().findViewById(R.id.editTextDescription);
            EditText amount = getActivity().findViewById(R.id.editTextAmount);

            TemplateItem templateItem = new TemplateItem();
            templateItem.setName(name.getText().toString());
            templateItem.setAmount(amount.getText().toString());
            templateItem.setDescription(description.getText().toString());
            templateItem.setTemplateId(selectedTemplateId);

            appDatabase.templateItemDAO().insertAll(templateItem);
            getActivity().onBackPressed();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}