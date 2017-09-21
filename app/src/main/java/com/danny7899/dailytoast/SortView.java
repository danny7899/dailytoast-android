package com.danny7899.dailytoast;

import java.util.Comparator;

/**
 * Created by danny7899 on 12/22/15.
 */
public class SortView implements Comparator<DataObject> {

    @Override
    public int compare(DataObject data1, DataObject data2) {

        if(data1.getmViewsInt() < data2.getmViewsInt()){
            return 1;
        } else {
            return -1;
        }
    }

}