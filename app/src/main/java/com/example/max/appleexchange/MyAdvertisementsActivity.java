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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAdvertisementsActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;



    private FirebaseStorage storage;
    private FirebaseFirestore databaseReference;
    private FirebaseFirestore databaseReferenceUsers;

    private List<Upload> mUploads;

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

        mUploads = new ArrayList<>();
        imageAdapter = new ImageAdapter(MyAdvertisementsActivity.this,mUploads);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(MyAdvertisementsActivity.this);

        databaseReference=FirebaseFirestore.getInstance();
        databaseReferenceUsers=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        databaseReference.collection("advertisements")
                .whereEqualTo("userId",FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }
                        mUploads.clear();

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                        Log.d(TAG, "Zero" );
                        for (final DocumentSnapshot doc : snapshots) {
                            Log.d(TAG, "First");

                            final Upload upload = new Upload();
                            Log.d(TAG, "newUpload");
                            databaseReferenceUsers.collection("users").document(doc.getString("userId"))
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                            upload.setKey(doc.getId());
                                            Log.d(TAG, "setPhone");
                                            upload.setPhone(Integer.toString(documentSnapshot.getLong("phone").intValue()));
                                            Log.d(TAG, "Second" );
                                            Log.d(TAG, "Third" );
                                            upload.setCity(doc.getString("city"));
                                            upload.setImageUrl(doc.getString("photo"));
                                            upload.setKind(doc.getString("kind"));
                                            upload.setType(doc.getString("type"));
                                            upload.setPrice(doc.getString("price") + "zł");

                                            upload.setVoivodeship(doc.getString("voivodeship"));
                                            upload.setVariety(doc.getString("variety"));


                                            mUploads.add(upload);
                                            Log.d(TAG, "fourth" );
                                        }

                                    });

                        }

                        Log.d(TAG, "fifth" );
                        imageAdapter.notifyDataSetChanged();
                        progressCircle.setVisibility(View.INVISIBLE);

                    }
                });

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"Kliknięcie" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = storage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.collection("advertisements")
                        .document(selectedKey).delete();
                Toast.makeText(MyAdvertisementsActivity.this,"Uunieto ogloszenie",Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this,"Usunięcie" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


