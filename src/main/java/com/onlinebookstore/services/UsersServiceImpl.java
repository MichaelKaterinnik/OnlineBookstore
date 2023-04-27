package com.onlinebookstore.services;

import com.onlinebookstore.dao.UserDao;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersServiceImpl implements UsersService {
    @Autowired
    private UserDao usersRepository;


    public UserEntity createUser() {
        return new UserEntity();
    }


    public void addNewUser(UserDTO userDTO) {
        UserEntity newUser = createUser();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setPhone(userDTO.getPhone());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());
        usersRepository.save(newUser);
    }

    /**
     * Блок методів для оновлення об'єктів типу "User" у БД:
     */
    public void updateUserName(Integer userID, String firstName, String LastName) {
        usersRepository.updateUserFirstAndLastName(userID, firstName, LastName);
    }
    public void updateUserPhone(Integer userID, String newPhone) {
        usersRepository.updateUserPhone(userID, newPhone);
    }
    public void updateUSerEmail(Integer userID, String newEmail) {
        usersRepository.updateUserEmail(userID, newEmail);
    }
    public void updateUserPassword(Integer userID, String newPassword) {
        usersRepository.updateUserPassword(userID, newPassword);
    }
    public void updateUser(Integer userID, String firstName, String LastName, String newPhone, String newEmail, String newPassword) {
        usersRepository.updateUserInfo(userID, firstName, LastName, newPhone, newEmail, newPassword);
    }

    public void deleteUserById(Integer userId) {
        usersRepository.deleteById(userId);
    }
    public void deleteUser(UserEntity user) {
        usersRepository.delete(user);
    }

}
