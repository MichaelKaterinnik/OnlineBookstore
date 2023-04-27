package com.onlinebookstore.services;

import com.onlinebookstore.dao.OrderItemDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderItemsServiceImpl implements OrderItemsService {
    @Autowired
    private OrderItemDao orderItemRepository;
    @Autowired
    private OrdersServiceImpl ordersService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private BookDiscountsService bookDiscountsService;

    public OrderItemEntity createOrderItem() {
        return new OrderItemEntity();
    }

    /**     *
     * здійснювати перевірку замовлення на WAITING перед додаванням нової книги
     */
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

    public void updateOrderPrice(Integer orderID, Integer orderItemID) {
        OrderItemEntity ourItem = orderItemRepository.getById(orderItemID);
        OrderEntity ourOrder = ordersService.getOrderById(orderID);
        BigDecimal oldPrice = ourOrder.getTotalPrice();
        BigDecimal newPrice = oldPrice.add(ourItem.getBookPrice());
        ourOrder.setTotalPrice(newPrice);
    }

    public List<OrderItemEntity> getOrderItemsByOrderId(Integer orderID) {
        return orderItemRepository.findAllByOrderId(orderID);
    }
    public OrderItemEntity getOrderItemById(Integer itemID) throws EntityNotFoundException {
        Optional<OrderItemEntity> optionalOrderItem = orderItemRepository.findById(itemID);
        if (optionalOrderItem.isPresent()) {
            return optionalOrderItem.get();
        } else throw new EntityNotFoundException();
    }

    public void deleteOrderItemByID(Integer orderItemID) {
        orderItemRepository.deleteById(orderItemID);
    }
    public void deleteOrderItem(OrderItemEntity orderItem) {
        orderItemRepository.delete(orderItem);
    }
}
