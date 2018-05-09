package at.fhv.mobilecomputing.fragments.Template;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import at.fhv.mobilecomputing.database.entities.Template;
import at.fhv.mobilecomputing.database.entities.TemplateItem;
import at.fhv.mobilecomputing.fragments.Dialogs.DeleteDialog;

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

    ListView templateItemsList;
    private int templateId;
    private Template template;
    private OnFragmentInteractionListener mListener;

    List<TemplateItem> templateItemsFromDB;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        templateItemsList = view.findViewById(R.id.TemplateItemList);

        updateData();

        templateItemsList.setOnItemClickListener((parent, view12, position, id) -> {
            // on product click
        });

        templateItemsList.setOnItemLongClickListener((parent, view1, arg2, arg3) -> {

            TemplateItem itemToDelete = templateItemsFromDB.get(arg2);

            DeleteDialog deleteDialog;
            deleteDialog = DeleteDialog.newInstance(getResources().getString(R.string.deleteTemplateItemMessage));

            deleteDialog.setTemplateItemToDelete(itemToDelete);
            deleteDialog.setTemplateItemsFragment(this);

            assert getFragmentManager() != null;
            deleteDialog.show(getFragmentManager(), "DeleteDialogFragment");

            return true;
        });
    }

    public void updateData() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        templateItemsFromDB = db.templateItemDAO().getAll().stream().filter(s -> s.getTemplateId() == templateId).collect(Collectors.toList());

        final ListAdapter listAdapter = createListAdapter(templateItemsFromDB);
        templateItemsList.setAdapter(listAdapter);
    }

    private ListAdapter createListAdapter(final List<TemplateItem> items) {
        final String[] fromMapKey = new String[] {"name", "description"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems(items);

        return new SimpleAdapter(getContext(), list,
                android.R.layout.simple_list_item_2,
                fromMapKey, toLayoutId);
    }

    private List<Map<String, String>> convertToListItems(final List<TemplateItem> items) {
        final List<Map<String, String>> listItem =
                new ArrayList<Map<String, String>>(items.size());

        for (final TemplateItem item: items) {
            String firstLine = item.getAmount() != null ? item.getAmount() + " " + item.getName() : item.getName();
            String secondLine = item.getDescription();

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}