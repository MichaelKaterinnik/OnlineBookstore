package com.onlinebookstore.services;

import com.onlinebookstore.domain.UserDiscountEntity;
import com.onlinebookstore.domain.UserEntity;

public interface UserDiscountsService {

    void addNewUserDiscount(Integer userID, Integer discountID);

    UserDiscountEntity findUserDiscountById(Integer userDiscountID);
    UserDiscountEntity findUserDiscountByUser(UserEntity user);
    UserDiscountEntity findUserDiscountByUserId(Integer userID);

    public void updateDiscountIDForUserDiscount(Integer userDiscountID, Integer newDiscountID);

    void deleteUserDiscount(UserDiscountEntity entity);
    void deleteUserDiscountById(Integer id);
}
