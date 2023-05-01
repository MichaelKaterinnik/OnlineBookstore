package com.onlinebookstore.services;

import com.onlinebookstore.commons.exceptions.BookIsNotAvailableException;
import com.onlinebookstore.dao.OrderItemDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@Service
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
    public OrderItemEntity addOrderItemToOrder(OrderItemDTO orderItemDTO, Integer orderId) throws EntityNotFoundException, BookIsNotAvailableException, IllegalArgumentException {
        BookEntity orderedBook = booksService.findBookByID(orderItemDTO.getBookId());
        if (!orderedBook.getAvailability()) {
            throw new BookIsNotAvailableException();
        }

        OrderItemEntity newOrderItem = createOrderItem();
        newOrderItem.setBookId(orderItemDTO.getBookId());
        newOrderItem.setQuantity(orderItemDTO.getQuantity());
        newOrderItem.setOrderId(orderId);

        // перевірка книги на наявність знижки і необхідність відповдіного корегування ціни
        if (bookDiscountsService.ifBookIsDiscounted(orderedBook.getId())) {
            bookDiscountsService.applyBookDiscountWhenOrdering(newOrderItem, orderedBook);
        }

        // зміна кількості доступних екземплярів книги при замовленні
        Integer quantity = orderItemDTO.getQuantity();
        if (quantity > orderedBook.getQuantity()) {
            throw new IllegalArgumentException("Кількість екземплярів, що бажається замовити, перевищує наявну кількість книг на складі.");
        }
        newOrderItem.setBookPrice(newOrderItem.getBookPrice().multiply(BigDecimal.valueOf(quantity)));
        orderedBook.setQuantity(orderedBook.getQuantity() - orderItemDTO.getQuantity());
        if (orderedBook.getQuantity().equals(0)) {
            orderedBook.setAvailability(false);
        }

        // онолвення загальної вартості замовлення
        orderItemRepository.save(newOrderItem);
        updateOrderPrice(orderId, newOrderItem);
        return newOrderItem;
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateOrderPrice(Integer orderID, OrderItemEntity orderItem) {
        OrderEntity ourOrder = ordersService.getOrderById(orderID);
        BigDecimal oldPrice = ourOrder.getTotalPrice();
        BigDecimal newPrice = oldPrice.add(orderItem.getBookPrice());
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