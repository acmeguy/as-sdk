# Activity Stream SDK 

The Activity Stream SDK is split up in to two components

* **as-sdk** that contains core SDK files (this)
* **[as-sdk-examples](../as-sdk-examples)** which contains code and message examples

Both of these can be cloned and extended and pull-requests are welcome.

### Getting started
It's a good start to read the **[Getting started guide]()** but the examples also contains boilerplate code designed to provide you with a quick start.
We want to grow our message examples directory, included in the examples, considerably and with your help. Please respect those messages as a guideline
for you messages but keep in mind that you can extend or alter them at will .

### SDK Design Principles
* Keep as-sdk as light as possible by introducing the as few external dependencies as possible.
* Build hand crafted versions for each supported programming language that suit it very well.
* Create interface classes that uses the terminology of specific verticals/industries to make adoption easier for those that prefer it over the generic 
terminology used in the AS Message model.

### Examples Design Principles
* Examples are provided in the form of runnable/passable tests
* Provide sample input and output and make sure its anonymous
* Create highly reusable code (that encourages copy/paste use)
* Encourage forking on github and be accommodating in regards to pull-requests
* Provide brief documentation for every example
* Organize examples around the data source and think of them as a code that might be used by them to integrate to AS directly
* Add examples to the message library when adding a message of a new event type

### The Activity Stream Schema
The [AS Schema](/schema) directory will contain the AS schema in different formats.

### Contact and support
* Feel free to create issues here or contact our [Integration Support team directly](mailto:support+integration@activivtystream.com).

### Todo
 - [X] Update the SDK so that it's based on the same Transportation classes as Core
 - [X] Add utility functions that make it easier to work with the SDK in "detached" mode
 - [X] Open source the as-sdk and unlock the github project
 - [ ] Add unit tests from core 
 - [ ] Move the appropriate material/documentation from api.activitystream.com to here
 - [ ] Add automatic building and maven releasing for this SDK
 - [ ] Clean up the project so that different languages have project files likely to useful for that language
 - [ ] Open the as-sdk-examples repository