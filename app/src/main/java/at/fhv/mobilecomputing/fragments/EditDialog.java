package at.fhv.mobilecomputing.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhv.mobilecomputing.R;
import at.fhv.mobilecomputing.database.entities.Item;
import at.fhv.mobilecomputing.fragments.Product.ShopDetailViewFragment;
import lombok.Getter;
import lombok.Setter;

public class EditDialog extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "message";

    // TODO: Rename and change types of parameters
    private String message;

    @Getter
    @Setter
    private Item itemToDelete;
    @Getter
    @Setter
    private ShopDetailViewFragment shopDetailViewFragment;

    private EditDialogListener mListener;

    public EditDialog() {
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
    public static EditDialog newInstance(String message) {
        EditDialog fragment = new EditDialog();
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
                    mListener.onEditDialogDeleteClick(this);
                })
                .setNeutralButton(R.string.edit, (dialog, id) -> {
                    mListener.onEditDialogEditClick(this);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    mListener.onEditDialogCancelClick(this);
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

        if (context instanceof DeleteDialog.DeleteDialogListener) {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EditDialogListener) context;
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

    public interface EditDialogListener {
        void onEditDialogDeleteClick(EditDialog dialog);

        void onEditDialogEditClick(EditDialog dialog);

        void onEditDialogCancelClick(EditDialog dialog);
    }

}
