package com.staticnur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class FileService {
    private final ParserXML parserXML;
    private final ParserRequestBody parserRequestBody;

    @Autowired
    public FileService(ParserXML parserXML, ParserRequestBody parserRequestBody) {
        this.parserXML = parserXML;
        this.parserRequestBody = parserRequestBody;
    }

    public void readArchiveFile(String regionsStr) {
        List<Integer> regions = parserRequestBody.parseRequestBody(regionsStr);
        Pattern pattern = Pattern.compile("(?:\\d{1,2}/)?([A-Za-z_]+(?:_TYPES)?)_[0-9]+_[0-9a-fA-F\\-]+\\.XML");
        //Pattern pattern = Pattern.compile("(?:\\d{1,2}/)?((AS_ADDR_OBJ|AS_APARTMENT|AS_APARTMENTS|AS_ADM_HIERARCHY|AS_HOUSE|AS_HOUSES|AS_ROOM|AS_ROOMS)(?:_TYPES)?)_?[0-9]+_[0-9a-fA-F\\-]+\\.XML");
        try {
            String zipFilePath = "data/gar_xml.zip";
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries;

            // fiXME: ПЕРЕДЕЛАТЬ
            entries = zipFile.entries();
            for (int i = 0; i < regions.size(); i++) {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    Matcher matcher = pattern.matcher(entryName);

                    if (matcher.find()) {
                        String name = matcher.group(1);
                        String suffix = regions.get(i) < 10 ? "0"+regions.get(i) : String.valueOf(regions.get(i));
                        if(name.endsWith("_TYPES") || entryName.startsWith(suffix)){
                            ZipEntry xmlFileEntry = zipFile.getEntry(entryName);

                            try (InputStream inputStream = zipFile.getInputStream(xmlFileEntry)){
                                parserXML.parseXML(name, inputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
String entryName = entry.getName();
                    Matcher matcher = pattern.matcher(entryName);
                    if(entry.isDirectory() && !regionAlreadyRead.contains(regions.get(i))){
                        regionAlreadyRead.add(regions.get(i));
                        ZipEntry xmlFileEntry = zipFile.getEntry(entryName);

                        try (InputStream inputStream = zipFile.getInputStream(xmlFileEntry)){
                            parserXML.parseXML(matcher.group(1), inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
