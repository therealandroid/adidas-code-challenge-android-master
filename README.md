## Adidas code challenge

All the documentation for this project
is available in the Wiki.

## Adidas Challenge

### Project Description

This project use Google Fit API to track user goals and progress. The goal of this project is to reach the Adidas code challenge.

---

### Scope

"Sport is at the core of everything we do. In this challenge, you will be using Apple
Health/Google Fit as services to get information about user's activity. Also, there is an
endpoint where you can find a list of daily goals that you should retrieve and display in
your app. Use aforementioned services to automatically track progress on those daily
goals."

#### Main features

* [x] Catch real time user steps
* [x] List user daily goals  (WALKING, RUNNING)
* [x] Earn points by reaching goals
* [ ] Daily goals should be cumulative on the next day

Tests Coverage
* [ ] Unity tests
* [x] Instrumentation tests

---
### Screens


1 - Daily Goals

<img src="https://user-images.githubusercontent.com/7516661/55089536-51d3f680-508c-11e9-9be8-893204cd49c4.jpg" alt="alt text" width="190" height="360">

2 - Rewards

<img src="https://user-images.githubusercontent.com/7516661/55089535-51d3f680-508c-11e9-90b9-128591529244.jpg" alt="alt text" width="180" height="360">

---
### Project architecture

![adidas_architecture](https://user-images.githubusercontent.com/7516661/55089645-7def7780-508c-11e9-8a54-db7b45e82b7d.png)

#### Database

* Db name: `Adidas-db`
* Domain Model

```
GoalsEntity(  
 @PrimaryKey var id: String = "",  
 var title: String = "",  
 var description: String = "",  
 var type: String = "",  
 var goal: Int = 0,  
 var progress: Int = 0,  
 var date: Date? = null,  
 var reward: RewardEntity,
 var state: Int = 0  
))
```
```
RewardEntity(  
    var trophy: String = "",  
    var points: Int = 0  
)
```
---

### Guideline

#### Reference

<img src="https://user-images.githubusercontent.com/7516661/55089709-9a8baf80-508c-11e9-941b-1ada8941179f.png" alt="alt text" width="180" height="300">

<img src="https://user-images.githubusercontent.com/7516661/55089710-9a8baf80-508c-11e9-95ff-10bf0213708d.png" alt="alt text" width="180" height="300">

<img src="https://user-images.githubusercontent.com/7516661/55089711-9a8baf80-508c-11e9-991c-51debf92e9b3.png" alt="alt text" width="180" height="300">


#### Color Pallete

| id | value | color |
|--|--|--|
| primary | #000000 | black |
| primaryLightColor | #2c2c2c | semi-black |
| primaryDarkColor | #000000 | black |
| primaryTextColor | #ffffff | white |
| secondaryTextColor | #000000 | white |
| ripple | #d1d1d1 | gray |
| RUNNING | #3aaf55 | green |
| FINISHED | #919191 | light-gray |
| RUNNING | #000000 | bllack |

---

### Project Configuration

#### Instalation

 SDK Configuration

 `Android SDK min version 24`

---

#### External Frameworks

```
// GOOGLE FIT
implementation 'com.google.android.gms:play-services-fitness:16.0.1'
implementation 'com.google.android.gms:play-services-auth:16.0.1'

//NETWORKING
implementation 'com.google.code.gson:gson:2.8.5'
//Retrofit will parse our requests easily with the endpoint
implementation 'com.squareup.retrofit2:retrofit:2.5.0'

//GSON converter for retrofit
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'

//Make async requests and provide easy ways to intercept a request
implementation 'com.squareup.okhttp3:okhttp:3.12.1'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1'
implementation 'com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2'

IMAGE CACHING / RECYCLE
implementation 'com.squareup.picasso:picasso:2.71828'
IMAGE CIRCLE TRANSFORMATIONS
implementation 'jp.wasabeef:picasso-transformations:2.2.1'
```

---

#### Rest API

Base URL: `https://thebigachallenge.appspot.com`

Endpoint:
`Method [GET]`

	1 - List Goals :  `_ah/api/myApi/v1/goals`

---

### Troubleshooting

* App isn't working

If you are trying to run the app and you didn't received app feedback you probably don't have
an Oauth2 client ID configured. To do this, you should register a new OAuth2 client by following these simple steps:

[ Request an OAuth 2.0 client ID in the Google API Console](https://developers.google.com/fit/android/get-api-key)

* SDK not found when open the project

Check your `local.properties` file and set the correct dir folder for your Android SDK.

* I need assistance with the project installation

Any questions, please contact: diogojme@gmail.com
