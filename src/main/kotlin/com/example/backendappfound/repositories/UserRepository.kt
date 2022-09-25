package com.example.backendappfound.repositories
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.example.models.User

interface UserRepository : MongoRepository<User?, String?> {
    @Query("{username:'?0'}")
    fun findByName(username: String?): User?
}
