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
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("investment")
class InvestmentController(
    @Autowired
    val investmentRepository: InvestmentRepository,
    @Autowired
    val userRepository:UserRepository) {

    @GetMapping("")
    fun index(): MutableList<Investment?> {
        return investmentRepository.findAll();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@RequestBody request:Investment, response:HttpServletResponse): Investment?
    {
        if (investmentRepository.findByInvestment(request.investment.username) == null) {
            response.sendError(HttpStatus.OK.value(),"La inversión ha sido creada");
            return this.investmentRepository.save(request);
        } else {
            response.sendError(HttpStatus.OK.value(),"El inversionista " + request.investment.username + " ya tiene una inversión");
            return null;
        }
    }

    @GetMapping("{username}")
    fun show(@PathVariable username:String):Investment?
    {
        return investmentRepository.findByName(username)!!
    }

    @PutMapping("{id}/investment_user/{investment_username}")
    fun update(@PathVariable id:String, @PathVariable investment_username:String, @RequestBody request:Investment, response:HttpServletResponse): Investment?
    {
        try {
            var investment_user = userRepository.findByName(investment_username)!!
            var investment = investmentRepository.findById(id).orElse(null);
            if (investment != null) {
                if (investmentRepository.findByInvestment(request.investment.username) == null || request.investment == investment_user) {
                    response.sendError(HttpStatus.OK.value(), "Inversión actualizada");
                    return this.investmentRepository.save(investment);
                }
            }
        }finally {
            response.sendError(HttpStatus.CONFLICT.value(), "El inversionista " + request.investment.username + " ya tiene una inversión");
            return null;
        }
    }
    @DeleteMapping("{id}")
    fun delete(@PathVariable id:String,  response: HttpServletResponse) {
        var investment = investmentRepository.findById(id).orElse(null);
        if (investment != null) {
            investment.state = 0;
            response.sendError(HttpStatus.NO_CONTENT.value(), "El usuario ha sido eliminado");
            this.investmentRepository.save(investment);
        }
    }


}
