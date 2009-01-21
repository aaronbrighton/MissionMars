/**
 * @(#)Mission_Mars.java
 *
 * Mission_Mars Applet application
 *
 * @author 
 * @version 1.00 2009/1/18
 */
 
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class Mission_Mars extends Applet implements KeyListener, MouseListener {
	
	// Main class for the project, location of the KeyListeners, MouseListeners and Applet //
	
	Image dbImage; // Used by the update() method to make clean movement of objects in the applet.
	Graphics dbg; // --
	Font font; // Used in drawing menus etc...
	
	int appletWidth = 800; // Width of the Applet as defined in the embed code.
	int appletHeight = 600; // Height of the Applet as defined in the embed code.
	int alienx = 0; // x-coordinate of the Flying Saucer, also the starting point of the Saucer.
	int alieny = 100;// y-coordinate of the Flying Saucer, also the starting point of the Saucer.
	int laserx = alienx; // x-coordinate of the Laser Beam.
	int lasery = alieny; // y-coordinate of the Laser Beam.
	int buildWidth = 75; // Width of the buildings
	int[] buildings = new int[55]; // The array that stores information on each, building 5 index elements are required per building.
	
	/*
	 * In-Game Booleans
	 * ----------------
	 * Changed on the fly during game play.
	 */
	
	boolean laser = false; // Set to true when the laser beam has been fired.
	boolean pause = false; // Set to true when the user pauses the game.
	boolean mute = false; // Set to true if the user would rather not listen to the sick music of the program.
	
	/*
	 * Menu Booleans
	 * ----------------
	 * Used for navigating back and forth in the menu.
	 */
	
	boolean inMenu = true; // Set to true when the menu is enabled and game play is disabled.
	boolean menuPage = true; // Set to true when the user is viewing the Main Menu.
	boolean instructionsPage = false; // Set to true when the user is viewing the Instructions page.
	boolean creditsPage = false; // Set to true when the user is viewing the Credits Page.
	
	boolean start = false; // Set to true to initiate game play.
	boolean complete = false; // Set to true when the user has finished playing the game/
	boolean failed = false; // Set to true if the UFO crashes into a building.
	int score = 0;
	
	Image mapBg; // Background image of the applet, may change during game play.
	Image alienShip; // The sprite for the Flying Saucer.
	Image laserBeam; // The sprite for the Laser Beam dropped by the Flying Saucer.
	Image speakerOn; // The ON image for the Mute toggle.
	Image speakerOff; // The OFF image for the Mute toggle.
	
	AudioClip backgroundMusic; // The background music played, during game play.
	AudioClip buildingExplosion; // The sound when a laser hits a building or the UFO crashes into a building.
	AudioClip laserSound; // The sound when the laser is initially fired.
	AudioClip winSound; // The sound played when the user has won.
	AudioClip failSound; // The sound played if the user fails the mission.
	
	public void init() {
		mapBg = getImage(getDocumentBase(), "includes/background-r2.jpg"); // Initialize background object.
		alienShip = getImage(getDocumentBase(), "includes/ufo.gif"); // Initialize UFO object.
		laserBeam = getImage(getDocumentBase(), "includes/laser.png"); // Initialize Laser Beam object.
		speakerOn = getImage(getDocumentBase(), "includes/speaker_on.png"); // Initialize Speaker ON image object.
		speakerOff = getImage(getDocumentBase(), "includes/speaker_off.png"); // Initialize Speaker OFF image object.
		backgroundMusic = getAudioClip(getCodeBase(), "includes/bgmusic.mid"); // Initialize background music audio.
		buildingExplosion = getAudioClip(getCodeBase(), "includes/kabang.mid"); // Initialize explosion sound.
		laserSound = getAudioClip(getCodeBase(), "includes/boing.mid"); // Initizialize laser sound.
		winSound = getAudioClip(getCodeBase(), "includes/win.mid"); // Initialize winning sound.
		failSound = getAudioClip(getCodeBase(), "includes/fail.mid"); // Initialize failing sound.
		addMouseListener(this); // Add Mouse listener for Mute Button.
		addKeyListener(this); // Add key listener to navigate the menu and play the game.
	}

	public void keyPressed(KeyEvent e){
		int key=e.getKeyCode();
		if (inMenu == true)
		{
			// The user is currently in the menu. //
			if(key==e.VK_1){
				
				/*
				 * 1 key has been pressed
				 * ----------------------
				 * Display main menu
				 */
				
				menuPage = true;
				instructionsPage = false;
				creditsPage = false;		
			}
			if(key==e.VK_2){
				
				/*
				 * 2 key has been pressed
				 * ----------------------
				 * Display instructions
				 */
					menuPage = false;
					instructionsPage = true;
					creditsPage = false;
			}
			if(key==e.VK_3){
				/*
				 * 3 key has been pressed
				 * ----------------------
				 * Instructions
				 */
					menuPage = false;
					instructionsPage = false;
					creditsPage = true;
			}
			if(key==e.VK_S){
				/*
				 * S key has been pressed
				 * ----------------------
				 * Start the game.
				 */
					menuPage = false;
					instructionsPage = false;
					creditsPage = false;
					start = true;
					inMenu = false;
					
					// Reset all the variables. //
					alienx = 0;
					alieny = 100;
					laserx = alienx;
					lasery = alieny;
					laser = false;
					complete = false;
					int i;
					for (i=0;i<buildings.length;i=i+5)
					{
						// Loop through the buildings array and set random buildings. //
						buildings[i] = (int)(Math.random()*255+1);
						buildings[i+1] = buildWidth;
						buildings[i+2] = (int)(Math.random()*255+1);
						buildings[i+3] = (int)(Math.random()*255+1);
						buildings[i+4] = (int)(Math.random()*255+1);
					}
					
					if (mute == false)
					{
						// If the mute button has not been pressed, start the background music. //
						backgroundMusic.loop();
					}
			}
		}
		else
		{
			// Game is in progress //
			if (pause == false)
			{
				// Game is not paused //
				if(key==e.VK_SPACE){
					// Space has been pressed fire the laser. //
					if (mute == false)
					{
						if (laser == false)
						{
							// Play lase sound as long as the audio isn't muted.
							laserSound.play();	
						}
					}
					laser=true;
				}
			}
			
			if (pause == true)
			{
				// Pause has been enabled, check for strokes from X and P keys. //
				if (key==e.VK_X)
				{
					// X has been pressed terminate the game, and go back to menu. //
					pause = false;
					inMenu = true;
					menuPage = true;
				}
				
				if (key==e.VK_P)
				{
					// P has been pressed unpause the game. //
					pause = false;
					if (mute == false)
					{
						backgroundMusic.loop();
					}
				}
			}
			else if (key==e.VK_P)
			{
				// P has been pressed, pause the game and stop the music //
				if (pause == false)
				{
					
					pause = true;
					backgroundMusic.stop();
				}
			}
			
		}
	}
	public void keyReleased(KeyEvent e){
	}
	public void keyTyped(KeyEvent e){
	}
	
	public void mousePressed(MouseEvent me)
	{
		// Mouse has been pressed //
		if (me.getX() >= 767 && me.getX() <= 797 && me.getY() >= 3 && me.getY() <= 28 && inMenu == false && pause == false)	
		 {
		 	
		 	// The mouse clicked on the mute button. //
		 	if (mute == true)
		 	{
		 		// Un-mute the audio, start playing it. //
		 		mute = false;
		 		backgroundMusic.loop();
		 	}
		 	else
		 	{
		 		// Mute the audio, stop playing it. //
		 		mute = true;
		 		backgroundMusic.stop();
		 	}
		 }
	}
	public void mouseExited(MouseEvent me)
	{
	}
	public void mouseClicked(MouseEvent me)
	{
	}
	public void mouseReleased(MouseEvent me)
	{
	}
	public void mouseEntered(MouseEvent me)
	{
	}
	
	public void paint(Graphics g) {
		
		g.drawImage(mapBg,0,0,this); // Draw the background.
		if (mute == true)
		{
			// Mute is active.
			g.drawImage(speakerOff,767,3,this); // Draw the mute button.
		}
		else
		{
			// Mute has not been activated.
			g.drawImage(speakerOn,767,3,this); // Draw the mute button.
		}
		
		if (inMenu == true)
		{
			
			// Currently drawing the menu //
			
			if (menuPage == true)
			{
				// Draw the Title onto the applet.
				font = new Font("Arial", Font.BOLD, 28);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Mission Mars", 300+2, 100+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Mission Mars", 300, 100);
				
				font = new Font("Arial", Font.BOLD, 14);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Press S to Play", 300+2, 150+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Press S to Play", 300, 150);
				
				font = new Font("Arial", Font.BOLD, 14);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Press 2 for Instructions", 300+2, 200+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Press 2 for Instructions", 300, 200);
				
				font = new Font("Arial", Font.BOLD, 14);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Press 3 for Credits", 300+2, 250+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Press 3 for Credits", 300, 250);
				
				
	
				
				//g.drawString("Mission Mars", 300, 150);
			}
			else if (instructionsPage == true)
			{
				// Instructions page currently, draw title.
				font = new Font("Arial", Font.BOLD, 28);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Instructions", 230+2, 100+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Instructions", 230, 100);
				
				font = new Font("Arial", Font.BOLD, 16);
				
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Mission:", 15+1, 150+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Mission:", 15, 150);
				
				font = new Font("Arial", Font.BOLD, 14);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Using your U.F.O. you must clear a path destroying", 85+1, 160+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Using your U.F.O. you must clear a path destroying", 85, 160);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("all buildings on mars with bombs.", 85+1, 175+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("all buildings on mars with bombs.", 85, 175);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Failure is not an option. Destroy the buildings to prevent crashing.", 85+1, 190+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Failure is not an option. Destroy the buildings to prevent crashing.", 85, 190);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Controls:", 220+1, 240+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Controls:", 220, 240);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Bomb:", 250+1, 260+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Bomb:", 250, 260);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Space", 310+1, 260+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Space", 310, 260);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Pause:", 250+1, 280+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Pause:", 250, 280);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("P", 310+1, 280+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("P", 310, 280);
				
				font = new Font("Arial", Font.BOLD, 28);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Have fun!", 220+2, 450+2);
				g.setColor(new Color(0, 138	, 255));
				g.drawString("Have fun!", 220, 450);	
			}
			else if (creditsPage == true)
			{
				// Credits page has been displayed, draw the credits. //
				font = new Font("Arial", Font.BOLD, 28);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Credits", 250+2, 100+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Credits", 250, 100);
				
				font = new Font("Arial", Font.BOLD, 16);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Code written by:", 15+1, 140+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Code written by:", 15, 140);
				
				font = new Font("Arial", Font.BOLD, 14);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Mark Covell", 40+1, 160+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Mark Covell", 40, 160);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Aaron Brighton", 40+1, 180+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Aaron Brighton", 40, 180);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("Josh Simpson", 40+1, 200+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Josh Simpson", 40, 200);
				
				g.setColor(new Color(50, 50, 50));
				g.drawString("This is our own version of the Mission Mars. Only stole the idea.", 15+1, 250+1);
				g.setColor(new Color(0, 138, 255));
				g.drawString("This is our own version of the Mission Mars. Only stole the idea", 15, 250);
				
				font = new Font("Arial", Font.BOLD, 16);
				g.setFont(font);
				g.setColor(new Color(50, 50, 50));
				g.drawString("Hope you enjoyed it, and thanks for playing", 150+2, 400+2);
				g.setColor(new Color(0, 138, 255));
				g.drawString("Hope you enjoyed it, and thanks for playing", 150, 400);
			}
		}
		else
		{
			g.drawString("Score: "+score, 20, 20); // Draw the score onto the applet.
			g.drawImage(alienShip,alienx,alieny,this); // Draw the UFO on the the applet.
			int m = 0; // useed later in the program.
			int i; // Used in loop.
			for (i=0;i<buildings.length;i=i+5)
			{
				// Draw the buildings. // 
				g.setColor(new Color(buildings[i+2], buildings[i+3], buildings[i+4])); // Set color of the buildings.
				g.fillRect(m*buildWidth,appletHeight-buildings[i],buildWidth,buildings[i]); // Draw the rectangle onto the applet.
				m++;
			}
		
			if (laser == true)
			{
				g.drawImage(laserBeam,laserx,lasery,this); // Draw the background.
				if (pause == false)
				{
					lasery = lasery + 1; // Move the laser down one more pixel.
					m = 0;
					
					complete = true;
					for (i=0;i<buildings.length;i=i+5)
					{
						if (laserx+15 >= m*buildWidth && laserx <= m*buildWidth+buildWidth && lasery+60 >= appletHeight-buildings[i] && lasery+60 <= appletHeight)
						{
							// Check to see if the laser hit the building.
							buildings[i] = buildings[i] - 100; // Drop the building down 100 pixels.
							if (mute == false)
							{
								buildingExplosion.play(); // Play buidling explosion sound.
							}
							score = score + 100; // Set the score up 100
							laser = false; // Turn laser off.
						}
						
						if (buildings[i] <= 0)
						{
							// detect to see if all the buildings have stopped playing.
							if (complete == true)
							{
								complete = true;
								// Set the complete variable to true.
							}
						}
						else
						{
							complete = false;
						}
						m++;
					}
					
				/*	m = 0;
					for (i=0;i<buildings.length;i=i+5)
					{
						if (alienx+60 >= m*buildWidth && alienx <= m*buildWidth+buildWidth && alieny+25 >= appletHeight-buildings[i] && alieny+25 <= appletHeight)
						{
							
							if (mute == false)
							{
								// 
								buildingExplosion.play();
							}
							failed = true;
						}
						
						m++;
					}
					if (lasery >= appletHeight)
					{
						laser = false;
					}*/
				}
			}
			else
			{
				if (pause == false)
				{
					// set the laser's coordinate to the same as the ufo.
					laserx = alienx;
					lasery = alieny;
				}
			}
			
			if (alienx >= appletWidth)
			{
				// Check to see if the UFO has gone off the applet window.
				if (pause == false)
				{
					// Place the alien space ship to the next line.
					alienx = -60;
					alieny = alieny + 50;
				}
			}
			else
			{
				if (pause == false)
				{
					// Move the space shift over one pixel.
					alienx = alienx + 1;
				}
			}
			
			if (pause == true)
			{
				// Draw the pause menu.
					font = new Font("Arial", Font.BOLD, 28);
					g.setFont(font);
					g.setColor(new Color(50, 50, 50));
					g.drawString("PAUSED", 250+2, 225+2);
					g.setColor(new Color(0, 138, 255));
					g.drawString("PAUSED", 250, 225);
					
					font = new Font("Arial", Font.BOLD, 14);
					g.setFont(font);
					g.setColor(new Color(50, 50, 50));
					g.drawString("Press P to return to game.", 200+2, 275+2);
					g.setColor(new Color(0, 138, 255));
					g.drawString("Press P to return to game.", 200, 275);
					
					font = new Font("Arial", Font.BOLD, 14);
					g.setFont(font);
					g.setColor(new Color(50, 50, 50));
					g.drawString("Press X to exit to main menu.", 200+2, 325+2);
					g.setColor(new Color(0, 138, 255));
					g.drawString("Press X to exit to main menu.", 200, 325);
			}
			
			if (alieny >= appletHeight)
			{
				// If the UFO has hit the ground, go back to menu.
				pause = false;
				inMenu = true;
				menuPage = true;
				backgroundMusic.stop();
			}
			
			/*if (failed == true)
			{
				pause = false;
				inMenu = true;
				menuPage = true;
				backgroundMusic.stop();
				if (mute == false)
				{
					failSound.play();
				}	
			}*/
			
			if (complete == true)
			{
				// The game is complete, go back to the main menu.
				pause = false;
				inMenu = true;
				menuPage = true;
				backgroundMusic.stop();
				if (mute == false)
				{
					winSound.play();
				}
			}
		}
		
		repaint();		
	}
	
public void update(Graphics g)
	{
	// This method is automatically called by repaint()
	// It captures the canvas to an image object
	// offscreen, changes it, then puts it back
	// on screen
		if (dbImage==null){
			dbImage=createImage(this.getSize().width,
								this.getSize().height);
			dbg=dbImage.getGraphics();
		}
		//dbg.setColor(getBackground());
		//dbg.fillRect(0,0,this.getSize().width,  this.getSize().height);
		dbg.setColor(getForeground());  //restore the colour to its previous state
		paint(dbg);
		g.drawImage(dbImage,0,0,this);
	}
}