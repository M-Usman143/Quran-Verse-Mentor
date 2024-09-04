package com.example.quranmentor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.R;
import com.example.quranmentor.models.NamazTimings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamazTimingsAdapter extends RecyclerView.Adapter<NamazTimingsAdapter.NamazTimingsViewHolder> {
    private Context context;
    private List<NamazTimings> namazTimingslist;

    public NamazTimingsAdapter(Context context) {
        this.context = context;
        namazTimingslist = new ArrayList<>();
    }
public void add(NamazTimings namazTimings){
        namazTimingslist.add(namazTimings);
        notifyDataSetChanged();
}
    public void clear(){
        namazTimingslist.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NamazTimingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutnamaz_timings_item, parent, false);
        return new NamazTimingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NamazTimingsViewHolder holder, int position) {
        NamazTimings namazTimings = namazTimingslist.get(position);
        holder.bind(namazTimings);
    }

    @Override
    public int getItemCount() {return namazTimingslist.size();}

    public static class NamazTimingsViewHolder extends RecyclerView.ViewHolder {
        TextView fajr , dahur , asr , maghrib , isha , date;
        public NamazTimingsViewHolder(@NonNull View itemView) {
            super(itemView);
            fajr = itemView.findViewById(R.id.fajrTimeTextView);
            dahur = itemView.findViewById(R.id.dhuhrTimeTextView);
            asr = itemView.findViewById(R.id.asrTimeTextView);
            maghrib = itemView.findViewById(R.id.maghribTimeTextView);
            isha = itemView.findViewById(R.id.ishaTimeTextView);
            date = itemView.findViewById(R.id.dateshowing);
     }

        public void bind(NamazTimings namazTimings) {
          fajr.setText(namazTimings.getFajr());
          dahur.setText(namazTimings.getDhaur());
          asr.setText(namazTimings.getAsr());
          maghrib.setText(namazTimings.getMaghrib());
          isha.setText(namazTimings.getIsha());
          date.setText(namazTimings.getDate());

        }
    }
}

