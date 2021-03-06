package com.example.gym_platform;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

public class TrainerActivity extends AppCompatActivity {

    List<Drawable> temp;
    RecyclerView mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);


        temp = new ArrayList<>();
        temp.add(ContextCompat.getDrawable(this, R.drawable.default_dot));
        temp.add(ContextCompat.getDrawable(this, R.drawable.default_dot));
        temp.add(ContextCompat.getDrawable(this, R.drawable.default_dot));

        TrainerActivity.Adapter a = new TrainerActivity.Adapter(temp, this);

        ViewPager pager = findViewById(R.id.viewpager1);
        pager.setAdapter(a);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);


        ArrayList<Integer> listImage1 = new ArrayList<>();
        listImage1.add(R.drawable.aaa);
        listImage1.add(R.drawable.bbb);
        listImage1.add(R.drawable.ccc);

        ViewPager viewPager1 = findViewById(R.id.viewpager1);
        TrainerActivity.FragmentAdapter fragmentAdapter1 = new TrainerActivity.FragmentAdapter(getSupportFragmentManager());

        viewPager1.setAdapter(fragmentAdapter1);
        for (int i = 0; i < listImage1.size(); i++) {
            ImageFragment1 imageFragment = new ImageFragment1();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", listImage1.get(i));
            imageFragment.setArguments(bundle);
            fragmentAdapter1.addItem(imageFragment);
        }
        fragmentAdapter1.notifyDataSetChanged();


        TabHost tab_host = (TabHost) findViewById(R.id.tabhost);
        tab_host.setup();

        TabHost.TabSpec ts1 = tab_host.newTabSpec("tab1");
        ts1.setIndicator("??????");
        ts1.setContent(R.id.tab1);
        tab_host.addTab(ts1);

        TabHost.TabSpec ts2 = tab_host.newTabSpec("tab2");
        ts2.setIndicator("????????????");
        ts2.setContent(R.id.tab2);
        tab_host.addTab(ts2);

        TabHost.TabSpec ts3 = tab_host.newTabSpec("tab3");
        ts3.setIndicator("??????");
        ts3.setContent(R.id.tab3);
        tab_host.addTab(ts3);

        tab_host.setCurrentTab(0);

        mRecyclerView2 = findViewById(R.id.membership_recycler_view);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager2).setOrientation(LinearLayoutManager.HORIZONTAL);//????????? ???????????????
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        ArrayList<membershipItem> membershipItemArrayList = new ArrayList<>();
        membershipItemArrayList.add(new membershipItem("1?????? - 7 pass", "1?????? / 7 pass", "34,300"));
        membershipItemArrayList.add(new membershipItem("3?????? - 20 pass", "3?????? / 20 pass", "67,300"));
        membershipItemArrayList.add(new membershipItem("6?????? - 40 pass", "6?????? / 40 pass", "95,300"));
        membershipItemArrayList.add(new membershipItem("9?????? - 60 pass", "9?????? / 60 pass", "120,000"));
        membershipItemArrayList.add(new membershipItem("12?????? - 80 pass", "12?????? / 80 pass", "150,000"));

        membershipAdapter membershipAdapter = new membershipAdapter (membershipItemArrayList);

        mRecyclerView2.setAdapter(membershipAdapter);


        Button writereview_t = (Button)findViewById(R.id.writereview_t);
        writereview_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerActivity.this, T_reviewActivity.class);
                startActivity(intent);
            }
        });

        //////////CHAT//////////////////////////////
        Button chat_button = (Button) findViewById(R.id.btnMessage);
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        ///////////////////////////////////////////
    }



    class FragmentAdapter extends FragmentPagerAdapter {

        // ViewPager??? ????????? Fragment?????? ?????? ?????????
        private ArrayList<Fragment> fragments = new ArrayList<>();

        // ?????? ?????????
        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        // List??? Fragment??? ?????? ??????
        void addItem(Fragment fragment) {
            fragments.add(fragment);
        }
    }


    class Adapter extends PagerAdapter {

        Context context;
        List<Drawable> obj;

        Adapter(List<Drawable> res, Context context) {
            obj = res;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            View view = null;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.pager_adapter, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageDrawable(obj.get(position));
            container.addView(view);

            return view;

        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return false;
        }


        public int getCount() {
            return obj.size();
        }

    }
}
