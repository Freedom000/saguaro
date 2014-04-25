Saguaro
=======

An Android library to make it easier to add version information and licensing information, and facilitate sending feedback.

Try out the sample application on [Google Play][6].

<a href="https://play.google.com/store/apps/details?id=com.willowtreeapps.saguaro.android.sample">
  <img alt="Saguaro Samples on Google Play"
         src="http://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Native Android Usage
====================

*For a working implementation of this project see the `sample/` folder.*

**VersionTextView**

You can declare a `VersionTextView` in your xml layout files to automatically populate the version information.  The class extends `TextView` so you can easily apply a custom `Typeface` or style.  By default, `VersionTextView` will display the version in the following format: `v[MAJOR].[MINOR].[POINT].[BUILD]` (e.g. `v1.2.3.456`).

```xml
<com.willowtreeapps.saguaro.android.widget.VersionTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

You can enable a "full" version text (`Version 1.2.3 build 456`) by declaring the `res-auto` namespace and adding an attribute like so:

```xml
<com.willowtreeapps.saguaro.android.widget.VersionTextView
    xmlns:saguaro="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    saguaro:saguaro__isFullVersionText="false" />
```

You can also override this dynamic behavior by defining your own `res/values` entry for `default_version_text_dynamic` (e.g. `<string name="default_version_text_dynamic">v%1$s b%2$s</string>`, which would yield `v1.2.3 b456`)

You can also programmatically get the version information as a `String`:

```java
Saguaro.getMinVersionString(mContext);
Saguaro.getFullVersionString(mContext);
```

**AcknowledgmentsTextView**

For licensing information, add a `saguaro_config.xml` file in your `res/values` folder and populate your own values.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="mit_projects">
        <item>MIT 1</item>
        <item>MIT 2</item>
        <item>MIT 3</item>
        <item>MIT 4</item>
        <item>MIT 5</item>
    </string-array>
    <string-array name="apache2_projects">
        <item>ActionBarSherlock</item>
        <item>Apache 1</item>
        <item>Apache 2</item>
        <item>Apache 3</item>
        <item>Apache 4</item>
    </string-array>
    <string-array name="bsd2_projects">
        <item>BSD 1</item>
        <item>BSD 2</item>
        <item>BSD 3</item>
        <item>BSD 4</item>
    </string-array>
</resources>
```

Valid built-in licenses are `mit`, `apache2`, `bsd2`, and `ccpl3` (Creative Commons).

You can further customize acknowledgments by adding custom prepended acknowledgments.

```xml
<string name="prepend_acknowledgments_text">Special thanks to Oprah for providing copious amounts of bees.</string>
```

Then declare an `AcknowledgmentsTextView` in your xml layout.  Again, this extends `TextView` so you can customize it as well.

```xml
<com.willowtreeapps.saguaro.android.widget.AcknowledgmentsTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

If you'd like to launch the `Dialog` on your own (for example in an `OnClickListener`), you can do so:

```java
Saguaro.showOpenSourceDialog(mContext);
```

**Adding your own license**

In addition to the provided licenses, you can add your own.

In your `saguaro_config.xml`, add the following to define your own licenses:

```xml
<string-array name="saguaro_licenses">
    <item>bees</item>
</string-array>

<string name="bees_name">Public Bees License</string>

<string-array name="bees_projects">
  <item>Bees Lib</item>
</string-array>
```

Then create the `res/raw/bees.txt` file and copy the raw text of your license into it.

**SendFeedbackTextView**

You can customize sending feedback with an e-mail address, and optionally a custom subject and body.

```xml
<string name="send_feedback_email">mytestemail@mytestdomain.com</string>
<string name="send_feedback_optional_subject">Feature request from Oprah</string>
<string name="send_feedback_optional_body">PLEASE ADD MORE BEES.</string>
```

Then declare a `SendFeedbackTextView` in your xml layout:

```xml
<com.willowtreeapps.saguaro.android.widget.SendFeedbackTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

You can also obtain the `Intent` to send feedback programmatically:

```java
Saguaro.getSendFeedbackIntent(mContext);
```

Saguaro Gradle Plugin
=====================

You can use the Saguaro Gradle Plugin to define and automatically fetch licenses from your dependencies in your `build.gradle` config.

Start by adding the following to your `build.gradle`:

