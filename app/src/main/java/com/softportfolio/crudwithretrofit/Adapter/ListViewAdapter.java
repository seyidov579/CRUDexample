package com.softportfolio.crudwithretrofit.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.R;
import com.softportfolio.crudwithretrofit.Utils.GlideApp;

import java.util.List;

public class ListViewAdapter  extends ArrayAdapter<Heroes> {
    private int resourceLayout;
    private Context mContext;

    public ListViewAdapter(Context context, int resource, List<Heroes> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Heroes p = getItem(position);
//        assert p != null;


        if (p != null) {
            ImageView heroPic = v.findViewById(R.id.heroPic);
            TextView heroTitle = v.findViewById(R.id.heroTitle);
            TextView heroShortDesc = v.findViewById(R.id.heroShortDesc);
            if (p.getImageurl() != null) {
                Uri uri = Uri.parse(p.getImageurl());
                GlideApp
                        .with(mContext)
                        .load(uri)
                        .override(200,200)
//                        .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(heroPic);
            }

            if (p.getName() != null) {
                heroTitle.setText(p.getName());
            }

            if (p.getBio() != null) {
                heroShortDesc.setText(p.getBio().substring(0, Math.min(p.getBio().length(), 100)));
            }
        }

        return v;
    }
}

