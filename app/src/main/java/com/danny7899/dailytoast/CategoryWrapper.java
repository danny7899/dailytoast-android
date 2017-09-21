package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/27/15.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryWrapper implements Serializable {

    private ArrayList<CategoryData> dataObjects;

    public CategoryWrapper(ArrayList<CategoryData> data) {
        this.dataObjects = data;
    }
    public ArrayList<CategoryData> getCategoryData() {
        return this.dataObjects;
    }

}