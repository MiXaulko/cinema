package ru.kirillov.cinema.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import ru.kirillov.cinema.entity.Category

@SpringBootTest(webEnvironment = NONE)
class CategoryRepositoryTest {
    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun testFindAll() = assertNotEquals(categoryRepository.findAll().size, 0)

    fun insertRow(): Category {
        val category = Category("category", 45f);
        return categoryRepository.save(category);
    }

    @Test
    fun testInsert() {
        val category = insertRow()
        assertNotEquals(category.id, 0)
    }

    @Test
    fun testUpdate() {
        val newName = "testName"
        val category = insertRow()
        category.name = newName
        categoryRepository.save(category)
        val categoryAfterUpdate = categoryRepository.findById(category.id).orElseThrow();
        assertEquals(categoryAfterUpdate.name, newName)
    }

    @Test
    fun testDelete() {
        val category = insertRow()
        categoryRepository.delete(category)
        assertTrue(categoryRepository.findById(category.id).isEmpty)
    }
}