package com.example.dzchumanov05;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterWeather extends RecyclerView.Adapter<AdapterWeather.ViewHolder> {
    List<String> times;
    List<Bitmap> images;
    List<String> temps;

    public AdapterWeather(List<String> times, List<Bitmap> images, List<String> temps) {
        this.times = times;
        this.images = images;
        this.temps = temps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: запомни, что вьюшка здесь инфлейтится с помощью LayoutInflater!!!
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // получить View'шку, которая будет основой ViewHolder -> Адаптера -> RV
        View view = layoutInflater.inflate(R.layout.item_tempstamp, parent, false);
        // вернуть ViewHolder, передав ему вьюшку
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(times.get(position), images.get(position), temps.get(position));
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private ImageView ivSky;
        private TextView tvTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTime =  itemView.findViewById(R.id.tvTime);
            this.ivSky = itemView.findViewById(R.id.ivSky);
            this.tvTemp = itemView.findViewById(R.id.tvTemp);
        }

        public void setData(String time, Bitmap image, String temp) {
            tvTime.setText(time);
            ivSky.setImageBitmap(image);
            tvTemp.setText(temp);
        }
    }
}
