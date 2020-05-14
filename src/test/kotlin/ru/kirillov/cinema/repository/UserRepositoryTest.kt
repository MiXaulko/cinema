package ru.kirillov.cinema.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import ru.kirillov.cinema.entity.User

@SpringBootTest(webEnvironment = NONE)
class UserRepositoryTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Test
    fun testFindAll() = assertNotEquals(userRepository.findAll().size, 0)

    fun insertRow(): User {
        val category = categoryRepository.findById(1L).orElseThrow()
        val role = roleRepository.findById(1L).orElseThrow()
        val user = User("user", category, role)
        return userRepository.save(user)
    }

    @Test
    fun testInsert() {
        val user = insertRow()
        val userFromRepo = userRepository.findById(user.id)

        assertTrue(userFromRepo.isPresent)
    }

    @Test
    fun testUpdate() {
        val newLogin = "newName"
        val user = insertRow()
        user.login = newLogin
        userRepository.save(user)
        val userFromRepo = userRepository.findById(user.id).orElseThrow()
        assertEquals(userFromRepo.login, newLogin)
    }

    @Test
    fun testDelete() {
        val user = insertRow()
        userRepository.delete(user)
        assertTrue(userRepository.findById(user.id).isEmpty)
    }
}