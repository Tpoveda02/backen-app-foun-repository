package com.example.models
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import lombok.Data

@Data
@Document("user")
class User {
    @Id
    lateinit var _id: String;
    var username: String = ""
    var password: String = ""
    var role: String = ""
    var total_money: Int = 0
    var state:Int = 1
    constructor()
    constructor(_id: String, username: String, password: String, role: String, total_money:Int, state:Int) : this(){
        this._id = _id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.total_money = total_money;
        this.state = state;
    }
}
