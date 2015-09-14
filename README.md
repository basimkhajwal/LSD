# LSD

### Game Information:
A casual physics based arcade game where you fire the character around, avoiding the obstacles, to hit and destroy all the black platforms in order to beat the level. 

This is a port of the original game idea (which was written in Lua) but this one is written in Java using the awesome libGDX framework. It can compile to desktop (any computer with Java 1.6+ and OpenGL 3+), mobile (iOS / Android) and to HTML5 using GWT. They should all work but only desktop and HTML5 builds have currenly been tested.

### Development:

#### Currently Working:
- [x] Platforms are loaded from a Tiled map (.tmx) format
- [x] Platforms destroy correctly when player launched off them
- [x] Platform count / destroyed
- [x] Player moves accurately and the aim is precise enough

### TODO:
- [ ] Implement player death when reaching map boundaries
- [ ] Create solid blocks the player has to avoid
- [ ] Implement player death with solid blocks
- [ ] Create camera movement to follow the player
