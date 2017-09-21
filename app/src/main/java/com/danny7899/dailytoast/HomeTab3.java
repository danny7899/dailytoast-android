package com.danny7899.dailytoast;
/**
 * Created by danny7899 on 12/19/15.
 *
 * TRENDING TAB
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

public class HomeTab3 extends Fragment {

    private String postID;
    private String postTitle;
    private String postAuthor;
    private String postTime;
    private String postDesc;
    private String postCategory;
    private String postViewsInt;
    private String postViewsDesc;
    private String postCommentsInt;
    private String postCommentsDesc;

    MainActivity activityMain = new MainActivity();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataObject> dataSet = new ArrayList<DataObject>(activityMain.dataSetTrending);

    private static String LOG_TAG = "CardViewActivity on Trending Tab";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3, container, false);

        //Sort based on Comment count
        Collections.sort(dataSet, new SortComment());

        //Setup RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.cardListTab3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        //Click Listener
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {return true;}
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                Integer position;

                if ((child != null) && mGestureDetector.onTouchEvent(motionEvent)) {
                    position = recyclerView.getChildPosition(child);
                    onItemClick(position);

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return v;
    }

    public void onItemClick(Integer position) {
        postID = (dataSet.get(position)).getmID();
        postTitle = (dataSet.get(position)).getmTitle();
        postAuthor = (dataSet.get(position)).getmAuthor();
        postTime = (dataSet.get(position)).getmTime();
        postDesc = (dataSet.get(position)).getmPostDesc();
        postCategory = (dataSet.get(position)).getmCategory();
        postViewsInt = (dataSet.get(position)).getmViewsInt().toString();
        postViewsDesc= (dataSet.get(position)).getmViewsDesc();
        postCommentsInt = (dataSet.get(position)).getmCommentsInt().toString();
        postCommentsDesc = (dataSet.get(position)).getmCommentsDesc();



        Intent in = new Intent(getActivity(), PostActivity.class);

        // sending pid to next activity
        in.putExtra("postID", postID);
        in.putExtra("postTitle", postTitle);
        in.putExtra("postAuthor", postAuthor);
        in.putExtra("postTime", postTime);
        in.putExtra("postDesc", postDesc);
        in.putExtra("postCategory", postCategory);
        in.putExtra("postViewsInt", postViewsInt);
        in.putExtra("postViewsDesc", postViewsDesc);
        in.putExtra("postCommentsInt", postCommentsInt);
        in.putExtra("postCommentsDesc", postCommentsDesc);

        // starting new activity and expecting some response back
        startActivity(in);


        Log.i(LOG_TAG, " Clicked on Item with ID "+ postID + " on Position " + position + " at Recent Tab");
    }
}