package com.example.bismillah.newsahabatauto2000.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bismillah.newsahabatauto2000.Model.IdNamaSpv;
import com.example.bismillah.newsahabatauto2000.R;

import java.util.List;

/**
 * Created by Bismillah on 02/11/2018.
 */

public class IdNamaSpvAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<IdNamaSpv> idNamaSpvList;
    private final int mResource;

    public IdNamaSpvAdapter(@NonNull Context context, int resource,
                            List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        idNamaSpvList = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int pos, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView namaSpv = (TextView) view.findViewById(R.id.nama_spv_tv);
        TextView idSpv = (TextView) view.findViewById(R.id.id_spv_tv);

        IdNamaSpv idNamaSpvData = idNamaSpvList.get(pos);

        namaSpv.setText(idNamaSpvData.getNama_spv());
        idSpv.setText(idNamaSpvData.getId_spv());

        return view;
    }
}
