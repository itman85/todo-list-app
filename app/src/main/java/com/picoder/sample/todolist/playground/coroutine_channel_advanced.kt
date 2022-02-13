package com.picoder.sample.todolist.playground

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    //produceFun(this)
    //actorFun(this)
    //bufferedChannel(this)
    //conflatedChannel(this)
    unlimitedChannel(this)
}


@ExperimentalCoroutinesApi
suspend fun produceFun(scope: CoroutineScope) {
    val squares: ReceiveChannel<Int> = scope.produce {
        // in produce can use methods of SendChannel, and return ReceiveChannel
        for (x in 1..5) {
            send(x * x) // this is Rendezvous channel (Unbuffered channel / Default channel)
            println("Sent $x")
            delay(1000)
        }
        // this is end of block so it will send special signal to close ReceiveChannel
    }
    // consumeEach is extension function of ReceiveChannel.
    // it help to receive value and do action for this value
    squares.consumeEach {
        println("Received $it")
    }
    // NOTE: squares is ReceiveChannel will receive special signal from scope.produce to close ReceiveChannel
    // that why this ReceiveChannel will know when finish consume and the process finished with exit code
    // produce call send but if not call squares.consumeEach (not receive) this process will suspend forever, never print Done!
    println("Done!")
}

@ObsoleteCoroutinesApi
suspend fun actorFun(scope: CoroutineScope) {
    // NOTE actor is ObsoleteCoroutinesApi, so maybe deprecated in the future
    val myActor: SendChannel<Int> = scope.actor {
        for (value in channel)
            println("Received $value")
        //val value = receive()     // it will receive value at here
        // println("Received $value")

        // NOTE: Iteration over channel will make this block code run until myActor.close()
        // so if call receive one time and this block code completed, then it will throw exception if it will call myActor.send(x)
    }
    for (x in 1..5) {
        myActor.send(x)
        println("Sent $x")
        delay(1000)
    }
    myActor.close()
    println("Done!")
}
// 4 types of channel: default (RENDEZVOUS, unbuffered) channel, buffered channel, conflated Channel, unlimited Channel
suspend fun bufferedChannel(scope: CoroutineScope) {
    //val channel = Channel<Int>() // default channel with RENDEZVOUS capacity = 0
    //val channel = Channel<Int>(capacity = Channel.RENDEZVOUS) // RENDEZVOUS capacity = 0
    val channel = Channel<Int>(capacity = 4) // create 1 buffered channel with capacity = 4, it store in array
    val senderJob = scope.launch {
        // launch 1 coroutine to send data
        repeat(10) { // send 10 data
            channel.send(it) // send function will be suspend as buffer is full
            println("Sending $it") // it will print out sending to value 4th then this buffer is full, it will suspend at line 'channel.send(it)'
        }
    }

    // delay to see if send is suspend after print log for value 4th (because buffer capacity = 4)
    delay(1000)
    senderJob.cancel() // cancel sender coroutine if not it will suspend forever
}

suspend fun conflatedChannel(scope: CoroutineScope) {
    val channel = Channel<Int>(Channel.CONFLATED) // it will override value in channel, so only last data sent will keep in channel
    val sender = scope.launch {
        repeat(5) { // send 5 value
            println("Sending $it")
            channel.send(it)
        }
        channel.close()// not call close at here, it will suspend at line channel.consumeEach forever
        delay(10000) // call sender.cancel() will finish this coroutine instead of waiting delay 10s
    }
    delay(1000) // delay 1s to wait it send all 5 value then will receive
    channel.consumeEach { println("item = $it") } // receive all value but print last value only
    sender.cancel() // cancel sender coroutine, actually cancel here is useless because it suspend at line consumeEach
}

suspend fun unlimitedChannel(scope: CoroutineScope) {
    val channel = Channel<Int>(Channel.UNLIMITED)// buffer is store by linkedlist, so it will unlimited, and throw out of memory exception
    val sender = scope.launch {
        repeat(7) { // send 7 data
            channel.send(it) // it will sent all data without suspend because buffer is unlimited
            println("sent $it")
        }
        channel.close() // channel close here although the channel not call receive yet, but it still receive 7 data before close
        // we need to understand close is send special signal to channel, as a last element in channel, so it will receive one by one until reach this special signal it will not receive anymore, throw exception is still call receive
        println("channel closed")
        delay(10000)
    }
    // delay 1s to see if channel send is suspend
    delay(1000)
    println("start receive")
    // if use consume, it need to call channel.close() as finishing sending
    //channel.consumeEach { println("received $it") }
    // repeat receive exact 7 data same to sent 7 data, then no send or receive suspend
    // if repeat receive 8 data, while only send 7 data, then it will receive suspend
    repeat(7) { // receive 7 data
        val value = channel.receive()
        println("received $value")
    }
    sender.cancel() // cancel sender coroutine so it will not wait delay 10s
}