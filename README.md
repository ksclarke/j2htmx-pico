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

## Incorporating HTMX elements

## Styling with Pico CSS
