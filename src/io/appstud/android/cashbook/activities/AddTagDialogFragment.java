package io.appstud.android.cashbook.activities;

import java.util.ArrayList;
import java.util.List;

import io.appstud.android.cashbook.R;
import io.appstud.android.cashbook.helpers.CashBookDataSource;
import io.appstud.android.cashbook.helpers.Tag;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class AddTagDialogFragment extends DialogFragment {

	boolean canceled;

	static AddTagDialogFragment newInstance() {

		AddTagDialogFragment f = new AddTagDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", "Add New Tag");
		f.setArguments(args);
		return f;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		String title = getActivity().getString(R.string.create_new_tag);
		String add = getActivity().getString(R.string.add);
		String cancel = getActivity().getString(android.R.string.cancel);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.add_tag_dialog, null);
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle(title).setView(v).setCancelable(false)
				.setNegativeButton(cancel, null)
				.setNeutralButton(add, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						EditText addTagEditText = (EditText) ((Dialog) dialog)
								.findViewById(R.id.createTagET);

						if (addTagEditText.getText().toString().length() == 0) {
							addTagEditText
									.setError(getString(R.string.please_enter_an_amount));

						} else {
							CashBookDataSource cashBookDataSource = new CashBookDataSource(
									getActivity());
							cashBookDataSource.open();
							cashBookDataSource.createTag(addTagEditText
									.getText().toString());

							LinearLayout tagsLinearLayout = (LinearLayout) getActivity()
									.findViewById(R.id.tagsLinearLayout);
							tagsLinearLayout.removeAllViews();
							ToggleButton toggleButton;
							List<Tag> tags = new ArrayList<Tag>();

							tags = cashBookDataSource.getTags();
							cashBookDataSource.close();

							for (Tag tag : tags) {
								toggleButton = new ToggleButton(getActivity());
								toggleButton.setText(tag.getTag());
								toggleButton.setTextOn(tag.getTag());
								toggleButton.setTextOff(tag.getTag());
								tagsLinearLayout.addView(toggleButton);
							}

							dialog.dismiss();
						}

					}
				}).create();

		return dialog;
	}
}
