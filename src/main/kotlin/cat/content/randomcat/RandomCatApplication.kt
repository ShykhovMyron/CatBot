package cat.content.randomcat

import lombok.AllArgsConstructor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
@AllArgsConstructor
class RandomCatApplication

fun main(args: Array<String>) {
    runApplication<RandomCatApplication>(*args)
}
