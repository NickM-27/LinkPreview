# LinkPreview
A convenient library to show clickable previews of links.

<img src="/sample.png" width="32%">

# Features

* Parse entire body of text to automatically find links.
* Find links from different sources and open them in the appropriate app, falling back to a custom tab if none is found.

# How Do I Use It?

## Setup

### Gradle

On your module's `build.gradle` file add this statement to the `dependencies` section:

```groovy
dependencies {
    implementation 'com.nick.mowen.linkpreview:linkpreview:3.0'
}
```

Also make sure that the `repositories` section includes both jcenter and `maven` section with the `"google()"` endpoint. 

```groovy
repositories {
    jcenter()
    google()
}
```

# Link Preview

### Add to your layout

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    ...

    <com.nick.mowen.linkpreview.view.LinkPreview
        android:id="@+id/preview"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</LinearLayout>
```

### Create And Setup The View

```kotlin
val preview: LinkPreview = findViewById(R.id.preview)
...
preview.parseTextForLink(string) //Use LinkPreview$parseTextForLink if you have a body of text that contains more than just the link
preview.setLink(linkString) //Use when you have a string that contains only the link
```

### Customize The View

```kotlin
preview.articleColor = ContextCompat.getColor(this, R.color.colorPrimary) //Set the color of the custom tab that is launched on link press

//Use the load listener if you want to take extra action when a link error or success occurs
preview.loadListener = object : LinkListener {

    override fun onError() {
        Toast.makeText(this@MainActivity, "Link loading failed", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(link: String) {

    }
}
```

### Advanced Usage

You can override the LinkPreview clicklistener all together using the click listener
```kotlin
preview.clickListener = object : LinkClickListener {

        override fun onLinkClicked(view: View?, url: String) {
                
        }
}
```

Appications using LinkPreview
---
Icon | Application
------------ | -------------
<img src="https://github.com/NickM-27/Texpert/blob/master/app/src/main/res/mipmap-hdpi/ic_launcher.png" width="48" height="48" /> | [Texpert](https://play.google.com/store/apps/details?id=com.nick.mowen.texpert)

Please [email](mailto:nick@nicknackdevelopment.com) me or send a pull request if you would like to be added here.

Also Seen On
---
![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LinkPreview-green.svg?style=flat)

Developed By
---
Nick Mowen - <nick@nicknackdevelopment.com>

Contributions
-------

Any contributions are welcome!

# Donations
If you would like to donate / contribute to future development, you can [paypal me here](https://paypal.me/nickmowen)

License
---

    Copyright 2018 Nick Nack Developments

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
