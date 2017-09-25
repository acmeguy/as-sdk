# Activity Stream SDK 

The Activity Stream SDK is split up in to two components

* **as-sdk** that contains core SDK files (this)
* **[as-sdk-examples](https://github.com/activitystream/as-sdk-examples)** which contains reusable code and message examples

Both of these can be cloned and extended and pull-requests are welcome.

### Getting started
It's a good start to read the **[Getting started guide](/docs)** but the examples also contains boilerplate code designed to get you off to a
quick start.
We want to grow our message examples directory, included in the examples, considerably and with your help. Please respect those messages as a guideline
for you messages but keep in mind that you can extend or alter them at will.


### Importing the SDK into your project

Use these maven coordinates for Java*:

    <dependency>
        <groupId>com.activitystream</groupId>
        <artifactId>as-sdk</artifactId>
        <version>0.1.3</version>
    </dependency>

*AS SDK will be available for other languages soon*

### SDK Design Principles
* Keep as-sdk as light as possible by introducing the as few external dependencies as possible.
* Build hand crafted versions for each supported programming language that suit it very well.
* Create interface classes that uses the terminology of specific verticals/industries to make adoption easier for those that prefer it over the generic 
terminology used in the AS Message model.
* Provide a wide range of [examples](https://github.com/activitystream/as-sdk-examples) tailored for specific verticals  

### The Activity Stream Schema
The [AS Schema](/schema) directory will contain the AS schema in different formats.

### Contact and support
* Feel free to create issues here or contact our [Integration Support team directly](mailto:integration-support@activitystream.com).

### Todo
 - [X] Update the SDK so that it's based on the same Transportation classes as Core
 - [X] Add utility functions that make it easier to work with the SDK in "detached" mode
 - [X] Open source the as-sdk and unlock the github project
 - [ ] Add unit tests from core 
 - [ ] Move the appropriate material/documentation from [api.activitystream.com](http://api.activitystream.com) to here
 - [ ] Add automatic building and maven releasing for this SDK
 - [ ] Clean up the project so that different languages have project files likely to useful for that language
 - [X] Open the as-sdk-examples repository

