package uz.abbosbek.chatappcodial.models

import java.io.Serializable

class User: Serializable {
    var uid:String?=null
    var name:String?=null
    var imageLink:String?=null
    var email:String?=null
    var statusTime:String?=null
    var isOnline:String?=null
    var token:String?=null



    constructor()
    constructor(
        uid: String?,
        name: String?,
        imageLink: String?,
        email: String?,
        statusTime: String?,
        isOnline: String?,
        token: String?,
    ) {
        this.uid = uid
        this.name = name
        this.imageLink = imageLink
        this.email = email
        this.statusTime = statusTime
        this.isOnline = isOnline
        this.token = token
    }
}