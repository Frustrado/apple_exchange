package com.example.max.appleexchange;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;


import com.example.max.appleexchange.expandableList.CustomExpandableListAdapter;
import com.example.max.appleexchange.expandableList.Choice;
import com.example.max.appleexchange.expandableList.Category;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class FilterFragment extends Fragment {

    private String[] voivodeship;
    private String[] type;
    private String[] kind;

    private ExpandableListView listView;

    private CustomExpandableListAdapter mAdapter;
    private ExpandableListView expList;
    private ArrayList<Category> retData;
    private ArrayList<Category> backData;

    private Button filter;
    public FilterFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        Bundle bundle = new Bundle();
        bundle=getArguments();
        backData=(ArrayList<Category>)bundle.getSerializable("retData");

        filter=view.findViewById(R.id.button_exp_fil);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), BrowseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("retData",retData);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar_fragment);
        ((BrowseActivity) getActivity()).getSupportActionBar().hide();
        ((BrowseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),BrowseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("retData",backData);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        prepareData(view);
        expList = (ExpandableListView)view.findViewById(R.id.exp_category);
        ArrayList<Category> data = InitData();
        mAdapter = new CustomExpandableListAdapter(getActivity(), data);
        expList.setAdapter(mAdapter);

        return view;
    }

    private ArrayList<Category> InitData() {

        retData = new ArrayList<Category>();
        ArrayList<Choice> serial1 = new ArrayList<Choice>();

        voivodeship = getResources().getStringArray(R.array.voivodeship);
        kind = getResources().getStringArray(R.array.kindOfApple);
        type = getResources().getStringArray(R.array.typeOfAdvertisement);

        Choice info1;
        for (int i = 0; i < voivodeship.length; i++) {

            if (backData == null || backData.get(0).getChoice().get(i).getIsChecked()){
                info1 = new Choice(voivodeship[i], true);
                serial1.add(info1);
            }

            else{
                info1=new Choice(voivodeship[i], false);
                serial1.add(info1);
            }

        }

        Category voucher1 = new Category("Województwo", serial1);
        retData.add(voucher1);
        //Log.d(ContentValues.TAG, "bundle pobrane " + retData.get(0).getChoice().get(0).getIsChecked());

        ArrayList<Choice> serial2 = new ArrayList<Choice>();
        Choice info4;
        for(int i=0;i<kind.length;i++){
            if (backData == null || backData.get(1).getChoice().get(i).getIsChecked()){
                info4 = new Choice(kind[i], true);
                serial2.add(info4);}
        else {
                info4 = new Choice(kind[i], false);
                serial2.add(info4);
            }


        }
        Category voucher2 = new Category("Rodzaj", serial2);
        retData.add(voucher2);

        ArrayList<Choice> serial3 = new ArrayList<Choice>();
        Choice info7;
        for(int i=0;i<type.length;i++) {
            if (backData == null || backData.get(2).getChoice().get(i).getIsChecked()) {
                info7 = new Choice(type[i], true);
                serial3.add(info7);
            } else {
                info7 = new Choice(type[i], false);
                serial3.add(info7);
            }
        }
        Category voucher3 = new Category("Typ", serial3);
        retData.add(voucher3);

        return retData;
    }

    private void prepareData(View view) {
        voivodeship = getResources().getStringArray(R.array.voivodeship);
        kind = getResources().getStringArray(R.array.kindOfApple);
        type = getResources().getStringArray(R.array.typeOfAdvertisement);

    }

}
