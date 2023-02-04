package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    public Page<User> getAllUsers(int page, int size) {
        log.info("Service receives params for getting");

        PageRequest pageRequest = PageRequest.of(page, size);

        log.debug("Service returns representation of users");
        return userRepository.findAll(pageRequest);
    }

    public User getById(long id) {
        log.info("Service receives ID for getting " + id);
        log.debug("Service returns representation of user");

        return userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("there are no users with (id = " + id + ")");
                    return new ItemNotFoundException("there are no users with (id = " + id + ")");
                });
    }

    public User updateUserOrder(User user) {
        log.info("Service receives User for creating order " + user.getId() + user.getOrders());
        log.debug("Service returns representation of updated user");

        return userRepository.save(user);
    }
}
