package discount.repository.impl;

import discount.DiscountPolicy;
import discount.repository.DiscountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryDiscountRepository implements DiscountRepository {
    private final Map<String, DiscountPolicy> activePromoCodes = new ConcurrentHashMap<>();

    @Override
    public void save(String code, DiscountPolicy policy) {
        activePromoCodes.put(code.trim().toUpperCase(), policy);
    }

    @Override
    public Optional<DiscountPolicy> findByCode(String code) {
        return Optional.ofNullable(activePromoCodes.get(code.trim().toUpperCase()));
    }
}
