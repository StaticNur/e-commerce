package com.staticnur.services;

import com.staticnur.models.Attribute;

import java.util.List;

public interface DataBatchProcessor {
    void processBatch(String fileName, List<Attribute> dataList);

    void processRemaining(String fileName, List<Attribute> dataList);
}
