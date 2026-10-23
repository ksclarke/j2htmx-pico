# j2htmx-pico [![Project Status: WIP – Initial development is in progress, but there has not yet been a stable, usable release suitable for the public.](https://www.repostatus.org/badges/latest/wip.svg)](https://www.repostatus.org/#wip)

An opinionated Java to HTML generator that supports HTMX (v4) and Pico CSS (v2). It's based on
the [j2html](https://j2html.com) project (which, to my knowledge, is
[no longer active](https://www.reddit.com/r/java/comments/mu3ptp/j2html_is_looking_for_a_maintainer/)). Since it's a
fork, it continues the licensing and versioning of the original project.

> [!WARNING]
> j2htmx-pico serves a narrow, specialized use case and is best suited for simple, small-scale HTML interfaces rather
than large, complex websites. While it offers type-safe, Java-native performance, it achieves this by abandoning the
Separation of Concerns design pattern that helps make larger websites maintainable.

## Getting Started

To use j2htmx-pico, you need to include it as a dependency in your Maven-built project.

### Add the Dependency to your POM:

```xml

<dependency>
    <groupId>info.freelibrary</groupId>
    <artifactId>j2htmx-pico</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

### Building the Project

If you want to build the project yourself, there are a few pre-requisites that need to be installed first.

**Pre-requisites:**

* **JDK 21 or higher** – _Personally, I'd recommend installing the JDK of your choice with
  [SDKMAN!](https://sdkman.io)_
* **Maven 3.8.6 or higher** – _Optional, if you use the `mvnw` or `mvnw.cmd` command in the project's root directory_

Once the pre-requisites are installed, check the project out from its source repository and then run the following
command inside the project's root directory:

```bash
mvn install -Denforcer.skip=true
```

> [!WARNING]
> The build's rule enforcer must be skipped because the project is currently in development and should not be considered
production-worthy. This is not a reflection on j2html, but on my AI-driven integration of HTMX and Pico CSS.

## Basic Use

`j2htmx-pico` allows you to construct type-safe HTML in Java using a fluent, functional builder pattern. By statically
importing `TagCreator.*`, you can compose DOM trees that compile into clean HTML strings with zero template-parsing
overhead.

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

public class Main {
    public static void main(String[] args) {
        String html = html(
          head(
            title("Basic Example")
          ),
          body(
            header(
              h1("Welcome to j2htmx-pico")
            ),
            main(
              p("Generate type-safe HTML directly in Java with minimal runtime overhead."),
              ul(
                li("Compile-time safety for element hierarchies"),
                li("Zero reflection or external template parsing costs"),
                li("Fluent tag chaining and conditional attributes")
              )
            )
          )
        ).render();

        System.out.println(html);
    }
}
```

Dynamic collections and conditional structures integrate smoothly with standard Java streams or the built-in `each()`
helper:

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

import java.util.List;

List<String> items = List.of("Item Alpha", "Item Beta", "Item Gamma");

String listHtml = ul(
  each(items, item -> li(item))
).render();
```

### Working with Attributes: `with*`, `is*`, and `withCond*`

`j2htmx-pico` provides fluent methods for configuring HTML, ARIA, and custom attributes. The method naming reflects how the attribute is rendered:

#### 1. Unconditional Setters (`with*`)
Parameterized methods accept an explicit value and unconditionally set the attribute:

```java
div().withClass("card").withId("main-content");
// Output: <div class="card" id="main-content"></div>

button().withAriaHidden(true);
// Output: <button aria-hidden="true"></button>
```

#### 2. Boolean & Flag Shorthands (`is*`)
For standard HTML boolean attributes where presence implies truthiness (and no value is used in HTML syntax), zero-argument `is*()` methods set the attribute flag:

```java
input().isDisabled().isRequired();
// Output: <input disabled required>

div().isHidden();
// Output: <div hidden></div>
```

Zero-argument `is*()` is also used for HTML on/off attributes that default to the enabled keyword:

```java
form().isAutocomplete();
// Output: <form autocomplete="on"></form>
```

#### 3. Conditional Setters (`withCond*`)
Conditional setters evaluate a boolean condition. How the condition is applied depends on the category of the attribute:

* **Inclusion Guards (Standard & Boolean Attributes):**
  For standard HTML boolean attributes, on/off attributes, and value-bearing attributes, `withCond*` acts as a guard. When the condition is `true`, the attribute is rendered; when `false`, the attribute is **omitted entirely**:

  ```java
  // Boolean flag guard:
  input().withCondDisabled(isReadOnly);
  // true  -> <input disabled>
  // false -> <input>

  // On/Off attribute guard:
  form().withCondAutocomplete(enableSuggestions);
  // true  -> <form autocomplete="on"></form>
  // false -> <form></form>

  // Value-bearing attribute guard:
  button().withCondAriaHidden(shouldHide, true);
  // true  -> <button aria-hidden="true"></button>
  // false -> <button></button>
  ```

* **Value Switchers (Tristate / Enumerated Attributes):**
  Certain HTML attributes (`translate`, `draggable`, `spellcheck`, `contenteditable`) are **tristate**: they can be explicitly positive, explicitly negative, or omitted to inherit parent/browser defaults. For these attributes, `withCond*` acts as a two-way switch between explicit positive and negative states:

  ```java
  p("Content").withCondTranslate(isTranslatable);
  // true  -> <p translate="yes">Content</p>
  // false -> <p translate="no">Content</p>

  div().withCondDraggable(canDrag);
  // true  -> <div draggable="true"></div>
  // false -> <div draggable="false"></div>

  div().withCondSpellcheck(checkSpelling);
  // true  -> <div spellcheck="true"></div>
  // false -> <div spellcheck="false"></div>

  div().withCondContenteditable(isEditable);
  // true  -> <div contenteditable="true"></div>
  // false -> <div contenteditable="false"></div>
  ```

  > [!NOTE]
  > Because `withCond*` on tristate attributes always outputs either the positive or negative keyword, if you want an element to inherit the browser or ancestor default, omit calling the setter altogether.

## Incorporating HTMX elements

HTMX attributes are exposed directly as type-safe builder methods on all tags, providing seamless dynamic updates,
server requests, and partial DOM swaps without requiring complex client-side JavaScript frameworks.

### Request and Target Attributes

Core HTMX interaction attributes—including `hx-get`, `hx-post`, `hx-target`, `hx-swap`, `hx-trigger`, and
`hx-indicator` are available as fluent methods:

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

button("Load Items")
  .

withHxGet("/api/items")
  .

withHxTarget("#item-list")
  .

withHxSwap("innerHTML")
  .

withHxIndicator("#loading-spinner")
  .

render();
```

This generates:

```html

<button
        hx-get="/api/items"
        hx-target="#item-list"
        hx-swap="innerHTML"
        hx-indicator="#loading-spinner">Load Items
</button>
```

### Event Handlers, Confirmations, and Partial Responses

You can attach inline event handlers using `withHxOn`, configure confirmations, and assemble multi-target server swap
fragments using `hxPartial`:

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

// Action with confirmation dialog and response callback

button("Delete Record")
  .

  withHxDelete("/records/42")
  .

  withHxConfirm("Are you sure you want to delete this record?")
  .

  withHxOn("htmx:afterRequest","console.log('Record removed')");

  // Server partial responses targeting multiple DOM regions
  String partialResponse = hxPartial(
    div("Update successful!").withId("status-message"),
    ul(li("Updated item 1"), li("Updated item 2")).withId("item-list")
  ).render();
```

## Styling with Pico CSS

Pico CSS (v2) provides pure semantic styling out of the box, producing clean, accessible, and responsive layouts while
avoiding manual CSS overhead.

### Linking the Stylesheet

You can reference Pico CSS directly from the CDN or embed the bundled minified stylesheet for self-contained,
offline-ready HTML documents:

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

import info.freelibrary.j2htmx.pico.Pico;

// Official Pico v2 CDN link

head(Pico.css());
  // Output: <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css">

  // Specific version or color scheme (e.g. amber, azure, zinc)
  head(Pico.css(Pico.Color.AZURE));
  // Output: <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.azure.min.css">

  // Bundled inline stylesheet for offline/standalone output
  head(Pico.inlineCss());
// Output: <style>...bundled minified css...</style>
```

### Layout Components, Accessibility, and HTMX Synergy

Pico uses semantic HTML elements, grid layouts, and ARIA attributes for state and interactivity. Helper methods in `Pico` such as
`Pico.container()`, `Pico.containerFluid()`, and `Pico.grid()` simplify document hierarchy:

```java
import static info.freelibrary.j2htmx.pico.TagCreator.*;

import info.freelibrary.j2htmx.pico.Pico;

String page = html(
  head(
    title("Pico CSS Dashboard"),
    Pico.css()
  ),
  body(
    Pico.container(
      h2("Interactive Form"),
      Pico.card(
        form(
          Pico.grid(
            input().withType("text").withName("name").withPlaceholder("Name"),
            input().withType("email").withName("email").withPlaceholder("Email")
          ),
          button("Submit")
            .asButton()
            .withAriaBusy(true) // Displays Pico's native loading indicator
            .withDataTooltip("Click to save record")
            .withDataPlacement("top")
            .withHxPost("/api/users")
            .withHxTarget("#result")
        )
      ),
      div().withId("result")
    )
  ).withDataTheme("dark") // Configure dark or light color theme
).render();
```
