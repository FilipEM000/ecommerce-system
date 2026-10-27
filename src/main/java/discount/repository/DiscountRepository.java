package discount.repository;

import discount.DiscountPolicy;

import java.util.Optional;

public interface DiscountRepository {
    void save(String code, DiscountPolicy policy);

    Optional<DiscountPolicy> findByCode(String code);
}
