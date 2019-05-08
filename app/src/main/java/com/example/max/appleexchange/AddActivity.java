package com.example.max.appleexchange;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AddActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private Spinner spinnerType;
    private Spinner spinnerVariety;
    private Spinner spinnerVoivodeship;
    private Spinner spinnerKind;
    private Button addAdvertisement;
    private EditText city;
    private EditText price;
    private EditText text;
    private Button addPhoto;
    private ImageView imageView;
    private Uri imageUri;
    private ProgressBar progressBar;
    String uploadId;
    //Firebase

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("uploads");


        addAdvertisement=findViewById(R.id.button_addAdvertisement);
        addAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(city)&&isEmpty(text)&&isEmpty(price)&&imageUri!=null)
                    addAdvertisement(mAuth,spinnerType,spinnerVariety,spinnerVoivodeship,spinnerKind,city,
                            price,text);
                else
                    Toast.makeText(AddActivity.this, "Wprowadz poprawne dane", Toast.LENGTH_SHORT).show();
            }
        });
        spinnerType=findViewById(R.id.spinner1);
        ArrayAdapter<String>myAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.typeOfAdvertisement));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(myAdapter1);


        spinnerVariety=findViewById(R.id.spinner2);
        ArrayAdapter<String>myAdapter2=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.varietyOfApple));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariety.setAdapter(myAdapter2);

        spinnerVoivodeship=findViewById(R.id.spinner3);
        ArrayAdapter<String>myAdapter3=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.voivodeship));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVoivodeship.setAdapter(myAdapter3);

        spinnerKind=findViewById(R.id.spinner4);
        ArrayAdapter<String>myAdapter4=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.kindOfApple));
        myAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKind.setAdapter(myAdapter4);

        city=findViewById(R.id.editTextCity);
        price=findViewById(R.id.editTextPrice);
        text=findViewById(R.id.editTextText);

        imageView=findViewById(R.id.imgView);
        addPhoto=findViewById(R.id.button_addphoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });



    }

    private void addAdvertisement(FirebaseAuth mAuth,Spinner spinnerType, Spinner spinnerVariety, Spinner spinnerVoivodeship,
                                  Spinner spinnerKind, EditText city, EditText price, EditText text) {
        writeNewAdvertisement(mAuth.getUid().toString(),spinnerType.getSelectedItem().toString(),spinnerVariety.getSelectedItem().toString(),
                spinnerVoivodeship.getSelectedItem().toString(),spinnerKind.getSelectedItem().toString(),
                city.getText().toString(),price.getText().toString(),text.getText().toString());



    }


    private void writeNewAdvertisement(final String userId,final String type,final String variety,final String voivodeship,
                                       final String kind,final String city,final String price,final String text)
    {

        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(imageUri));
       // StorageReference ref = storageReference.child("uploads"+"/"+imageUri);
        final UploadTask uploadTask = fileReference.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Uploadedphoto:failure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Uploadedphoto:success");
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }
                                Log.d(TAG, "UploadedphotoToDatabase:success");
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    uploadId = task.getResult().toString();
                                    Log.d(TAG, "UploadedphotoToDatabase:success"  + uploadId);


                                    Map<String,Object> advertisement=new HashMap<>();
                                    advertisement.put("userId",userId);
                                    advertisement.put("type",type);
                                    advertisement.put("variety",variety);
                                    advertisement.put("voivodeship",voivodeship);
                                    advertisement.put("kind",kind);
                                    advertisement.put("city",city);
                                    advertisement.put("price",price);
                                    advertisement.put("text",text);
                                    advertisement.put("photo",uploadId);


                                    mDatabase.collection("advertisements").document().set(advertisement)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "AdvertisementDatabase:success");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "AdvertisementDatabase:failure");
                                                }
                                            });


                                }
                            }
                        });
                    }
                });
        Log.d(TAG, "UploadedphotoToDatabase:success"  + uploadId);
        Toast.makeText(this, "Ogłoszenie zostało dodane",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }





    private boolean isEmpty(EditText input)
    {
        String sInput=input.getText().toString().trim();
        if(sInput.isEmpty()){
            input.setError("Pole nie może być puste");
            return false;
        } else{
            input.setError(null);
            return true;
        }
    }

    private void openFileChooser(){



        //Intent intent = new Intent(getActivity(), AddFragment.class);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Log.d(TAG, "OPENFILECHOOSER");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
        //getActivity().startActivityForResult(intent,PICK_IMAGE_REQUEST);
        Log.d(TAG, "OPENFILECHOOSER2");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "OPENFILECHOOSER3");
        super.onActivityResult(requestCode,resultCode,data);


        if(requestCode == 1 && resultCode == Activity.RESULT_OK
                &&data != null && data.getData() !=null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).resize(50,50).into(imageView);

        }


    }

    private String getFileExtension(Uri uri){
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


   /* private void uploadFile() {
        if (imageUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
            + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 5000);

                        Toast.makeText(AddActivity.this,"Załądowano plik", Toast.LENGTH_LONG).show();
                        mDatabase.collection("advertisements").document().set()
                       String uploadId = storageReference.getDownloadUrl().toString();
                    }
                })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }else{
            Toast.makeText(this,"Brak plika", Toast.LENGTH_SHORT).show();
        }
    }*/
}




    /*public void openFileChooser(){

        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

        //Intent intent = new Intent(getActivity(), AddFragment.class);
        Matisse.from(getActivity())
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .addFilter(new Filter() {
                    @Override
                    protected Set<MimeType> constraintTypes() {
                        return null;
                    }

                    @Override
                    public IncapableCause filter(Context context, Item item) {
                        return null;
                    }
                } )

                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.media_grid_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(PICK_IMAGE_REQUEST);

        Log.d(TAG, "OPENFILECHOOSER2");

    }*/



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Matisse", "mSelected: " + mSelected);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }*/
