package com.example.max.appleexchange.expandableList;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	private String mValue;
	private ArrayList<Choice> mChoice;
	
	public Category(String value, ArrayList<Choice> serial) {
		mValue = value;
		mChoice = serial;
	}
	
	public String getValue() {
		return mValue;
	}
	public void setValue(String value) {
		mValue = value;
	}
	
	public ArrayList<Choice> getChoice() {
		return mChoice;
	}
	public void SetChoice(ArrayList<Choice> choice) {
		mChoice = choice;
	}
}
