package com.example.shoppinglist.controller;

import com.example.shoppinglist.model.ShoppingItem;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/items")
public class ShoppingController {

    //временное хранилище в памяти
    private final Map<Long, ShoppingItem> items = new ConcurrentHashMap<>();
    //генератор айди
    private final AtomicLong idGenerator = new AtomicLong(1);

    //блок инициализации чтоб список не был пустым при запуске
    {
        items.put(idGenerator.get(), new ShoppingItem(idGenerator.getAndIncrement(), "Молоко", false));
        items.put(idGenerator.get(), new ShoppingItem(idGenerator.getAndIncrement(), "Хлеб", true));
    }

    //просмотр всего списка
    @GetMapping
    public Collection<ShoppingItem> getAllItems() {
        return items.values();
    }

    //добавление элемента
    @PostMapping
    public ShoppingItem addItem(@RequestBody ShoppingItem item) {
        long id = idGenerator.getAndIncrement();
        item.setId(id);
        item.setCompleted(false); //новая покупка по умолчанию не куплена
        items.put(id, item);
        return item;
    }

    //удаление элемента
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        items.remove(id);
    }

    //отметить элемент купленным или снять отметку
    @PatchMapping("/{id}/toggle")
    public ShoppingItem toggleItemStatus(@PathVariable Long id) {
        ShoppingItem item = items.get(id);
        if (item != null) {
            item.setCompleted(!item.isCompleted());
        }
        return item;
    }
}