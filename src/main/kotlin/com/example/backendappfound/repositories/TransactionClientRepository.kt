package com.example.backendappfound.repositories
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.example.models.TransactionClient

interface TransactionClientRepository : MongoRepository<TransactionClient?, String?> {
}
