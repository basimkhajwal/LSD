# LSD

![](./android/assets/images/icon.png)

### Game Information:
A casual physics based arcade game where you fire the character around, avoiding the obstacles, to hit and destroy all the black platforms in order to beat the level.  This is a port of the original game idea (which was written in Lua) but this one is written in Java using the awesome libGDX framework. 

### Running the current game:
It can compile to the following platforms:
- Desktop (requires Java 1.6+ and OpenGL 3+) *Tested current version*
- Mobile (iOS / Android) *Tested (only android)*
- HTML5 (any modern browser, Internet Explorer requires Google Chrome Frame) *Tested (Box2D error cropping up for now)*

A compiled (but not regularly updated) HTML5 version is at: [basimkhajwal.freeoda.com] (http://basimkhajwal.freeoda.com)
Note: The HTML version is currently having physics issues and objects can slide through each other, it is not currently working correctly

They should all work but only desktop and HTML5 builds have currently been tested. For the latest version, download this repository and run the gradle-based projects on your computer -- more [here](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline).

### Development:

#### TODO:
- [ ] Improve menu/title colours
- [ ] Improve level select and pack select (make them look nicer)
- [ ] Add smooth transitions between screens
- [ ] Introduce variable level numbers within a pack
- [ ] Change GUI scale so it feels more comfortable on a smaller device 
- [ ] Create more levels

#### Currently Working:
- [x] Platforms are loaded from a Tiled map (.tmx) format
- [x] Platforms destroy correctly when player launched off them
- [x] Platform count / destroyed
- [x] Player moves accurately and the aim is precise
- [x] Camera movement to follow the player
- [x] Add ability to restart the level
- [x] Implement player death when reaching map boundaries
- [x] Add moving down jump animation
- [x] Create solid blocks the player has to avoid
- [x] Implement player death with solid blocks
- [x] Update the GUI and create a flat theme
- [x] Add better buttons and make the menu screen more attractive
- [x] Change the font rendering to BitmapFontData scale rather than many pre-loaded image files
- [x] Create a player died menu
- [x] Create a level finished menu
- [x] Add particle effects when platform is destroyed
- [x] Add camera shake effect when platform is destroyed
- [x] Add a sound manager
- [x] Implement background music and sounds for player death, jump etc.
- [x] Different sized particles
- [x] Add ability to progress from level to level
- [x] Create a loader to load levels from the previous version of LSD
- [x] Add custom shaders for the background
- [x] Add parallax effect for the game
- [x] Create a sub-menu / level selection screen
- [x] Particles for player death
- [x] Slow-motion level complete / player death
- [x] Add timer for each platform
- [x] Add a central event queue / re-organise code
- [x] Gesture detection and pinch zoom in/out
- [x] Prevent player movement after death
- [x] Prevent firing through a block
- [x] Add a pause option/screen
- [x] Add JSON read/write for a persistent Settings value
- [x] Create a settings screen
- [x] Create an extensible map format that will allow various objects
- [x] Add moving platforms
- [x] Customize shader for the game (add a new one)
- [x] Add lasers that automatically toggle on intervals
- [x] Add option of having multiple map packs (with a scroll screen in between)
- [x] Prevent accessing end levels until unlocked
- [x] Allow incrementing levels unlocked
- [x] Add sounds for button clicks / randomize explosion sounds
- [x] Add a 3-star collection per level and a total star count
- [x] Add "reset game" option
- [x] Create a generic dialog box screen
- [x] Add a currency system with stars in order to unlock new packs