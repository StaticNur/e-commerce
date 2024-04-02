package com.staticnur.services.impl;

import com.staticnur.models.Attribute;
import com.staticnur.repositories.DataHandler;
import com.staticnur.services.ParserXML;
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
    private final DataHandler dataHandler;

    @Autowired
    public ParserXMLImpl(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
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
                        if("".equals(name)){
                            name = parser.getLocalName();
                        }
                        currentData = new Attribute();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            currentData.addAttribute(parser.getAttributeLocalName(i), parser.getAttributeValue(i));// [i] = parser.getAttributeValue(i);
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (currentData != null) {
                            //currentData.setValue(parser.getText().trim());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (currentData != null){
                            String isActive = currentData.getAttributesByKey("ISACTIVE");
                            String isActual = currentData.getAttributesByKey("ISACTUAL");
                            if (("true".equals(isActive) || "1".equals(isActive)) && ("".equals(isActual) || "1".equals(isActual))) {
                                dataList.add(currentData);
                                currentData = null;
                            }
                        }
                        break;
                }
                // TODO ВЫНЕСТИ В ОТДЕЛЬНЫЙ СЕРВИС
                if(dataList.size() > 100){
                    dataHandler.assignMethodToData(fileName, dataList);
                    dataList.clear();
                }
            }
            if (!dataList.isEmpty()){
                dataHandler.assignMethodToData(fileName, dataList);
            }
            System.out.println("Ready file: "+name);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}