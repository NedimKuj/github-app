package com.nedkuj.github.model.base


data class PermissionResultEntity(
    var permissions: List<String>,
    var requestCode: Int = 0,
    var result: Boolean = false)
