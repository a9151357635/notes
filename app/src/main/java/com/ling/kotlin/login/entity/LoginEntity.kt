package com.ling.kotlin.login.entity

import java.io.Serializable

data class LoginEntity(
    val expiration: String, //过期时间
    val issuedAt: String, //签发时间
    val issuer: String, //签发人
    val notBefore: String, //不在时间之前
    val perms: List<String>, //权限集合
    val refreshToken: String,//刷新token
    val roles: List<String>, //角色集合
    val token: String
):Serializable