# Getting Started with j2html (including HTMX)

j2html is a fast and typesafe Java HTML builder. It allows you to build HTML using a fluent API that looks very similar to the actual HTML.

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>info.freelibrary</groupId>
    <artifactId>j2html</artifactId>
    <version>1.6.1-SNAPSHOT</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```groovy
dependencies {
    implementation 'info.freelibrary:j2html:1.6.1-SNAPSHOT'
}
```

## Basic Usage

The primary way to use j2html is by importing `info.freelibrary.j2html.TagCreator.*` and using its static methods.

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

public class Main {
    public static void main(String[] args) {
        String renderedHtml = body(
            h1("Welcome to j2html"),
            p("Typesafe HTML in Java is great!"),
            a("Check it out").withHref("https://j2html.com")
        ).render();
        
        System.out.println(renderedHtml);
    }
}
```

## HTMX Support

j2html now includes first-class support for **HTMX v4**, allowing you to build reactive web applications entirely in Java.

### Core Attributes

All core HTMX attributes are available as fluent methods starting with `withHx`.

```java
button("Click me")
    .withHxPost("/api/data")
    .withHxTarget("#result-container")
    .withHxSwap("innerHTML")
```

Commonly used attributes include:
* `withHxGet(String url)`
* `withHxPost(String url)`
* `withHxPut(String url)`
* `withHxDelete(String url)`
* `withHxPatch(String url)`
* `withHxTrigger(String trigger)`
* `withHxTarget(String target)`
* `withHxSwap(String swap)`
* `withHxPushUrl(String pushUrl)`
* `withHxConfirm(String confirm)`

### Conditional HTMX Attributes

You can also add HTMX attributes conditionally using `withCondHx*` methods.

```java
boolean isAdmin = true;
button("Delete Item")
    .withCondHxDelete(isAdmin, "/api/item/1")
    .withCondHxConfirm(isAdmin, "Are you sure?")
```

### Inline Scripts with `hx-on`

Use `withHxOn` to handle events with inline JavaScript.

```java
button("Say Hello")
    .withHxOn("click", "alert('Hello from HTMX!')")
```

### The `hx-partial` Tag (HTMX v4)

HTMX v4 introduced the `<hx-partial>` tag for partial updates. You can use it just like any other HTML tag in j2html.

```java
hxPartial(
    div("This is partial content").withClass("updated-content")
).withHxTarget("#main-area")
```

### Complete HTMX Example

Here is a more complete example showing a search input with an indicator:

```java
div(
    label("Search:"),
    input().withType("text")
           .withName("q")
           .withHxPost("/search")
           .withHxTrigger("keyup changed delay:500ms")
           .withHxTarget("#search-results")
           .withHxIndicator("#loading-spinner"),
    img().withId("loading-spinner")
         .withSrc("/spinner.gif")
         .withClass("htmx-indicator"),
    div().withId("search-results")
)
```

## Tips and Tricks

### Using `template` with HTMX
You can use the `template()` tag with HTMX attributes easily:
```java
template().isHx().withType("partial").withHxTarget("#target")
```

### Short Form ID and Classes
j2html supports a short form for IDs and classes:
```java
div("#my-id.my-class", "Content")
// Renders as: <div id="my-id" class="my-class">Content</div>
```
This also works with HTMX tags:
```java
hxPartial("#update-1", p("New data"))
```

## More Information
Visit the official j2html website at [j2html.com](https://j2html.com) for more examples and documentation.
