# Code Style
The following should be applied throughout the code base.

## Layout

### Braces

``` java
    if (something)
    {
        // Correct
    }

    if (something) {
        // Incorrect
    }
```

### Packages

* Always lowercase with a '.' between a word, for example: ```some.package.name```
* Should be split into a folder like structure, such as ```common.config``` and ```common.message```

### Imports

* Organise package imports into the same group.
* No wildcard imports.

### Comments

```java 
    // Example.
```

```java
    /**
    * A comment that spans
    * across multiple lines.
    */
```

### Documentation

* Documentation should only be used when necessary, for example the method ```getName()``` does not need documenting, but a method like ```sendRequestMessage(request)``` would perhaps need some documentation on where the request is going and what type of request it is.

* Each class should also have a relevant documentation block before it:

``` java
    /**
    * An abstract class for processing messages in the message folder. <p>
    * Each service that implements <code>MessageReceiver</code> has a list of file types it is allowed to process - so that each service
    * does not process messages made for other services (e.g. the List service cannot processes messages for the Bug service).
    */
    public abstract class MessageReceiver
    {
        // ...
    }
```

## Statements

### Classes

* Written in the `UpperCamelCase` form.
* See the Camel Case Definition section for classes which have acronyms.

### Methods

* Written in the `lowerCamelCase` form. Methods should be self describing and only do one thing, for example `updateUsername()` or `getDatabaseConnection()`. If the method does more than one thing, it needs to be refactored.
* Other examples:
    
```java
    exportHtmlSource(); // NOT: exportHTMLSource();
    openDvdPlayer();    // NOT: openDVDPlayer();
```

* Methods should have no more than four parameters. If it requires more, refactor your code (for example, use a Builder).
* Parameters should remain on the same line unless its absolutely necessary to put them on a new line (e.g. readability):  

```java
    throw new IllegalStateException("Failed to process"
                                    + " request " + request.getId()
                                    + " for user " + user.getId()
                                    + " query: '" + query.getText() + "'");
```

### Constants

* Constants are written in `CONSTANT_CASE`:
    ```java
    private int SOME_VARIABLE = 5;
    ```

### Variables

* Written in the `lowerCamelCase` form. Variables should be self describing: 
    ```java
        private String username = "";
        private String primaryAddress = "";
        private String newCustomerId = "";
    ```
* Do not use public variables unless absolutely necessary. Variables should be private with a public `get()` and `set()` method.

### Modifiers

* Modifiers should be specified in the following order as defined by the Java Language Specification:

```java
    public protected private abstract default static final transient volatile synchronized native strictfp

    // Good - static before final
    public static final String variable = "";

    // Bad - final before static
    public final static String variable = "";
```

### Conditions

* Where applicable, use a Ternary operator for simple statements:

```java
    int number = 5
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

### Try/Catch

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

### Exceptions

* Where appropriate, throw the correct exception instead of just a general `Exception`. If one does not exist, create your own:  

```java
    class StorageException extends Exception 
    {
        ...
    }
```

### Camel Case Defined

Camel casing should follow this rule, with the exception of the word ```Bug``` (as this looks like the word ```or```):

|           Form          |      Correct      |     Incorrect     |
|:-----------------------:|:-----------------:|:-----------------:|
| "XML HTTP request"      | XmlHttpRequest    | XMLHTTPRequest    |
| "new customer ID"       | newCustomerId     | newCustomerID     |
| "supports IPv6 on iOS?" | supportsIpv6OnIos | supportsIPv6OnIOS |
| "change Bug request    " | changeBugRequest   | changeOrRequest   |
