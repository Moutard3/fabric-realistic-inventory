# Realistic Inventory

A Minecraft Fabric mod that changes the player's inventory size depending on what he is wearing.


## Usage

Drop the jar in your mods folder alongside fabric-api.
The mod works in single-player and multi-player (must be present on the server too).

### Important notice (incompatibilities)

This mod changes the way inventories are rendered, thus it is incompatible with any mod that adds an inventory screen (chest, machine, ...).

## Features

- Start with only your two hands
- Equip some leggings: +2 pockets
- Craft and equip a backpack: +9 inventory slots
- More to come...


## FAQ

#### Can I use this mod in my modpack?

You can but keep in mind that any mod adding an inventory will **not** work with this one.

#### I'm still seeing my full inventory

Check that you have the mod on both the client and server and that you're not using any mod adding an inventory, then feel free to open an issue.


## Building

Clone the project

```bash
  git clone https://github.com/Moutard3/fabric-realistic-inventory.git
```

Go to the project directory

```bash
  cd fabric-realistic-inventory
```

Build with gradle

```bash
  gradlew build
```


## License

[MIT](https://choosealicense.com/licenses/mit/)

