package com.example.quranmentor.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranmentor.R;
import com.example.quranmentor.models.Surah;
import com.example.quranmentor.models.SurahDetail;

import java.util.List;
public class SurahDetailAdapter extends RecyclerView.Adapter<SurahDetailAdapter.SurahDetailViewHolder> {
    private List<SurahDetail> surahDetails;
    private Context context;
    public SurahDetailAdapter(List<SurahDetail> surahDetails , Context context) {
        this.surahDetails = surahDetails;
        this.context  =context;
    }

    @NonNull
    @Override
    public SurahDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_surah, parent, false);
        return new SurahDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahDetailAdapter.SurahDetailViewHolder holder, int position) {
        SurahDetail surah = surahDetails.get(position);
        holder.arabictext.setText(surah.getArabic_text());
        holder.translation.setText(surah.getTranslation());
        holder.numberAYAT.setText(String.valueOf(surah.getAya()));

        Typeface urduFont = Typeface.createFromAsset(context.getAssets(), "fonts/Jameel_Noori_Nastaleeq_Kasheeda.ttf");
        holder.translation.setTypeface(urduFont);

        Typeface arabicFont = Typeface.createFromAsset(context.getAssets(), "fonts/MUHAMMADI QURANIC FONT.ttf");
        holder.arabictext.setTypeface(arabicFont);
    }

    @Override
    public int getItemCount() {
        return surahDetails.size();
    }
    public void updateSurahDetails(List<SurahDetail> surahDetails) {
        this.surahDetails = surahDetails;
        notifyDataSetChanged();
    }
    public class SurahDetailViewHolder extends RecyclerView.ViewHolder {

        private  TextView translation , arabictext  , numberAYAT;
        public SurahDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            translation=itemView.findViewById(R.id.translations);
            arabictext=itemView.findViewById(R.id.arabic_texts);
            numberAYAT=itemView.findViewById(R.id.ayatnumber);

        }
    }
}


