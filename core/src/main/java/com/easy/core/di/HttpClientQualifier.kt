package com.easy.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BasicHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RPCHttpClient
