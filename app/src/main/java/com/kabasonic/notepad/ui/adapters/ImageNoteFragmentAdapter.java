package com.kabasonic.notepad.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kabasonic.notepad.R;

import java.util.ArrayList;

public class ImageNoteFragmentAdapter extends RecyclerView.Adapter<ImageNoteFragmentAdapter.ViewHolder> {

    private Context mContext;
    private  ArrayList<ImageItem> mImageItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickDeleteImage(int position);
        void onItemClickPickImage(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public ImageNoteFragmentAdapter(Context context, ArrayList<ImageItem> imageItems) {
        this.mContext = context;
        this.mImageItems = imageItems;
    }

    public void addImage(Bitmap bitmap) {
        mImageItems.add(new ImageItem(bitmap));
    }

    public void deleteImageItem(int position) {
        mImageItems.remove(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_item_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageItem item = mImageItems.get(position);
        holder.mImage.setImageBitmap(item.getmImage());

    }

    @Override
    public int getItemCount() {
        if (mImageItems.size() == 0)
            return 0;
        else return mImageItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImage;
        public ImageButton mImageDelete;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mImage = itemView.findViewById(R.id.item_image);
            mImageDelete = itemView.findViewById(R.id.item_image_delete);


            mImageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClickDeleteImage(position);
                        }
                    }
                }
            });

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClickPickImage(position);
                        }
                    }
                }
            });
        }
    }
}
