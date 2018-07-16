[![Project Status: Active - Initial development has started, temporary release; work hasn't been stopped ](http://www.repostatus.org/badges/0.1.0/active.svg)](http://www.repostatus.org/#active)

BubblingImageView
=============
A beautiful ImageView that bubbles soapiely!!!

## Appearance

![Demo](shots/appearance.gif)

## Quick Start

> Gradle

```xml
   dependencies {
        implementation 'com.github.54LiNKeR:BubblingImageView:1.0.0'
    }
```

> XML

```xml
        <linkersoft.blackpanther.bubbles.BubblingImageView
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_gravity="center"
            android:id="@+id/bubbler"
            android:src="@drawable/blackpanther"
            app:Length="120dp"
            app:SRC_IN="true"
            app:Ox="0"
            app:Oy="0"
            app:Centralise="true"
            app:BubbleScale="true"
            />

```

