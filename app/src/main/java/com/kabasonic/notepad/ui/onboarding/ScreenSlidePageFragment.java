package com.kabasonic.notepad.ui.onboarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.kabasonic.notepad.R;

public class ScreenSlidePageFragment extends Fragment {

    ImageView imView;
    TextView txView;
    MaterialButton btView;

    private int position = 0;

    EventOnBoarding eventOnBoarding;

    public interface EventOnBoarding{
        void event();
    }

    public ScreenSlidePageFragment(int position) {
        this.position = position;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventOnBoarding = (EventOnBoarding) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        imView = view.findViewById(R.id.im_slide_on_boarding);
        txView = view.findViewById(R.id.tx_slide_on_boarding);
        btView = view.findViewById(R.id.bt_on_boarding);

        setContentView();

        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventOnBoarding.event();
            }
        });
        return view;
    }

    private void setContentView() {
        switch (this.position) {
            case 0:
                imView.setImageResource(R.drawable.illustration_1);
                txView.setText(R.string.lorem_ipsum);
                break;
            case 1:
                imView.setImageResource(R.drawable.illustration_2);
                txView.setText(R.string.lorem_ipsum);
                break;
            case 2:
                imView.setImageResource(R.drawable.illustration_3);
                txView.setText(R.string.lorem_ipsum);
                break;
            default:
                break;
        }
    }

}
