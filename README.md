# mars-photo-exporer
This Android application shows photo taken by the Mars rovers send by NASA.

This app is example for this [Android Jetpack Compose Udemy course](https://www.udemy.com/course/android-jetpack-compose-retrofit-room-hilt/?referralCode=E687F9D8E0057A0DF4B2)

### Features

<img width="200" alt="Demo of Mars photo explorer application" src="raw/compse_mars_rover_android_application.gif"/>

The first screen display a list of rovers hard coded in the app.

<img width="200" alt="Compose rover screen" src="raw/rover_screen.png"/>

The second display list of sol (day on Mars) for the rover. For each item in the list the corresponding earth date and the number of photos are displayed.

<img width="200" alt="Compose manifest screen" src="raw/manifest_screen.png"/>

The thrid screen display a list of photo for the previously chosen rover and sol.

<img width="200" alt="Compose photo screen" src="raw/photo_screen.png"/>

The fourth screen display the list of saved photo for all rovers. It is avialable by the bottom nav.

<img width="200" alt="Compose saved screen" src="raw/saved_screen.png"/>

### Code architecture

The code is divided in four layers.

<img width="400" alt="Jetpack compose Mars rover architecture compose hilt retrofit room" src="raw/mars_rover_architecture_compose_hilt_retrofit_room.png"/>

The Android UI component are represented in green. The Activity is an Android Activity. The ViewModel is from the Android Jetpack library.

The repository is represented in blue. It will transform and mix data from the data layer.

The data layer in orange are Room and Retrofit interfaces.

An article that describe this [architecture with Hilt, Retrofit, Room with Jetpack Compose is avialable on Medium](https://medium.com/@alexandre.genet7/android-viewmodel-repository-room-and-retrofit-with-jetpack-compose-2b652d8ff3b9).


