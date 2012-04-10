package io.appstud.android.cashbook.activities;

import io.appstud.android.cashbook.R;
import io.appstud.android.cashbook.helpers.CashBookDataSource;
import io.appstud.android.cashbook.helpers.Entry;
import io.appstud.android.cashbook.helpers.EntryAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CashBookActivity extends ListActivity {

	private CashBookDataSource cashBookDataSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashbook_date);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);

		cashBookDataSource = new CashBookDataSource(this);
		cashBookDataSource.open();

		List<Entry> entries = new ArrayList<Entry>();

		entries = cashBookDataSource.getAllEntries();

		EntryAdapter adapter = new EntryAdapter(this, R.layout.row_entry_date,
				entries);

		setListAdapter(adapter);
		cashBookDataSource.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, CashBookActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menuAdd:
			intent = new Intent(this, AddEntryActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}