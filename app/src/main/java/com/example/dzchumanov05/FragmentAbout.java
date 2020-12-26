package com.example.dzchumanov05;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FragmentAbout extends DialogFragment {
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    /** Можно обойтись одним onCreateDialog(), но тогда работать будем со стандартным
     *  диалоговым окном через билдер, и нельзя будет установить слушателя на
     *  пользовательские элементы лейаута (выбранного в setView).
     *  Как я понял, это происходит, потому что onCreateDialog() выполняется
     *  ДО onCreateView, и поэтому inflater еще не доступен
     *  (а значит вьюшку заинфлейтить не удастся). Верно???
     *  Иначе неясно, почему код ниже падает с ошибкой StackOverflow
     */
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        View view = getLayoutInflater().inflate(R.layout.fragment_about, null);
//        TextView websiteLink = view.findViewById(R.id.websiteLink);
//
//        websiteLink.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink.getText().toString()));
//            startActivity(intent);
//        });
//
//        builder.setTitle(R.string.about)
//                .setView(view)
//                .setCancelable(true)
//                .setNegativeButton("CLOSE", (dialog, which) ->{} /*dismiss()*/)
//        ;
//
//        return builder.create();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView websiteLink = view.findViewById(R.id.websiteLink);
        websiteLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink.getText().toString()));
            startActivity(intent);
        });
    }
}