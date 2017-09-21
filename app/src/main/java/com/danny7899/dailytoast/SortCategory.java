package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/27/15.
 */

import java.util.Comparator;

public class SortCategory implements Comparator<CategoryData> {

    @Override
    public int compare(CategoryData data1, CategoryData data2) {

        if(data1.getmCatCountInt() < data2.getmCatCountInt()){
            return 1;
        } else {
            return -1;
        }
    }

}