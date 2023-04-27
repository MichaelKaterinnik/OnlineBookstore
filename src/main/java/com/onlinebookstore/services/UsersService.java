package com.onlinebookstore.services;

import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface UsersService {

    void addNewUser(UserDTO userDTO);

    void updateUserName(Integer userID, String firstName, String LastName);
    void updateUserPhone(Integer userID, String newPhone);
    void updateUSerEmail(Integer userID, String newEmail);
    void updateUserPassword(Integer userID, String newPassword);
    void updateUser(Integer userID, String firstName, String LastName, String newPhone, String newEmail, String newPassword);

    void deleteUserById(Integer userId);
    void deleteUser(UserEntity user);
}
