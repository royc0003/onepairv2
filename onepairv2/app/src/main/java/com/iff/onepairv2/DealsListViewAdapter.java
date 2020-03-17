package com.iff.onepairv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DealsListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Deal> modellist;
    ArrayList<Deal> arrayList;

    //constructor
    public DealsListViewAdapter(Context context, List<Deal> modellist)
    {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Deal>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int position) {
        return modellist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            //holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the result into textviews
        holder.mTitleTv.setText(modellist.get(position).getName());
        //holder.mDescTv.setText("");
        //set the result in imageview
        Picasso.get().load(Uri.parse(modellist.get(position).getImage())).into(holder.mIconIv);

        //listview item clicks, all deals from webscraping put here
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start newActivity with title for actionbar and text for textview
                Intent intent = new Intent(mContext, SelectedDealPage.class);
                intent.putExtra("Deal", modellist.get(position));
                if((Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if(charText.length() == 0){
            modellist.addAll(arrayList);
        }
        else {
            for(Deal deal : arrayList) {
                if(deal.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    modellist.add(deal);
                }
            }
        }
        notifyDataSetChanged();
    }
}

