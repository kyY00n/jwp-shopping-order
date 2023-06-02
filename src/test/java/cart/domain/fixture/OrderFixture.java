package cart.domain.fixture;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import java.util.List;

public class OrderFixture {
    public static final Member member = new Member(null, "hi@hi.com", "password");
    public static final Order order = new Order(member, List.of(
            new OrderItem("치킨", 1, "http://example.com/chicken.jpg", new Money(10_000)),
            new OrderItem("피자", 2, "http://example.com/pizza.jpg", new Money(30_000)),
            new OrderItem("보쌈", 1, "http://example.com/pizza.jpg", new Money(15_000))
    ));
    public static final Order orderUnderDiscountThreshold = new Order(member, List.of(
            new OrderItem("치킨", 1, "http://example.com/chicken.jpg", new Money(10_000)),
            new OrderItem("피자", 2, "http://example.com/pizza.jpg", new Money(30_000))
    ));


}