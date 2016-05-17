package com.bwg.iot.validator;

import com.bwg.iot.model.User;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by triton on 5/16/16.
 */
@Component
public class BeforeCreateUserValidator implements Validator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Override
    public boolean supports(Class<?> clazz) {
        if (clazz.equals(User.class)) { return true; }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastname.required", "lastName is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstname.required", "firstName is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "email is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roles", "roles.required", "a user must have at least 1 role");

        User user = (User) target;
        if (user.getRoles() != null) {
            for (String role : user.getRoles()) {
                if (!EnumUtils.isValidEnum(User.Role.class, role)) {
                    errors.rejectValue("roles", "roles.invalid", new Object[]{role}, "Invalid Role Value: " + role);
                }
            }
        }

        if (user.getEmail() != null) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue("email", "email.invalid", "Invalid Email Address");
            }
        }
    }
}
