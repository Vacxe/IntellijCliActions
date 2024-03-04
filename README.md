# CLI Actions (IntelliJ Plugin)

## Description

CLI Actions is an Android Studio plugin that allows you to create shortcuts to terminal commands with grouping for easy access. This plugin simplifies your workflow by enabling you to define and execute frequently used commands right from within Android Studio.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Installation

You can install the CLI Actions plugin in Android Studio using the Plugin Marketplace:

1. Open Android Studio.
2. Navigate to `Preferences/Settings` > `Plugins`.
3. Search for "CLI Actions" in the marketplace.
4. Click `Install` and restart Android Studio to activate the plugin.

For manual installation, download the latest release from the [JetBrains Plugin Marketplace](https://plugins.jetbrains.com/plugin/22134-cli-actions/versions#tabs). You can also visit the [GitHub repository](https://github.com/Vacxe/IntellijCliActions) for more information.

## Usage

CLI Actions allows you to streamline your workflow by defining and executing terminal commands from within Android Studio. Here are a few usage examples:

1. **Grouping of Commands:** Organize your commands into groups for easier access and management.
2. **Custom Configuration Files:** Create configuration files with the `.cliactions.yaml` suffix in your project root or user home directory.
3. **Dynamic Shortcut Generation:** Generate shortcuts dynamically based on your project structure.
4. **Cross-Project Commands:** Define commands at the root user level to make them available across all your projects.

![Plugin Usage](docs%2Fimages%2Fimg.png)

## Features

- **Grouping of Commands:** Organize your commands into groups for efficient management.
- **Custom Configuration Files:** Create `.cliactions.yaml` files in your project or user directory to define shortcuts.
- **Cross-Project Commands:** Define root-level commands accessible across all projects.
- **Dynamic Shortcuts:** Generate shortcuts based on your project structure.

## Configuration

Configure CLI Actions using YAML-based configuration files with the following structure:

```yaml
groups:
  - name: Group Name
    commands:
      - name: Command Name
        command: your-terminal-command
        prompt: true/false (default false)
        forceNewTab: true/false (default false)
```

## Troubleshooting

- Incorrect formatting may render the plugin useless, so ensure your configuration files are correctly formatted.
- Restart your IDE after making any configuration changes.

## Contributing

Contributions to the CLI Actions plugin are welcome. To contribute:

1. Fork the [GitHub repository](https://github.com/Vacxe/IntellijCliActions).
2. Make your changes.
3. Open a pull request (PR) to the repository.

## License

CLI Actions is distributed under the [Apache License, Version 2.0](https://github.com/Vacxe/IntellijCliActions/blob/main/LICENSE). Third-party libraries and components used in the plugin may have their own licenses.
```
