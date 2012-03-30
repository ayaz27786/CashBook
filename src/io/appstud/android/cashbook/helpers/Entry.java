package io.appstud.android.cashbook.helpers;

import java.util.List;

public class Entry {

	private long id;
	private String amount;
	private String date;
	private String flag;
	private String desciption;
	private List<Tag> tags;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDesciption() {
		return desciption;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

}
