package cat.content.randomcat.command

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import io.github.bucket4j.local.SynchronizationStrategy.SYNCHRONIZED
import java.time.Duration

private val actualChatLimits = mutableMapOf<Pair<Long, String>, Bucket>()
private val bandwidth = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)))

fun checkChat(userId: Long, command: String, consume: Long = 1): Boolean {
    val bucket = actualChatLimits.getOrPut(userId to command) {
        Bucket.builder().addLimit(bandwidth).withSynchronizationStrategy(
            SYNCHRONIZED
        ).build()
    }
    return if (consume == 0L) true
    else bucket.tryConsume(consume)
}

fun checkTimeLeft(userId: Long, command: String): Long {
    checkChat(userId, command, 0)
    val bucket = actualChatLimits[userId to command]!!
    return bucket.estimateAbilityToConsume(1).nanosToWaitForRefill / 1000000000L
}