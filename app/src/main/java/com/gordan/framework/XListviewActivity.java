package com.gordan.framework;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gordan.framework.adapter.XListviewAdapter;
import com.gordan.framework.db.dao.NewsDao;
import com.gordan.framework.db.model.TB_News;
import com.gordan.framework.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class XListviewActivity extends BaseActivity implements XListView.IXListViewListener
{
    final String[] cols={"id","title","content","createtime","category"};

    private XListView myXListView;

    private List<TB_News> arrayList=new ArrayList<TB_News>();

    private int pageNo=1;

    private NewsDao dao=null;

    XListviewAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlistview);
        myXListView=(XListView)this.findViewById(R.id.xlv_news);

        dao=NewsDao.getInstance(this);

        arrayList= dao.pageListNews(cols, null, null, " createtime desc",pageNo,10);

        Log.i("XPZ","当前查询的结果:"+arrayList.size());

        int resId=R.layout.item_xlistview;
        adapter=new XListviewAdapter(this,arrayList,resId);
        myXListView.setAdapter(adapter);
        myXListView.setPullLoadEnable(true);
        myXListView.setPullRefreshEnable(false);
        myXListView.setXListViewListener(this);









    }


    @Override
    public void onRefresh()
    {
        adapter.setAdapterData(arrayList);
        myXListView.stopRefresh();
        myXListView.setPullRefreshEnable(false);
        myXListView.setPullLoadEnable(true);

    }





    @Override
    public void onLoadMore()
    {
        pageNo++;
        //注意加载更多时 是将新查询的数据追加到原数组中，不是替换！
        arrayList.addAll(dao.pageListNews(cols, null, null, " createtime desc",pageNo,10));
        adapter.setAdapterData(arrayList);
        myXListView.stopLoadMore();
        myXListView.setPullLoadEnable(false);
        myXListView.setPullRefreshEnable(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xlistview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
