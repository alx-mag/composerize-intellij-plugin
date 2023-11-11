# Docker Run Command to Compose Converter

A plugin for Intellij IDEs.
<!-- Plugin description -->
Converts `docker run` commands into yaml format when pasted into `docker-compose.yml` file. 

Works like [composerize.com](https://www.composerize.com/).

### Example

Copy the `docker run` text:
```
docker run --name some-postgres -e POSTGRES_PASSWORD_FILE=/run/secrets/postgres-passwd -d postgres
```

Paste it into the file:
```yaml
# docker-compose.yml (before)
version: "3"
```

The text will be pasted like this:

```yaml
# docker-compose.yml (after)
version: "3"
services:
  some-postgres:
    image: postgres
    environment:
      - POSTGRES_PASSWORD_FILE=/run/secrets/postgres-passwd
    container_name: some-postgres
```

<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Docker Run Command to Compose Converter"</kbd> >
  <kbd>Install</kbd>
  
- Manually:

  Download the [latest release](https://github.com/alx-mag/composerize-intellij-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
