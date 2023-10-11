# About StarWars Character Explorer App

In this project I tried showcasing how to build an Android Application with clean architecture and
MVVM using some of the jetpack libraries with Kotlin Coroutines & Dagger2. This App is using
the [Star Wars API](https://swapi.dev/) as a remote data source.

# To be added

* Character search with filter
* Starship list
* Planet list
* more testing
* animations

# Project Architecture

### Communication between layers

1. UI calls method from ViewModel.
2. ViewModel executes Use case.
4. The Repository returns data from one or multiple Data Sources. the repository is the single
   source of truth
5. Information flows back to the UI where we display the data fetched from data sources.

Libraries Used
---------------

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and
  manage UI-related data in a lifecycle conscious way
* [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - enable flows to
  optimally emit state updates and emit values to multiple consumers..
* [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - write code that
  interacts with views more easily
* [Material](https://material.io/develop/android/docs/getting-started/) - Material Components.
* [Coroutine](https://github.com/Kotlin/kotlinx.coroutines#user-content-android) - performs
  background tasks
* [Flows](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) -
  for asynchronous data streams
* [Retrofit2](https://square.github.io/retrofit/)- networking
* [Gson](https://github.com/google/gson) - JSON Parser
* [Dagger2](https://dagger.dev/users-guide) - dependency injector
* [Espresso](https://developer.android.com/training/testing/espresso/) // UI test
* [Junit](https://junit.org/junit4/) // unit tests
* [Truth](https://github.com/google/truth) // Makes your test assertions and failure messages more
  readable


