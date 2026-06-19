package com.ecommerce.user.sevices;

import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public UserResponse getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return modelMapper.map(user, UserResponse.class);
    }

    public boolean updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(u -> {
                        u.setFirstName(user.getFirstName());
                        u.setLastName(user.getLastName());
                        userRepository.save(u);
                        return true;
                    }
                ).orElse(false);
    }
}
