# Easy Route Drawing Library

[![](https://jitpack.io/v/msalihguler/easyroutedrawing.svg)](https://jitpack.io/#msalihguler/easyroutedrawing)
	![](https://img.shields.io/shippable/5444c5ecb904a4b21567b0ff.svg)

Easy Route Drawing library is a library aimed to used for drawing a route between two or multiple points in an easier way.

#Import

Add this in your project's build.gradle at the end of repositories:

```java

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
```

Add the module's build.gradle dependency

```java
  
	dependencies {
	        compile 'com.github.msalihguler:easyroutedrawing:1.0.4'
	}
	
```
	
# Usage

For calculating a route between two points we use the following snippet.

```java
  GoogleMap map;
  
  EasyRouteCalculation easyRouteCalculation = new EasyRouteCalculation(this,map);
  LatLng ankara = new LatLng(39.55,32.51);
  LatLng istanbul = new LatLng(41.0,28.58);
  easyRouteCalculation.calculateRouteBetweenTwoPoints(ankara,istanbul);
```

### Output of this code
<img src="https://raw.githubusercontent.com/msalihguler/easyroutedrawing/master/art/device-2016-08-26-114126.png"/>

  For starting from your current location

```java
      EasyRouteCalculation easyRouteCalculation = new EasyRouteCalculation(this,map);
      easyRouteCalculation.gotoMyLocation(true);
  ```
  

  For calculating the distance from your current location you can use 'calculateRouteFromMyLocation'.
  
```java
      EasyRouteCalculation easyRouteCalculation = new EasyRouteCalculation(this,map);
      
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                easyRouteCalculation.calculateRouteFromMyLocation(latLng);
            }
        });
        
```

### Output of this code
<img src="https://raw.githubusercontent.com/msalihguler/easyroutedrawing/master/art/device-2016-08-26-115447.png"/>


For calculating route for multiple location

```java
    EasyRouteCalculation easyRouteCalculation = new EasyRouteCalculation(this,map);
    
    LatLng ankara = new LatLng(39.55,32.51);
    LatLng izmir = new LatLng(38.25,27.7);
    LatLng istanbul = new LatLng(41.0,28.58);
    LatLng antalya = new LatLng(36.52,30.45);
    LatLng mugla = new LatLng(37.15,28.22);
    LatLng samsun = new LatLng(41.15,36.22);
    LatLng sinop = new LatLng(42.,35.11);
    
    easyRouteCalculation.addLocation(ankara);
    easyRouteCalculation.addLocation(izmir);
    easyRouteCalculation.addLocation(istanbul);
    easyRouteCalculation.addLocation(antalya);
    easyRouteCalculation.addLocation(mugla);
    easyRouteCalculation.addLocation(samsun);
    easyRouteCalculation.addLocation(sinop);
    
    easyRouteCalculation.calculateRouteForMultiplePositions();

```
### Output of this code

<img src="https://raw.githubusercontent.com/msalihguler/easyroutedrawing/master/art/device-2016-08-26-142917.png"/>

#More on Library
  You can check if GPS is enabled as follows
  
  ```java
        easyRouteCalculation.isGPSEnabled(this);
```
  You can use a prepared dialog to open GPS settings
  
  ```java
        easyRouteCalculation.createDialogForOpeningGPS(this);
```

  Getting current location is easy with this code snippet
  
  ```java
        easyRouteCalculation.getCurrentLocation();
  ```

  You can change color and width of the line and travel mode
  
  ```java
        /*
        *     Default value for color is RED, width is 5 and travel mode is TravelMode.WALKING
        *
        */
        
        // For making blue
        
        easyRouteCalculation.setLineColor(Color.BLUE);
        
        // For changing width 
        
        easyRouteCalculation.setLineWidth(7);
        
        // For changing travel mode 
        
        easyRouteCalculation.setTravelMode(TravelMode.DRIVING);
  ```
  
  Getting distance between two points 
  
  ```java
      //This will return the distance in kilometers
      
      easyRouteCalculation.getDistanceBetweenPoints(new LatLng(41.15,36.22),new LatLng(37.15,28.22));
      
  ```
  
  Getting duration between two points
  
  ```java
  
    //This will return duration between two points
    
    easyRouteCalculation.getDurationBetweenPoints(new LatLng(41.15,36.22),new LatLng(37.15,28.22));
    
  ```
  
  In multiple location calculations we will use following methods
  
  ```java
    
      LatLng location = new LatLng(42.13,34.15);
      
      // For adding location
      
      easyRouteCalculation.addLocation(location);
      
      // For deleting location
      
      easyRouteCalculation.deleteLocation(location);
  
      //or
      
      easyRouteCalculation.deleteLocation(position);
      
      // For deleting all values added.
      
      easyRouteCalculation.deleteAllLocations();
  ```
  
# Author 
  
  * Muhammed Salih GÜLER - <muhammedsalihguler@gmail.com>

<a href="https://tr.linkedin.com/in/msalihguler">
  <img alt="Add me to Linkedin" src="https://github.com/JorgeCastilloPrz/EasyMVP/blob/master/art/linkedin.png" />
</a>

# License

    Copyright 2016 Muhammed Salih GÜLER - msalihguler 

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
