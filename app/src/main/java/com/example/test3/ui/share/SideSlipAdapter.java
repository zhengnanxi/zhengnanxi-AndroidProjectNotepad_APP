package com.example.test3.ui.share;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.R;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;
import java.util.Map;

public class SideSlipAdapter extends RecyclerView.Adapter<SideSlipAdapter.myViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Map.Entry<String, String>> cardMessagesList; //数据源

    public SideSlipAdapter(Context context, List<Map.Entry<String,String>> cardMessagesList)
    {
        this.inflater = LayoutInflater.from(context);
        this.cardMessagesList = cardMessagesList;
        this.context = context;
    }

    //监控item控件用
    static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView cloud_title;   //标题
        TextView cloud_message; //内容
        Button btn_delete;  //删除按钮
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cloud_title = itemView.findViewById(R.id.content_title);
            cloud_message = itemView.findViewById(R.id.content_message);
            btn_delete = itemView.findViewById(R.id.btnDelete);
        }
    }


    @Override
    public myViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemview = View.inflate(context, R.layout.delete_list_item_layout, null);
        return new myViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {

        View ssss = null;
        ssss = View.inflate(context, R.layout.delete_list_item_layout, null);

        //适配器设置每个Item----展示数据
        holder.cloud_title.setText("标题: " + cardMessagesList.get(position).getKey());
        holder.cloud_message.setText("内容: " + cardMessagesList.get(position).getValue());

        //删除按钮监听
        if (deleItemListener != null)
        {
            View finalSsss = ssss;
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SwipeMenuLayout)(finalSsss)).quickClose();
                    int position = holder.getAdapterPosition();
                    deleItemListener.delete(holder.itemView, position);

                }
            });
        }




    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        View closeView = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.delete_list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.cloud_title = convertView.findViewById(R.id.content_title);
            holder.cloud_message = convertView.findViewById(R.id.content_message);
            holder.btn_delete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        }

        if (closeView == null){
            closeView = convertView;
        }

        final View finalCloseView = closeView;
        holder = (ViewHolder) closeView.getTag();
        holder.cloud_title.setText("标题: " + cardMessagesList.get(position).getKey());
        holder.cloud_message.setText("内容: " + cardMessagesList.get(position).getValue());

        //删除按钮监听
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout)(finalCloseView)).quickClose();
                Log.e("错误","不知道会不会出现");
                delItemListener.delete(position);
                notifyDataSetChanged();

            }
        });

        return convertView;
    }
     */





    @Override
    public int getItemCount() {
        return cardMessagesList.size();
    }




    public interface OnItemClickListener
    {
        void delete(View view, int position);
    }

    public SideSlipAdapter.OnItemClickListener deleItemListener;

    //设置监听器
    public void setDelItemListener(OnItemClickListener deleItemListener)
    {
        this.deleItemListener = deleItemListener;
    }

}
