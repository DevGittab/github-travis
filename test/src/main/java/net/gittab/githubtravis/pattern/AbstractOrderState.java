//package net.gittab.test.pattern;
//
///**
// * AbstractOrderState.
// *
// * @author xiaohua zhou
// **/
//public abstract class AbstractOrderState {
//
//    public abstract OrderState pay(Order order);
//
//    public abstract OrderState paySuccess(Order order);
//
//    public abstract OrderState refund(Order order);
//
//    public abstract OrderState refundSuccess(Order order);}
//
//public class PayingOrderState implement OrderState {
//
//    public OrderState pay(Order order) {
//        throw IllegalStateException("已经在支付中");
//    }
//    public OrderState paySuccess(Order order, long fee) {
//        doPaySuccess(Order order, long fee);
//
//        order.setState(new PaidOrderState());
//
//    }
//    public OrderState refund(Order order) {
//        throw IllegalStateException("尚未完成支付");
//    }
//
//    public OrderState refundSuccess(Order order) {
//        throw IllegalStateException("尚未完成支付");
//    }
//
//}
//
//    public class UnpaidOrder implement OrderState { ... }
//
//    public class PaidOrderState implement OrderState { ... }
//
//    public class RefundingOrderState implement OrderState { ... }
//
//    public class RefundedOrderState implement OrderState { ... }
//
//
//
//}
//
//public class Order {
//
//    OrderState state = new UnpaidOrder();
//
//    public void pay(long fee) {
//        state.pay(fee);
//    }
//
//    public void paySuccess(long fee) {
//        state.paySuccess(this, fee);
//    }
//
//    public void refund() { ... }
//
//    public void refundSuccess() { ... }
//
//}
//
//public class PaymentService {
//
//    public void payOrder(long orderId) {
//        Order order = OrderRepository.find(orderId)
//        order.pay();
//        OrderRepository.save(order);
//    }
//}
//
//
//
//public interface CanPayOrder {
//    Order pay();
//}
//
//public interface CanPaySuccessOrder { ... }
//
//public interface CanRefundOrder { ... }
//
//public interface CanRefundSuccessOrder { ... }
//
//public class UnpaidOrder implements CanPayOrder { ... }
//
//public class PayingOrder implements CanPaySuccessOrder { ... }
//
//public class PartialPaidOrder implements CanPayOrder, CanRefundOrder { ... }
//
//public class PaidOrder implements CanRefundOrder { ... }
//
//public class RefundingOrder implements CanRefundSuccessOrder { ... }
//
//public class PaymentService {
//
//    public void pay(long orderId) {
//        Order order = OrderRepository.find(orderId)
//        // 转换为 CanPayOrder，如果无法转换则抛异常
//        CanPayOrder orderToPay = order.asCanPayOrder();
//        Order payingOrder = orderToPay.pay();
//        OrderRepository.save(payingOrder);
//    }
//}
//
//    let orderToPay = order.asOrderStateEntity();
//    if (typeof orderToPay['pay'] === 'function') {
//        orderToPay.pay();
//    } else {
//        throw new ServiceError("该订单不能进行支付操作");
//    }
