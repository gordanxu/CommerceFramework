package com.gordan.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gordan.framework.R;
import com.gordan.framework.db.model.TB_News;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Gordan on 2015/10/26.
 */
public class XListviewAdapter extends BaseAdapter
{
    private Context mContext;

    private List<TB_News> arrayList;

    private int resId;

    private SimpleDateFormat sdf=null;

    public XListviewAdapter(Context mContext,List<TB_News> arrays,int resId)
    {
        this.mContext=mContext;
        this.arrayList=arrays;
        this.resId=resId;

        sdf=new SimpleDateFormat("MM-dd HH:mm:ss");
    }

    public void setAdapterData(List<TB_News> arrays)
    {
        this.arrayList=arrays;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TB_News bean=arrayList.get(position);
        TextView tvTitle=null;
        TextView tvTime=null;
        if(convertView==null)
        {
            convertView=View.inflate(mContext,resId,null);
        }
        tvTitle=(TextView)convertView.findViewById(R.id.xlv_item_tv_title);
        tvTime=(TextView)convertView.findViewById(R.id.xlv_item_tv_time);

        tvTitle.setText(bean.getTitle());
        tvTime.setText(sdf.format(new Date(bean.getCreatetime())));

        return convertView;
    }
}
