package io.appstud.android.cashbook.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CashBookSQLiteOpenHelper extends SQLiteOpenHelper {
	// TAG
	private static final String TAG = "CashBookSQLiteOpenHelper";

	// IVARS
	private static final int DB_VERSION = 2;
	private static final String DB_NAME = "cashbook.db";

	public static final String TABLE_NAME_ENTRIES = "entries";
	public static final String TABLE_NAME_TAGS = "tags";
	public static final String TABLE_NAME_ENTRY_HAS_TAG = "entry_has_tags";
	public static final String COL_ID = "_id";
	public static final String COL_DESC = "description";
	public static final String COL_AMT = "amount";
	public static final String COL_DATE = "date";
	public static final String COL_FLAG = "flag";
	public static final String COL_TAG = "tag";
	public static final String COL_TAG_ID = "tag_id";
	public static final String COL_ENTRY_ID = "entry_id";

	public CashBookSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_TABLE_ENTRIES = "CREATE TABLE " + TABLE_NAME_ENTRIES
				+ "( " + COL_ID + " integer primary key autoincrement, "
				+ COL_AMT + " text not null, " + COL_FLAG + " text not null, "
				+ COL_DATE + " text not null, " + COL_DESC + " text );";
		db.execSQL(CREATE_TABLE_ENTRIES);
		Log.d(TAG, "Table Created : " + TABLE_NAME_ENTRIES);

		String CREATE_TABLE_TAGS = "CREATE TABLE " + TABLE_NAME_TAGS + "( "
				+ COL_ID + " integer primary key autoincrement, " + COL_TAG
				+ " text not null unique);";
		db.execSQL(CREATE_TABLE_TAGS);
		Log.d(TAG, "Table Created : " + TABLE_NAME_TAGS);

		String CREATE_TABLE_EHT = "CREATE TABLE " + TABLE_NAME_ENTRY_HAS_TAG
				+ "( " + COL_ID + " integer primary key autoincrement, "
				+ COL_ENTRY_ID + " integer, " + COL_TAG_ID + " integer, "
				+ " FOREIGN KEY ( " + COL_ENTRY_ID + " ) REFERENCES "
				+ TABLE_NAME_ENTRIES + " ( " + COL_ID + " ),"
				+ " FOREIGN KEY ( " + COL_TAG_ID + " ) REFERENCES "
				+ TABLE_NAME_TAGS + " ( " + COL_ID + " ));";
		db.execSQL(CREATE_TABLE_EHT);
		Log.d(TAG, "Table Created : " + TABLE_NAME_ENTRY_HAS_TAG);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ENTRIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TAGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ENTRY_HAS_TAG);
		onCreate(db);
	}

}
