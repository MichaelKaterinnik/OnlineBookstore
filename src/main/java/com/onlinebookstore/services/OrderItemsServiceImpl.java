package com.onlinebookstore.services;

import com.onlinebookstore.commons.exceptions.BookIsNotAvailableException;
import com.onlinebookstore.config.GlobalExceptionHandler;
import com.onlinebookstore.dao.OrderItemDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    private ModelMapper modelMapper;


    public OrderItemEntity createOrderItem() {
        return new OrderItemEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderItemEntity addOrderItemToOrder(OrderItemDTO orderItemDTO, Integer orderId) throws EntityNotFoundException, BookIsNotAvailableException, IllegalArgumentException {
        BookEntity orderedBook = null;
        try {
            orderedBook = booksService.findBookByID(orderItemDTO.getBookId());
        } catch (EntityNotFoundException e) {
            exceptionHandler.entityNotFoundException(e);
        }
        if (!orderedBook.getAvailability()) {
            throw new BookIsNotAvailableException();
        }

        OrderItemEntity newOrderItem = createOrderItem();
        newOrderItem.setBookId(orderItemDTO.getBookId());
        newOrderItem.setQuantity(orderItemDTO.getQuantity());
        newOrderItem.setOrderId(orderId);

        // checking discounted book price availability
        if (bookDiscountsService.ifBookIsDiscounted(orderedBook.getId())) {
            bookDiscountsService.applyBookDiscountWhenOrdering(newOrderItem, orderedBook);
        }

        // miltiplying book price according to required quantity
        Integer quantity = orderItemDTO.getQuantity();
        if (quantity > orderedBook.getQuantity()) {
            throw new IllegalArgumentException();
        }
        newOrderItem.setBookPrice(newOrderItem.getBookPrice().multiply(BigDecimal.valueOf(quantity)));
        orderedBook.setQuantity(orderedBook.getQuantity() - orderItemDTO.getQuantity());
        if (orderedBook.getQuantity().equals(0)) {
            orderedBook.setAvailability(false);
        }

        // updating sum order price and returning to "Add new Item" controller method
        orderItemRepository.save(newOrderItem);
        updateOrderPrice(orderId, newOrderItem);
        return newOrderItem;
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateOrderPrice(Integer orderID, OrderItemEntity orderItem) {
        OrderEntity ourOrder = null;
        try {
            ourOrder = ordersService.getOrderById(orderID);
        } catch (EntityNotFoundException e) {
            exceptionHandler.entityNotFoundException(e);
        }

        BigDecimal oldPrice = ourOrder.getTotalPrice();
        BigDecimal newPrice = oldPrice.add(orderItem.getBookPrice());
        ourOrder.setTotalPrice(newPrice);
    }


    // get-methods:
    public List<OrderItemEntity> getOrderItemsByOrderId(Integer orderID) {
        return orderItemRepository.findAllByOrderId(orderID);
    }
    public List<OrderItemDTO> getOrderItemsDTOByOrderId(Integer orderID) {
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findAllByOrderId(orderID);
        return orderItemEntities.stream()
                .map(orderItemEntity -> modelMapper.map(orderItemEntity, OrderItemDTO.class))
                .collect(Collectors.toList());
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
