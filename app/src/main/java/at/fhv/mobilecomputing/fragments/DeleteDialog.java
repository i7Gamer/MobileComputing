package at.fhv.mobilecomputing.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.database.entities.Shop;
import at.fhv.mobilecomputing.fragments.Shop.ShoppingListFragment;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeleteDialog.DeleteDialogListener} interface
 * to handle interaction events.
 * Use the {@link DeleteDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "message";

    // TODO: Rename and change types of parameters
    private String message;

    @Getter
    @Setter
    private Shop shopToDelete;
    @Getter
    @Setter
    private List<Item> itemsToDelete;
    @Getter
    @Setter
    private ShoppingListFragment shoppinglistFragment;
    private DeleteDialogListener mListener;

    public DeleteDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param message Parameter 1.
     * @return A new instance of fragment DeleteDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteDialog newInstance(String message) {
        DeleteDialog fragment = new DeleteDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    mListener.onDialogDeleteClick(this);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    mListener.onDialogCancelClick(this);
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_dialog, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DeleteDialogListener) {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DeleteDialogListener) context;
        } else {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.getClass().toString()
                    + " must implement DeleteDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface DeleteDialogListener {
        void onDialogDeleteClick(DeleteDialog dialog);

        void onDialogCancelClick(DeleteDialog dialog);
    }
}
