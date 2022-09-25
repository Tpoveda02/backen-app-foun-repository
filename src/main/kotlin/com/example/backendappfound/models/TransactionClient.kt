package com.example.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import lombok.Data

@Data
@Document("transactionClient")
class TransactionClient{
    @Id
    lateinit var _id: String;
    var client: User = User()
    var type_action: String = ""
    var amount:Int = 0
    var state:Int = 1
    constructor()
    constructor(_id:String,client: User, type_action: String, amount:Int, state:Int) : this() {
        this._id = _id;
        this.client = client;
        this.type_action = type_action;
        this.amount = amount;
        this.state = state;
    }
}
