package cart.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DiscountPolicies {
    TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS(
            1L, "5만원 이상 구매 시 10% 할인",
            order -> order.calculateOriginalTotalPrice().isGreaterThanOrEqual(Money.from(50_000)),
            order -> order.calculateOriginalTotalPrice().multiply(0.1)
    );

    private final Long id;
    private final String name;
    Predicate<Order> orderTester;
    Function<Order, Money> discountCalculator;

    DiscountPolicies(Long id, String name, Predicate<Order> orderTester,
                     Function<Order, Money> discountCalculator) {
        this.id = id;
        this.name = name;
        this.orderTester = orderTester;
        this.discountCalculator = discountCalculator;
    }

    public static List<DiscountPolicies> getApplicablePolicies(Order order) {
        return Arrays.stream(DiscountPolicies.values())
                .filter(discountPolicies -> discountPolicies.orderTester.test(order))
                .collect(Collectors.toList());
    }

    public static DiscountPolicies findById(Long id) {
        return Stream.of(values())
                .filter(discountPolicy -> discountPolicy.getId().equals(id))
                .findFirst()
                .orElseThrow(); // TODO: 커스텀 예외 처리하기
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money calculateDiscountAmount(Order order) {
        return discountCalculator.apply(order);
    }
}
