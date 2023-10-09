package com.example.data.model

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("page"        ) var page       : Int?            = null,
    @SerializedName("per_page"    ) var perPage    : Int?            = null,
    @SerializedName("total"       ) var total      : Int?            = null,
    @SerializedName("total_pages" ) var totalPages : Int?            = null,
    @SerializedName("data"        ) var data       : List<Users> = listOf(),
    @SerializedName("support"     ) var support    : Support?        = Support()
) {

    data class Users (

        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("email"      ) var email     : String? = null,
        @SerializedName("first_name" ) var firstName : String? = null,
        @SerializedName("last_name"  ) var lastName  : String? = null,
        @SerializedName("avatar"     ) var avatar    : String? = null

    ){

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

    )}

    data class Support (

        @SerializedName("url"  ) var url  : String? = null,
        @SerializedName("text" ) var text : String? = null

    )
}