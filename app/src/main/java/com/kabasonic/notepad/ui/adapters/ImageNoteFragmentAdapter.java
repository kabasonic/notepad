package com.kabasonic.notepad.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageNoteFragmentAdapter extends RecyclerView.Adapter<ImageNoteFragmentAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mImageItems;
    private List<Image> mImageList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClickDeleteImage(int position);

        void onItemClickPickImage(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public ImageNoteFragmentAdapter(Context context) {
        this.mContext = context;
        this.mImageItems = new ArrayList<>();
    }

    public void deleteImageItem(int position) {
        mImageItems.remove(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_item_adapter, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.mImage.setImageBitmap(mImageItems.get(position));
            Glide.with(mContext).load(mImageItems.get(position)).into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        if (mImageItems.size() == 0)
            return 0;
        else return mImageItems.size();
    }

    public void addImageToList(String toString) {
        this.mImageItems.add(toString);
    }

    public List<String> getAllImageList(){
        return this.mImageItems;
    }

    public void setImagesToList(List<String> mImageList){
        this.mImageItems = mImageList;
    }

    public void setImageObjectToList(List<Image> imageList){
        this.mImageList = imageList;
    }

    public Image getAtImageItem(int position){
        return mImageList.get(position);
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
