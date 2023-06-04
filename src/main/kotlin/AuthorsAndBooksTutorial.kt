@file:Suppress("DataClassPrivateConstructor")

import arrow.core.*
import arrow.core.raise.*

object EmptyAuthorName

data class Author private constructor(val name: String) {
    companion object {
        operator fun invoke(name: String): Either<EmptyAuthorName, Author> = either {
            ensure(name.isNotEmpty()) { EmptyAuthorName }
            Author(name)
        }

        operator fun unaryMinus() {}
    }
}

sealed interface BookValidationError
object EmptyTitle : BookValidationError
object NoAuthors : BookValidationError
data class EmptyAuthor(val index: Int) : BookValidationError

data class Book private constructor(val title: String, val authors: NonEmptyList<Author>) {
    companion object {
        operator fun invoke(
            title: String, authors: Iterable<String>
        ): Either<NonEmptyList<BookValidationError>, Book> = either {
            zipOrAccumulate(
                { ensure(title.isNotEmpty()) { EmptyTitle } },
                {
                    val validatedAuthors = mapOrAccumulate(authors.withIndex()) {
                        Author(it.value)
                            .recover { _ -> raise(EmptyAuthor(it.index)) }
                            .bind()
                    }
                    ensureNotNull(validatedAuthors.toNonEmptyListOrNull()) { NoAuthors }
                }
            ) { _, authorsNel ->
                Book(title, authorsNel)
            }
        }

        fun workAround(title: String, authors: Iterable<String>): EitherNel<BookValidationError, Book> = either {
            zipOrAccumulate(
                { ensure(title.isNotEmpty()) { EmptyTitle } },
                {
                    val validatedAuthors = authors.withIndex().mapOrAccumulate {
                        Author(it.value)
                            .recover { _ -> raise(EmptyAuthor(it.index)) }
                            .bind()
                    }
                    ensureNotNull(validatedAuthors.toNonEmptyListOrNull()) { NoAuthors }
                }
            ) { _, authorsNel ->
                Book(title, authorsNel)
            }
        }
    }
}

fun main() {
    println(Book("The Universe And I", listOf("Olaf")))
    println(Book("", listOf("Olaf")))
    println(Book("", emptyList()))
    println(Book("The Universe", listOf("")))
    println(Book("The Universe", emptyList()))
    println(Book("", listOf("Olaf", "")))
}
