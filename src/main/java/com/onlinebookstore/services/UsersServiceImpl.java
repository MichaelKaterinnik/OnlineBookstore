package com.onlinebookstore.services;

import com.onlinebookstore.dao.UserDao;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UsersServiceImpl implements UsersService {
    @Autowired
    private UserDao usersRepository;


    public UserEntity createUser() {
        return new UserEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addNewUser(UserDTO userDTO) {
        UserEntity newUser = createUser();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setPhone(userDTO.getPhone());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());
        usersRepository.save(newUser);
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateUserName(Integer userID, String firstName, String LastName) {
        usersRepository.updateUserFirstAndLastName(userID, firstName, LastName);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateUserPhone(Integer userID, String newPhone) {
        usersRepository.updateUserPhone(userID, newPhone);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateUSerEmail(Integer userID, String newEmail) {
        usersRepository.updateUserEmail(userID, newEmail);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateUserPassword(Integer userID, String newPassword) {
        usersRepository.updateUserPassword(userID, newPassword);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateUser(Integer userID, String firstName, String LastName, String newPhone, String newEmail, String newPassword) {
        usersRepository.updateUserInfo(userID, firstName, LastName, newPhone, newEmail, newPassword);
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUserById(Integer userId) {
        usersRepository.deleteById(userId);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUser(UserEntity user) {
        usersRepository.delete(user);
    }

}
