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
            a("Check it out").withHref("https://projects.freelibrary.info/j2htmx-pico")
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

## Pico.css Support

`j2htmx-pico` includes built-in support for **Pico.css v2**, providing semantic CSS, responsive layouts, color theming, and accessibility attributes without requiring JavaScript or custom CSS writing.

### Linking the Stylesheet

You can load Pico.css from the official CDN using `picoCss()` or embed the bundled minified stylesheet directly into your HTML with `picoInlineCss()`:

```java
// Default Pico v2 stylesheet via CDN
picoCss();
// Output: <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css">

// Specific version and color palette (e.g. amber, azure, zinc)
picoCss("2.0.6", "amber");
// Output: <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.amber.min.css">

// Using Color enum
picoCss(Pico.Color.AZURE);

// Bundled inline stylesheet for offline or standalone HTML
picoInlineCss();
// Output: <style>...minified pico css...</style>
```

### Layout Helpers

Pico.css emphasizes semantic layout tags:

* `container(DomContent...)` – creates `<main class="container">`
* `containerFluid(DomContent...)` – creates full-width `<main class="container-fluid">`
* `grid(DomContent...)` – creates responsive `<div class="grid">` columns

```java
container(
    h1("Dashboard"),
    grid(
        card(h3("Card 1"), p("First column")),
        card(h3("Card 2"), p("Second column"))
    )
)
```

### Semantic Components

Pico provides helpers for UI components:

```java
// Dropdown menu (<details class="dropdown">)
Pico.dropdown("Actions",
    a("Edit Profile").withHref("/profile/edit"),
    a("Log Out").withHref("/logout")
);

// Semantic card (<article>)
Pico.card(
    header(h2("Card Header")),
    p("Card content goes here..."),
    footer(button("Submit"))
);

// Modal Dialog (<dialog id="...">)
Pico.modal("confirm-dialog",
    article(
        header(h3("Confirm Action")),
        p("Are you sure you want to proceed?"),
        footer(
            button("Cancel").withClass(Pico.SECONDARY),
            button("Confirm")
        )
    )
);
```

### Themes, Tooltips & Form Accessibility

Leverage Pico.css data attributes and ARIA attributes via fluent methods on any tag:

```java
// Light/Dark Theme toggle on <html> or any container
html().withDataTheme("dark");

// Tooltips and tooltip placement
button("Help").withDataTooltip("Helpful explanation").withDataPlacement("bottom");

// Loading states
button("Saving...").isAriaBusy();

// Form validation states
input().withType("text").withAriaInvalid(true); // Red invalid outline
input().withType("text").withAriaInvalid(false); // Green valid outline

// Role convenience methods
a("Action Link").asButton();  // <a role="button">
div().asGroup();              // <div role="group">
form().asSearch();            // <form role="search">
```

### Combining Pico.css with HTMX

Pico.css and HTMX complement each other seamlessly:

```java
html(
    head(
        title("Pico + HTMX App"),
        picoCss()
    ),
    body(
        container(
            h1("User Management"),
            grid(
                form(
                    input().withType("text").withName("username").withPlaceholder("Username"),
                    button("Add User")
                        .asButton()
                        .withHxPost("/api/users")
                        .withHxTarget("#user-list")
                        .withHxIndicator("#loading")
                ).asSearch(),
                div().withId("loading").isAriaBusy()
            ),
            div().withId("user-list")
        )
    )
)
```

## More Information
Visit the project website at [j2htmx-pico](https://projects.freelibrary.info/j2htmx-pico) for more examples and documentation.
