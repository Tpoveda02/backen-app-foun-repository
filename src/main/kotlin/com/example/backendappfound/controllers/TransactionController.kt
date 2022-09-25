package com.example.backendappfound.controllers;

import com.example.backendappfound.repositories.UserRepository;
import com.example.models.Investment;
import com.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.backendappfound.repositories.InvestmentRepository;
import com.example.backendappfound.repositories.TransactionClientRepository
import com.example.backendappfound.repositories.TransactionRepository
import com.example.models.Transaction
import com.example.models.TransactionClient
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("transaction")
class TransactionController(
    @Autowired
    val investmentRepository: InvestmentRepository,
    @Autowired
    val userRepository:UserRepository,
    @Autowired
    val transactionRepository:TransactionRepository,
    @Autowired
    val transactionClientRepository:TransactionClientRepository,
){
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/client/add")
    fun create(@RequestBody request:TransactionClient, response:HttpServletResponse): TransactionClient?
    {
        var user = userRepository.findByName(request.client.username)
        request.type_action = "Agregar"
        if(user != null){
            user.total_money +=  request.amount
            userRepository.save(user)
            response.sendError(HttpStatus.OK.value(),"El dinero se ha agregado");
            return transactionClientRepository.save(request)
        }else{
            response.sendError(HttpStatus.CONFLICT.value(),"El dinero no se ha agregado");
            return null;
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/investment/add")
    fun createInvestment(@RequestBody request:Transaction, response:HttpServletResponse): Transaction? {
        var user = userRepository.findByName(request.client.username)
        var investment = investmentRepository.findByInvestment(request.investment.investment.username)
        request.type_action = "Agregar"
        if (user != null) {
            if (investment != null) {
                if (user.total_money < request.amount) {
                    response.sendError(HttpStatus.CONFLICT.value(), "La inversión no ha sido creada, no cuenta con suficiente dinero")
                } else {
                    if (investment.min_amount <= request.amount) {
                        investment.investment.total_money += request.amount
                        userRepository.save(investment.investment)
                        response.sendError(HttpStatus.OK.value(), "Inversión realizada");
                        return transactionRepository.save(request)
                    } else {
                        response.sendError(HttpStatus.CONFLICT.value(), "La inversión no ha sido creada, el dinero mínimo para invertir es $" + investment.min_amount);
                        return null
                    }
                }
            }
        }
        response.sendError(HttpStatus.CONFLICT.value(), "El dinero no se ha agregado");
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/client/remove")
    fun createRemove(@RequestBody request:TransactionClient, response:HttpServletResponse): TransactionClient?
    {
        var user = userRepository.findByName(request.client.username)
        request.type_action = "Retirar"
        if(user != null){
            if(request.amount<=user.total_money) {
                user.total_money -= request.amount
                userRepository.save(user)
                response.sendError(HttpStatus.OK.value(),"El dinero se ha retiro");
                return transactionClientRepository.save(request)
            }else{
                response.sendError(HttpStatus.CONFLICT.value(),"No cuenta con suficiente dinero para retirar");
                return null;
            }
        }
        response.sendError(HttpStatus.CONFLICT.value(),"El dinero no se ha retiro");
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/investment/remove")
    fun createInvestmentRemove(@RequestBody request:Transaction, response:HttpServletResponse): Transaction? {
        var user = userRepository.findByName(request.client.username)
        var investment = investmentRepository.findByInvestment(request.investment.investment.username)
        request.type_action = "Retirar"
        if (user != null) {
            if (investment != null) {
                if(user.total_money < request.amount){
                    response.sendError(HttpStatus.CONFLICT.value(),"No cuenta con suficiente dinero para retirar de la inversión")
                    return null
                }else{
                    if(investment.min_amount<=investment.investment.total_money-request.amount || investment.investment.total_money-request.amount==0){
                        investment.investment.total_money -= request.amount
                        investmentRepository.save(investment)
                        response.sendError(HttpStatus.OK.value(), "Inversión realizada");
                        return transactionRepository.save(request)
                    }else{
                        response.sendError(HttpStatus.CONFLICT.value(), "El retiro no se ha hecho, ya que el dinero mínimo para invertir es $${investment.min_amount}")
                        return null
                    }
                }
            }
        }
        response.sendError(HttpStatus.CONFLICT.value(), "El dinero no se ha retirado");
        return null;
    }

    @GetMapping("/client/{username}")
    fun show(@PathVariable username:String,  response:HttpServletResponse): List<Any?>? {
        val clientTransactions = transactionClientRepository.findAll()!!.filter{ it?.client?.username == username}
        var transaction_investment = transactionRepository.findAll().filter { it?.client?.username == username}
        var transactions = clientTransactions.plus(transaction_investment)
        if (transactions == null) {
            response.sendError(HttpStatus.CONFLICT.value(), "No hay transacciones");
            return null;
        }else{
            return transactions
        }
    }
    @GetMapping("/investment/{username}")
    fun showInvestment(@PathVariable username:String,  response:HttpServletResponse): List<Transaction?>? {
        var investment_user = userRepository.findByName(username)
        if(investment_user!=null){
            var transaction_investment = transactionRepository.findAll().filter { it?.investment?.investment?.username == username}
            if (transaction_investment == null) {
                response.sendError(HttpStatus.CONFLICT.value(), "No hay transacciones para esta inversión");
                return null
            }else{
                return transaction_investment
            }
        }else{
            response.sendError(HttpStatus.CONFLICT.value(), "La inversión no existe");
            return null
        }
    }
}
