This is my terrible and rushed attempt at a tool to allow uploading of a spoiler log for the OoT/MM Combined Randomizer and giving you the ability to check if a certain item is in a certain location.  The UI is horrendous but I am a backend web developer... if this was just for myself it would be a UI-less terminal program where I would pass in command line arguments to check things, but I'm stepping out of my comfort zone into a minimal amount of UI work to try and make this as user friendly as possible.

The idea for this came about watching a randomizer run by ZFG (https://www.youtube.com/watch?v=x2MnvMyfjOY&t=2s) where the seed was potentially impossible at around 4 hours in when Oath to Order was discovered as locked behind swamp skulltula tokens.  I thought it would be an interesting exercise to make a small app to let you check if an item is in a certain location like a skulltula token on the moon to determine if the seed is impossible.

This is meant as a way to check if a seed is beatable without spoiling what is in a given location, this means it will only tell you if something is there, not the items that are there.

There are likely bugs and issues with it, so until I can do more thorough testing I would take what it says with a grain of salt.

The spoiler logs included in the resources directory can help test functionality, though feel free to make randomizer or plandomizers that will test it as well.

Future releases if I can continue working on it will include other checks in the easy check button (Hammer behind Volv, Mirror Shield behind Twinrova etc.) and potentially a way to search a specific check to see if it is an item.  Other suggestions are welcome as well.

This has been compiled and tested on both Mac and Windows

Requires Java 11+ to run the JAR file