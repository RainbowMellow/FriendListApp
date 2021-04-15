package com.example.listapp.Model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

class Friends {

    @RequiresApi(Build.VERSION_CODES.O)
    private val friends = createFriends()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFriends(): ArrayList<BEFriend>
    {
        val friendList = ArrayList<BEFriend>()

        //Mockdata from Mockaroo.com
        friendList.add(BEFriend(name = "Emyle Chowne", phone = "51887794", address = "461 Crest Line Junction", email = "email.email@hotmail.com", url = "http://www.easv.dk", location = Pair(39.421998333333335, -130.084), birthday = LocalDate.now(), picture = null))
        /**
        friendList.add(BEFriend(name = "Aida Gatchell", phone = "73573619", address = "41300 Sullivan Avenue", email = "email.email@hotmail.com", url = "http://www.easv.dk", location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Munmro Retchless", phone = "14599565", address = "1156 Elmside Parkway", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-13"), picture = null))
        friendList.add(BEFriend(name = "Jory Songest", phone = "36023595", address = "765 Meadow Valley Lane", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-12"), picture = null))
        friendList.add(BEFriend(name = "Petronia Folke", phone = "82520137", address = "631 Kenwood Hill", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Kalli Stedman", phone = "93499806", address = "7170 Warrior Terrace", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Bel Axton", phone = "33298866", address = "485 Dexter Street", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Tomlin Pech", phone = "78122877", address = "92 Carey Avenue", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Myranda Flaws", phone = "43452555", address = "8730 Hintze Parkway", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Oliviero Trehearn", phone = "67141073", address = "2 Blaine Trail", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Shelli Skeeles", phone = "23289042", address = "49487 Carpenter Street", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Abagael McFade", phone = "87829975", address = "68 Haas Drive", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Mufinella Uzelli", phone = "73150808", address = "6510 Westerfield Plaza", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Lisle Bragge", phone = "90542401", address = "7271 Fieldstone Circle", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Sheree Brewers", phone = "85949752", address = "8286 Little Fleur Plaza", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Buckie Shillito", phone = "59547916", address = "395 Sage Circle", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Jean Swaton", phone = "71287577", address = "763 Fair Oaks Drive", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Eleanore Humber", phone = "32129350", address = "3 Spaight Drive", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Giordano Gibson", phone = "30520833", address = "00667 Carey Way", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Ediva Hodinton", phone = "60373534", address = "794 Northfield Lane", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Laure Aleksahkin", phone = "82136435", address = "05564 Cody Plaza", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Pearla Pelzer", phone = "64082535", address = "9406 Jenna Park", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Ernesta Waddington", phone = "76375361", address = "59 Arkansas Point", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Cordie Belle", phone = "56226913", address = "17044 Twin Pines Point", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        friendList.add(BEFriend(name = "Temple Slaymaker", phone = "52453808", address = "8960 Miller Court", email = "email.email@hotmail.com", url = "http://www.easv.dk",location = null, birthday = LocalDate.parse("2021-04-03"), picture = null))
        **/
        return friendList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): ArrayList<BEFriend>
    {
        return friends
    }
}