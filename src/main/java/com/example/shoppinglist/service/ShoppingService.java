package com.example.shoppinglist.service;

import com.example.shoppinglist.model.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ShoppingService {
    private final ShoppingRepository repository;

    public ShoppingService(ShoppingRepository repository) {
        this.repository = repository;
    }

    public Collection<ShoppingItem> getAllItems() {
        return repository.findAll();
    }

    public ShoppingItem addItem(ShoppingItem item) {
        item.setCompleted(false);
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }

    public ShoppingItem toggleItemStatus(Long id) {
        ShoppingItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setCompleted(!item.isCompleted());
        return repository.save(item);
    }
}