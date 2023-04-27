package com.onlinebookstore.services;

import com.onlinebookstore.dao.UserDiscountDao;
import com.onlinebookstore.domain.UserDiscountEntity;
import com.onlinebookstore.domain.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserDiscountsServiceImpl implements UserDiscountsService {
    @Autowired
    private UserDiscountDao userDiscountRepository;


    public UserDiscountEntity createUserDiscount() {
        return new UserDiscountEntity();
    }


    public void addNewUserDiscount(Integer userID, Integer discountID) {
        UserDiscountEntity newUserDiscount = createUserDiscount();
        newUserDiscount.setUserId(userID);
        newUserDiscount.setDiscountId(discountID);
        userDiscountRepository.save(newUserDiscount);
    }

    public UserDiscountEntity findUserDiscountById(Integer userDiscountID) throws EntityNotFoundException {
        Optional<UserDiscountEntity> optionalUserDiscount = userDiscountRepository.findById(userDiscountID);
        if (optionalUserDiscount.isPresent()) {
            return optionalUserDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public UserDiscountEntity findUserDiscountByUser(UserEntity user) throws EntityNotFoundException {
        Optional<UserDiscountEntity> optionalUserDiscount = userDiscountRepository.findByUser(user);
        if (optionalUserDiscount.isPresent()) {
            return optionalUserDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public UserDiscountEntity findUserDiscountByUserId(Integer userID) throws EntityNotFoundException {
        Optional<UserDiscountEntity> optionalUserDiscount = userDiscountRepository.findByUserId(userID);
        if (optionalUserDiscount.isPresent()) {
            return optionalUserDiscount.get();
        } else throw new EntityNotFoundException();
    }

    public void updateDiscountIDForUserDiscount(Integer userDiscountID, Integer newDiscountID) {
        userDiscountRepository.updateDiscountId(userDiscountID, newDiscountID);
    }

    public void deleteUserDiscount(UserDiscountEntity userDiscount) {
        userDiscountRepository.delete(userDiscount);
    }
    public void deleteUserDiscountById(Integer id) {
        userDiscountRepository.deleteById(id);
    }
}
