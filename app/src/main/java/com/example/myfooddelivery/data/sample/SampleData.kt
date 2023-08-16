package com.example.myfooddelivery.data.sample

import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Account
import com.example.myfooddelivery.data.db.entities.Address
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.Ratings
import com.example.myfooddelivery.constants.FoodCategories
import com.example.myfooddelivery.ui.helper.DatabaseProvider


object SampleData {

    private lateinit var databaseProvider: DatabaseProvider
    private fun createSampleCustomers() {
        val address = Address(14, "Abc Street", "Adayar", "Tamil Nadu", 605003)
        val customer = CustomerAccount(
            "Luffy",
            address,
            "8989878989",
            Account.PasswordWrapper("Pirate1!"),
            listOf()
        )
        databaseProvider.customerAccountRepository.insertAccount(customer)
    }

    private fun createSampleHotels() {
        val addressBaratie = Address(32, "kjdbsa", "Guduvanchery", "kjbsd", 605001)
        val hotelBaratie = HotelAccount(
            "Baratie",
            addressBaratie,
            "7878787878",
            Account.PasswordWrapper("Allblue1!"),
            true,
            R.drawable.hotel_baratie,
            listOf(FoodCategories.RICE, FoodCategories.NAAN, FoodCategories.PANEER, FoodCategories.PARATHA),
            Ratings(340, 4.5f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelBaratie)

        val addressKfc = Address(32, "kjdbsa", "Marina", "kjbsd", 605002)
        val hotelKfc = HotelAccount(
            "KFC",
            addressKfc,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            false,
            R.drawable.hotel_kfc,
            listOf(FoodCategories.BIRYANI, FoodCategories.CHICKEN, FoodCategories.FRIES),
            Ratings(400, 4.8f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelKfc)

        val addressMcDonalds = Address(42, "dsaf", "Lougetown", "kjbsd", 605002)
        val hotelMcDonalds = HotelAccount(
            "McDonald's",
            addressMcDonalds,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            false,
            R.drawable.hotel_mcdonalds,
            listOf(FoodCategories.BURGER, FoodCategories.CHICKEN, FoodCategories.PIZZA),
            Ratings(290, 4.7f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelMcDonalds)

        val addressBurgerKing = Address(42, "dsaf", "Potheri", "Potheri", 605002)
        val hotelBurgerKing = HotelAccount(
            "Burger King",
            addressBurgerKing,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            false,
            R.drawable.hotel_burger_king,
            listOf(FoodCategories.BURGER, FoodCategories.CHICKEN, FoodCategories.PIZZA),
            Ratings(100, 4.2f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelBurgerKing)

        val addressAnjappar = Address(42, "dsaf", "Thambaram", "kjbsd", 605002)
        val hotelAnjappar = HotelAccount(
            "Anjappar",
            addressAnjappar,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            false,
            R.drawable.hotel_anjappar,
            listOf(FoodCategories.BIRYANI, FoodCategories.CHICKEN, FoodCategories.FRIES),
            Ratings(120, 3.6f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelAnjappar)

        val addressSubway = Address(42, "dsaf", "Anna Nagar", "kjbsd", 605002)
        val hotelSubway = HotelAccount(
            "Subway",
            addressSubway,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            true,
            R.drawable.hotel_subway,
            listOf(FoodCategories.RICE, FoodCategories.NAAN, FoodCategories.PANEER, FoodCategories.PARATHA),
            Ratings(260, 4.5f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelSubway)

        val addressFoodParadise = Address(42, "dsaf", "White Town", "kjbsd", 605002)
        val hotelFoodParadise = HotelAccount(
            "Food Paradise",
            addressFoodParadise,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            false,
            R.drawable.hotel_food_paradise,
            listOf(FoodCategories.BIRYANI, FoodCategories.CHICKEN, FoodCategories.FRIES),
            Ratings(180, 3.5f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelFoodParadise)

        val addressPizzaHut = Address(42, "dsaf", "Auroville", "kjbsd", 605002)
        val hotelPizzaHut = HotelAccount(
            "Dominos",
            addressPizzaHut,
            "7878787879",
            Account.PasswordWrapper("Allblue1!"),
            true,
            R.drawable.hotel_pizza_hut,
            listOf(FoodCategories.RICE, FoodCategories.NAAN, FoodCategories.PANEER, FoodCategories.PARATHA),
            Ratings(200, 3.7f)
        )
        databaseProvider.hotelAccountRepository.insertAccount(hotelPizzaHut)
    }

    private fun createSampleItems() {
        val item11 = Item("Sambar rice", 60, true, hotelId = 1, R.drawable.item_sambar_rice , FoodCategories.RICE, "Hot and Spicy dish prepared with dhal and other ingredients which are good for health", itemId = 1)
        val item12 = Item("Butter Naan", 40, true, hotelId = 1, R.drawable.item_butter_naan, FoodCategories.NAAN, "Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item13 = Item("Paneer Butter Masala", 80, true, hotelId = 1, R.drawable.item_paneer_butter_masala, FoodCategories.PANEER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item14 = Item("Curd Rice", 30, true, hotelId = 1, R.drawable.item_curd_rice, FoodCategories.RICE,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item15 = Item("Paratha", 50, true, hotelId = 1, R.drawable.item_parotta, FoodCategories.PARATHA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item16 = Item("Paneer Tikka", 100, true, hotelId = 1, R.drawable.item_paneer_tikka, FoodCategories.PANEER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item21 = Item("Chicken Biryani", 140, false, hotelId = 2, R.drawable.item_chicken_biryani,
            FoodCategories.BIRYANI,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item22 = Item("Mutton Biryani", 200, false, hotelId = 2, R.drawable.item_mutton_biryani, FoodCategories.BIRYANI,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item23 = Item("Fried Chicken", 250, false, hotelId = 2, R.drawable.item_fried_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item24 = Item("Chicken Nuggets", 180, false, hotelId = 2, R.drawable.item_chicken_nuggets, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item25 = Item("Peri Peri Fries", 110, true, hotelId = 2, R.drawable.item_peri_peri_fries, FoodCategories.FRIES,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item26 = Item("Crispy Fries", 90, true, hotelId = 2, R.drawable.item_crispy_fries, FoodCategories.FRIES,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item31 = Item("Veg Mini Burger",70, true, hotelId = 3, R.drawable.item_veg_mini_burger, FoodCategories.BURGER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item32 = Item("Veg Max Burger", 130, false, hotelId = 3, R.drawable.item_veg_max_burger, FoodCategories.BURGER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item33 = Item("Chicken Burger", 150, false, hotelId = 3, R.drawable.item_chicken_burger, FoodCategories.BURGER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item34 = Item("Popcorn Chicken", 160, false, hotelId = 3, R.drawable.item_popcorn_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item35 = Item("Dragon Chicken", 200, false, hotelId = 3, R.drawable.item_dragon_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item36 = Item("Veg Pizza", 100, true, hotelId = 3, R.drawable.item_veg_pizza, FoodCategories.PIZZA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item37 = Item("Veg Cheese Pizza", 100, true, hotelId = 3, R.drawable.item_veg_cheese_pizza, FoodCategories.PIZZA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item41 = Item("Veg Burger",70, true, hotelId = 4, R.drawable.item_veg_mini_burger, FoodCategories.BURGER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item42 = Item("Veg Burger", 130, false, hotelId = 4, R.drawable.item_veg_max_burger, FoodCategories.BURGER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item43 = Item("Chicken Zinger Burger", 150, false, hotelId = 4, R.drawable.item_chicken_burger, FoodCategories.BURGER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item44 = Item("crispy Chicken", 160, false, hotelId = 4, R.drawable.item_popcorn_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item45 = Item("Flame Chicken", 200, false, hotelId = 4, R.drawable.item_dragon_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item46 = Item("Veg Corn Pizza", 100, true, hotelId = 4, R.drawable.item_veg_pizza, FoodCategories.PIZZA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item47 = Item("Veg Tomato Pizza", 100, true, hotelId = 4, R.drawable.item_veg_cheese_pizza, FoodCategories.PIZZA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item51 = Item("Chicken 65 Biryani", 160, false, hotelId = 5, R.drawable.item_chicken_biryani,
            FoodCategories.BIRYANI,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item52 = Item("Mutton 65 Biryani", 230, false, hotelId = 5, R.drawable.item_mutton_biryani, FoodCategories.BIRYANI,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item53 = Item("Chicken Chukka", 250, false, hotelId = 5, R.drawable.item_fried_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item54 = Item("Chicken Fry", 180, false, hotelId = 5, R.drawable.item_chicken_nuggets, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item55 = Item("Salty Fries", 60, true, hotelId = 5, R.drawable.item_peri_peri_fries, FoodCategories.FRIES,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item56 = Item("Tangy Fries", 70, true, hotelId = 5, R.drawable.item_crispy_fries, FoodCategories.FRIES,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item61 = Item("Parupu rice", 50, true, hotelId = 6, R.drawable.item_sambar_rice , FoodCategories.RICE, "Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item62 = Item("Naan", 30, true, hotelId = 6, R.drawable.item_butter_naan, FoodCategories.NAAN, "Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item63 = Item("Paneer Gravy", 70, true, hotelId = 6, R.drawable.item_paneer_butter_masala, FoodCategories.PANEER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item64 = Item("Special Curd Rice", 40, true, hotelId = 6, R.drawable.item_curd_rice, FoodCategories.RICE,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item65 = Item("Aloo Paratha", 60, true, hotelId = 6, R.drawable.item_parotta, FoodCategories.PARATHA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item66 = Item("Paneer Spicy masala", 90, true, hotelId = 6, R.drawable.item_paneer_tikka, FoodCategories.PANEER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item71 = Item("Chicken Dum Biryani", 155, false, hotelId = 7, R.drawable.item_chicken_biryani,
            FoodCategories.BIRYANI,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item72 = Item("Mutton Dum Biryani", 225, false, hotelId = 7, R.drawable.item_mutton_biryani, FoodCategories.BIRYANI,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item73 = Item("Hariyali Chicken", 250, false, hotelId = 7, R.drawable.item_fried_chicken, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item74 = Item("Andhra Chicken", 180, false, hotelId = 7, R.drawable.item_chicken_nuggets, FoodCategories.CHICKEN,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item75 = Item("Potato Fries", 40, true, hotelId = 7, R.drawable.item_peri_peri_fries, FoodCategories.FRIES,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item76 = Item("French Fries", 60, true, hotelId = 7, R.drawable.item_crispy_fries, FoodCategories.FRIES,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        val item81 = Item("Lemon Rice", 50, true, hotelId = 8, R.drawable.item_sambar_rice , FoodCategories.RICE, "Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item82 = Item("Garlic Naan", 30, true, hotelId = 8, R.drawable.item_butter_naan, FoodCategories.NAAN, "Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item83 = Item("Kadai Paneer", 70, true, hotelId = 8, R.drawable.item_paneer_butter_masala, FoodCategories.PANEER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item84 = Item("Coconut Rice", 40, true, hotelId = 8, R.drawable.item_curd_rice, FoodCategories.RICE,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item85 = Item("Bun Paratha", 60, true, hotelId = 8, R.drawable.item_parotta, FoodCategories.PARATHA,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")
        val item86 = Item("Palak Paneer", 90, true, hotelId = 8, R.drawable.item_paneer_tikka, FoodCategories.PANEER,"Hot and Spicy dish prepared with dhal and other ingredients which are good for health")

        databaseProvider.itemRepository.insertItem(item11)
        databaseProvider.itemRepository.insertItem(item12)
        databaseProvider.itemRepository.insertItem(item13)
        databaseProvider.itemRepository.insertItem(item14)
        databaseProvider.itemRepository.insertItem(item15)
        databaseProvider.itemRepository.insertItem(item16)

        databaseProvider.itemRepository.insertItem(item21)
        databaseProvider.itemRepository.insertItem(item22)
        databaseProvider.itemRepository.insertItem(item23)
        databaseProvider.itemRepository.insertItem(item24)
        databaseProvider.itemRepository.insertItem(item25)
        databaseProvider.itemRepository.insertItem(item26)

        databaseProvider.itemRepository.insertItem(item31)
        databaseProvider.itemRepository.insertItem(item32)
        databaseProvider.itemRepository.insertItem(item33)
        databaseProvider.itemRepository.insertItem(item34)
        databaseProvider.itemRepository.insertItem(item35)
        databaseProvider.itemRepository.insertItem(item36)
        databaseProvider.itemRepository.insertItem(item37)

        databaseProvider.itemRepository.insertItem(item41)
        databaseProvider.itemRepository.insertItem(item42)
        databaseProvider.itemRepository.insertItem(item43)
        databaseProvider.itemRepository.insertItem(item44)
        databaseProvider.itemRepository.insertItem(item45)
        databaseProvider.itemRepository.insertItem(item46)
        databaseProvider.itemRepository.insertItem(item47)

        databaseProvider.itemRepository.insertItem(item51)
        databaseProvider.itemRepository.insertItem(item52)
        databaseProvider.itemRepository.insertItem(item53)
        databaseProvider.itemRepository.insertItem(item54)
        databaseProvider.itemRepository.insertItem(item55)
        databaseProvider.itemRepository.insertItem(item56)

        databaseProvider.itemRepository.insertItem(item61)
        databaseProvider.itemRepository.insertItem(item62)
        databaseProvider.itemRepository.insertItem(item63)
        databaseProvider.itemRepository.insertItem(item64)
        databaseProvider.itemRepository.insertItem(item65)
        databaseProvider.itemRepository.insertItem(item66)

        databaseProvider.itemRepository.insertItem(item71)
        databaseProvider.itemRepository.insertItem(item72)
        databaseProvider.itemRepository.insertItem(item73)
        databaseProvider.itemRepository.insertItem(item74)
        databaseProvider.itemRepository.insertItem(item75)
        databaseProvider.itemRepository.insertItem(item76)

        databaseProvider.itemRepository.insertItem(item81)
        databaseProvider.itemRepository.insertItem(item82)
        databaseProvider.itemRepository.insertItem(item83)
        databaseProvider.itemRepository.insertItem(item84)
        databaseProvider.itemRepository.insertItem(item85)
        databaseProvider.itemRepository.insertItem(item86)
    }


//    private fun createSampleDeliveryPersons() {
//        val address = Address(32, "qwert", "dfnsgj", "jknafd", 605007)
//        val d = DeliveryPersonAccount(
//            "itachi",
//            address,
//            "8989898980",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(1, 2, 3)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d)
//        val d1 = DeliveryPersonAccount(
//            "sasuke",
//            address,
//            "8989898981",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(5, 6, 7)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d1)
//        val d2 = DeliveryPersonAccount(
//            "naruto",
//            address,
//            "8989898982",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(7, 8, 9)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d2)
//        val d3 = DeliveryPersonAccount(
//            "kakashi",
//            address,
//            "8989898983",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(2, 3, 4)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d3)
//        val d4 = DeliveryPersonAccount(
//            "sakura",
//            address,
//            "8989898984",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(8, 9, 10)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d4)
//        val d5 = DeliveryPersonAccount(
//            "lee",
//            address,
//            "8989898985",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(4, 5, 6)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d5)
//        val d6 = DeliveryPersonAccount(
//            "neji",
//            address,
//            "8989898986",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(8, 9, 10)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d6)
//        val d7 = DeliveryPersonAccount(
//            "obito",
//            address,
//            "8989898987",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(1, 2, 3)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d7)
//        val d8 = DeliveryPersonAccount(
//            "rin",
//            address,
//            "8989898988",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(2, 3, 4)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d8)
//        val d9 = DeliveryPersonAccount(
//            "gaara",
//            address,
//            "8989898989",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(6, 7, 8)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d9)
//        val d10 = DeliveryPersonAccount(
//            "jiraya",
//            address,
//            "8989898999",
//            Account.PasswordWrapper("Sharingan1!"),
//            listOf(3, 4, 5)
//        )
//        databaseProvider.deliveryPersonAccountRepository.insertAccount(d10)
//    }

    fun createSampleData(databaseProvider: DatabaseProvider) {
        this.databaseProvider = databaseProvider
        createSampleCustomers()
        createSampleHotels()
        //createSampleDeliveryPersons()
        createSampleItems()
    }
}