package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }
}
