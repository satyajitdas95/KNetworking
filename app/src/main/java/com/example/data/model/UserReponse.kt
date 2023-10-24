package com.example.data.model

import com.google.gson.annotations.SerializedName

class UserResponse(

    @SerializedName("page") var page: Number? = null,
    @SerializedName("per_page") var perPage: Number? = null,
    @SerializedName("total") var total: Number? = null,
    @SerializedName("total_pages") var totalPages: Number? = null,
    @SerializedName("data") var data: List<Users> = arrayListOf(),
    @SerializedName("support") var support: Support? = Support()

) {
    data class Users(

        @SerializedName("id") var id: Int? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("first_name") var firstName: String? = null,
        @SerializedName("last_name") var lastName: String? = null,
        @SerializedName("avatar") var avatar: String? = null

    )

    data class Support(

        @SerializedName("url") var url: String? = null,
        @SerializedName("text") var text: String? = null

    )

}