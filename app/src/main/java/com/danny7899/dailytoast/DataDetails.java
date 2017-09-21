package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/24/15.
 */

import java.io.Serializable;

public class DataDetails implements Serializable {

    private String mPostID;
    private String mTitle;
    private String mAuthor;
    private String mTime;
    private String mPostDesc;
    private String mPostCont;
    private String mCategory;
    private String mViewsInt;
    private String mViewsDesc;
    private String mCommentsInt;
    private String mCommentsDesc;

    DataDetails(String postID, String title, String author, String time, String postDesc, String postCont, String category, String viewsInt, String viewsDesc, String commentsInt, String commentsDesc) {
        mPostID = postID;
        mTitle = title;
        mAuthor = author;
        mTime = time;
        mPostDesc = postDesc;
        mPostCont = postCont;
        mCategory = category;
        mViewsInt = viewsInt;
        mViewsDesc = viewsDesc;
        mCommentsInt = commentsInt;
        mCommentsDesc = commentsDesc;
    }

    //ID
    public String getmPostID() {
        return mPostID;
    }

    //Tile
    public String getmTitle() {
        return mTitle;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    //Author
    public String getmAuthor() {
        return mAuthor;
    }
    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    //Time
    public String getmTime() {
        return mTime;
    }
    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    //Description
    public String getmPostDesc() {
        return mPostDesc;
    }
    public void setmPostDesc(String mPostDesc) {
        this.mPostDesc = mPostDesc;
    }

    //Content
    public String getmPostCont() {
        return mPostCont;
    }
    public void setmPostCont(String mPostCont) {
        this.mPostCont = mPostCont;
    }

    //Category
    public String getmCategory() {
        return mCategory;
    }
    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    //Views
    public String getmViewsStr() {
        return mViewsInt;
    }
    public String getmViewsInt() {
        return mViewsInt;
    }
    public String getmViewsDesc() {
        return mViewsDesc;
    }
    public void setmViewsInt(String mViewsInt) {
        this.mViewsInt = mViewsInt;
    }
    public void setmViewsDesc(String mViewsDesc) {
        this.mViewsDesc = mViewsDesc;
    }

    //Comments
    public String getmCommentsStr() {
        return mCommentsInt;
    }
    public String getmCommentsInt() {
        return mCommentsInt;
    }
    public String getmCommentsDesc() {
        return mCommentsDesc;
    }
    public void setmCommentsInt(String mCommentsInt) {
        this.mCommentsInt = mCommentsInt;
    }
    public void setmCommentsDesc(String mCommentsDesc) {
        this.mCommentsDesc = mCommentsDesc;
    }

}