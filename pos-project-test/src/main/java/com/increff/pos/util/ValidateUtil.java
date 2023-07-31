package com.increff.pos.util;

import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ValidateUtil {
    // for db validations
    public static <T> Set<ConstraintViolation<T>> validate(T form) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(form);
    }
    public static <T> void checkValid(T obj) throws ApiException {
        Set<ConstraintViolation<T>> violations = validate(obj);
        if (violations.isEmpty()) {
            return;
        }
        List<String> errorList = new ArrayList<String>(violations.size());
        for (ConstraintViolation<T> violation : violations) {
            errorList.add(violation.getMessage());
            throw new ApiException(violation.getMessage());
        }
    }
}
