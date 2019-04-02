package com.example.max.appleexchange;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.max.appleexchange.startActivity.ActivityResultBus;
import com.example.max.appleexchange.startActivity.ActivityResultEvent;
import com.example.max.appleexchange.startActivity.Glide4Engine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AddFragment extends Fragment {

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
    private final int PICK_IMAGE_REQUEST = 1;
    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
    List<Uri> mSelected;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add,container,false);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        addAdvertisement=v.findViewById(R.id.button_addAdvertisement);
        addAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(city)&&isEmpty(text))
                    addAdvertisement(mAuth,spinnerType,spinnerVariety,spinnerVoivodeship,spinnerKind,city,
                            price,text);
                else
                    Toast.makeText(getActivity(), "Wprowadz poprawne dane", Toast.LENGTH_SHORT).show();
            }
        });
        spinnerType=v.findViewById(R.id.spinner1);
        ArrayAdapter<String>myAdapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.typeOfAdvertisement));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(myAdapter1);


        spinnerVariety=v.findViewById(R.id.spinner2);
        ArrayAdapter<String>myAdapter2=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.varietyOfApple));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariety.setAdapter(myAdapter2);

        spinnerVoivodeship=v.findViewById(R.id.spinner3);
        ArrayAdapter<String>myAdapter3=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.voivodeship));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVoivodeship.setAdapter(myAdapter3);

        spinnerKind=v.findViewById(R.id.spinner4);
        ArrayAdapter<String>myAdapter4=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.kindOfApple));
        myAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKind.setAdapter(myAdapter4);

        city=v.findViewById(R.id.editTextCity);
        price=v.findViewById(R.id.editTextPrice);
        text=v.findViewById(R.id.editTextText);

        imageView=v.findViewById(R.id.imgView);
        addPhoto=v.findViewById(R.id.button_addphoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });


        return v;
    }

    private void addAdvertisement(FirebaseAuth mAuth,Spinner spinnerType, Spinner spinnerVariety, Spinner spinnerVoivodeship,
                                  Spinner spinnerKind, EditText city, EditText price, EditText text) {
        writeNewAdvertisement(mAuth.getUid().toString(),spinnerType.getSelectedItem().toString(),spinnerVariety.getSelectedItem().toString(),
                spinnerVoivodeship.getSelectedItem().toString(),spinnerKind.getSelectedItem().toString(),
                city.getText().toString(),price.getText().toString(),text.getText().toString());
        homeFragment();


    }


    private void writeNewAdvertisement(String userId, String type, String variety, String voivodeship,
                              String kind, String city, String price, String text)
    {
        Map<String,Object> advertisement=new HashMap<>();
        advertisement.put("userId",userId);
        advertisement.put("type",type);
        advertisement.put("variety",variety);
        advertisement.put("voivodeship",voivodeship);
        advertisement.put("kind",kind);
        advertisement.put("city",city);
        advertisement.put("price",price);
        advertisement.put("text",text);


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

    private void homeFragment (){
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
}
