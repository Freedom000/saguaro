Saguaro
=======

An Android library to make it easier to add version information and licensing information, and facilitate sending feedback.

Try out the sample application on [Google Play][6].

<a href="https://play.google.com/store/apps/details?id=com.willowtreeapps.saguaro.android.sample">
  <img alt="Saguaro Samples on Google Play"
         src="http://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Usage
=====

*For a working implementation of this project see the `sample/` folder.*

  1. Add a `saguaro_config.xml` file in your `res/values` folder and populate your own values.

        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <string-array name="mit_licensed_projects">
                <item>MIT 1</item>
                <item>MIT 2</item>
                <item>MIT 3</item>
                <item>MIT 4</item>
                <item>MIT 5</item>
            </string-array>
            <string-array name="apache_2_0_licensed_projects">
                <item>ActionBarSherlock</item>
                <item>Apache 1</item>
                <item>Apache 2</item>
                <item>Apache 3</item>
                <item>Apache 4</item>
            </string-array>
            <string name="send_feedback_email">mytestemail@mytestdomain.com</string>
        </resources>

* You can further customize sending feedback with a custom subject and body.

        <string name="send_feedback_optional_subject">Feature request from Oprah</string>
        <string name="send_feedback_optional_body">PLEASE ADD MORE BEES.</string>

* You can further customize acknowledgments by adding custom prepended acknowledgments.

        <string name="prepend_acknowledgments_text">Special thanks to Oprah for providing copious amounts of bees.</string>

Including in Your Project
=========================

Saguaro is packaged as an [Android library project][7].

You can include this project by [referencing it as a library project][8] in
Eclipse or ant.

If you are a Maven user you can easily include the library by specifying it as
a dependency:

    <dependency>
      <groupId>com.willowtreeapps.saguaro.android</groupId>
      <artifactId>library</artifactId>
      <version>1.0.0</version>
      <type>aar</type>
    </dependency>

If you are a Gradle user you can also easily include the library:

    compile 'com.willowtreeapps.saguaro.android:library:1.0.0'

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