package com.staticnur.services.impl;

import com.staticnur.models.Attribute;
import com.staticnur.services.DataBatchProcessor;
import com.staticnur.services.ParserXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserXMLImpl implements ParserXML {
    private static final Logger log = LoggerFactory.getLogger(ParserXMLImpl.class);

    private final DataBatchProcessor dataBatchProcessor;

    @Autowired
    public ParserXMLImpl(DataBatchProcessor dataBatchProcessor) {
        this.dataBatchProcessor = dataBatchProcessor;
    }

    public void parseXML(String fileName, InputStream inputStream) {
        List<Attribute> dataList = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = null;
        try {
            parser = factory.createXMLStreamReader(inputStream);
            Attribute currentData = null;

            String name = "";
            while (parser.hasNext()) {
                int event = parser.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if(name.isEmpty()){
                            name = parser.getLocalName();
                        }
                        currentData = new Attribute();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            currentData.addAttribute(parser.getAttributeLocalName(i), parser.getAttributeValue(i));
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (currentData != null){
                            String isActive = currentData.getAttributesByKey("ISACTIVE");
                            String isActual = currentData.getAttributesByKey("ISACTUAL");
                            if ((isActive.equals("true") || isActive.equals("1"))
                                && (isActual.isEmpty() || isActual.equals("1"))) {
                                dataList.add(currentData);
                                currentData = null;
                            }
                        }
                        break;
                }
                dataBatchProcessor.processBatch(fileName, dataList);
            }
            dataBatchProcessor.processRemaining(fileName, dataList);
        } catch (XMLStreamException e) {
            log.error("Ошибка при парсинге xml файла", e);
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (XMLStreamException e) {
                    log.error("Ошибка при закрытии потока xml файла", e);
                }
            }
        }
    }
}