package org.paidaxing.recycledemo;

import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.paidaxing.recycledemo.R;
import org.paidaxing.recycledemo.adapters.RecycleViewAdapter;
import org.paidaxing.recycledemo.model.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecycleViewAdapter.OnViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.frag_recommend_3)
    RecyclerView mRecyclerView;
    @BindView(R.id.frag_recommend_3_swipe)
    SwipeRefreshLayout mRefreshLayout;

    private List<Item> mItems;
    private RecycleViewAdapter mAdapter;
    private String url = "http://live.9158.com/Room/GetHotLive_v2?lon=0.0&province=&lat=0.0&page=1&type=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        // 设置下拉刷新的样式
        mRefreshLayout.setOnRefreshListener(this);

        mItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Item item = new Item();
            item.setImageUrl("sdf");
            item.setName("hahahhaha");
            mItems.add(item);
        }
        //  进行联网请求数据
        getDataVolley();
        initRecycleView();
    }

    private void initRecycleView() {
        mAdapter = new RecycleViewAdapter(mItems, this);
//        GridLayoutManager layout = new GridLayoutManager(getContext(), 3);
//        layout.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layout);
//        // 一行上的图片都会在同一水平线的.
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(this);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
    }

    // 首先我们需要创建一个RequestQueue reqQueue，
    // 然后构建一个自己所需要的XXRequest req，之后通过reqQueue.add(req);将请求添加至请求队列；
    private void getDataVolley() {
        mRefreshLayout.setRefreshing(true);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 添加自己的响应逻辑
                mItems.clear();
                JSONArray listArray = response.optJSONObject("data").optJSONArray("list");
                int len = listArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject json = listArray.optJSONObject(i);
                    Item item = Item.createItmfromJson(json);
                    mItems.add(item);
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 错误处理
                Toast.makeText(MainActivity.this , "联网错误", Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void setOnClickListener(View view, int position) {
        // TODO: 点击播放
        Toast.makeText(this, mItems.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getDataVolley();
    }

    /**
     * 实现 每个Item之间的间隔的, 本项目中没有用到
     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
        }
    }
}
