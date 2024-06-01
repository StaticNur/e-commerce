package com.staticnur.services.impl;

import com.staticnur.models.Attribute;
import com.staticnur.repositories.DataHandler;
import com.staticnur.services.DataBatchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataBatchProcessorImpl implements DataBatchProcessor {

    private static final int BATCH_SIZE = 5000;

    private final DataHandler dataHandler;

    @Autowired
    public DataBatchProcessorImpl(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public void processBatch(String fileName, List<Attribute> dataList) {
        if (dataList.size() > BATCH_SIZE) {
            handleBatch(fileName, dataList);
        }
    }

    public void processRemaining(String fileName, List<Attribute> dataList) {
        if (!dataList.isEmpty()) {
            handleBatch(fileName, dataList);
        }
    }

    private void handleBatch(String fileName, List<Attribute> dataList) {
        dataHandler.assignMethodToData(fileName, dataList);
        dataList.clear();
    }
}
