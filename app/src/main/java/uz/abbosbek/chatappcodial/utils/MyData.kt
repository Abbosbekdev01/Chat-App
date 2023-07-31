package uz.abbosbek.chatappcodial.utils

import com.google.firebase.database.DatabaseReference
import uz.abbosbek.chatappcodial.models.Group
import uz.abbosbek.chatappcodial.models.User

object MyData {
    var USER = User()
    var screenLengthItem = 1
    var chatReference: DatabaseReference? = null
    var userList = ArrayList<User>()
    var GROUP = Group()

    var TYPE_TEXT = "text"
    var TYPE_IMAGE = "image"


    fun getSenderUser(id:String):User{
        userList.forEach {
            if (id == it.uid) return it
        }
        return USER
    }
}