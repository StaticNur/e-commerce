package com.staticnur.services.impl;

import com.staticnur.services.FileService;
import com.staticnur.services.ParserRequestBody;
import com.staticnur.services.ParserXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final ParserXML parserXML;
    private final ParserRequestBody parserRequestBody;
    private final Set<String> parsed;

    @Autowired
    public FileServiceImpl(ParserXML parserXML, ParserRequestBody parserRequestBody) {
        this.parserXML = parserXML;
        this.parserRequestBody = parserRequestBody;
        this.parsed = new HashSet<>();
    }

    public void readArchiveFile(String regionsStr) {
        List<Integer> regions = parserRequestBody.parseRequestBody(regionsStr);
        Pattern pattern = Pattern.compile("(?:\\d{1,2}/)?([A-Za-z_]+(?:_TYPES)?)_[0-9]+_[0-9a-fA-F\\-]+\\.XML");
        String zipFilePath = "data/gar_xml.zip";

        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            int numberOfRegionsRead = 0;
            for (int region : regions) {
                numberOfRegionsRead++;
                String suffix = region < 10 ? "0" + region : String.valueOf(region);
                processZipEntries(zipFile.entries(), pattern, zipFile, suffix, numberOfRegionsRead);
            }
        } catch (IOException e) {
            log.error("Ошибка при обработке архивного файла", e);
        }
    }

    private void processZipEntries(Enumeration<? extends ZipEntry> entries, Pattern pattern,
                                   ZipFile zipFile, String suffix, int numberOfRegionsRead) throws IOException {
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            Matcher matcher = pattern.matcher(entryName);

            if (matcher.find()) {
                String name = matcher.group(1);
                String[] parts = entryName.split("/");
                if ((numberOfRegionsRead < 2 && name.endsWith("_TYPES")
                     || (suffix.startsWith(parts[0]) && !parsed.contains(entryName)))) {
                    processXmlEntry(zipFile, entryName, name);
                }
            }
        }
    }

    private void processXmlEntry(ZipFile zipFile, String entryName, String name) {
        try (InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(entryName))) {
            parserXML.parseXML(name, inputStream);
            parsed.add(entryName);
            System.out.println("Ready file: "+entryName);
        } catch (IOException e) {
            log.error("Ошибка при парсинге xml файла", e);
        }
    }
}