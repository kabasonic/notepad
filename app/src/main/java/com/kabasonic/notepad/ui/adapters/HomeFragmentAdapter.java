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

import com.kabasonic.notepad.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Note> mRowItem;

    public HomeFragmentAdapter() {
        //empty constructor
    }

    public HomeFragmentAdapter(Context context, ArrayList<Note> mRowItem) {
        this.mContext = context;
        this.mRowItem = mRowItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_row_home_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note noteItem = mRowItem.get(position);
        holder.mTitle.setText(noteItem.getmTitle());


        if(displayingBody()){
            holder.mBody.setText(noteItem.getmBody());
            holder.mBody.setVisibility(View.VISIBLE);
        }else{
            holder.mBodyLayout.setVisibility(View.GONE);
        }

        if (noteItem.getmBadge() > 1) {
            holder.mImageLayout.setVisibility(View.VISIBLE);
            holder.mBadgeImage.setText(String.valueOf(noteItem.getmBadge()));
            holder.mImage.setImageBitmap(noteItem.getmImageList().get(position));
        } else if (noteItem.getmBadge() == 1) {
            holder.mImageLayout.setVisibility(View.VISIBLE);
            holder.mBadgeImage.setVisibility(View.GONE);
            holder.mImage.setImageBitmap(noteItem.getmImageList().get(position));
        }else {
            holder.mImageLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (mRowItem.size() == 0)
            return 0;
        else
            return mRowItem.size();
    }

//    public ArrayList<Note> getmRowItem(){
//        return mRowItem;
//    }

    private boolean displayingBody(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
        int modeDisplayText = (sharedPref.getInt(mContext.getResources().getString(R.string.saved_displaying_text_note), 0));
        if(modeDisplayText == 1)
            return true;
        else
            return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mBody;
        public TextView mBadgeImage;
        public ImageView mImage;
        public FrameLayout mImageLayout;
        public LinearLayout mBodyLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.title_row_home);
            mBody = itemView.findViewById(R.id.body_text_home_fragment);
            mBadgeImage = itemView.findViewById(R.id.badge_image_home_fragment);
            mImage = itemView.findViewById(R.id.image_row_home_fragment);
            mImageLayout = itemView.findViewById(R.id.layout_image_home_fragment);
            mBodyLayout = itemView.findViewById(R.id.body_layout);
        }
    }

}
