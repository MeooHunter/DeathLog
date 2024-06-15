# DeathLogPlugin

## Support : 1.16.5 -> 1.20.6

DeathLogPlugin is a Minecraft plugin to record information when a player dies in the game.

## Build Instructions
1. Clone the project to your computer:
```sh
git clone <project-URL>
```
2.Move into the project directory:
```sh
cd DeathLogPlugin
```
3.Build the plugin with Maven:
```sh
mvn clean install
```

How the plugin works

When a player dies, a record will be created and stored with information about the date of death, location of death, death message, and cause of death.
**Use the command **/deathlog to display a list of death records.

Example
When a player dies in the game, the record will be saved as follows:
```
DeathRecord{
    playerName='Meo',
    deathMessage='Meo was shot to death by skeleton',
    deathLocation=Location{world=world, x=123.45, y=67.89, z=101.12},
    deathDate=Sat Jun 15 2024 13:49:37,
    causeOfDeath='arrow from skeleton'
}
```

> Use the command /deathlog to display these records.

Plugin developed by Nguyen Minh Tuan
