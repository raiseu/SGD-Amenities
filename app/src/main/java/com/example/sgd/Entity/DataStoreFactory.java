package com.example.sgd.Entity;

public class DataStoreFactory {
    public static DataStoreInterface getDatastore(String datastoreOption) {
        DataStoreInterface ds = null;
        if(datastoreOption.equals("carpark")){
            ds = new Carpark();
        }
        else{
            ds = new Amenities();
        }

        return ds;
    }

}
