Bugzilla Live is a JavaFX desktop tool to manage bugs from Bugzilla using either a list of numbers or a username. It replaces the traditional way of using Excel spreadsheets to track bugs.

# Getting started

## Prerequisites:

* JavaFX (64-bit update 181 JDK & JRE or greater)
* Apache Ant (1.10.5 or greater)

## Load Into Eclipse

* Clone the master branch and open Eclipse. 
* Choose to import an existing project into Eclipse and select the root bugzilla-live folder. 
* Make sure to check 'Search for nested projects' - Bugzilla Live is split into a set of microservices that are nested Eclipse projects under the main eclipse project.

## Build & Run Bugzilla Live

* Run the build.xml script in the bugzilla-live eclipse project to build the installer for Bugzilla Live. 
* Navigate to the build folder generated from the build script and run the 'Install Bugzilla Live.bat' as an administrator. This will install python and create the neccessary folders for the application to run in Eclipse. You may need to edit the project directory variable in each of the build scripts for them to work.

## Microservices

* Bugzilla Live is split into a set of microservices that use JSON files to pass messages between each service. An example would be: 

    ```GUI -> OR Request Message (JSON) -> OR Microservice```

* The OR microservice would then parse the JSON message data, perform the required tasks and send a response message back:

    ```OR Microservice -> OR Response Message (JSON) -> GUI```

## Fixing Problems

* Issues are currently tracked via Bitbucket under the ['Issues'](https://bitbucket.org/tomhewitt/bugzilla-live/issues) page for the Bugzilla Live repository. 
* Choose an issue, create a new branch for it and code it.
* When you are done create a pull request and I will review the code before it can be merged into the develop branch, and then master.

# Code Style
The following should be applied throughout the code base.

# *Layout*

## **Braces**

* Braces should be used when the code block has more than one statement. If there is one statement braces are not required. For example:

``` java
    if (something)
    {
        doSomething();
    }

    // OR

    if (something)
        doSomething();
```

* Braces should follow the One True Brace Style, and always start on a new line, for example: 

``` java
    if (something)
    {
        // Correct
    }

    if (something) {
        // Incorrect
    }
```

Regardless of language.

## **Packages**

* Always lowercase with a '.' between a word, for example: ```some.package.name```
* Should be split into a folder like structure, such as ```common.config``` and ```common.message```

## **Imports**

* Organise packge imports into the same group.
* No wildcard imports.

## **Comments**

* Single line comment: 

```java 
    // Example.
```

* Multiline comment:

```java
    /**
    * A comment that spans
    * across multiple lines.
    */
```

## **Documentation**

* Documentation should only be used when necessary, for example the method ```getName()``` does not need documenting, but a method like ```sendRequestMessage(request)``` would perhaps need some documentation on where the request is going and what type of request it is.

* Each class should also have a relevant documentation block before it:

``` java
    /**
    * An abstract class for processing messages in the message folder. <p>
    * Each service that implements <code>MessageReceiver</code> has a list of file types it is allowed to process - so that each service
    * does not process messages made for other services (e.g. the List service cannot processes messages for the OR service).
    */
    public abstract class MessageReceiver
    {
        // ...
    }
```

# *Statements*

## **Classes**

* Written in the ```UpperCamelCase``` form.
* See the Camel Case Definition section for classes which have acronyms.

## **Methods**

* Written in the ```lowerCamelCase``` form. Methods should be self describing and only do one thing, for example ```updateUsername()``` or ```getDatabaseConnection()```. If the method does more than one thing, it needs to be refactored.
* Other examples:
    
```java
    exportHtmlSource(); // NOT: exportHTMLSource();
    openDvdPlayer();    // NOT: openDVDPlayer();
```

* Methods should have **no more** than four parameters. If it requires more, refactor your code (for example, use a Builder).
* Parameters should remain on the same line unless its absolutely necessary to put them on a new line (e.g. readability):  

```java
    throw new IllegalStateException("Failed to process"
                                    + " request " + request.getId()
                                    + " for user " + user.getId()
                                    + " query: '" + query.getText() + "'");
```

## **Constants**

* Constants are written in ```CONSTANT_CASE```:
    ```java
    private int SOME_VARIABLE = 5;
    ```

## **Variables**

* Written in the ```lowerCamelCase``` form. Variables should be self describing: 
    ```java
        private String username = "";
        private String primaryAddress = "";
        private String newCustomerId = "";
    ```
* Do not use public variables unless absolutely neccessary. Variables should be private with a public ```get()``` and ```set()``` method.

## **Modifiers**

* Modifiers should be specified in the following order as defined by the Java Language Specification:

```java
    public protected private abstract default static final transient volatile synchronized native strictfp

    // Good - static before final
    public static final String variable = "";

    // Bad - final before static
    public final static String variable = "";
```

## **Conditions**

* Where applicable, use a Ternary operator for simple statements:

```java
    int number = 10
    boolean isLessThanTen = (number < 10) ? true : false; 

    // Instead of:

    int number = 10;
    boolean isLessThanTen;

    if (number < 10)
    {
        isLessThanTen = true;
    }
    else
    {
        isLessThanTen = false;
    }
```

* Switch statements should always have a default statement:

```java
    switch (condition)
    {
        case someCase:
            // do something
            break;
        
        case anotherCase:
            // do something else
            break;

        default:
            // do a default, perhaps logging or a default value
            break
    }
```

## **Try/Catch**

* Clean up with finally:

```java
    // Bad - Connection is not closed if sendMessage throws.
    if (receivedBadMessage) 
    {
        conn.sendMessage("Bad request.");
        conn.close();
    }

    // Good
    if (receivedBadMessage) 
    {
        try 
        {
            conn.sendMessage("Bad request."); 
        } 
        finally 
        {
            conn.close();
        }
    }
```

## **Exceptions**

* Where appropiate, throw the correct exception instead of just a general ```Exception```. If one does not exist, create your own:  

```java
    class StorageException extends Exception 
    {
        ...
    }
```

## **Camel Case Defined**

Camel casing should follow this rule, with the exception of the word ```OR``` (as this looks like the word ```or```):

|           Form          |      Correct      |     Incorrect     |
|:-----------------------:|:-----------------:|:-----------------:|
| "XML HTTP request"      | XmlHttpRequest    | XMLHTTPRequest    |
| "new customer ID"       | newCustomerId     | newCustomerID     |
| "supports IPv6 on iOS?" | supportsIpv6OnIos | supportsIPv6OnIOS |
| "change OR request    " | changeORRequest   | changeOrRequest   |
