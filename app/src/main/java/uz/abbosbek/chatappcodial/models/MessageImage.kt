package uz.abbosbek.chatappcodial.models

class MessageImage {
    var id: String? = null
    var imageLink: String? = null

    constructor(id: String?, imageLink: String?) {
        this.id = id
        this.imageLink = imageLink
    }

    constructor()

}