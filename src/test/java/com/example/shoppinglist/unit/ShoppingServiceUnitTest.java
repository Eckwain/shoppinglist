package com.example.shoppinglist.unit;

import com.example.shoppinglist.model.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingRepository;
import com.example.shoppinglist.service.ShoppingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceUnitTest {

    @Mock
    private ShoppingRepository shoppingRepository; // Заглушка вместо реальной БД

    @InjectMocks
    private ShoppingService shoppingService; // Тестируемый класс с внедренным моком

    // ТЕСТ 1: Юнит-тест метода toggleItemStatus (успешное переключение статуса)
    @Test
    void testUnit_ToggleItemStatus_Success() {
        // Arrange (Настройка мока)
        ShoppingItem itemInDb = new ShoppingItem(1L, "Масло", false);
        when(shoppingRepository.findById(1L)).thenReturn(Optional.of(itemInDb));
        when(shoppingRepository.save(any(ShoppingItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ShoppingItem updatedItem = shoppingService.toggleItemStatus(1L);

        // Assert
        assertTrue(updatedItem.isCompleted(), "Статус должен измениться с false на true");
        verify(shoppingRepository, times(1)).findById(1L);
        verify(shoppingRepository, times(1)).save(itemInDb);
    }

    // ТЕСТ 2: Юнит-тест метода toggleItemStatus на несуществующий ID (обработка ошибки)
    @Test
    void testUnit_ToggleItemStatus_NotFound() {
        // Arrange (Репозиторий возвращает пустой Optional, имитируя отсутствие записи)
        when(shoppingRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingService.toggleItemStatus(99L);
        });

        assertEquals("Item not found", exception.getMessage());
        // Проверяем, что метод save() даже не пытался вызваться, так как упали по ошибке
        verify(shoppingRepository, never()).save(any());
    }
}