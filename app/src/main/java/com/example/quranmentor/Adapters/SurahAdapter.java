package com.example.quranmentor.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.R;
import com.example.quranmentor.common.completeTranslation;
import com.example.quranmentor.activities.completeSurah;
import com.example.quranmentor.models.Surah;

import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.SurahViewHolder> {

    private List<Surah> surahList;
    private Context context;


    public SurahAdapter(List<Surah> surahList , Context context ) {
        this.surahList = surahList;
        this.context  =context;
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surah_recyclerview, parent, false);
        return new SurahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
        Surah surah = surahList.get(position);
        holder.surahnumber.setText(String.valueOf(surah.getNumber()));
        holder.surahName.setText(surah.getEnglishName());
        holder.ArabicsurahName.setText(surah.getName());
        holder.ayanumbers.setText("Verses: " + String.valueOf(surah.getNumberOfAyahs()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, completeSurah.class);
            intent.putExtra(completeTranslation.SURAH_NO, surah.getNumber());
            intent.putExtra(completeTranslation.SURAH_NAME, surah.getName());
            intent.putExtra(completeTranslation.SURAH_TYPE, surah.getRevelationType());
            intent.putExtra(completeTranslation.SURAH_TOTALAYA, surah.getNumberOfAyahs());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    class SurahViewHolder extends RecyclerView.ViewHolder {
        TextView surahnumber;
        TextView surahName;
        TextView ArabicsurahName;
        TextView ayanumbers;
        SurahViewHolder(@NonNull View itemView) {
            super(itemView);
            surahName = itemView.findViewById(R.id.surahName);
            ArabicsurahName = itemView.findViewById(R.id.surahName1);
            surahnumber = itemView.findViewById(R.id.surahnumber);
            ayanumbers = itemView.findViewById(R.id.ayanumber);
        }
    }

}

