package com.example.max.appleexchange.expandableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.max.appleexchange.R;

import static android.content.ContentValues.TAG;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Category> data;
	private LayoutInflater inflater;

	
	public CustomExpandableListAdapter(Activity a, ArrayList<Category> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	@Override
	public Object getChild(int groupPosition, int chidPosition) {
		// TODO Auto-generated method stub
		return data.get(groupPosition).getChoice().get(chidPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if(vi == null) {
			vi = inflater.inflate(R.layout.expand_list_child, parent, false);
		}
		TextView textViewChoiceNo = (TextView)vi.findViewById(R.id.text_view_choice);
		final CheckBox buttonCheck = (CheckBox)vi.findViewById(R.id.button_checkbox);
        buttonCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				// TODO Auto-generated method stub
				Choice choice = (Choice)button.getTag();
                choice.setIsChecked(isChecked);

			}

			
		});

		
		Choice choice = data.get(groupPosition).getChoice().get(childPosition);
        buttonCheck.setTag(choice);

		String choiceNo = choice.getChoiceNo();
		Boolean isChecked = choice.getIsChecked();

        textViewChoiceNo.setText(choiceNo);
        buttonCheck.setChecked(isChecked);


		return vi;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return data.get(groupPosition).getChoice().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if(vi == null){
			vi = inflater.inflate(R.layout.expand_list_parent, parent, false);
		}

		TextView textViewCategory = (TextView)vi.findViewById(R.id.text_view_category);
		String value = data.get(groupPosition).getValue().toString();

        textViewCategory.setText(value);
		return vi;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}



}
