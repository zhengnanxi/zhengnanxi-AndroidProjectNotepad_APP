package com.example.test3.ui.home;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.R;


import java.util.ArrayList;
import java.util.List;



public class CarAdapter extends RecyclerView.Adapter<CarAdapter.myViewHodler> implements Filterable {
    private List<CardMessage> mCardMessageList;     //数据源--原始
    private List<CardMessage> filterCardMessageList;    //过滤后的数据源
    private Context context;        //上下文
    public Bitmap bitmap;


    public CarAdapter(Context context, List<CardMessage> mCardMessageList) {
        this.context = context;
        this.mCardMessageList = mCardMessageList;
        this.filterCardMessageList = mCardMessageList;
    }
    public CarAdapter(Context context, List<CardMessage> mCardMessageList,Bitmap bitmap){
        this.context = context;
        this.mCardMessageList = mCardMessageList;
        this.filterCardMessageList = mCardMessageList;
        this.bitmap = bitmap;
    }



    static class myViewHodler extends RecyclerView.ViewHolder {
        public View cardview;
        public ImageView themeIamge;
        public TextView title;
        public TextView text;
        public Bitmap bitmap;
        public myViewHodler(final View itemView) {
            //监听子控件
            super(itemView);
            cardview = itemView;
            themeIamge = (ImageView) itemView.findViewById(R.id.image_theme);
            title = (TextView) itemView.findViewById(R.id.article_title);
            text = (TextView) itemView.findViewById(R.id.article_text);
        }
    }


    @Override
    public myViewHodler onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = View.inflate(context,R.layout.shr_product_card,null);
        /*
        final myViewHodler my = new myViewHodler(itemView);
        my.themeIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = my.getAdapterPosition();
                monImageClickListener.OnImageClick(my.itemView,position);
            }
        });
        */
        return new myViewHodler(itemView);

    }



    @Override
    public void onBindViewHolder(final myViewHodler holder, final int position) {
        /*
        //适配器设置每个Item----展示数据
        StringAndBitmap stringAndBitmap = new StringAndBitmap();
        CardMessage data = mCardMessageList.get(position);
        String imageid = data.image_theme_id;
        //bitmap = (Bitmap) stringAndBitmap.stringToBitmap(imageid);
        holder.title.setText(data.article_title);
        holder.text.setText(data.article_text);
        holder.themeIamge.setImageBitmap(BitmapFactory.decodeFile(imageid));


         */
        //适配器设置每个Item----展示数据
        StringAndBitmap stringAndBitmap = new StringAndBitmap();
        CardMessage data = filterCardMessageList.get(position);
        String imageid = data.user_Picture_1;
        //bitmap = (Bitmap) stringAndBitmap.stringToBitmap(imageid);
        holder.title.setText(data.article_title);
        holder.text.setText(data.article_text);
        holder.themeIamge.setImageBitmap(BitmapFactory.decodeFile(imageid));

        //单点Item监听
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView, position);

                }
            });
        }
        //长按Item监听
        if (mOnItemClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.OnItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return filterCardMessageList.size();
    }

    //item点击事件实现---有bug弃用
    public interface OnItemClickListener {
        void OnItemClick (View view, int position);
        void OnItemLongClick (View view, int position);
    }
    //Item子控件事件----有bug弃用
    public interface OnImageClickListener {
        void OnImageClick (View view, int position);
    }



    public CarAdapter.OnItemClickListener mOnItemClickListener;
    public CarAdapter.OnImageClickListener monImageClickListener;
    public CarAdapter.SetPicture setPicture;

    public void setOnItemClickListener(CarAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public void setOnImageClickListener(CarAdapter.OnImageClickListener monImageClickListener){
        this.monImageClickListener = monImageClickListener;
    }

    public void setImagePicture(CarAdapter.SetPicture setPicture){
        this.setPicture = setPicture;
    }

    public interface SetPicture {
        void setimage(Bitmap bitmap);
    }

    //过滤器设置
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty())
                {
                    //没有过滤的内容，则使用源数据
                    filterCardMessageList = mCardMessageList;
                }else
                {
                    List<CardMessage> filterlist = new ArrayList<>();
                    for (CardMessage str : mCardMessageList)
                    {
                        if (str.getArticle_title().contains(charString))
                        {
                            filterlist.add(str);
                        }
                    }

                    filterCardMessageList = filterlist;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterCardMessageList;

                return filterResults;
            }

            //把过滤后的值返回出来----设置了也没用---
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filterCardMessageList = (ArrayList<CardMessage>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
