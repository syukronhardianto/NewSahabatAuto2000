package com.example.bismillah.newsahabatauto2000.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bismillah.newsahabatauto2000.Activity.ProspekActivity;
import com.example.bismillah.newsahabatauto2000.Activity.UpdateDeleteProspekActivity;
import com.example.bismillah.newsahabatauto2000.Model.DataProspek;
import com.example.bismillah.newsahabatauto2000.R;

import java.util.List;

public class ProspekListAdapter extends RecyclerView.Adapter<ProspekListAdapter.ViewHolder> {
    private Context context;
    private List<DataProspek> dataProspekList;

    public ProspekListAdapter(Context context, List<DataProspek> dataProspekList) {
        this.context = context;
        this.dataProspekList = dataProspekList;
    }

    @Override
    public ProspekListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview_prospek_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProspekListAdapter.ViewHolder holder, int position) {
        holder.namaTv.setText(dataProspekList.get(position).getNama_cust());
        holder.kategoriTv.setText(dataProspekList.get(position).getKategori());
        holder.jenisMobilTv.setText(dataProspekList.get(position).getJenis_mobil());
        holder.warnaMobilTv.setText(dataProspekList.get(position).getWarna_mobil());
        holder.telpTv.setText(dataProspekList.get(position).getTelp_cust());

        holder.idCust = dataProspekList.get(position).getId_cust();
        holder.jenisKel = dataProspekList.get(position).getJenis_kel_cust();
        holder.alamatCust = dataProspekList.get(position).getAlamat_cust();
        holder.emailCust = dataProspekList.get(position).getEmail_cust();
        holder.tglCust = dataProspekList.get(position).getTgl_cust();
        holder.agamaCust = dataProspekList.get(position).getAgama_cust_do();
        holder.tglLahirCust = dataProspekList.get(position).getTgl_lahir_cust_do();
    }

    @Override
    public int getItemCount() {
        return dataProspekList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView namaTv, kategoriTv, jenisMobilTv, warnaMobilTv, telpTv;
        String idCust, jenisKel, alamatCust, emailCust, tglCust, agamaCust, tglLahirCust;

        ViewHolder(View itemView) {
            super(itemView);
            namaTv = itemView.findViewById(R.id.nama_cust_tv);
            kategoriTv = itemView.findViewById(R.id.kategori_cust_tv);
            jenisMobilTv = itemView.findViewById(R.id.jenis_mobil_tv);
            warnaMobilTv = itemView.findViewById(R.id.warna_mobil_tv);
            telpTv = itemView.findViewById(R.id.telp_cust_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String namaCust = namaTv.getText().toString();
            String kategoriCust = kategoriTv.getText().toString();
            String jenisMobilCust = jenisMobilTv.getText().toString();
            String warnaMobilCust = warnaMobilTv.getText().toString();
            String telpCust = telpTv.getText().toString();

            Intent i = new Intent(context, UpdateDeleteProspekActivity.class);
            i.putExtra("namaCust", namaCust);
            i.putExtra("kategoriCust", kategoriCust);
            i.putExtra("jenisMobilCust", jenisMobilCust);
            i.putExtra("warnaMobilCust", warnaMobilCust);
            i.putExtra("telpCust", telpCust);

            i.putExtra("idCust", idCust);
            i.putExtra("jenisKel", jenisKel);
            i.putExtra("alamatCust", alamatCust);
            i.putExtra("emailCust", emailCust);
            i.putExtra("tglCust", tglCust);
            i.putExtra("agamaCust", agamaCust);
            i.putExtra("tglLahirCust", tglLahirCust);
            context.startActivity(i);
        }
    }
}
