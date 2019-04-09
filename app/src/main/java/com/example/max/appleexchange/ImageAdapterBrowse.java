package com.example.max.appleexchange;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapterBrowse extends RecyclerView.Adapter<ImageAdapterBrowse.ImageViewHolder> {

    private Context context;
    private List<Upload> uploadList;
    private OnItemClickListener mListener;

    public ImageAdapterBrowse(Context context, List<Upload> uploads) {
        this.context = context;
        uploadList = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, viewGroup, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadCurrent = uploadList.get(i);
        //imageViewHolder.textViewName.setText(uploadCurrent.getName());
        imageViewHolder.textViewKind.setText(uploadCurrent.getKind());
        imageViewHolder.textViewType.setText(uploadCurrent.getType());
        imageViewHolder.textViewVoivodeship.setText(uploadCurrent.getVoivodeship());
        imageViewHolder.textViewPhone.setText(uploadCurrent.getPhone());
        imageViewHolder.textViewVariety.setText(uploadCurrent.getVariety());
        imageViewHolder.textViewPrice.setText(uploadCurrent.getPrice());
        imageViewHolder.textViewCity.setText(uploadCurrent.getCity());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageViewHolder.imageView);



    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public TextView textViewName;
        public TextView textViewKind;
        public TextView textViewType;
        public TextView textViewVoivodeship;
        public TextView textViewPhone;
        public TextView textViewVariety;
        public ImageView imageView;
        public TextView textViewPrice;
        public TextView textViewCity;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            //textViewName=itemView.findViewById(R.id.text_view_name);
            textViewCity = itemView.findViewById(R.id.text_view_city);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewKind = itemView.findViewById(R.id.text_view_kind);
            textViewType = itemView.findViewById(R.id.text_view_type);
            textViewVoivodeship = itemView.findViewById(R.id.text_view_Voivodeship);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewVariety = itemView.findViewById(R.id.text_view_variety);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }


        }
    public  void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}
