package com.epam.esm.user.authComponent;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.utils.validation.DataValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthUserVerify {
    public boolean hasPermission(User user, Long id) {
        log.debug("Validation request model User ID " + id);
        if (DataValidation.moreThenZero(id)) {
            return id == user.getId() || user.is(Role.ADMIN);
        }
        log.error("The user ID is not valid: id = " + id);
        throw new ServerException("value id = " + id + " should be more than zero");
    }
}

