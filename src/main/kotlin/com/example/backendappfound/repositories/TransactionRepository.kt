package com.example.backendappfound.repositories
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.example.models.Transaction

interface TransactionRepository : MongoRepository<Transaction?, String?> {

}
