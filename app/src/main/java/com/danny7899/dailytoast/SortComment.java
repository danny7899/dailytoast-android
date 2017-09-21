package com.danny7899.dailytoast;

import java.util.Comparator;

/**
 * Created by danny7899 on 12/22/15.
 */
public class SortComment implements Comparator<DataObject> {

    @Override
    public int compare(DataObject data1, DataObject data2) {

        /**FIX THIS**/
        if(data1.getmCommentsInt() < data2.getmCommentsInt()){
            return 1;
        } else {
            return -1;
        }
    }

}