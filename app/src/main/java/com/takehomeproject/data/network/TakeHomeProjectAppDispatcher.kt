package com.takehomeproject.data.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val takeHomeAppDispatchers: TakeHomeProjectAppDispatchers)

enum class TakeHomeProjectAppDispatchers {
    IO,
}