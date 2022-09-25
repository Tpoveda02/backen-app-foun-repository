package com.example.backendappfound.controllers;


import javax.servlet.http.HttpServletResponse;

import com.example.backendappfound.repositories.UserRepository;
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


@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController( @Autowired val userRepository:UserRepository) {

	//Metodos propios
	@GetMapping("")
	fun index(): MutableList<User?> {
		return userRepository.findAll();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	fun create(@RequestBody request:User, response:HttpServletResponse): User? {
		if(userRepository.findByName(request.username)==null){
			response.sendError(HttpStatus.OK.value(),"Usuario creado");
			return this.userRepository.save(request);
		}else{
			response.sendError(HttpStatus.CONFLICT.value(),"Usuario no ha sido creado, ese nombre ya existe");
		}
		return null;
	}
	@GetMapping("/logIn/{username}/{password}")
	fun logIn(@PathVariable username:String,@PathVariable password:String, response:HttpServletResponse):User? {
		var user = userRepository.findByName(username)!!
		if (user == null) {
			response.sendError(HttpStatus.CONFLICT.value(),"El usuario no existe");
			return null;
		}else{
			if(!password.equals(user.password)) {
				response.sendError(HttpStatus.CONFLICT.value(),"La contraseña no coincide");
				return null;
			}
		}
		return user;
	}

	@GetMapping("{id}")
	fun show(@PathVariable id:String ):User? {
		var user = userRepository.findById(id).orElse(null);
		return user;
	}
	
	@PutMapping("{username}")
	fun update(@PathVariable username:String, @RequestBody request:User, response:HttpServletResponse): User? {
		try {
			var user = userRepository.findByName(request.username)
			if (user != null) {
				if (request.username.equals(username)) {
					response.sendError(HttpStatus.OK.value(), "El usuario ha sido actualizado");
					return this.userRepository.save(request);
				} else {
					response.sendError(HttpStatus.CONFLICT.value(), "El nombre de usuario ya existe");
					return null;
				}
			}
		}finally{
			response.sendError(HttpStatus.OK.value(), "El usuario ha sido actualizado");
			return this.userRepository.save(request);
		}
	}

	@DeleteMapping("{id}")
	fun delete(@PathVariable id:String, response:HttpServletResponse) {
		var user = userRepository.findById(id).orElse(null)
		if(user != null ) {
			user.state = 0;
			response.sendError(HttpStatus.NO_CONTENT.value(),"El usuario ha sido eliminado");
			this.userRepository.save(user);
		}
	}

}
