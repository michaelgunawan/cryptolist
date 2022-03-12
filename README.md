# Crypto List
Demo for showing simple list, as a `RecyclerView` contained in a `Fragment`. Architecturally, this project is using a `MVVM` with strong UI state definition and all of the reactive component and background processes are handled using `Coroutine`.

Some of the more important paradigm I adopt for this project:
1. Preferring async for business logic and proccesses, and reactive for user interaction
2. Preferring thread management via coroutine scopes instead of handling our own thread pool, etc.
3. Unidirectional data flow via UI state
4. Shared data via `ViewModel` instead of having `Activity` referencing it's own `Fragment` (eg. how the recycler view item tap is handled)
5. As much as possible, created clean architecture implementation via DI, data separation of concerns, and clear `View` vs `Model` separation

![Screenshot_1647125576](https://user-images.githubusercontent.com/1829109/158037820-35fe228c-c9b6-4e6a-96eb-6ae4c2574918.png)
