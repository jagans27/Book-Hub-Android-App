package com.jagan.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity (
    @PrimaryKey val book_id: Int,
    @ColumnInfo(name="book_name") val bookNames:String,
    @ColumnInfo(name="book_author") val bookAuthors:String,
    @ColumnInfo(name="book_price") val bookPrices:String,
    @ColumnInfo(name="book_rating") val bookRatings:String,
    @ColumnInfo(name="book_desc") val bookDescs:String,
    @ColumnInfo(name="book_image") val bookImages:String
)