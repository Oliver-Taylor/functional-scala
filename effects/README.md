# Effects

* Total: return a value for every possible input
* Deterministic: return the same value for the same input
* Inculpable: no (direct) interaction with the world or program state.

Effects refer to the core behaviour of a monad, for example:

An Option *monad* models optionality as an effect
A Try *monad* models failure as an effect
A Future *monad* models latency as an effect.
An IO *monad* models side effects

## References

* https://alvinalexander.com/scala/what-effects-effectful-mean-in-functional-programming
* http://degoes.net/articles/zio-environment
* https://leanpub.com/fpmortals
* https://typelevel.org/cats-effect
* https://blog.softwaremill.com/final-tagless-seen-alive-79a8d884691d