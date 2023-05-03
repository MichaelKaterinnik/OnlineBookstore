package com.onlinebookstore.services;

import com.onlinebookstore.dao.UserDao;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UserDao usersRepository;

    private ModelMapper modelMapper;


    public UserEntity createUser() {
        return new UserEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserEntity addNewUser(UserDTO userDTO) {
        UserEntity newUser = createUser();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setPhone(userDTO.getPhone());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());

        if (userDTO.getRole().equalsIgnoreCase("ADMIN")) {
            newUser.setRole(UserEntity.Role.ADMIN);
        } else
            newUser.setRole(UserEntity.Role.USER);

        usersRepository.save(newUser);
        return newUser;
    }


    //get-methods
    public List<UserEntity> findAll(Pageable pageable) {
        return usersRepository.findAll();
    }
    public List<UserDTO> getAllUserDTO(Pageable pageable) {
        List<UserEntity> userEntities = usersRepository.findAll();
        return userEntities.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserEntity findById(Integer userID) throws EntityNotFoundException {
        Optional<UserEntity> optionalUser = usersRepository.findById(userID);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else throw new EntityNotFoundException();
    }
    public UserEntity findByEmail(String email) throws EntityNotFoundException {
        Optional<UserEntity> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else throw new EntityNotFoundException();
    }
    public List<UserEntity> findAllByPhone(String phone) {
        return usersRepository.findAllByPhone(phone);
    }
    public List<UserEntity> findAllByPassword(String password) {
        return usersRepository.findAllByPassword(password);
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


    public <S extends UserEntity> S save(S entity) {
        return usersRepository.save(entity);
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
