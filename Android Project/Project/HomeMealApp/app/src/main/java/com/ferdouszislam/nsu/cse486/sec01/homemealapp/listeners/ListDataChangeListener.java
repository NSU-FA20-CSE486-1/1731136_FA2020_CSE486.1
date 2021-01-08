package com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners;

/**
 * list of data item changed in database listener
 * @param <DataType> type of data being listened for
 */
public interface ListDataChangeListener<DataType> {

    void onDataAdded(DataType data);
    void onDataUpdated(DataType data);
    void onDataRemoved(DataType data);
}
