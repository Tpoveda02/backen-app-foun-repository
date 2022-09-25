package com.example.models
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import lombok.Data

@Data
@Document("investment")
class Investment {
    @Id
    lateinit var _id: String;
    var name: String = ""
    var investment: User = User()
    var min_amount: Int = 0
    var state:Int = 1

    constructor(_id: String, name: String, investment: User,  min_amount: Int, state:Int) : this() {
        this._id = _id;
        this.name = name;
        this.investment = investment;
        this.min_amount = min_amount;
        this.state = state;
    }
    constructor(){}
}