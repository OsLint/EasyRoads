# EasyRoads 🛤️

**EasyRoads** is a Minecraft Bukkit plugin that enhances player movement by modifying their speed based on their location on predefined roads. This plugin allows server administrators to create custom roads with specific speed attributes.

## Table of Contents 📚
- [Features](#features)
- [Installation](#installation)
- [Configuration](#configuration)
- [Commands](#commands)
- [Permissions](#permissions)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)



## Features ✨

- Define roads with specific speeds and blocks.
- Adjust player and entity movement speeds based on their location on roads.
- Reload configuration and list roads via in-game commands.

## Installation 📥

1. Download the latest version of EasyRoads from the releases section.
2. Place the `EasyRoads.jar` file into your server's `plugins` directory.
3. Restart or reload your server to enable the plugin.

## Configuration ⚙️

The plugin uses a configuration file located in `plugins/EasyRoads/config.yml`. Key settings include:

- `speedIncreaseRate`: Rate at which the speed increases when on a road.
- `speedDecayRate`: Rate at which the speed decreases when off a road.
- `messages`: Customizable messages for various plugin actions.
- `roads`: Define roads with their respective speed and block types.
- `affectedEntities`: List of entity types affected by the road speeds.

## Example configuration:

```yaml
# This is the main configuration file for the plugin
# You can add more roads by adding more road names
# and blocks to the list of blocks for each road

# maximum acceleration per tick
speedIncreaseRate: 0.1
# maximum deceleration per tick
speedDecayRate: 1

# Customizable messages (you can use color codes)
messages:
  onRoad: "&cYou are on a road!"
  noPermission: "&4You do not have permission to use this command."
  reloadSuccess: "&aConfiguration reloaded successfully."
  listHeader: "&6Roads:"
  help:
    header: "&bEasyRoads commands:"
    reload: "&7/easyroads reload - Reload the EasyRoads configuration."
    list: "&7/easyroads list - List all roads."
    help: "&7/easyroads help - Display this help message."
  invalidCommand: "&cInvalid subcommand. Use /easyroads help for available commands."

# Affected non-player living entities, may affect performance negatively
# See https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html
affectedEntities: [ ]

# Road definitions
roads:
  road1:
    speed: 0.5
    blocks:
      - DIRT_PATH
  road2:
    speed: 3
    blocks:
      - ANY
      - GRAVEL
      - COBBLESTONE
      - COBBLESTONE
  road3:
    speed: 5
    blocks:
      - Any
      - STONE_BRICKS
      - GRAVEL
      - COBBLESTONE
      - COBBLESTONE
```
Example roads
![Screenshot 2024-08-22 at 18 49 22](https://github.com/user-attachments/assets/ea7e2e9c-c0a5-4183-b733-d00eb46a796b)


## Commands 📝
```/easyroads help:``` Display the help message.

```/easyroads reload``` Reload the plugin configuration.

```/easyroads list``` List all configured roads.
## Permissions 🔑
```easyroads.reload``` Permission to reload the configuration.

```easyroads.list``` Permission to list all roads.
## Troubleshooting 🛠️
If you encounter issues, check the server console for error messages and ensure that the configuration file is correctly formatted. For further assistance, feel free to open an issue on the GitHub repository.
## Contributing 🤝
Contributions are welcome! Please fork the repository and submit a pull request with your changes.
## License 📜
This project is licensed under the MIT License.
