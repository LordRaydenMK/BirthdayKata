# The birthday greetings kata

A refactoring exercise to learn about dependency inversion and hexagonal architecture. Inspired by a [blog post][kata-blog] from [Matteo Vaccari][mateo-vaccari]. You can find the original [repo here][kata-repo].

## Solution

I decided to solve the problem in a functional programming style using the Kotlin programming language and [Arrow][arrow-kt], a functional companion to the Kotlin standard library.

## Resource

The following resources helped me in designing this solution:

- [Functional and Reactive Domain Modeling][frdomain]
- [Domain Modeling made functional][dmfunc]
- [Dependency rejection][deprej]
- [Self-contained example of testing with modules and Arrow FX][fx-module]

[kata-blog]: http://matteo.vaccari.name/blog/archives/154
[mateo-vaccari]: http://matteo.vaccari.name/
[kata-repo]: https://github.com/xpmatteo/birthday-greetings-kata
[arrow-kt]: https://arrow-kt.io/
[frdomain]: https://www.manning.com/books/functional-and-reactive-domain-modeling
[dmfunc]: https://pragprog.com/book/swdddf/domain-modeling-made-functional
[deprej]: https://blog.ploeh.dk/2017/02/02/dependency-rejection/
[fx-module]: https://www.msec.it/blog/self-contained-example-of-testing-with-modules-and-arrow-fx/