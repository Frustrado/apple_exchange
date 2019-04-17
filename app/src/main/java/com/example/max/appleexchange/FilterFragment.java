package com.example.max.appleexchange;

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

    private Button filter;
    public FilterFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

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
                startActivity(new Intent(getContext(),BrowseActivity.class));
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
        for(int i=0;i<voivodeship.length;i++){
        Choice info1 = new Choice(voivodeship[i], false); serial1.add(info1);}
        Category voucher1 = new Category("Województwo", serial1);
        retData.add(voucher1);

        ArrayList<Choice> serial2 = new ArrayList<Choice>();
        for(int i=0;i<kind.length;i++){
        Choice info4 = new Choice(kind[i], false); serial2.add(info4);}
        Category voucher2 = new Category("Rodzaj", serial2);
        retData.add(voucher2);

        ArrayList<Choice> serial3 = new ArrayList<Choice>();
        for(int i=0;i<type.length;i++){
        Choice info7 = new Choice(type[i], false); serial3.add(info7);}
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
