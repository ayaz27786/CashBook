package io.appstud.android.cashbook.helpers;

public class Tag {

	private long id;
	private String tag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String toString() {
		return id + " - " + tag;
	}

}
