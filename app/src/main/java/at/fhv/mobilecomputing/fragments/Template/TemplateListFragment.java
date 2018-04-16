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
import java.util.stream.Collectors;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.AppDatabase;
import at.fhv.mobilecomputing.database.entities.Template;
import at.fhv.mobilecomputing.database.entities.TemplateItem;
import at.fhv.mobilecomputing.fragments.DeleteDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateListFragment extends Fragment {
    ListView templateNameList;
    List<Template> templates;

    private OnFragmentInteractionListener mListener;

    public TemplateListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

            String selectedTemplateName = (String) templateNameList.getAdapter().getItem(position);
            Template template = AppDatabase.getAppDatabase(getContext()).templateDAO().findByName(selectedTemplateName);

            TemplateItemsFragment templateItemsFragment = TemplateItemsFragment.newInstance(template.getId());
            fragmentTransaction.replace(R.id.nav_content, templateItemsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        templateNameList.setOnItemLongClickListener((parent, view1, arg2, arg3) -> {
            Template templateToDelete = templates.get(arg2);
            List<TemplateItem> templateItemsToDelete = AppDatabase.getAppDatabase(getContext()).templateItemDAO().getAll().stream().filter(i -> i.getTemplateId() == templateToDelete.getId()).collect(Collectors.toList());

            DeleteDialog deleteDialog = DeleteDialog.newInstance(getResources().getString(R.string.deleteTemplateMessage));

            deleteDialog.setTemplateItemsToDelete(templateItemsToDelete);
            deleteDialog.setTemplateToDelete(templateToDelete);

            deleteDialog.setTemplateListFragment(this);
            assert getFragmentManager() != null;
            deleteDialog.show(getFragmentManager(), "DeleteDialogFragment");

            return true;
        });
    }

    public void updateData() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        templates = db.templateDAO().getAll();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1);

        templateNameList.setAdapter(adapter);

        for (Template template: templates) {
            adapter.add(template.getName());
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
