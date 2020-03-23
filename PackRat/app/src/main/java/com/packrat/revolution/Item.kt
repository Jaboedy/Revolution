package com.packrat.revolution

data class Item(var id: Int, var bcode: String, var name: String, var desc: String) {
    var ItemId = id
    var Barcode = bcode
    var ItemName = name
    var ItemDescription = desc
}