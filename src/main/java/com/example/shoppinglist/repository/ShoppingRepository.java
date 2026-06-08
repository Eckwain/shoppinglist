package com.example.shoppinglist.repository;

import com.example.shoppinglist.model.ShoppingItem;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ShoppingRepository {
    private final Map<Long, ShoppingItem> items = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ShoppingRepository() {
        // Начальные данные
        save(new ShoppingItem(null, "Молоко", false));
        save(new ShoppingItem(null, "Хлеб", true));
    }

    public Collection<ShoppingItem> findAll() {
        return items.values();
    }

    public Optional<ShoppingItem> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public ShoppingItem save(ShoppingItem item) {
        if (item.getId() == null) {
            item.setId(idGenerator.getAndIncrement());
        }
        items.put(item.getId(), item);
        return item;
    }

    public void deleteById(Long id) {
        items.remove(id);
    }

    public void deleteAll() {
        items.clear();
    }
}