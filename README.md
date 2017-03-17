# Android CustomTabs Helper
[![Download](https://api.bintray.com/packages/saschpe/maven/android-utils/images/download.svg)](https://bintray.com/saschpe/maven/android-utils/_latestVersion)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/saschpe/android-utils.svg?branch=master)](https://travis-ci.org/saschpe/android-utils)

This library collects a range of Android classes that I often use in projects.

# Usage
Avoid adding padding to recycler view item XML files and use item decoration
instead. The class *SpacesItemDecoration* can be used to add space between
items. It can be used several times to provide different values for *HORIZONTAL*
and *VERTICAL* orientations.

```java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
recyclerView.addItemDecoration(new SpacesItemDecoration(16, SpacesItemDecoration.VERTICAL));
```

The *DisplayHelper* class provides a range of functions for screen measurement
and to find suitable layout managers for RecyclerView based on the screen width:

```java
recyclerView.setLayoutManager(DisplayHelper.getSuitableLayoutManager(this));
```

Array-based adapters are the most common recycler view adapters. Avoid
re-inventing the whell and subclass from *ArrayAdapter*:

```java
public final class MyArrayAdapter extends ArrayAdapter<Thing, MyArrayAdapter.MyViewHolder> {
    public MyArrayAdapter(List<Thing> objects) {
        super(objects);
        // ...
    }
    
    static final class MyViewHolder extends RecyclerView.ViewHolder {
        // ...
    }
}
 
public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Thing> things = new ArrayList<>();
        
        MyAdapter adapter = new Adapter(things);
        
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
    }
}
```

The recycler view library does not provide Cursor-based adapters. Use
*CursorRecyclerAdapter* to directly display database data:

```java
public final class EventAdapter extends CursorRecyclerAdapter<EventAdapter.EventViewHolder> {
    public EventAdapter(@NonNull Context context) {
        // ...
        Cursor cursor = context.getContentResolver()
                .query(eventsUri, PROJECTION, SELECTION, selectionArgs,
                        CalendarContract.Instances.DTSTART + " ASC");
    
        init(cursor); // See base class
    }
    
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ...
        return new EventViewHolder(v);
    }
    
    @Override
    public void onBindViewHolderCursor(final EventViewHolder holder, final Cursor cursor) {
        // ...
    }
    
    static class EventViewHolder extends RecyclerView.ViewHolder {
        // ...
    }
}
```


The Android SDK does not provide a PreferenceActivity that is based on
AppCompat. To avoid using the old and deprecated PreferenceActivity or rolling
your own, subclass from *AppCompatPreferenceActivity* instead:

```java
public final class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    // ...
}
```

# Download
```groovy
compile 'saschpe.android:customtabs-helper:1.0.6'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].


# License

    Copyright 2016 Sascha Peilicke

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
