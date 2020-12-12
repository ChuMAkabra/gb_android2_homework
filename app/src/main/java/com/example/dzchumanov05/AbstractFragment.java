package com.example.dzchumanov05;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AbstractFragment extends Fragment {
    protected String className = this.getClass().getName();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(className, "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(className, "onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(className, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(className, "onViewCreated()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(className, "onActivityCreated()");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(className, "onViewStateRestored()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(className, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(className, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(className, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(className, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(className, "onDestroyView()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(className, "onSaveInstanceState()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(className, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(className, "onDetach()");
    }
}
