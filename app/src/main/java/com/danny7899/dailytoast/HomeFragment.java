package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/21/15.
 */
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static android.support.design.widget.CoordinatorLayout.*;

public class HomeFragment extends Fragment {

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private static SlidingTabLayout tabs;
    public static LinearLayout mTabLayoutContainer;

    public SessionManager session;
    private SQLiteHandler db;
    MainActivity activityMain = new MainActivity();

    private CharSequence Titles[] = {"Recent", "Featured", "Trending"};
    int Numboftabs = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles for the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) v.findViewById(R.id.tabs);
        mTabLayoutContainer = (AppBarLayout) v.findViewById(R.id.appBarLayout);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        session = new SessionManager(getActivity());
        db = new SQLiteHandler(getActivity());

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        if (session.isLoggedIn() == true) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(null);
            fab.setLayoutParams(p);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                intent.putExtra("categorySet", new CategoryWrapper(activityMain.categorySet));
                startActivity(intent);
            }
        });

        return v;
    }


    public static void moveBar(Integer distance) {
        mTabLayoutContainer.setTranslationY(-distance);
    }
}