# Vape City
An attempt at a procedually generated, beautifully stylized city - inspired by both the sound and aesthetic of vaporwave.

If you're asking "What does that mean?", that's just fine. You don't need to know the music to appreciate the aesthetic - which can be seen [here](https://levels.io/wp-content/uploads/2015/10/tumblr_nv2cqhOJPL1ufh7yno1_1280.png), [over here](https://ih1.redbubble.net/image.210256057.1956/flat,1000x1000,075,f.u1.jpg), and [right over there](https://thump-images.vice.com/images/tracks/meta/2015/09/15/prepare-to-get-deliriously-dreamy-with-vaporwave-sensation-2814s-beyond-blissful-shinjitsu-no-koi-1442324234.png?crop=1xw:0.9542220113852xh;center,top&resize=800:*&output-quality=75). Or just Google it! 

Basically, it's brightly colored, delibarately low fidelity sort of aesthetic. That could mean looking like a seriously abused VHS tape - but in this case, it means pixel textures and blocky structures.

Uses techniques described by Shamus Young in his [Pixel City](http://www.shamusyoung.com/twentysidedtale/?p=2940) project.

This project was built using the [JMonkey Engine](https://github.com/jMonkeyEngine) and this repository is maintained using the built-in team tools in the JMonkey SDK. So if you want to look at the project or build it for yourself, you'll need to download the SDK first.

This project also makes use of [Tralala's SpriteLibrary](https://hub.jmonkeyengine.org/t/spritelibrary-efficient-render-of-sprites/20901) for the JMonkeyEngine. I am currently working to remove this dependency.

Requirements
-----
Although this is an ongoing project, and is subject to changes in direction, I think it'd be useful to outline some basic requirements.

1. The progam must have a smooth and consistent framerate. I intend to use this software live; a poor framerate would make the software completely useless.
  
  
2. While each city is randomly generated, the user should have control over what is randomly generated and what is generated according to a seed.


3. There must be some way to influence or determine the pattern of the city - by "pattern", I mean the scheme that we use to determine building characteristics based on position. Some examples:
	* The "pattern" of [most](http://www.incimages.com/uploaded_files/image/1940x900/los-angeles11-44-43_40530.jpg) [modern](http://theneotrad.com/wp-content/uploads/2015/03/Chicago-14-09-7331-1024x682.jpg) [(American)](http://vignette1.wikia.nocookie.net/godfather/images/4/4b/New_Orleans.jpg/revision/latest?cb=20131123150035) [cities](http://alumni.virginia.edu/uvaclubs/wp-content/uploads/sites/15/2013/05/Philadelphia.jpg) are tall skyscraper buildings at the center, which then quickly shorten into smaller buildings as they sprawl outwards.
	* The "pattern" of [Manhattan](http://www.newmediasystems.net/wp/wp-content/uploads/2015/02/000_2479.jpg) is clusters of skyscrapers with smaller buildings elsewhere.
	* The "pattern" of [Hong Kong](http://kontak-perkasaf.com/wp-content/uploads/2016/07/victoria-peak-2.gif) seems to be buildings that are consistently at skyscraper height, decreasing in height as they go into the distance - a much more gradual slope.


4. Before the city is generated, the user must be able to choose from a variety of color schemes. Each scheme must be represented by an easily exchangable file - such as JSON or XML. 


5. The city must not appear homogenous - there should be variation both in building height, building structure, and street elevation.


6. The actual program should consist of a single shot of the city - which holds for a certain period before transitioning to a different shot. 


7. There should be mutliple types of shots - static shots, linear tracking shots, circular tracking shots, etc. These shots should be randomly chosen from a list of shots, generated so that none of the shots clip buildings.
 

8. Transitions should be smooth. While I would like to have multiple types of transitions, a fade in-and-out from black should suffice.


9. The user should be able to transition to a different, shot at will, before the period ends.


10. The user should be able to determine the variables involved with the shots befor the city is generated. Speculatively, this includes: the frequencies of certain types of shots, the frequencies of certain types of transitions, the average time period of shots, the time period for each type of transition, the frenquecy of shot targets, and possibly others. 

Eventual Goals - Hopes, Dreams, Miscellaneous
-----
* I hope to (eventually) add some decidedly sci-fi options to the city generation, primarily inspired by the art of [Simon Stålenhag](http://www.simonstalenhag.se/). His art frequently features [levitating](https://s-media-cache-ak0.pinimg.com/originals/81/4c/b3/814cb35540d8185cc4717c678e3ecae6.jpg) [vehicles](http://i.pi.gy/ndoz.png) and [monolithic](https://ih0.redbubble.net/image.235766581.0426/flat,1000x1000,075,f.jpg) [industrial or residential](https://s-media-cache-ak0.pinimg.com/originals/2d/2f/ad/2d2fadc7c59406470caabfed57d07118.jpg) [structures](http://www.simonstalenhag.se/bilder/by_mainservers1.jpg). I'm particularly excited about the ships - having them slowly (or quickly) moving over the city, silhoutted against the sky, will look really cool!


* I also want to add structures that aren't just buildings - plazas would be a nice start. If that goes well, I could try things like ports and wharfs, or maybe parks.


* The way I'm going to use this, I will be having a separate program playing music from a playlist. It could be good to integrate this into the program itself - and maybe even perform some sort of analysis, so that transitions could be tied in-time with the music.

* Weather effects would be nice. I love cities wreathed in fog and rain - and [Simon Stålenhag](http://www.simonstalenhag.se/) seems to love it as well! At minimum, I'd like to try and implement a sort of rainy lens effect - like the one seen [here](http://igloomag.com/wp/wp-content/uploads/2016/04/2814-birth-of-a-new-day-animated.gif). 

* What should occupy the horizon? Probably some sort of mountains, or an ocean of some kind (especially if I implement some sort of port), or maybe just even more sprawl.

* I'd also love some extra details - elevated rails, regular rails, canals, washes, rivers, etc.
