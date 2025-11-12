package com.goldenoaks.library

import android.app.Application
import com.goldenoaks.library.data.repository.BookRepository
import com.goldenoaks.library.data.repository.CopyRepository
import com.goldenoaks.library.data.repository.MemberRepository
import com.goldenoaks.library.data.repository.UserRepository
import com.goldenoaks.library.util.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LibraryApplication : Application() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var bookRepository: BookRepository

    @Inject
    lateinit var copyRepository: CopyRepository

    @Inject
    lateinit var memberRepository: MemberRepository

    override fun onCreate() {
        super.onCreate()
        // Note: Hilt injection happens after onCreate, so we'll initialize in MainActivity
    }
}

