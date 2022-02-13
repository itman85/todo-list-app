package com.picoder.sample.todolist.playground


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main() = runBlocking {
    main5(this)
    //main2(this)
}

fun main1(scope: CoroutineScope){
    // channel is hot stream, it will suspend send until receive called
    // use channel when we want to send data from a coroutine to other coroutine
    val channel = Channel<Int>()
    val job = scope.launch {
        for (x in 1..5) {
            println("start sending $x")
            channel.send(x * x) // NOTE: send will suspend until channel receive called in other coroutine
            println("Done sent $x")
            //println(channel.receive()) // if we call receive in same coroutine with send, it will never reach receive because it suspend at send, so we can only use channel in cross coroutines
        }
    }
    println("Done! Receive coroutine is active?: ${scope.isActive} / Send coroutine is active?: ${job.isActive}")
    /*for (x in 1..5) {
        delay(1000)
        println("listening $x")
    }*/
    /*for (x in 1..10) {
        println("Coroutine is completed?: ${job.isCompleted} / Coroutine is active?: ${job.isActive}")
        println(channel.receive()) // channel receive also suspend until send called
    }
    println("Done! Run blocking coroutine is active?: ${scope.isActive}") // at this point the job is completed but this runBlocking not done yet because will waiting for receiving from channel
    */

    /*
    // delay before receive value
    delay(1000) // delay 1s
    println(channel.receive()) // receive 1
    delay(1000) // delay 1s
    println(channel.receive()) // receive 2
    delay(1000) // delay 1s
    println(channel.receive()) // receive 3
    delay(1000) // delay 1s
    println(channel.receive()) // receive 4
    delay(1000) // delay 1s
    println(channel.receive()) // receive 5
    delay(1000) // if not delay at here the job will not completed at this point
    println("Done! Channel is empty?: ${channel.isEmpty} / Coroutine is completed?: ${job.isCompleted} / Coroutine is active?: ${job.isActive}")

     */
}

// Iteration over channel
suspend fun main2(scope: CoroutineScope) {
    val channel = Channel<Int>()
    scope.launch {
        for (x in 1..5) {
            println("start sending $x")
            channel.send(x * x)
            println("Done sent $x")
            delay(1000)
        }
        channel.close() // call this to close channel and finish for loop below and Process finished with exit code 0
    }
    for (value in channel) println(value)
    println("Done!") // this line never print out because for (value in channel) will loop forever until channel closed
}

// throw ClosedReceiveChannelException
suspend fun main3(scope: CoroutineScope) {
    val channel = Channel<Int>()
    scope.launch {
        for (x in 1..5) channel.send(x * x)
        channel.close() // close after send value 5th
    }
    for (y in 1..10) println(channel.receive()) // receive 10 value although it send 5 value only
    println("Done!")
}

// throw ClosedSendChannelException
suspend fun main4(scope: CoroutineScope) {
    val channel = Channel<Int>()
    scope.launch {
        for (i in 1..10) {
            if (i == 5) channel.close() // close at value 5th
            channel.send(i * i) // but still call send at value 5th-> throw ClosedSendChannelException
        }
    }
    for (y in 1..5) {
        println(channel.receive())
    }
    println("Done!")
}

// custom exception as close channel
suspend fun main5(scope: CoroutineScope) {
    val channel = Channel<Int>()
    scope.launch {
        for (i in 1..10) {
            if (i == 5) channel.close(Throwable("Channel already close,should not send or receive anymore")) // close at value 5th
            channel.send(i * i) // but still call send at value 5th-> throw custom exception
        }
    }
    for (y in 1..5) {
        println(channel.receive())
    }
    println("Done!")
}