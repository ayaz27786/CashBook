package io.appstud.android.cashbook.activities;

import java.util.ArrayList;
import java.util.List;

import io.appstud.android.cashbook.R;
import io.appstud.android.cashbook.helpers.CashBookDataSource;
import io.appstud.android.cashbook.helpers.Entry;
import io.appstud.android.cashbook.helpers.Tag;
import android.app.Activity;
import android.os.Bundle;

public class CashBookActivity extends Activity {
	
	private CashBookDataSource  cashBookDataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		cashBookDataSource = new CashBookDataSource(this);
		cashBookDataSource.open();
		List<Tag> tags = new ArrayList<Tag>(); 
		Tag tag = new Tag();
		tag.setTag("Education");
		tags.add(tag);
		tag = new Tag();
		tag.setTag("Yippeee");
		tags.add(tag);
		Entry entry = new Entry();
		entry.setAmount("1000");
		entry.setDate("11/2012/2");
		entry.setDesciption("Hello Expense");
		entry.setFlag("Credit");
		entry.setTags(tags);
		cashBookDataSource.createEntry(entry);
		cashBookDataSource.close();
	}
}