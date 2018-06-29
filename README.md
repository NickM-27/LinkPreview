# LinkPreview
A convenient library to show clickable previews of links

# Features

* Parse entire body of text to automatically find links
* Find links from different sources and open them in the appropriate app, falling back to a custom tab if none is found

# How Do I Use It?

## Setup

### Gradle

On your module's `build.gradle` file add this statement to the `dependencies` section:

```groovy
dependencies {
  implementation 'com.nick.mowen.linkpreview:linkpreview:1.0.0'
}
```

Also make sure that the `repositories` section includes both jcenter and `maven` section with the `"google()"` endpoint. 

```
repositories {
  jcenter()
  google()
}
```

# Link Preview

### Add to your layout

```
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

```
val preview: LinkPreview = findViewById(R.id.preview)
...
preview.parseTextForLink(string) //Use LinkPreview$parseTextForLink if you have a body of text that contains more than just the link
preview.setLink(linkString) //Use when you have a string that contains only the link
```

### Customize The View

```
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
