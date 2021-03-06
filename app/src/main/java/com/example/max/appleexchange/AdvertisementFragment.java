package com.example.max.appleexchange;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.appleexchange.expandableList.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.ContentValues.TAG;

public class AdvertisementFragment extends Fragment {

    private TextView textViewName;
    private TextView textViewKind;
    private TextView textViewType;
    private TextView textViewVoivodeship;
    private Button buttonPhone;
    private TextView textViewVariety;
    private ImageView imageView;
    private TextView textViewPrice;
    private TextView textViewCity;
    private TextView textViewText;
    private Upload selectedItem;
    public AdvertisementFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView=inflater.inflate(R.layout.fragment_advertisement,container,false);

        Bundle bundle = new Bundle();
        bundle=getArguments();
        selectedItem= (Upload)bundle.getSerializable("selecteditem");
        final ArrayList<Category> retData=(ArrayList<Category>)bundle.getSerializable("advData");

        Toolbar toolbar = itemView.findViewById(R.id.toolbar_fragment);
        ((BrowseActivity) getActivity()).getSupportActionBar().hide();
        ((BrowseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getContext(), BrowseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("retData",retData);
            intent.putExtras(bundle);
            startActivity(intent);
            }
        });

        textViewName=itemView.findViewById(R.id.text_view_name);
        textViewCity=itemView.findViewById(R.id.text_view_city);
        imageView=itemView.findViewById(R.id.image_view);
        textViewKind=itemView.findViewById(R.id.text_view_kind);
        textViewType=itemView.findViewById(R.id.text_view_type);
        textViewVoivodeship=itemView.findViewById(R.id.text_view_Voivodeship);
        buttonPhone=itemView.findViewById(R.id.text_view_phone);
        textViewVariety=itemView.findViewById(R.id.text_view_variety);
        textViewPrice=itemView.findViewById(R.id.text_view_price);
        textViewText=itemView.findViewById(R.id.text_view_text);

        textViewName.setText(selectedItem.getName());
        textViewCity.setText(selectedItem.getCity());
        textViewKind.setText(selectedItem.getKind());
        textViewType.setText(selectedItem.getType());
        textViewVoivodeship.setText(selectedItem.getVoivodeship());
        buttonPhone.setText(selectedItem.getPhone());
        textViewVariety.setText(selectedItem.getVariety());
        textViewPrice.setText(selectedItem.getPrice());
        textViewText.setText(selectedItem.getText());
        Picasso.get()
                .load(selectedItem.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(this.imageView);

        PhoneCallListener phoneCallListener = new PhoneCallListener();
        TelephonyManager telManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                phoneCallIntent.setData(Uri.parse("tel:"+selectedItem.getPhone()));
                if (ContextCompat.checkSelfPermission(getContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(phoneCallIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }

            }
        });

        return itemView;
    }



    private class PhoneCallListener extends PhoneStateListener {

        String TAG = "LOGGING PHONE CALL";

        private boolean phoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(TAG, "OFFHOOK");

                phoneCalling = true;
            }

            // When the call ends launch the main activity again
            if (TelephonyManager.CALL_STATE_IDLE == state) {

                Log.i(TAG, "IDLE");

                if (phoneCalling) {

                    Log.i(TAG, "restart app");

                    // restart app
                    Intent i = getContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getContext().getPackageName());

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    phoneCalling = false;
                }

            }
        }
    }























}
