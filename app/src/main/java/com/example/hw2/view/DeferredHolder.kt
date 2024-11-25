package com.example.hw2.view

import kotlinx.coroutines.Deferred

data class DeferredHolder<T>(var deferred: Deferred<T>)