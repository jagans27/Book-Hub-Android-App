package com.jagan.bookhub.database

import androidx.room.*

@Dao
interface BookDao {

    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks() : List<BookEntity>

    @Query("SELECT * FROM books WHERE book_id=:bookId")
    fun getBookById(bookId:String) : BookEntity

}