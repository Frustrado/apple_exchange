package com.example.max.appleexchange.expandableList;

import java.io.Serializable;

public class Choice implements Serializable {
	private String mChoiceNo;
	private Boolean mIsChecked;
	
	public Choice(String choice, Boolean check) {
		mChoiceNo = choice;
		mIsChecked = check;
	}
	
	public String getChoiceNo() {
		return mChoiceNo;
	}
	public void setChoiceNo(String choice) {
		mChoiceNo = choice;
	}
	
	public Boolean getIsChecked() {
		return mIsChecked;
	}
	public void setIsChecked(Boolean check) {
		mIsChecked = check;
	}
}
