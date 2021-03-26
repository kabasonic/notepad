package com.kabasonic.notepad.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private Context mContext;
    private List<Note> mRowItem;
    private List<List<Image>> mImageList;

    public interface OnItemClickListener{
        void onClickItemView(Note note);
    }

    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener = mListener;
    }

    public HomeFragmentAdapter(Context context){
        this.mContext = context;
        this.mRowItem = new ArrayList<>();
        this.mImageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_row_home_fragment, parent, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note noteItem = mRowItem.get(position);

        holder.mTitle.setText(noteItem.getTitle());
        holder.mLinearLayout.setBackgroundColor(noteItem.getBackgroundColor());


        if(displayingBody()){
            holder.mBody.setText(noteItem.getBody());
            holder.mBody.setVisibility(View.VISIBLE);
        }else{
            holder.mBodyLayout.setVisibility(View.GONE);
        }

        if(mImageList.get(position).size() > 1){
                holder.mImageLayout.setVisibility(View.VISIBLE);
                holder.mBadgeImage.setText(String.valueOf(mImageList.get(position).size()));
                Glide.with(mContext).load(mImageList.get(position).get(mImageList.get(position).size()-1).getUri()).into(holder.mImage);
        } else if(mImageList.get(position).size() == 1){
            holder.mImageLayout.setVisibility(View.VISIBLE);
            holder.mBadgeImage.setVisibility(View.GONE);
            Glide.with(mContext).load(mImageList.get(position).get(0).getUri()).into(holder.mImage);
        }else{
            holder.mImageLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (mRowItem == null)
            return 0;
        else
            return mRowItem.size();
    }


    public void setDataAdapter(List<Note> noteList,List<List<Image>> imageList){
        this.mRowItem = noteList;
        this.mImageList = imageList;
    }


    public Note getNoteAt(int position){
        return mRowItem.get(position);

    }

    private boolean displayingBody(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
        int modeDisplayText = (sharedPref.getInt(mContext.getResources().getString(R.string.saved_displaying_text_note), 0));
        if(modeDisplayText == 1)
            return true;
        else
            return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mLinearLayout;
        public TextView mTitle;
        public TextView mBody;
        public TextView mBadgeImage;
        public ImageView mImage;
        public FrameLayout mImageLayout;
        public LinearLayout mBodyLayout;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener mListener) {
            super(itemView);

            mLinearLayout = itemView.findViewById(R.id.linear_row_home_fragment);
            mTitle = itemView.findViewById(R.id.title_row_home);
            mBody = itemView.findViewById(R.id.body_text_home_fragment);
            mBadgeImage = itemView.findViewById(R.id.badge_image_home_fragment);
            mImage = itemView.findViewById(R.id.image_row_home_fragment);
            mImageLayout = itemView.findViewById(R.id.layout_image_home_fragment);
            mBodyLayout = itemView.findViewById(R.id.body_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItemView(getNoteAt(getAdapterPosition()));
                }
            });


        }
    }

}
