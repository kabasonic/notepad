package com.kabasonic.notepad.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.ui.adapters.HomeFragmentAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton mFabMore, mFabCreateNote, mFabCreateReminder;
    private boolean clicked = false;
    private Animation fromBottomAnim, rotateCloseAnim, rotateOpenAnim, toBottomAnim;

    private HomeFragmentAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private HomeViewModel homeViewModel;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.clicked = false;
        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //Initialization animations for FABs
        initAnimElements();
        //Initializations view elements
        initViewElements(view);
        //setAdapter(view);
        setAdapter(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        fabListeners();
        getAllNotes();
        getDisplayElement();
        getDisplayContent();
    }


    private void getAllNotes() {
        homeViewModel.getGetAllNotesWithImages().observe(getViewLifecycleOwner(), noteWithImages -> {
            List<Note> noteList = new ArrayList<>();
            List<List<Image>> imagesList = new ArrayList<List<Image>>();
            for (NoteWithImages itemObject : noteWithImages) {
                noteList.add(itemObject.note);
                imagesList.add(itemObject.imageList);
            }
            mAdapter.setDataAdapter(noteList, imagesList);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void getDisplayElement(){
        homeViewModel.getDisplayElements().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("HomeViewModel", "onChanged");
                if(mRecyclerView != null && mAdapter != null){
                    mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, integer));
                    mAdapter.displayingView(integer);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void getDisplayContent(){
        homeViewModel.getDisplayContent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(mAdapter!=null){
                    mAdapter.displayingBody(integer);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setAdapter(View view) {

        mRecyclerView = view.findViewById(R.id.home_fragment_rv);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HomeFragmentAdapter(mContext);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener(new HomeFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClickItemView(Note note) {
                // Navigation!!!
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment("", note.getId());
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onClickFavorite(boolean action, int position) {
                NoteWithImages noteWithImages = mAdapter.getNoteWithImagesAt(position);
                noteWithImages.note.setFavorite(!action);
                homeViewModel.updateNoteWithImages(noteWithImages);
                mAdapter.notifyDataSetChanged();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // remove with adapter and delete with BD

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy hh:mm");
                Date date = new Date(System.currentTimeMillis());
                NoteWithImages noteWithImages = mAdapter.getNoteWithImagesAt(viewHolder.getAdapterPosition());
                noteWithImages.note.setDeletedAt(sdf.format(date));
                homeViewModel.updateNoteWithImages(noteWithImages);
                Snackbar.make(view,"The note was moved to the trash.",Snackbar.LENGTH_SHORT).show();

//                homeViewModel.deleteNoteWithImages(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
//                mAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
//                Snackbar.make(view, "Note has been deleted", Snackbar.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void fabListeners() {
        mFabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonClicked();
            }
        });

        mFabCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(getResources().getString(R.string.note_key), -1);
                Navigation.findNavController(view).navigate(action);
            }
        });

        mFabCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(getResources().getString(R.string.note_key_remembering), -1);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initViewElements(View view) {
        mFabMore = view.findViewById(R.id.fab_more);
        mFabCreateNote = view.findViewById(R.id.fab_two);
        mFabCreateReminder = view.findViewById(R.id.fab_one);
    }

    private void initAnimElements() {
        fromBottomAnim = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_anim);
        rotateCloseAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_close_anim);
        rotateOpenAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_open_anim);
        toBottomAnim = AnimationUtils.loadAnimation(mContext, R.anim.to_bottom_anim);
    }

    private void actionButtonClicked() {
        playAnimation();
        setVisibilityFab();
        setClicked();
        clicked = !clicked;
    }

    private void playAnimation() {
        if (!this.clicked) {
            mFabMore.setAnimation(rotateOpenAnim);
            mFabCreateNote.setAnimation(fromBottomAnim);
            mFabCreateReminder.setAnimation(fromBottomAnim);
        } else {
            mFabMore.setAnimation(rotateCloseAnim);
            mFabCreateNote.setAnimation(toBottomAnim);
            mFabCreateReminder.setAnimation(toBottomAnim);
        }

    }

    private void setVisibilityFab() {
        if (!this.clicked) {
            mFabMore.setVisibility(View.VISIBLE);
            mFabCreateNote.setVisibility(View.VISIBLE);
            mFabCreateReminder.setVisibility(View.VISIBLE);
        } else {
            mFabMore.setVisibility(View.VISIBLE);
            mFabCreateNote.setVisibility(View.INVISIBLE);
            mFabCreateReminder.setVisibility(View.INVISIBLE);
        }


    }

    private void setClicked() {
        if (!this.clicked) {
            mFabCreateNote.setClickable(true);
            mFabCreateReminder.setClickable(true);
        } else {
            mFabCreateNote.setClickable(false);
            mFabCreateReminder.setClickable(false);
        }
    }

}
