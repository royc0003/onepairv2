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
    private Context mContext;
    private LayoutInflater inflater;
    private List<ChatUser> modelList;
    private ArrayList<ChatUser> chatList;

    //constructor
    public MatchesListViewAdapter(Context context, List<ChatUser> modelList) {
        mContext = context;
        this.modelList = modelList;
        inflater = LayoutInflater.from(mContext);
        this.chatList = new ArrayList<ChatUser>();
        this.chatList.addAll(modelList);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
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
        holder.mTitleTv.setText(modelList.get(position).getTitle());
        //holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        Picasso.get().load(Uri.parse(modelList.get(position).getIcon())).into(holder.mIconIv);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                System.out.println("ONCLICK CHAT");
                //start newActivity with title for actionbar and text for textview
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", modelList.get(position).getDesc());
                intent.putExtra("user_name", modelList.get(position).getTitle());
                intent.putExtra("user_image", modelList.get(position).getIcon());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    //filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        modelList.clear();
        if (charText.length() == 0) {
            modelList.addAll(chatList);
        } else {
            for (ChatUser model : chatList) {
                if (model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    modelList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }
}

