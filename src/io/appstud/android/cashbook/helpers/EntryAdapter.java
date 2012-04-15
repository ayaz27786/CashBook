package io.appstud.android.cashbook.helpers;

import io.appstud.android.cashbook.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Entry> {

	Context context;
	int layoutResourceId;
	List<Entry> entries = new ArrayList<Entry>();

	public EntryAdapter(Context context, int layoutResourceId,
			List<Entry> entries) {
		super(context, layoutResourceId, entries);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.entries = entries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		EntryHolder holder = null;
		String dateFormatted;

		if (row == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = layoutInflater.inflate(layoutResourceId, parent, false);

			holder = new EntryHolder();
			holder.date = (TextView) row.findViewById(R.id.singleRowDate);
			holder.total = (TextView) row.findViewById(R.id.singleRowAmount);

			row.setTag(holder);
		} else {
			holder = (EntryHolder) row.getTag();
		}

		Entry entry = entries.get(position);
		dateFormatted = DateFormat.getDateInstance(DateFormat.MEDIUM).format(
				entry.getDate());

		holder.date.setText(dateFormatted);
		holder.total.setText(entry.getAmount());

		if (entry.getFlag().equals(
				context.getResources().getString(R.string.credit))) {
			holder.total.setTextColor(context.getResources().getColor(
					R.color.light_green));
		} else
			holder.total.setTextColor(context.getResources().getColor(
					R.color.light_red));

		return row;
	}

	static class EntryHolder {
		TextView date;
		TextView total;
	}

}
