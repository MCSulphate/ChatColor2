# ChatColor2
## Description:
ChatColor allows players to change the color and style of their chat to 
many different things! All the Minecraft colors and modifiers supported!

## Features:
* Change the color of yours or another player's chat to a huge variety of options!
* Choose exactly what you want to use, and type one single command to use it!
* This plugin supports all the Minecraft colors as well as the modifiers, giving
a wide range of customisation!
* Choose up to four different modifiers for your color!
* Rainbow chat color allows you to have a color pattern for your messages!
* Not only that, but you can change the colors in the pattern to whatever you like!
* Now also supporting console commands! Have a donor purchase? Why not add a chat color? :)
* Useful help commands to sort out any problems you might run in to!
* Easy to use permissions to restrict specific colors and modifiers, or allow all
of them with wildcard permissions!
* A permission for every color and every modifier, as well as every command!
* This plugin is fully customisable!! What does that mean? Well:
  * You can change nearly all of the messages in the plugin in the config!
  * As well as this, change cooldowns, join message, notifications and more!

## Commands:

### Key:
\<value> = Required value.\
\[value] = Optional value.

### Main Command:
* **/chatcolor \[player] <color> \[modifiers]** - Change yours or a player's chat color!

### Admin Commands:
* **/chatcolor enable** - Attempts to enable the plugin if something went wrong.
* **/chatcolor permissionshelp** - Shows permissions help!
* **/chatcolor settingshelp** - Shows settings help!
* **/chatcolor reload** - Reloads the config, use if you edited it!
* **/chatcolor reloadmessages** - Reloads all messages, use if you changed them!
* **/chatcolor reset** - Use with caution, resets the config to the default!
* **/chatcolor set <setting> <value>** - Changes one of the many settings! (See below
for details)

### Other Commands:
* **/chatcolor** - Shows your current chat color!
* **/chatcolor available** - Shows your available colours and modifiers!
* **/chatcolor commandshelp** - Shows command help! You can also use **/chatcolor help**.

### Settings:
* **command-name** - Changes the base command to whatever you want!
* **color-override** - Changes whether ChatColor overrides '&' color symbols in messages.
* **confirm-timeout** - Changes the time that players get when making a decision.
* **default-color** - Changes the color that all players get when joining (also one-time
sets all players).
* **join-message** - Changes if players are told their color when joining.
* **notify-others** - Changes whether players are told if their chat color is changed
by someone else.
* **rainbow-sequence** - Changes the colors in the rainbow chat color pattern.
* **auto-save** - Changes whether the plugin will auto-save to files every 5 minutes, in
case of a crash.

### Colors & Modifiers:
**Valid Colors:**
* 0 or black
* 1 or dark.blue
* 2 or green
* 3 or dark.aqua
* 4 or red
* 5 or purple
* 6 or gold
* 7 or grey
* 8 or dark.grey
* 9 or blue
* a or light.green
* b or aqua
* c or light.red
* d or magenta
* e or yellow
* f or white

**Valid Modifiers:**
* k or obfuscated
* l or bold
* m or strikethrough
* n or underlined
* o or italic

### Permissions:
* **chatcolor.*** - Allows a player to use everything in ChatColor.
* **chatcolor.use** - Allows a player to use /chatcolor and /chatcolor cmdhelp.
* **chatcolor.color.*** - Allows a player to use all colors.
* **chatcolor.color.\<color>** - Allows a player to use color.
* **chatcolor.modifier.*** - Allows a player to use all modifiers.
* **chatcolor.modifier.\<modifier>** - Allows a player to use a modifier.
* **chatcolor.change.*** - Allows a player to change everyone's chat color.
* **chatcolor.change.self** - Allows a player to change their own color.
* **chatcolor.admin.*** - Allows a player to use all admin commands.
* **chatcolor.admin.\<command>** -Allows a player to use an admin command.

**Note:** You must use either numbers or letters found above for the colors and
modifiers permissions except 'chatcolor.color.rainbow'.

### Videos
* Plugin showcase by honanulu :)