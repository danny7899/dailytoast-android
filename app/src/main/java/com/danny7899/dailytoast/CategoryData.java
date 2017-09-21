package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/27/15.
 */

import java.io.Serializable;

public class CategoryData implements Serializable {

    private Integer mCatID;
    private String mCatName;
    private Integer mCatCount;

    CategoryData(Integer catID, String catName, Integer catCount) {
        mCatID = catID;
        mCatName = catName;
        mCatCount = catCount;
    }

    //ID
    public String getmCatID() {
        return mCatID.toString();
    }

    //Tile
    public String getmCatName() {
        return mCatName;
    }

    //Author
    public String getmCatCountStr() {
        return mCatCount.toString();
    }
    public Integer getmCatCountInt() {
        return mCatCount;
    }

}