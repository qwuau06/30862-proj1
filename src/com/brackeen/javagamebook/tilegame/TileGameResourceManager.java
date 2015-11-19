package com.brackeen.javagamebook.tilegame;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.tilegame.sprites.*;
import com.brackeen.javagamebook.sound.MidiPlayer;
import com.brackeen.javagamebook.sound.SoundManager;
import com.brackeen.javagamebook.state.ResourceManager;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class TileGameResourceManager extends ResourceManager {

    private ArrayList tiles;
    private int currentMap;

    // host sprites used for cloning
    private Sprite playerSprite;
    private Sprite musicSprite;
    private Sprite coinSprite;
    private Sprite goalSprite;
    private Sprite mushSprite;
    private Sprite grubSprite;
    private Sprite flySprite;
    public Sprite bulletSprite;
    public Sprite starbufSprite;
    public Sprite expoSprite;
    public Sprite poisSprite;
    public Sprite stopSprite;

    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public TileGameResourceManager(GraphicsConfiguration gc,
        SoundManager soundManager, MidiPlayer midiPlayer)
    {
        super(gc, soundManager, midiPlayer);
    }


    public void loadResources() {
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }


    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            currentMap++;
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) {
                if (currentMap == 1) {
                    // no maps to load!
                    return null;
                }
                currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap reloadMap() {
        try {
        	return loadMap(
                "maps/map" + currentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(filename);
        if (url == null) {
            throw new IOException("No such map: " + filename);
        }

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(url.openStream()));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, grubSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, flySprite, x, y);
                }
                // Mushroom denoted by 'M'
                else if (ch == '3') {
                	addSprite(newMap,mushSprite,x,y);
                }
                
            }
        }

        // add the player to the map
        Sprite player = (Sprite)playerSprite.clone();
        player.setX(TileMapRenderer.tilesToPixels(3));
        player.setY(0);
        newMap.setPlayer(player);

        return newMap;
    }

    public Image getTileType(int i){
    	return (Image)tiles.get(i);
    }

    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                TileMapRenderer.tilesToPixels(tileX) +
                (TileMapRenderer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapRenderer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }

    

    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        while (true) {
            String name = "tile_" + ch + ".png";
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource("images/" + name);
            if (url == null) {
                break;
            }
            tiles.add(loadImage(name));
            ch++;
        }
    }


    public void loadCreatureSprites() {

        Image[][] images = new Image[4][];

        // load left-facing images
        images[0] = new Image[] {
            loadImage("player1.png"),
            loadImage("player2.png"),
            loadImage("player3.png"),
            loadImage("fly1.png"),//3
            loadImage("fly2.png"),
            loadImage("fly3.png"),
            loadImage("grub1.png"),//6
            loadImage("grub2.png"),
            loadImage("bt0.png"),//8
            loadImage("bt1.png"),
            loadImage("bt2.png"),
            loadImage("bt3.png"),
            loadImage("btd0.png"),//12
            loadImage("btd1.png"),
            loadImage("starbuff1.png"),//14
            loadImage("starbuff2.png"),
            loadImage("starbuff3.png"),
            loadImage("Expo1.png"),//17
            loadImage("Expo2.png"),
            loadImage("Expo3.png"),
            loadImage("Expo4.png"),
            loadImage("Expo5.png"),
            loadImage("Expo6.png"),
            loadImage("Expo7.png"),
            loadImage("Expo8.png"),
            loadImage("Expo9.png"),
            loadImage("Expoa.png"),
            loadImage("Expob.png"),
            loadImage("Expoc.png"),
            loadImage("Expod.png"),
            loadImage("Expoe.png"),
            loadImage("Expof.png"),
            loadImage("stop.png") //32
        };

        images[1] = new Image[14];
        images[2] = new Image[14];
        images[3] = new Image[14];

        for (int i=0; i<14; i++) {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
        }

        // create creature animations
        Animation[] playerAnim = new Animation[4];
        Animation[] flyAnim = new Animation[4];
        Animation[] grubAnim = new Animation[4];
        for (int i=0; i<4; i++) {
            playerAnim[i] = createPlayerAnim(
                images[i][0], images[i][1], images[i][2]);
            flyAnim[i] = createFlyAnim(
                images[i][3], images[i][4], images[i][5]);
            grubAnim[i] = createGrubAnim(
                images[i][6], images[i][7]);
        }
        
        // create bullet animation
        Animation[] bulletAnim = new Animation[4];
        bulletAnim[1] = createBulletAnim(images[0][8],images[0][9],images[0][10],images[0][11]);
        bulletAnim[0] = createBulletAnim(images[1][8],images[1][9],images[1][10],images[1][11]);
        bulletAnim[3] = createBulletDeadAnim(images[0][12],images[0][13]);
        bulletAnim[2] = createBulletDeadAnim(images[1][12],images[1][13]);
        
        Animation starbufAnim = createBufAnim(images[0],14,16,80);
        
        Animation expoAnim = createBufAnim(images[0],17,31,40);
        
        Animation stopAnim = createBufAnim(images[0],32,32,500);
        
        // create creature sprites
        playerSprite = new Player(playerAnim[0], playerAnim[1],
            playerAnim[2], playerAnim[3]);
        flySprite = new Fly(flyAnim[0], flyAnim[1],
            flyAnim[2], flyAnim[3]);
        grubSprite = new Grub(grubAnim[0], grubAnim[1],
            grubAnim[2], grubAnim[3]);
        bulletSprite = new Bullet(bulletAnim[0],bulletAnim[1],
        	bulletAnim[2],bulletAnim[3]);
        starbufSprite = new Starbuf.StarB(starbufAnim,240);
        expoSprite = new Starbuf.Expo(expoAnim, 600);
        stopSprite = new Starbuf.Pois(stopAnim, 999999);
    }


    private Animation createPlayerAnim(Image player1,
        Image player2, Image player3)
    {
        Animation anim = new Animation();
        anim.addFrame(player1, 250);
        anim.addFrame(player2, 150);
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 200);
        anim.addFrame(player2, 150);
        return anim;
    }


    private Animation createFlyAnim(Image img1, Image img2,
        Image img3)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 50);
        anim.addFrame(img2, 50);
        anim.addFrame(img3, 50);
        anim.addFrame(img2, 50);
        return anim;
    }


    private Animation createGrubAnim(Image img1, Image img2) {
        Animation anim = new Animation();
        anim.addFrame(img1, 250);
        anim.addFrame(img2, 250);
        return anim;
    }

    private Animation createBulletAnim(Image img1, Image img2, Image img3, Image img4){
    	Animation anim = new Animation();
    	anim.addFrame(img1, 75);
    	anim.addFrame(img2, 75);
    	anim.addFrame(img3, 75);
    	anim.addFrame(img4, 75);
    	return anim;
    }
    
    private Animation createBulletDeadAnim(Image img1, Image img2){
    	Animation anim = new Animation();
    	anim.addFrame(img1, 35);
    	anim.addFrame(img2, 35);
    	return anim;
    }
    

    private Animation createBufAnim(Image buf[], int start, int end,int frame){
    	Animation anim = new Animation();
    	int i = 0;
    	for (i = start;i<end+1;i++){
    		anim.addFrame(buf[i], frame);
    	}
    	return anim;
    }
    
    private void loadPowerUpSprites() {
        // create "goal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage("heart1.png"), 150);
        anim.addFrame(loadImage("heart2.png"), 150);
        anim.addFrame(loadImage("heart3.png"), 150);
        anim.addFrame(loadImage("heart2.png"), 150);
        goalSprite = new PowerUp.Goal(anim);

        // create "star" sprite
        anim = new Animation();
        anim.addFrame(loadImage("star1.png"), 100);
        anim.addFrame(loadImage("star2.png"), 100);
        anim.addFrame(loadImage("star3.png"), 100);
        anim.addFrame(loadImage("star4.png"), 100);
        coinSprite = new PowerUp.Star(anim);

        // create "music" sprite
        anim = new Animation();
        anim.addFrame(loadImage("music1.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        anim.addFrame(loadImage("music3.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        musicSprite = new PowerUp.Music(anim);
        
        // create "mushroom" sprite
        anim = new Animation();
        anim.addFrame(loadImage("mush1.png"), 500);
        anim.addFrame(loadImage("mush2.png"), 500);
        mushSprite = new PowerUp.Mushroom(anim);
    }

}
