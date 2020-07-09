# Specials App

## Setup

To get a local copy of this repo, run: `git clone git@github.com:ABouzo/specials.git`. This project can either be imported into Android Studio or Intellij, or can be build from the command line. 

`./gradlew clean build`

Verify that your [JAVA_HOME](https://docs.oracle.com/cd/E19182-01/821-0917/inst_jdk_javahome_t/index.html) is setup correctly.

## Libraries Used

* **Retrofit** for cleaner, better defined api calls.
* **Koin** for a simple clean dependency injection
* **Picasso** to lazy load pictures as they arrive
* **Flexbox** html-like grid layout for cards of different sizes

## Design Decisions

I decided to use a **Repository** design to make the data source transparent from the UI. The Repository can pull data from memory, a database, or api, and carryout its own logic about which data source to choose without the UI knowing or caring. To facilitate this decoupling the Repo manages to Data Model, one from the *Rest api* and one used by the *UI*. This way we can change out the api, or adapt to a changing api, without changing any ui code.

Since the size of a deal card can change depending on the server, a **Custom View** is being used. This custom view can have four variants: 'Tiny, Small, Medium, Big'.
Currently each variant had different sizes for the Text fields. Having variants rather than just adjusting all text sizes dynamically, there fewer versions to keep track off, definitions of each variant can be changed, and in the future each variant can have a more unique layout (hiding the item name or image on tiny variant).

I used **ViewModel** so that the data would outlive the lifecycle of the view it's being presented on. The application won't have to fetch the data from the repo whenever the device is rotated. The **RecyclerView** was used to keep inflation to a minimum, and thus reduce stutters.

## License

Licensed under the [GNU v3 Public License](/LICENSE)