package com.staticnur.services.impl;

import com.staticnur.services.ParserRequestBody;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParserRequestBodyImpl implements ParserRequestBody {
    public List<Integer> parseRequestBody(String regionsString) {
        List<Integer> regions = new ArrayList<>();
        String[] parts = regionsString.split(",");
        for (String part : parts) {
            if (part.contains("-")) {
                String[] rangeParts = part.split("-");
                int start = Integer.parseInt(rangeParts[0]);
                int end = Integer.parseInt(rangeParts[1]);
                for (int i = start; i <= end; i++) {
                    regions.add(i);
                }
            } else {
                int num = Integer.parseInt(part);
                regions.add(num);
            }
        }
        return regions;
    }
}
