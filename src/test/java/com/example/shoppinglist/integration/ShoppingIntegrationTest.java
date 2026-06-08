package com.example.shoppinglist.integration;

import com.example.shoppinglist.model.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        shoppingRepository.deleteAll(); // Гарантируем чистоту окружения перед каждым тестом
    }

    // ТЕСТ 1: Интеграционный тест добавления элемента (Controller -> Service -> Repository)
    @Test
    void testIntegration_AddItem() throws Exception {
        ShoppingItem newItem = new ShoppingItem(null, "Колбаса", false);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Колбаса"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    // ТЕСТ 2: Интеграционный тест получения списка (база предзаполнена)
    @Test
    void testIntegration_GetAllItems() throws Exception {
        // Имитируем, что в БД уже есть один товар
        shoppingRepository.save(new ShoppingItem(null, "Сыр", true));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Сыр"))
                .andExpect(jsonPath("$[0].completed").value(true));
    }
}