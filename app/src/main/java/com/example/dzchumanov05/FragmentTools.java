package com.example.dzchumanov05;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// TODO: программа падает при смене темы с помощью этого фрагмента! Разберись!
public class FragmentTools extends BottomSheetDialogFragment {
    private SwitchCompat swDarkTheme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swDarkTheme = view.findViewById(R.id.swDarkTheme);
        // установить переключатель в позицию, записанную в SharedPreferences
        // (активити надо кастануть перед вызовом метода)
        swDarkTheme.setChecked(((ActivityMain) getActivity()).isDarkTheme());
        // установить слушатель на переключатель
        swDarkTheme.setOnCheckedChangeListener((buttonView, isChecked) ->
                ((ActivityMain) getActivity()).setDarkTheme(isChecked));
    }
}
