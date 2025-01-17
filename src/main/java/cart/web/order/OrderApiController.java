package cart.web.order;

import cart.application.OrderService;
import cart.application.PaymentService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.payment.Payment;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.exception.OrderException.IllegalMember;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderApiController(final OrderService orderService, final PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    private static void validateAuthorization(Member member, Order order) {
        if (!order.getMember().equals(member)) {
            throw new IllegalMember(order, member);
        }
    }

    @PostMapping
    public ResponseEntity<Void> postOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        final Long orderId = this.orderService.createOrderAndSave(member, orderRequest.getCartItemIds());
        this.paymentService.createPaymentRecordAndSave(this.orderService.retrieveOrderById(orderId));
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(final Member member) {
        final List<Order> orders = this.orderService.retrieveMemberOrders(member);
        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(final Member member, @PathVariable final Long orderId) {
        final Order order = this.orderService.retrieveOrderById(orderId);
        validateAuthorization(member, order);
        final Payment payment = this.paymentService.findByOrder(order);
        return ResponseEntity.ok(OrderDetailResponse.from(payment));
    }
}
