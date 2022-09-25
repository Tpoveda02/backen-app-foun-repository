package com.example.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import lombok.Data

@Data
@Document("transaction")
class Transaction {
    @Id
    lateinit var _id: String;
    var client: User = User()
    var investment: Investment = Investment()
    var type_action: String = ""
    var amount:Int = 0
    var state:Int = 1

    constructor(_id:String,client: User, investment: Investment, type_action: String, amount:Int, state:Int) : this() {
        this._id = _id;
        this.client = client;
        this.investment = investment;
        this.type_action = type_action;
        this.amount = amount;
        this.state = state;
    }
    constructor(){

    }

}