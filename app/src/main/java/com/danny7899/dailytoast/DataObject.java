package com.danny7899.dailytoast;

import java.io.Serializable;

/**
 * Created by danny7899 on 12/18/15.
 */
public class DataObject implements Serializable {

    private Integer mPostID;
    private String mTitle;
    private String mAuthor;
    private String mTime;
    private String mPostDesc;
    private String mCategory;
    private Integer mViewsInt;
    private String mViewsDesc;
    private Integer mCommentsInt;
    private String mCommentsDesc;
    private Integer mRatingInt;

    DataObject(Integer postID, String title, String author, String time, String postDesc, String category, Integer viewsInt, String viewsDesc, Integer commentsInt, String commentsDesc, Integer ratingInt) {
        mPostID = postID;
        mTitle = title;
        mAuthor = author;
        mTime = time;
        mPostDesc = postDesc;
        mCategory = category;
        mViewsInt = viewsInt;
        mViewsDesc = viewsDesc;
        mCommentsInt = commentsInt;
        mCommentsDesc = commentsDesc;
        mRatingInt = ratingInt;
    }

    //ID
    public String getmID() {
        return mPostID.toString();
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

    //Category
    public String getmCategory() {
        return mCategory;
    }
    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    //Views
    public String getmViewsStr() {
        return mViewsInt.toString();
    }
    public Integer getmViewsInt() {
        return mViewsInt;
    }
    public String getmViewsDesc() {
        return mViewsDesc;
    }
    public void setmViewsInt(Integer mViewsInt) {
        this.mViewsInt = mViewsInt;
    }
    public void setmViewsDesc(String mViewsDesc) {
        this.mViewsDesc = mViewsDesc;
    }

    //Comments
    public String getmCommentsStr() {
        return mCommentsInt.toString();
    }
    public Integer getmCommentsInt() {
        return mCommentsInt;
    }
    public String getmCommentsDesc() {
        return mCommentsDesc;
    }
    public void setmCommentsInt(Integer mCommentsInt) {
        this.mCommentsInt = mCommentsInt;
    }
    public void setmCommentsDesc(String mCommentsDesc) {
        this.mCommentsDesc = mCommentsDesc;
    }

    //Rating
    public Integer getmRatingInt() {
        return mRatingInt;
    }

}