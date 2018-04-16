package at.fhv.mobilecomputing.fragments.Template;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Template;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView templateNameList;

    private OnFragmentInteractionListener mListener;

    public TemplateListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateListFragment newInstance(String param1, String param2) {
        TemplateListFragment fragment = new TemplateListFragment();
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
        return inflater.inflate(R.layout.fragment_template_list, container, false);
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

        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        List<Template> templates = db.templateDAO().getAll();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1);

        templateNameList = view.findViewById(R.id.TemplateList);
        templateNameList.setAdapter(adapter);

        for (Template template : templates) {
            adapter.add(template.getName());
        }

        templateNameList.setOnItemClickListener((parent, view1, position, id) -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            /*
            String selectedItem = (String) shoppingList.getAdapter().getItem(position);
            Shop shop = AppDatabase.getAppDatabase(getContext()).shopDAO().findByName(selectedItem);

            ShopDetailViewFragment shopDetailViewFragment = ShopDetailViewFragment.newInstance(shop.getId());
            fragmentTransaction.replace(R.id.nav_content, shopDetailViewFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            */
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