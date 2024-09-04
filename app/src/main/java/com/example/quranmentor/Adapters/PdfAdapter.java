package com.example.quranmentor.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.R;
import com.example.quranmentor.activities.PDF_View_Activity;
import com.example.quranmentor.models.PdfItem;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private List<PdfItem> pdfList;
    private Context context;

    public PdfAdapter(List<PdfItem> pdfList, Context context) {
        this.pdfList = pdfList;
        this.context = context;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item_recyclerview, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        final PdfItem pdfItem = pdfList.get(position);
        holder.pdfName.setText(pdfItem.getPdfName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDF_View_Activity.class);
                intent.putExtra("pdfName", pdfItem.getPdfName());
                context.startActivity(intent);
            }
        });
        holder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDF_View_Activity.class);
                intent.putExtra("pdfName", pdfItem.getPdfName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView pdfName;
        AppCompatButton viewbtn;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdfTitle);
            viewbtn=itemView.findViewById(R.id.viewButton);
        }
    }
}
