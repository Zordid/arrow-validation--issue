import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class AuthorsAndBooksTutorialTest : FunSpec({

    context("using mapOrAccumulate(iter) with recover") {
        val bookCreator = Book::invoke

        test("good case") {
            Book("The Universe And I", listOf("Foo", "Bar")).shouldBeRight()
        }

        test("without title and no authors") {
            Book("", listOf()).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyTitle, NoAuthors
            )
        }
        test("with title, but no authors") {
            Book("The Universe And I", listOf()).shouldBeLeft().shouldContainExactlyInAnyOrder(
                NoAuthors
            )
        }
        test("without title, but with authors") {
            Book("", listOf("Foo")).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyTitle
            )
        }
        test("with title, but with one empty author") {
            Book("The Universe And I", listOf("Foo", "")).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyAuthor(1)
            )
        }
        test("without title, and with one empty author") {
            Book("", listOf("Foo", "")).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyTitle, EmptyAuthor(1)
            )
        }
    }

    context("using iter.mapOrAccumulate with mapLeft") {
        test("good case") {
            Book.workAround("The Universe And I", listOf("Foo", "Bar")).shouldBeRight()
        }

        test("without title and no authors") {
            Book.workAround("", listOf()).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyTitle, NoAuthors
            )
        }
        test("with title, but no authors") {
            Book.workAround("The Universe And I", listOf()).shouldBeLeft().shouldContainExactlyInAnyOrder(
                NoAuthors
            )
        }
        test("without title, but with authors") {
            Book.workAround("", listOf("Foo")).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyTitle
            )
        }
        test("with title, but with one empty author") {
            Book.workAround("The Universe And I", listOf("Foo", "")).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyAuthor(1)
            )
        }
        test("without title, and with one empty author") {
            Book.workAround("", listOf("Foo", "")).shouldBeLeft().shouldContainExactlyInAnyOrder(
                EmptyTitle, EmptyAuthor(1)
            )
        }
    }


})