package com.example.max.appleexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.max.appleexchange.old_unused.Advertisement;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    private FirebaseFirestore databaseReference;
    private List<Upload> mUploads;
    private List<Advertisement> mAdvertisements;

    private ProgressBar progressCircle;


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

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressCircle=findViewById(R.id.progress_circle);

        databaseReference=FirebaseFirestore.getInstance();
        mUploads = new ArrayList<>();
        mAdvertisements = new ArrayList<>();

        databaseReference.collection("advertisements").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                for (DocumentSnapshot doc : snapshots) {
                    Upload upload= new Upload();
                    upload.setName(doc.getString("text"));
                    upload.setImageUrl(doc.getString("photo"));
                    mUploads.add(upload);
                }
                imageAdapter = new ImageAdapter(BrowseActivity.this,mUploads);
                recyclerView.setAdapter(imageAdapter);
                progressCircle.setVisibility(View.INVISIBLE);

            }
        });












        /*uploads = new ArrayList<>();

        databaseReference = FirebaseFirestore.getInstance();

        Query query = databaseReference.collection("advertisements");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(BrowseActivity.this,"Blad wczytania danych",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    for(DocumentSnapshot postSnapshot : documentSnapshot.){
                        Upload upload=postSnapshot
                    }
                    System.out.println("Current data: " + documentSnapshot.getData());
                } else {
                    System.out.print("Current data: null");
                }
            }
        });
*/


    }
}
