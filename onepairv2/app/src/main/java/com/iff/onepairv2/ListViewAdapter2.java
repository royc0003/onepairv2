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

public class ListViewAdapter2 extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<ChatUser> modellist;
    ArrayList<ChatUser> arrayList;

    //constructor
    public ListViewAdapter2(Context context, List<ChatUser> modellist) {
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
            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the result into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        Picasso.get().load(Uri.parse(modellist.get(position).getIcon())).into(holder.mIconIv);

        //holder.mIconIv.setImageResource(modellist.get(position).getIcon());

        //listview item clicks, all deals from webscraping put here


        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                //start newActivity with title for actionbar and text for textview
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("Deal", modellist.get(position));
                if((Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mContext.startActivity(intent);
            }
        });*/

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
       /* view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListViewAdapter2.this, ""+ model.getName(), Toast.LENGTH_SHORT).show();
                CharSequence initiate_options[] = new CharSequence[]{"Send Message"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(initiate_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Intent chatIntent = new Intent(mContext, ChatActivity.class);
                            chatIntent.putExtra("user_id", model.getUid());
                            chatIntent.putExtra("user_name", model.getName());
                            chatIntent.putExtra("user_image", model.getImage());
                            startActivity(chatIntent);
                        }
                    }
                });
                builder.show();
            }
        });*/
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

