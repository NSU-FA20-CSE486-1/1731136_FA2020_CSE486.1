package com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners;

/**
 * single data item changed in database listener
 * @param <DataType> type of data being listened for
 */
public interface SingleDataChangeListener<DataType> {

    void onDataChange(DataType data);
}
