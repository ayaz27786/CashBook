package io.appstud.android.cashbook.activities;

import io.appstud.android.cashbook.R;
import io.appstud.android.cashbook.helpers.CashBookDataSource;
import io.appstud.android.cashbook.helpers.Entry;
import io.appstud.android.cashbook.helpers.EntryAdapter;
import io.appstud.android.cashbook.helpers.EntryByDateAdapter;
import io.appstud.android.cashbook.helpers.EntryByTagAdapter;
import io.appstud.android.cashbook.helpers.Tag;
import io.appstud.android.cashbook.helpers.TagAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CashBookActivity extends ListActivity {
	int mYear;
	int mDay;
	int mMonth;
	Button fromDate, toDate;
	Dialog dialog;
	static final int FROM_DATE_DIALOG_ID = 0;
	static final int TO_DATE_DIALOG_ID = 1;
	private CashBookDataSource cashBookDataSource;
	
	//ENTRIES OBJECT & ADAPTER DECLARATION:
	List<Entry> entries = new ArrayList<Entry>();
	List<Tag> tags = new ArrayList<Tag>();
	List<Entry> dates = new ArrayList<Entry>();
	
	EntryByDateAdapter dateAdapter;
	private EntryAdapter entryAdapter;//**********
	
	
	ListView date_listview;
	
	
	public static String tagData;
	long dateSelect;	
	public static int flag;
	String freqSelected;

	
	// DATE SPAN DECLARATION:
	int ALL_DATA=0;
	int LAST_30DAYS_DATA=1;
	int CURRENT_MONTH_DATA=2;
	int CUSTOM_DATA=3;
	
	//TIME/DATE SPAN DECLARATION
	int TAG_DISPLAY=0;
	int DATE_DISPLAY=1;
	
	Date startDate;
	Date  to_date;
	Date currentDate=new Date();


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashbook_date);
		cashBookDataSource = new CashBookDataSource(this);
		cashBookDataSource.open();
		getAllEntryData();

	}

	public void lastThirtyDayata() throws ParseException {	
		 Calendar cal=Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				-30);
		startDate=cal.getTime();	
	entries = cashBookDataSource.dateEntry(startDate, currentDate);
		createDateList(entries);

	}

	public void currentMonthData() throws ParseException {	
		 Calendar cal=Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				00);
		startDate=cal.getTime();		
	entries = cashBookDataSource.dateEntry(startDate, currentDate);
		createDateList(entries);
	}

	public void getAllEntryData() {
		long from_Date=cashBookDataSource.minDate();
		startDate=new Date(from_Date);
				
		try {
			entries = cashBookDataSource.dateEntry(startDate, currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createDateList(entries);

	}

	private void createDateList(List<Entry> entries) {
		final ListView listView = getListView();
		entryAdapter = new EntryAdapter(this, R.layout.row_entry_date, entries);
		listView.setAdapter(entryAdapter);
		OnItemClickListener(listView);
	}

	private void OnItemClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				dateSelect = entries.get(position).getDate();

				ArrayList<Entry> arrayList = new ArrayList<Entry>();
				ListView listView = getListView();
				arrayList = (ArrayList<Entry>) cashBookDataSource
						.getAllEntriesByDate(dateSelect);
				dateAdapter = new EntryByDateAdapter(getApplicationContext(),
						R.layout.entries_by_date, arrayList);
				listView.setAdapter(dateAdapter);

			}
		});
	}

	public void getAllTagData() {

		Tag tag;
		final List<Tag> tagss = new ArrayList<Tag>();
		tags = cashBookDataSource.getTags();
		for (int i = 0; i < tags.size(); i++) {
			float total = 0;
			entries = cashBookDataSource
					.findEntriesByTagId(tags.get(i).getId());
			for (int j = 0; j < entries.size(); j++) {
				total = total + Float.parseFloat(entries.get(j).getAmount());
			}
			tag = new Tag();
			tag.setId(tags.get(i).getId());
			tag.setTag(tags.get(i).getTag());
			tag.setAmount(String.valueOf(total));
			tagss.add(tag);

		}
		createTagList(tagss);

	}

	private void createTagList(List<Tag> tagss) {
		ListView listView = getListView();
		TagAdapter tagAdapter = new TagAdapter(getApplicationContext(),
				R.layout.row_entry_date, tagss);
		listView.setAdapter(tagAdapter);
		OnTagItemClickListener(listView, tagss);

	}

	private void OnTagItemClickListener(ListView listView, final List<Tag> tagss) {
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				long dateSelect = tagss.get(position).getId();
				tagData = tagss.get(position).getTag();
				ArrayList<Entry> arrayList = new ArrayList<Entry>();
				arrayList = (ArrayList<Entry>) cashBookDataSource
						.findEntriesByTagId(dateSelect);
				EntryByTagAdapter tagAdapter = new EntryByTagAdapter(
						getApplicationContext(), R.layout.entries_by_tag,
						arrayList);
				ListView subListView = getListView();
				subListView.setAdapter(tagAdapter);				
			}			
		});
	}

	private void customDate() {

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		String today = DateFormat.getDateInstance(DateFormat.MEDIUM).format(
				c.getTime());

		AlertDialog.Builder builder_custom = new AlertDialog.Builder(this);
		builder_custom.setTitle("Select date From_To");
		builder_custom.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog_tag, int id) {

						try {
							startDate = DateFormat.getDateInstance().parse(
									fromDate.getText().toString());
							to_date = DateFormat.getDateInstance().parse(
									toDate.getText().toString());

					
						
								entries = cashBookDataSource.dateEntry(
										startDate, to_date);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							createDateList(entries);

						
					}

				});

		builder_custom.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog_tag, int which) {
						dialog.cancel();
					}
				});
		
		TableLayout tableLayout = new TableLayout(getApplicationContext());
		TableRow first_row = new TableRow(this);
		TableRow second_row = new TableRow(this);

		fromDate = new Button(this);
		fromDate.setText(today);
		fromDate.setWidth(450);

		toDate = new Button(this);
		toDate.setWidth(450);
		toDate.setText(today);
		first_row.addView(fromDate);
		tableLayout.addView(first_row);

		second_row.addView(toDate);
		tableLayout.addView(second_row);
		builder_custom.setView(tableLayout);

		final Dialog dialog_custom = builder_custom.create();
		dialog = dialog_custom;
		dialog_custom.show();
		
		
		fromDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Toast.makeText(getApplicationContext(), "From Date",
						Toast.LENGTH_SHORT).show();
				showDialog(FROM_DATE_DIALOG_ID);
			}
		});
		
		
		toDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "To Date",
						Toast.LENGTH_SHORT).show();
				showDialog(TO_DATE_DIALOG_ID);
			}
		});

	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case FROM_DATE_DIALOG_ID:

			dialog = new DatePickerDialog(this, fromDateSetListener, mYear,
					mMonth, mDay);
			break;
		case TO_DATE_DIALOG_ID:
			dialog = new DatePickerDialog(this, toDateSetListener, mYear,
					mMonth, mDay);
			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			startDate = new Date(year - 1900, monthOfYear, dayOfMonth);
			String fromDatestr = DateFormat.getDateInstance(DateFormat.MEDIUM)
					.format(startDate);
			fromDate.setText(fromDatestr);
		}
	};

	private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			to_date = new Date(year - 1900, monthOfYear, dayOfMonth);
			String toDatestr = DateFormat.getDateInstance(DateFormat.MEDIUM)
					.format(to_date);
			toDate.setText(toDatestr);
		}
	};

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuTagDate:
			AlertDialog.Builder builder_tag= new AlertDialog.Builder(this);
			builder_tag.setTitle("Short By");

			ListView tag_listview = new ListView(this);
			String[] stringArray1 = new String[] {"Date","Tag" };
			ArrayAdapter<String> modeAdapter1 = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					stringArray1);
			tag_listview.setAdapter(modeAdapter1);
			builder_tag.setView(tag_listview);
			final Dialog dialog_tag = builder_tag.create();
			dialog_tag.show();

			tag_listview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					if (position == TAG_DISPLAY) {
						Toast.makeText(getApplicationContext(),
								"Date has selected", Toast.LENGTH_SHORT).show();
						getAllEntryData();
						dialog_tag.cancel();
					} else if (position == DATE_DISPLAY) {
						Toast.makeText(getApplicationContext(),
								"Tag has selected", Toast.LENGTH_SHORT).show();
						getAllTagData();
						dialog_tag.cancel();
					}
				}
			});

			return true;
		case R.id.menuTimeSpan:
			AlertDialog.Builder builder_date = new AlertDialog.Builder(this);
			builder_date.setTitle("Short By");
			ListView date_listview = new ListView(this);
			String[] stringArray11 = new String[] { "All", "Last 30 days",
					"Current Month", "Custom" };

			ArrayAdapter<String> date_arrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1,
					android.R.id.text1, stringArray11);

			date_listview.setAdapter(date_arrayAdapter);
			builder_date.setView(date_listview);
			final Dialog dialoge_date = builder_date.create();
			dialoge_date.show();

			date_listview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					
					if (position == ALL_DATA) {
						getAllEntryData();
						dialoge_date.cancel();
					} 
					
					else if (position == LAST_30DAYS_DATA) {
						try {
							lastThirtyDayata();
							dialoge_date.cancel();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					else if (position == CURRENT_MONTH_DATA) {
						try {
							currentMonthData();
							dialoge_date.cancel();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} 
					
					else if(position==CUSTOM_DATA){
						try {

							customDate();
							dialoge_date.cancel();
						} catch (org.apache.http.ParseException e) {
							e.printStackTrace();
						}

					}
				}

			});
			return true;
		case R.id.menuAdd:
			Intent intent = new Intent(getApplicationContext(),
					AddEntryActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}