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
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private FirebaseFirestore databaseReference;
    private FirebaseFirestore databaseReferenceUsers;

    private List<Upload> mUploads;


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
        databaseReferenceUsers=FirebaseFirestore.getInstance();

        mUploads = new ArrayList<>();
        imageAdapter = new ImageAdapterBrowse(BrowseActivity.this,mUploads);
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
        imageAdapter.getFilter().filter("mazowieckie");
        Toast.makeText(this,"Kliknięcie" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {

    }
}
