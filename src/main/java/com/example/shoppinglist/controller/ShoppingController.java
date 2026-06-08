package com.example.shoppinglist.controller;

import com.example.shoppinglist.model.ShoppingItem;
import com.example.shoppinglist.service.ShoppingService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/items")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @GetMapping
    public Collection<ShoppingItem> getAllItems() {
        return shoppingService.getAllItems();
    }

    @PostMapping
    public ShoppingItem addItem(@RequestBody ShoppingItem item) {
        return shoppingService.addItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        shoppingService.deleteItem(id);
    }

    @PatchMapping("/{id}/toggle")
    public ShoppingItem toggleItemStatus(@PathVariable Long id) {
        return shoppingService.toggleItemStatus(id);
    }
}