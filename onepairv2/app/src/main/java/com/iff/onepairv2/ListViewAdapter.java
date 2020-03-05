package com.iff.onepairv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class ListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;

    //constructor
    public ListViewAdapter(Context context, List<Model> modellist)
    {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
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
            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the result into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        Picasso.get().load(Uri.parse(modellist.get(position).getIcon())).into(holder.mIconIv);

        //listview item clicks, all deals from webscraping put here
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if(modellist.get(position).getTitle().equals("Starbucks")){
                    System.out.println("Sb clicked"); //for debugging
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "Starbucks");
                    intent.putExtra("contentTv", "Starb details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Long John Silvers")){
                    System.out.println("LJ clicked"); //for debugging
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "LongJohn");
                    intent.putExtra("contentTv", "LJ details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Macdonalds")){
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "Macdonalds");
                    intent.putExtra("contentTv", "Mac details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("GongCha")){
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "GongCha");
                    intent.putExtra("contentTv", "Gongcha details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Liho")){
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "Liho");
                    intent.putExtra("contentTv", "Liho details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("CoffeeBean")){
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "CoffeeBean");
                    intent.putExtra("contentTv", "CoffeeBean details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Jack's Place")){
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "Jack's Place");
                    intent.putExtra("contentTv", "Jack's Place details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Cathay Cineplex")) {
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "Cathay Cineplex");
                    intent.putExtra("contentTv", "Cathay details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Golden Village")) {
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "GV Cineplex");
                    intent.putExtra("contentTv", "GV details");
                    mContext.startActivity(intent);
                }
                if(modellist.get(position).getTitle().equals("Universal Studios")) {
                    //start newActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, SelectedDealPage.class);
                    intent.putExtra("actionBarTitle", "USS");
                    intent.putExtra("contentTv", "USS details");
                    mContext.startActivity(intent);
                }


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
            for(Model model : arrayList) {
                if(model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}

