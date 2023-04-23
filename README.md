# mars-phto-exporer
This Android application shows photo taken by the Mars rovers send by NASA.

This app is example for this [Android Jetpack Compose Udemy course](https://www.udemy.com/course/android-jetpack-compose-retrofit-room-hilt/)

### Features

<video width="200" controls>
  <source src="raw/compse_mars_rover_android_application.mp4" type="video/mp4">
</video>

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


