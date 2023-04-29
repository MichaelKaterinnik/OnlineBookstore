package com.onlinebookstore.services;

import com.onlinebookstore.dao.OrderItemDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderItemsServiceImpl implements OrderItemsService {
    @Autowired
    private OrderItemDao orderItemRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private BookDiscountsService bookDiscountsService;

    public OrderItemEntity createOrderItem() {
        return new OrderItemEntity();
    }


    // add-methods:
    /**
     * PLEASE NOTE that you must check the WAITING order status in controller before adding a new book to it
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addOrderItemToOrder(OrderItemDTO orderItemDTO, Integer orderId) throws EntityNotFoundException {
        OrderItemEntity newOrderItem = createOrderItem();
        newOrderItem.setBookId(orderItemDTO.getBookId());
        newOrderItem.setQuantity(orderItemDTO.getQuantity());
        newOrderItem.setOrderId(orderId);

        // перевірка книги на наявність знижки і необхідність відповдіного корегування ціни
        BookEntity orderedBook = booksService.findBookByID(orderItemDTO.getBookId());
        if (bookDiscountsService.ifBookIsDiscounted(orderedBook.getId())) {
            bookDiscountsService.applyBookDiscountWhenOrdering(newOrderItem, orderedBook);
        }

        // зміна кількості доступних екземплярів книги при замовленні
        Integer quantity = orderItemDTO.getQuantity();
        newOrderItem.setBookPrice(newOrderItem.getBookPrice().multiply(BigDecimal.valueOf(quantity)));
        orderedBook.setQuantity(orderedBook.getQuantity() - orderItemDTO.getQuantity());

        // онолвення загальної вартості замовлення
        updateOrderPrice(orderId, newOrderItem.getId());
        orderItemRepository.save(newOrderItem);
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateOrderPrice(Integer orderID, Integer orderItemID) {
        OrderItemEntity ourItem = orderItemRepository.getById(orderItemID);
        OrderEntity ourOrder = ordersService.getOrderById(orderID);
        BigDecimal oldPrice = ourOrder.getTotalPrice();
        BigDecimal newPrice = oldPrice.add(ourItem.getBookPrice());
        ourOrder.setTotalPrice(newPrice);
    }


    // get-methods:
    public List<OrderItemEntity> getOrderItemsByOrderId(Integer orderID) {
        return orderItemRepository.findAllByOrderId(orderID);
    }
    public OrderItemEntity getOrderItemById(Integer itemID) throws EntityNotFoundException {
        Optional<OrderItemEntity> optionalOrderItem = orderItemRepository.findById(itemID);
        if (optionalOrderItem.isPresent()) {
            return optionalOrderItem.get();
        } else throw new EntityNotFoundException();
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteOrderItemByID(Integer orderItemID) {
        orderItemRepository.deleteById(orderItemID);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteOrderItem(OrderItemEntity orderItem) {
        orderItemRepository.delete(orderItem);
    }
}
