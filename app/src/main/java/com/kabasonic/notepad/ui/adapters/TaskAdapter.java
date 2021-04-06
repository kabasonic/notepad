package com.kabasonic.notepad.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mItemList;

    private Context mContext;

    public interface OnClickListener {
        void removeTask(int position);

        void fieldTask(int position, String changedText);

        void checkBoxTask(int position, boolean isChecked);
    }

    private OnClickListener mListener;

    public TaskAdapter(Context context) {
        this.mContext = context;
        this.mItemList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_row_task, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mItemList.get(position).isCompletedTask()) {
            holder.fieldTask.setPaintFlags(holder.fieldTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.fieldTask.setPaintFlags(0);
        }
        holder.checkBoxTask.setChecked(mItemList.get(position).isCompletedTask());
        holder.fieldTask.setText(mItemList.get(position).getBody());


    }

    @Override
    public int getItemCount() {
        if (mItemList == null)
            return 0;
        else return mItemList.size();
    }

    public void setmItemList(List<Task> mItemList) {
        this.mItemList = mItemList;
    }

    public List<Task> getmItemList() {
        return mItemList;
    }

    public void removeTaskWithList(int position) {
        this.mItemList.remove(position);
    }

    public String getListToString() {
        String output = "";
        for (Task items : mItemList) {
            Log.d("Debug", "All task to string: " + output);
            output += items.getBody() + "\n";
        }
        return output;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView moveTask, removeTask;
        public MaterialCheckBox checkBoxTask;
        public EditText fieldTask;


        public ViewHolder(@NonNull View itemView, OnClickListener mListener) {
            super(itemView);

            moveTask = itemView.findViewById(R.id.move_task);
            checkBoxTask = itemView.findViewById(R.id.checkbox_task_row);
            fieldTask = itemView.findViewById(R.id.edit_text_row_task);
            removeTask = itemView.findViewById(R.id.delete_row_task);

            fieldTask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (!hasFocus) {
                                Log.d("Debug", "Focus false, text: " + fieldTask.getText().toString());
                                Log.d("Debug", "Adapter position:  " + position);
                                mListener.fieldTask(position, fieldTask.getText().toString());
                                mListener.fieldTask(position, fieldTask.getText().toString());
                            }

                        }
                    }
                }

            });

            removeTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.removeTask(getAdapterPosition());
                        }
                    }

                }
            });

            checkBoxTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.checkBoxTask(position, checkBoxTask.isChecked());
                        }
                    }
                }
            });
        }
    }
}
