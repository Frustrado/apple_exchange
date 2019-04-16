package com.example.max.appleexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.max.appleexchange.expandableList.Category;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BrowseActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView recyclerView;
    private ImageAdapterBrowse imageAdapter;
    private ProgressBar progressCircle;

    private Button toolbarFilterButton;

    private FirebaseFirestore databaseReference;
    private FirebaseFirestore databaseReferenceUsers;

    private List<Upload> mUploads;

    private List<String> voivodeships;
    private List<String> kinds;
    private List<String> types;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

        toolbarFilterButton= (Button)findViewById(R.id.button_filter);
        toolbarFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                FilterFragment filterFragment=new FilterFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,filterFragment).commit();
                Toast.makeText(getBaseContext(), "Kliknięto filtruj", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressCircle=findViewById(R.id.progress_circle);

        databaseReference=FirebaseFirestore.getInstance();
        databaseReferenceUsers=FirebaseFirestore.getInstance();



        final Bundle bundle = getIntent().getExtras();

        ArrayList<Category> retData=null;

        if(bundle!=null) {
            voivodeships=new ArrayList<>();
            types=new ArrayList<>();
            kinds=new ArrayList<>();
            Log.d(TAG, "bundle porbrane ");
            retData = (ArrayList<Category>) bundle.getSerializable("retData");
            for (int i = 0; i < 16; i++)
                if(retData.get(0).getChoice().get(i).getIsChecked()){
                    voivodeships.add(retData.get(0).getChoice().get(i).getChoiceNo());
                Log.d(TAG, "V:  " + retData.get(0).getChoice().get(i).getChoiceNo() + "    " + retData.get(0).getChoice().get(i).getIsChecked());}
            for (int i = 0; i < 2; i++)
                if(retData.get(1).getChoice().get(i).getIsChecked()){
                    kinds.add(retData.get(1).getChoice().get(i).getChoiceNo());
                    Log.d(TAG, "K:  " + retData.get(1).getChoice().get(i).getChoiceNo() + "    " + retData.get(1).getChoice().get(i).getIsChecked());}
            for (int i = 0; i < 2; i++)
                if(retData.get(2).getChoice().get(i).getIsChecked()){
                    types.add(retData.get(2).getChoice().get(i).getChoiceNo());
                    Log.d(TAG, "T:  " + retData.get(2).getChoice().get(i).getChoiceNo() + "    " + retData.get(2).getChoice().get(i).getIsChecked());}

        }

        mUploads = new ArrayList<>();
        imageAdapter = new ImageAdapterBrowse(BrowseActivity.this,mUploads,voivodeships,kinds,types);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(BrowseActivity.this);





        databaseReference.collection("advertisements").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                for (final DocumentSnapshot doc : snapshots) {
                    final Upload upload= new Upload();
                    databaseReferenceUsers.collection("users").document(doc.getString("userId")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            Log.d(TAG, "eh" +Integer.toString(documentSnapshot.getLong("phone").intValue()));
                            upload.setPhone(Integer.toString(documentSnapshot.getLong("phone").intValue()));
                            upload.setCity(doc.getString("city"));
                            upload.setImageUrl(doc.getString("photo"));
                            upload.setKind(doc.getString("kind"));
                            upload.setType(doc.getString("type"));
                            upload.setPrice(doc.getString("price") + "zł");

                            upload.setVoivodeship(doc.getString("voivodeship"));
                            upload.setVariety(doc.getString("variety"));


                            mUploads.add(upload);
                            if(bundle!=null)
                            imageAdapter.getFilter().filter("mazowieckie"); //filtrowanie
                            imageAdapter.notifyDataSetChanged();

                        }
                    });



                }


                progressCircle.setVisibility(View.INVISIBLE);

            }
        });



}

    @Override
    public void onItemClick(int position) {
       // imageAdapter.getFilter().filter("mazowieckie");
        Toast.makeText(this,"Kliknięcie" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {

    }
}
