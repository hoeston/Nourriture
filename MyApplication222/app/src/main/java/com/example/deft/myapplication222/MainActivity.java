package com.example.deft.myapplication222;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();

    private LinearLayout mTableUploadDish;
    private LinearLayout mTableLastestDish;
    private LinearLayout mTableNearby;

    private ImageButton mUploadDishImg;
    private ImageButton mLastestDishImg;
    private ImageButton mNearbyImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

        com.example.deft.myapplication222.Topbar topbar=(com.example.deft.myapplication222.Topbar)findViewById(R.id.topbar);
        topbar.setOnTopbarClickListener(new com.example.deft.myapplication222.Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(MainActivity.this, "display the types page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(MainActivity.this,"display the query page",Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void initEvent() {
        mTableUploadDish.setOnClickListener(this);
        mTableLastestDish.setOnClickListener(this);
        mTableNearby.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int currentItem = mViewPager.getCurrentItem();
                resetImg();
                switch (currentItem){
                    case 0:
                        mUploadDishImg.setImageResource(R.drawable.menu_icon_0_pressed);
                        break;
                    case 1:
                        mLastestDishImg.setImageResource(R.drawable.menu_icon_1_pressed);
                        break;
                    case 2:
                        mNearbyImg.setImageResource(R.drawable.menu_icon_3_pressed);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initView() {
        mViewPager=(ViewPager) findViewById(R.id.id_viewpager);

        mTableUploadDish=(LinearLayout)findViewById(R.id.id_ud);
        mTableLastestDish=(LinearLayout)findViewById(R.id.id_ld);
        mTableNearby=(LinearLayout)findViewById(R.id.id_nd);

        mUploadDishImg=(ImageButton)findViewById(R.id.id_ud_img);
        mLastestDishImg=(ImageButton)findViewById(R.id.id_ld_img);
        mNearbyImg=(ImageButton)findViewById(R.id.id_nd_img);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View ud = mInflater.inflate(R.layout.ud,null);
        View ld = mInflater.inflate(R.layout.ld,null);
        View nd = mInflater.inflate(R.layout.nd,null);
        mViews.add(ld);
        mViews.add(ud);
        mViews.add(nd);

        mAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
        };


        mViewPager.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        resetImg();

        switch (v.getId()){
            case R.id.id_ud:
                mViewPager.setCurrentItem(0);
                mUploadDishImg.setImageResource(R.drawable.menu_icon_0_pressed);
                break;
            case R.id.id_ld:
                mViewPager.setCurrentItem(1);
                mLastestDishImg.setImageResource(R.drawable.menu_icon_1_pressed);
                break;
            case R.id.id_nd:
                mViewPager.setCurrentItem(2);
                mNearbyImg.setImageResource(R.drawable.menu_icon_3_pressed);
                break;

            default:
                break;

        }
    }

    private void resetImg(){
        mUploadDishImg.setImageResource(R.drawable.menu_icon_0_normal);
        mLastestDishImg.setImageResource(R.drawable.menu_icon_1_normal);
        mNearbyImg.setImageResource(R.drawable.menu_icon_3_normal);
    }
}
