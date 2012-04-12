package io.appstud.android.cashbook.activities;

import io.appstud.android.cashbook.R;
import io.appstud.android.cashbook.helpers.CashBookDataSource;
import io.appstud.android.cashbook.helpers.Entry;
import io.appstud.android.cashbook.helpers.Tag;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class AddEntryActivity extends Activity {

	CashBookDataSource cashBookDataSource = new CashBookDataSource(this);
	List<Tag> tags = new ArrayList<Tag>();
	Entry entry = new Entry();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_entry);
		setupViews();

	}

	private void setupViews() {
		LinearLayout linearLayout;
		Button button = new Button(this);
		linearLayout = (LinearLayout) findViewById(R.id.tagsLinearLayout);
		cashBookDataSource.open();
		for (Tag tag : tags) {
			button.setText(tag.getTag());
			linearLayout.addView(button);
		}
		entry.setTags(tags);
		cashBookDataSource.close();
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
		EditText date = (EditText) findViewById(R.id.addDate);
		EditText description = (EditText) findViewById(R.id.desciptionEditText);
		ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleCredit);
		entry.setAmount(amount.getText().toString());
		entry.setDate(date.getText().toString());
		entry.setDesciption(description.getText().toString());
		entry.setFlag(toggleButton.getText().toString());

		Tag t = new Tag();
		t.setTag("Hi");
		tags.add(t);

		entry.setTags(tags);

		Intent intent = new Intent();
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, CashBookActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.actionbar_addmenu_save:
			cashBookDataSource.open();
			cashBookDataSource.createEntry(entry);
			cashBookDataSource.close();
			return true;
		case R.id.actionbar_addmenu_cancel:
			intent = new Intent(this, CashBookActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
