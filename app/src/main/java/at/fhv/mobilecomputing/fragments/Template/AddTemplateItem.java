package at.fhv.mobilecomputing.fragments.Template;

import android.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTemplateItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTemplateItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTemplateItem extends Fragment {
    private OnFragmentInteractionListener mListener;
    public int selectedTemplateId;

    public AddTemplateItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTemplateItem.
     */
    public static AddTemplateItem newInstance() {
        AddTemplateItem fragment = new AddTemplateItem();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            templateItem.setTemplateId(0);

            appDatabase.templateItemDAO().insertAll(templateItem);
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
        void onFragmentInteraction(Uri uri);
    }
}
