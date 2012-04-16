package io.appstud.android.cashbook.activities;

import io.appstud.android.cashbook.R;
import io.appstud.android.cashbook.helpers.CashBookDataSource;
import io.appstud.android.cashbook.helpers.Entry;
import io.appstud.android.cashbook.helpers.Tag;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class AddEntryActivity extends Activity {

	private static final String TAG = "AddEntryActivity";

	CashBookDataSource cashBookDataSource = new CashBookDataSource(this);
	List<Tag> tags = new ArrayList<Tag>();
	Entry entry = new Entry();
	static final int DATE_DIALOG_ID = 0;
	static final int ADD_TAG_DIALOG_ID = 1;

	int mYear;
	int mDay;
	int mMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_entry);
		setupViews();
	}

	private void setupViews() {
		setupDatePicker();
		setupTagSelector();
	}

	public void showAddTagDialog(View v) {

		DialogFragment addTagDialog = AddTagDialogFragment.newInstance();
		addTagDialog.show(getFragmentManager(),
				getResources().getString(R.string.create_new_tag));

	}

	private void setupTagSelector() {
		LinearLayout tagsLinearLayout = (LinearLayout) findViewById(R.id.tagsLinearLayout);
		ToggleButton toggleButton;
		List<Tag> tags = new ArrayList<Tag>();

		cashBookDataSource.open();
		tags = cashBookDataSource.getTags();
		cashBookDataSource.close();

		for (Tag tag : tags) {
			toggleButton = new ToggleButton(this);
			toggleButton.setText(tag.getTag());
			toggleButton.setTextOn(tag.getTag());
			toggleButton.setTextOff(tag.getTag());
			tagsLinearLayout.addView(toggleButton);
		}

	}

	private List<Tag> getSelectedTags(LinearLayout tagsLinearLayout) {
		List<Tag> selectedTags = new ArrayList<Tag>();
		tagsLinearLayout = (LinearLayout) findViewById(R.id.tagsLinearLayout);
		for (int i = 0; i < tagsLinearLayout.getChildCount(); i++) {
			View v = tagsLinearLayout.getChildAt(i);
			if (((ToggleButton) v).isChecked()) {
				Tag selectedTag = new Tag();
				selectedTag.setTag(((ToggleButton) v).getText().toString());
				selectedTags.add(selectedTag);
			}
		}
		return selectedTags;
	}

	private void setupDatePicker() {
		Button dateButton = (Button) findViewById(R.id.addDate);

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		String today = DateFormat.getDateInstance(DateFormat.MEDIUM).format(
				c.getTime());
		dateButton.setText(today);
		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDate();
		}
	};

	private void updateDate() {
		Button dateButton = (Button) findViewById(R.id.addDate);
		Date updatedDate = new Date();
		updatedDate.setYear(mYear - 1900);
		updatedDate.setDate(mDay);
		updatedDate.setMonth(mMonth);
		String updatedDateStr = DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(updatedDate);
		dateButton.setText(updatedDateStr);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DATE_DIALOG_ID:
			dialog = new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth, mDay);
			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_addmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		EditText amount = (EditText) findViewById(R.id.addAmountET);
		Button date = (Button) findViewById(R.id.addDate);
		EditText description = (EditText) findViewById(R.id.desciptionEditText);
		ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleCredit);
		LinearLayout tagsLinearLayout = (LinearLayout) findViewById(R.id.tagsLinearLayout);
		boolean error = false;

		Date dateAdded = null;
		try {
			dateAdded = DateFormat.getDateInstance().parse(
					date.getText().toString());
		} catch (ParseException e) {
			Log.d(TAG, "Date cannot be parsed");
			e.printStackTrace();
		}

		entry.setAmount(amount.getText().toString());
		entry.setDate(dateAdded.getTime());
		entry.setDesciption(description.getText().toString());
		entry.setFlag(toggleButton.getText().toString());
		entry.setTags(getSelectedTags(tagsLinearLayout));

		switch (item.getItemId()) {
		case android.R.id.home:
			gotoHome();
			return true;
		case R.id.actionbar_addmenu_save:
			if (amount.getText().toString().length() == 0) {
				amount.setError(getString(R.string.please_enter_an_amount));
				error = true;
			}
			if (!error) {
				cashBookDataSource.open();
				cashBookDataSource.createEntry(entry);
				cashBookDataSource.close();
				gotoHome();
			}
			return true;
		case R.id.actionbar_addmenu_cancel:
			gotoHome();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private void gotoHome() {
		Intent intent = new Intent();
		intent = new Intent(this, CashBookActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
