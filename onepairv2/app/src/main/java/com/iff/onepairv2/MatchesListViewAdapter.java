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

public class MatchesListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<ChatUser> modellist;
    ArrayList<ChatUser> arrayList;

    //constructor
    public MatchesListViewAdapter(Context context, List<ChatUser> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<ChatUser>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder {
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
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            //holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the result into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        //holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        Picasso.get().load(Uri.parse(modellist.get(position).getIcon())).into(holder.mIconIv);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                System.out.println("ONCLICK CHAT");
                //start newActivity with title for actionbar and text for textview
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", modellist.get(position).getDesc());
                intent.putExtra("user_name", modellist.get(position).getTitle());
                intent.putExtra("user_image", modellist.get(position).getIcon());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    //filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length() == 0) {
            modellist.addAll(arrayList);
        } else {
            for (ChatUser model : arrayList) {
                if (model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}

