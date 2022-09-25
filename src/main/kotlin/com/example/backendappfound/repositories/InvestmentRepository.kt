package com.example.backendappfound.repositories
import com.example.models.Investment
import com.example.models.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface InvestmentRepository : MongoRepository<Investment?, String?> {
    @Query("{name:'?0'}")
    fun findByName(name: String?): Investment?

    @Query("{'investment.username':'?0'}")
    fun findByInvestment(investment: String?): Investment?
}
