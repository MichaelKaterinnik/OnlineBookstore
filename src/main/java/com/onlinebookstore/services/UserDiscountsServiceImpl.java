package com.onlinebookstore.services;

import com.onlinebookstore.dao.UserDiscountDao;
import com.onlinebookstore.domain.UserDiscountEntity;
import com.onlinebookstore.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Service
public class UserDiscountsServiceImpl implements UserDiscountsService {
    @Autowired
    private UserDiscountDao userDiscountRepository;


    public UserDiscountEntity createUserDiscount() {
        return new UserDiscountEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addNewUserDiscount(Integer userID, Integer discountID) {
        UserDiscountEntity newUserDiscount = createUserDiscount();
        newUserDiscount.setUserId(userID);
        newUserDiscount.setDiscountId(discountID);
        userDiscountRepository.save(newUserDiscount);
    }


    // get-methods:
    public UserDiscountEntity findUserDiscountById(Integer userDiscountID) {
        Optional<UserDiscountEntity> optionalUserDiscount = userDiscountRepository.findById(userDiscountID);
        return optionalUserDiscount.orElse(null);
    }
    public UserDiscountEntity findUserDiscountByUser(UserEntity user) {
        Optional<UserDiscountEntity> optionalUserDiscount = userDiscountRepository.findByUser(user);
        return optionalUserDiscount.orElse(null);
    }
    public UserDiscountEntity findUserDiscountByUserId(Integer userID) {
        Optional<UserDiscountEntity> optionalUserDiscount = userDiscountRepository.findByUserId(userID);
        return optionalUserDiscount.orElse(null);
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountIDForUserDiscount(Integer userDiscountID, Integer newDiscountID) {
        userDiscountRepository.updateDiscountId(userDiscountID, newDiscountID);
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUserDiscount(UserDiscountEntity userDiscount) {
        userDiscountRepository.delete(userDiscount);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUserDiscountById(Integer id) {
        userDiscountRepository.deleteById(id);
    }

}