```groovy
buildscript {
  repositories { mavenCentral() }
  dependencies {
    classpath "com.willowtreeapps.saguaro:gradle-plugin:1.0.0"
  }
}

apply plugin: 'saguaro'

saguaro {
  // Configure your licenses
}
```

Then run `gradle saguaroGenerate` to generate license resources based on your dependencies.

You may run into a few issues when generating the licenses.

Some dependencies may have licenses that are not defined in their `pom.xml` file. In this case, you can add the library manually:

```groovy
saguaro {
  license apache2, 'Apache 1'
  license mit, ['Mit 1', 'Mit 2']
}
```

The constants `apache2`, `bsd2`, `mit`, and `ccpl3` are built in to Saguaro.

You can also define your own license that either downloads from a URL or that you provide in `res/raw`. The below example downloads one license from `beegifs.com` and gets the other from `res/raw/second.txt`.

```groovy
saguaro {
  def bees = [name: 'Bees License', url: 'http://www.beegifs.com/license.txt']
  def myLicense2 = [name: 'My Second License', key: 'second']

  license bees, 'Bees Lib'
  license myLicense2, 'Lib 2'
}
```

You can also ignore dependencies in case you don't want them to show up or you want to explicitly set their license.

```groovy
saguaro {
  ignore 'com.oprah.bees:bees'
}
```

Another problem that you may run into is having multiple dependencies with the same license, but the licenses are named differently. In this case, you can consolidate them by using an alias.

```groovy
saguaro {
  alias apache2, 'Apache 2'
  alias 'Bees License', ['Beees License', 'Beeees License']
}
```

Any libraries labeled with the aliased names will show up as the same license.

Saguaro Maven Plugin
====================

There is also a Saguaro Maven Plugin, which works in a similar way to the Gradle plugin.

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.willowtreeapps.saguaro</groupId>
      <artifactId>saguaro-maven-plugin</artifactId>
      <version>1.0.0</version>
    </plugin>
    <configure><!-- Configure your licenses --></configure>
  </plugins>
</build>
```

You can then execute the `generate` goal.

```bash
mvn com.willowtreeapps.saguaro:saguaro-maven-plugin:generate
```

Configuration is very similar, albeit more verbose.
```xml
<configure>
  <licenses>
    <!-- built in license -->
    <license>
      <key>apache2</key>
      <libraries>
        <item>Apache 1</item>
        <item>Apache 2</item>
      </libraries>
    </license>
    <!-- custom local license -->
    <license>
      <name>Public Bees License</name>
      <key>bees</key>
      <library>Bees Lib</library>
    </license>
    <!-- custom license with url -->
    <license>
      <name>Other license</name>
      <url>http://www.beegifs.com/license.txt</url>
    </license>
  </licenses>

  <ignore>
    <dependency>
      <groupId>.com.oprah.bees</groupId>
      <artifactId>bees</artifactId>
    </dependency>
  </ignore>

  <aliases>
    <alias>
      <key>apache2</key>
      <alias>Apache 2</alias>
    </alias> 
    <alias>
      <name>Bees License</name>      
      <aliases>
        <item>Beees License</item>
        <item>Beeees License</item>
      </aliases>
    </alias>
  </alises>
</configure>
```

Including in Your Project
=========================

Saguaro is packaged as an [Android library project][7].

You can include this project by [referencing it as a library project][8] in
Eclipse or ant.

If you are a Maven user you can easily include the library by specifying it as
a dependency:

```xml
    <dependency>
      <groupId>com.willowtreeapps.saguaro.android</groupId>
      <artifactId>library</artifactId>
      <version>1.0.0</version>
      <type>aar</type>
    </dependency>
```

If you are a Gradle user you can also easily include the library:

```groovy
    compile 'com.willowtreeapps.saguaro.android:library:1.0.0'
```

Changelog
=========

**1.0.0**
* AcknowledgmentsTextView
* SendFeedbackTextView
* VersionTextView

Contribution
============

### Pull requests are welcome!

Feel free to contribute to Saguaro.

If you've fixed a bug or have a feature you've added, just create a pull request.

If you've found a bug, want a new feature, or have other questions, [file an issue][10]. We'll try to answer as soon as possible.

License
=======

    Copyright 2013 WillowTree Apps

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [6]: https://play.google.com/store/apps/details?id=com.willowtreeapps.saguaro.android.sample
 [7]: http://developer.android.com/guide/developing/projects/projects-eclipse.html
 [8]: http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject
 [10]: https://github.com/willowtreeapps/saguaro-android/issues/new
