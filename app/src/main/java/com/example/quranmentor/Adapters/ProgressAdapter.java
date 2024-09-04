//package com.example.quranmentor.Adapters;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.DataEntry;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import com.anychart.charts.Pie;
//import com.example.quranmentor.R;
//import com.example.quranmentor.activities.ShowProgressDetail;
//import com.example.quranmentor.models.ProgressReport;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {
//    private Context context;
//    private List<ProgressReport> studentProgressList;
//
//    public ProgressAdapter(Context context,List<ProgressReport> studentProgressList) {
//        this.studentProgressList = studentProgressList;
//        this.context=context;
//    }
//
//    @NonNull
//    @Override
//    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_report_recyclerview, parent, false);
//        return new ProgressViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
//        ProgressReport progressReport = studentProgressList.get(position);
//        // Set up the pie chart
//        Pie pie = AnyChart.pie();
//        List<DataEntry> dataEntries = new ArrayList<>();
//        dataEntries.add(new ValueDataEntry("Pronunciation", progressReport.getPronunciation()));
//        dataEntries.add(new ValueDataEntry("Fluency", progressReport.getFluency()));
//        dataEntries.add(new ValueDataEntry("Memorization", progressReport.getMemorization()));
//        dataEntries.add(new ValueDataEntry("Tajweed", progressReport.getTajweed()));
//        dataEntries.add(new ValueDataEntry("Result", progressReport.getEssentialDuas()));
//        pie.data(dataEntries);
//
//        holder.progressChart.setChart(pie);
//
//        holder.showcompleteprogress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ShowProgressDetail.class);
//                intent.putExtra("studentName",progressReport.getStudentName());
//                context.startActivity(intent);
//            }
//        });
//
//    }
//    @Override
//    public int getItemCount() {
//        return studentProgressList.size();
//    }
//
//    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
//        AnyChartView progressChart;
//        TextView showcompleteprogress;
//        public ProgressViewHolder(@NonNull View itemView) {
//            super(itemView);
//            progressChart = itemView.findViewById(R.id.progresschart);
//            showcompleteprogress = itemView.findViewById(R.id.showcompleteprogress);
//        }
//    }
//}


package com.example.quranmentor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.quranmentor.R;
import com.example.quranmentor.activities.ShowProgressDetail;
import com.example.quranmentor.models.ProgressReport;

import java.util.ArrayList;
import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {
    private Context context;
    private List<ProgressReport> studentProgressList;

    public ProgressAdapter(Context context, List<ProgressReport> studentProgressList) {
        this.studentProgressList = studentProgressList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_report_recyclerview, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        ProgressReport progressReport = studentProgressList.get(position);

        // Set up the pie chart
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.add(new ValueDataEntry("Pronunciation", progressReport.getPronunciation()));
        dataEntries.add(new ValueDataEntry("Fluency", progressReport.getFluency()));
        dataEntries.add(new ValueDataEntry("Memorization", progressReport.getMemorization()));
        dataEntries.add(new ValueDataEntry("Tajweed", progressReport.getTajweed()));
        dataEntries.add(new ValueDataEntry("Essential Duas", progressReport.getEssentialDuas()));
        pie.data(dataEntries);

        holder.progressChart.setChart(pie);

        holder.showcompleteprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowProgressDetail.class);
                intent.putExtra("progressReportId", progressReport.getReportID()); // Pass the progress report ID
                intent.putExtra("studentName", progressReport.getStudentName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentProgressList.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        AnyChartView progressChart;
        TextView showcompleteprogress;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            progressChart = itemView.findViewById(R.id.progresschart);
            showcompleteprogress = itemView.findViewById(R.id.showcompleteprogress);
        }
    }
}
