package com.staticnur.utils;


import com.staticnur.models.RegionsDTO;
import com.staticnur.services.ParserRequestBody;
import com.staticnur.services.impl.ParserRequestBodyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ValidateFormat implements Validator {
    private final ParserRequestBody parserRequestBody;

    @Autowired
    public ValidateFormat(ParserRequestBodyImpl parserRequestBody) {
        this.parserRequestBody = parserRequestBody;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegionsDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegionsDTO inputString = (RegionsDTO) o;
        if (inputString.getRegions() == null){
            errors.rejectValue("regions","","Ошибка: Регион не должен быть пустым.");
        }else {
            try {
                List<Integer> elements = parserRequestBody.parseRequestBody(inputString.getRegions());
                for (Integer num : elements) {
                    if (num < 1 || num > 99) {
                        errors.rejectValue("regions","","Ошибка: Регион должен быть в диапазоне от 1 по 99");
                    }
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("regions","","Ошибка: Неверный формат числа");
            }
        }
        if (errors.hasErrors()){
            errors.rejectValue("regions","","Format examples: 13 или 13,14,77 или 1-3,6-8,98 или 1-99");
        }
    }
}
