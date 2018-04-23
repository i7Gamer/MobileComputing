package at.fhv.mobilecomputing.fragments.Template;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Template;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateItemsFragment extends Fragment {
    private static final String ARGUMENT_TEMPLATE = "template";

    ListView templateItems;
    private int templateId;
    private Template template;
    private OnFragmentInteractionListener mListener;

    public TemplateItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static TemplateItemsFragment newInstance(int templateId) {
        TemplateItemsFragment fragment = new TemplateItemsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TEMPLATE, templateId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            templateId = getArguments().getInt(ARGUMENT_TEMPLATE);
        }
        template = AppDatabase.getAppDatabase(getContext()).templateDAO().getAll().stream().filter(s -> s.getId() == templateId).findFirst().get();
        this.getActivity().setTitle(getResources().getString(R.string.navigation_template) + ": " + template.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template_items, container, false);
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