package com.danny7899.dailytoast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by danny7899 on 12/20/15.
 */
public class DataWrapper implements Serializable {

    private ArrayList<DataObject> dataObjects;

    public DataWrapper(ArrayList<DataObject> data) {
        this.dataObjects = data;
    }
    public ArrayList<DataObject> getDataObjects() {
        return this.dataObjects;
    }

}