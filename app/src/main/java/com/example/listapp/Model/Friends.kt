package com.example.listapp.Model

class Friends {

    private val friends = createFriends()

    private fun createFriends(): ArrayList<BEFriend>
    {
        val friendList = ArrayList<BEFriend>()
        friendList.add(BEFriend(name = "Tiffany", phone = "123", address = "Applestreet 22, 6580", isFavorite = true))
        friendList.add(BEFriend(name = "Anders", phone = "1234", address = "Applestreet 21, 6580", isFavorite = false))
        friendList.add(BEFriend(name = "Hans", phone = "12345", address = "Applestreet 23, 6580", isFavorite = true))
        friendList.add(BEFriend(name = "Andreas", phone = "123456", address = "Applestreet 24, 6580", isFavorite = false))
        friendList.add(BEFriend(name = "Anders And", phone = "123", address = "Applestreet 25, 6580", isFavorite = true))
        friendList.add(BEFriend(name = "Minnie", phone = "1234", address = "Applestreet 26, 6580", isFavorite = false))
        friendList.add(BEFriend(name = "Mickey", phone = "12345", address = "Applestreet 27, 6580", isFavorite = true))
        friendList.add(BEFriend(name = "Fedtmule", phone = "123456", address = "Applestreet 28, 6580", isFavorite = false))
        friendList.add(BEFriend(name = "Pluto", phone = "123", address = "Applestreet 29, 6580", isFavorite = true))
        friendList.add(BEFriend(name = "Sophie", phone = "1234", address = "Applestreet 20, 6580", isFavorite = false))
        friendList.add(BEFriend(name = "Ariel", phone = "12345", address = "Applestreet 21, 6580", isFavorite = true))
        friendList.add(BEFriend(name = "Mulan", phone = "123456", address = "Applestreet 25, 6580", isFavorite = false))

        return friendList
    }

    fun getAllNames(): ArrayList<String>
    {
        val friendNames = ArrayList<String>()

        friends.forEach { f ->
            friendNames.add(f.name)
        }

        return friendNames
    }

    fun getAll(): ArrayList<BEFriend>
    {
        return friends
    }
}