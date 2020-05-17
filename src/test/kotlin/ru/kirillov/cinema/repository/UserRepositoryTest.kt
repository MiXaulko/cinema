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

    fun insertRow(login: String): User {
        val category = categoryRepository.findById(1L).get()
        val role = roleRepository.findById(1L).get()
        val user = User(login, category, role)
        return userRepository.save(user)
    }

    @Test
    fun testInsert() {
        val user = insertRow("")
        val userFromRepo = userRepository.findById(user.id)

        assertTrue(userFromRepo.isPresent)
    }

    @Test
    fun testUpdate() {
        val newLogin = "newName"
        val user = insertRow("")
        user.login = newLogin
        userRepository.save(user)
        val userFromRepo = userRepository.findById(user.id).get()
        assertEquals(userFromRepo.login, newLogin)
    }

    @Test
    fun testDelete() {
        val user = insertRow("")
        userRepository.delete(user)
        assertFalse(userRepository.findById(user.id).isPresent)
    }

    @Test
    fun testFindByLogin(){
        val login = "login"
        insertRow(login)
        val userFromDb = userRepository.findByLogin(login)
        assertTrue(userFromDb.isPresent)
    }
}