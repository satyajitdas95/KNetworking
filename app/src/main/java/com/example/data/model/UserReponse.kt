package com.example.data.model

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("results")
    var results: ArrayList<Users> = arrayListOf()
) {
    data class Users(
        @SerializedName("name") var name: Name? = Name(),
        @SerializedName("location") var location: Location? = Location(),
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("picture") var picture: Picture? = Picture(),
    )

    data class Picture (

        @SerializedName("large"     ) var large     : String? = null,
        @SerializedName("medium"    ) var medium    : String? = null,
        @SerializedName("thumbnail" ) var thumbnail : String? = null

    )

    data class Id (

        @SerializedName("name"  ) var name  : String? = null,
        @SerializedName("value" ) var value : String? = null

    )

    data class Dob (

        @SerializedName("date" ) var date : String? = null,
        @SerializedName("age"  ) var age  : Int?    = null

    )

    data class Location (

        @SerializedName("state"       ) var state       : String?      = null,
        @SerializedName("country"     ) var country     : String?      = null,
    )

    data class Name (

        @SerializedName("title" ) var title : String? = null,
        @SerializedName("first" ) var first : String? = null,
        @SerializedName("last"  ) var last  : String? = null

    )
}