package ru.kirillov.cinema.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import ru.kirillov.cinema.entity.Role

@SpringBootTest(webEnvironment = NONE)
class RoleRepositoryTest {
    @Autowired
    lateinit var roleRepository: RoleRepository

    @Test
    fun testFindAll() {
        assertNotEquals(roleRepository.findAll().size, 0)
    }

    fun insertRow(): Role {
        val role = Role("moderator");
        return roleRepository.save(role);
    }

    @Test
    fun testInsert() {
        val role = insertRow()
        assertNotEquals(role.id, 0)
    }

    @Test
    fun testUpdate() {
        val newName = "testName"
        val role = insertRow()
        role.name = newName
        roleRepository.save(role)
        val roleAfterUpdate = roleRepository.findById(role.id).orElseThrow();
        assertEquals(roleAfterUpdate.name, newName)
    }

    @Test
    fun testDelete() {
        val role = insertRow()
        roleRepository.delete(role)
        assertTrue(roleRepository.findById(role.id).isEmpty)
    }
}