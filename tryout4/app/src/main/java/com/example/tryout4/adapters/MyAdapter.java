package com.example.tryout4.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tryout4.R;
import com.example.tryout4.model.ListItem;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder>
{
    List<ListItem>listItemsArrayList;
    Context context,BarcodeCallback;
    public MyAdapter(List<ListItem>listItemsArrayList, Context context){

        this.listItemsArrayList = listItemsArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        return new MyAdapterViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        ListItem listItem = listItemsArrayList.get(position);
        holder.phone_type.setText(listItem.getType());
        holder.barcode.setText(listItem.getCode());
        Linkify.addLinks(holder.barcode,Linkify.ALL);


    }

    @Override
    public int getItemCount() {

        return listItemsArrayList.size();
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder

    {
        TextView barcode,phone_type;
        ImageView mDeleteImage;
        CardView cardView;
        private ExampleAdapter.OnItemClickListener listener;

        public MyAdapterViewHolder(@NonNull final View itemView) {
        super(itemView);
        barcode=itemView.findViewById(R.id.txt_Barcode);
        phone_type=itemView.findViewById(R.id.txt_phone_type);
        cardView=itemView.findViewById(R.id.cardview);
        mDeleteImage = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = listItemsArrayList.get(getAdapterPosition()).getType();

                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT,type);
                i.setType("text/plain");
                itemView.getContext().startActivity(i);

            }
        });




        }

    }
}
