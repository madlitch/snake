import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.Timer;

////////////////////////////////////////////// MASSIMO ALBANESE | ICS4U | 24/04/2015

public class frame extends JFrame implements ActionListener{
	

	private static gameArea gamepad = new gameArea();
	static Timer timer;                                     // Timer
	static short delay = 200;                               // Delai
	enum directions{up, down, left, right}           // Enum
	private static directions currentlyHeaded = directions.right;   // Formation d'un nouveau enum
	static JLabel goLabel;                                    // Declaration d'un nouveau jlabel static universel
	
	public static void main(String[] args) {                // Main 
					frame frame = new frame();
					frame.setVisible(true);
					frame.getContentPane().add(gamepad, BorderLayout.CENTER);}
	
	private frame() {                                        // Nouveau instance de frame
		timer = new Timer(delay, this);
		addKeyListener(new keylistener());                  // Ajoute du keylistener au classe 
		setFocusable(true);                                 // Set le focus pour la classe, pour que le keylistener 
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 505, 590);

		JPanel contentPane;contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton newGameButton = new JButton("New Game"); // Bouton pour commencer un nouveau jeu
		newGameButton.setBackground(new Color(34, 139, 34));
		newGameButton.setBounds(10, 3, 225, 23);
		contentPane.add(newGameButton);
		newGameButton.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  newGame();                               // Commence le jeu
  		    	  gamepad.drawApple();}});                 // Dessine la premiere pomme
		
		JButton quitGame = new JButton("Quit");         // Bouton pour finir le jeu
		quitGame.setBackground(new Color(255, 0, 0));
		quitGame.setBounds(264, 3, 225, 23);
		contentPane.add(quitGame);
		
		goLabel = new JLabel("");                            // JLabel pour le "game over"
		goLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		goLabel.setForeground(Color.RED);
		goLabel.setBounds(165, 161, 280, 223);
		contentPane.add(goLabel);
		quitGame.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  System.exit(0);}});
	}
	
	public class keylistener extends KeyAdapter {          // Le keylistener, qui est liee avec un actionlistener pour chaque clef
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();                      // Change la direction du enum 
            if ((key == KeyEvent.VK_LEFT) ) {
            	currentlyHeaded = directions.left;}
            if ((key == KeyEvent.VK_RIGHT) ) {
            	currentlyHeaded = directions.right;}
            if ((key == KeyEvent.VK_UP) ) {
            	currentlyHeaded = directions.up;}
            if ((key == KeyEvent.VK_DOWN) ) {
            	currentlyHeaded = directions.down;}}}

	private void newGame(){ // Commence le timer
		gamepad.presentX = 0;                              // Reset tout les variables , incluant le delai
	    gamepad.presentY = 0;
	    gamepad.points = 0;
	    goLabel.setText("");
	    frame.timer.setDelay(200);	
	    currentlyHeaded = directions.right;
	    for(int i = gamepad.circleList.size() - 1; i >= 5; i-- ){ // Supprime tout les cercles, sauf pour les premieres 5
    		gamepad.circleList.remove(i);}
		for(int i = 1; i <= gamepad.circleList.size() - 1; i++ ){ // Deplace les coordonnes des cerlces par un
    		gamepad.circleList.get(i).setX(0);
    		gamepad.circleList.get(i).setY(0);}
		timer.start();}

	@Override                                     // Méthode pour le mouvement du snake, fait possible par le enum
	public void actionPerformed(ActionEvent e) {  // Le actionlistener pour le timer, qui fonctionne main en main avec le keylistener 
		requestFocus();	
      switch (currentlyHeaded){
      case up:{
    	  gamepad.presentY =  gamepad.presentY - 20;
    	  break;}
      case down:{
    	  gamepad.presentY =  gamepad.presentY + 20;
    	  break;}
      case left:{
    	  gamepad.presentX =  gamepad.presentX - 20;
    	  break;}
      case right:{
    	  gamepad.presentX =  gamepad.presentX + 20;
    	  break;}}
      gamepad.painter();}}

class gameArea extends JComponent { // Le peinteur
	  ArrayList<circles> circleList = new ArrayList<>();
	  private Image drawingImage; // La plane de l'image
	  private Graphics2D graphic; // Le component graphique qui dessine
	  private int randX, randY;
	  int points, presentX = 0, presentY = 0; // Tout les variables
	  
	  gameArea() {
		  circleAdder(5);
		  setBounds(0, 30, 500, 500); // Grandeur du image (place de peinture)
	    setDoubleBuffered(false);
	  }
	  
	  public void paintComponent(Graphics drawer) { 
	    if (drawingImage == null) {
	      drawingImage = createImage(getSize().width, getSize().height);
	      graphic = (Graphics2D) drawingImage.getGraphics();
	      graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Ajoute l'anti-aliasing
	      RenderingHints.VALUE_ANTIALIAS_ON);
	      clear();
	    }
	    drawer.drawImage(drawingImage, 0, 0, null);
	  }
	  
	  private void circleAdder(int o){                               // Methode pour ajouter les cercles au serpent
		for(int i = 0; i < o; i++ ){
		  circles newCircle = new circles(-20, -20);
		  circleList.add(newCircle);
		}
	  }
	  
	  private void clear() {                                         // Repeint tout la zone de jeu blanc pour repeintre
	  		graphic.setPaint(Color.white);
	    	graphic.fillRect(0, 0, getSize().width, getSize().height);
	    	graphic.setPaint(Color.black);
	    	repaint();}
	    	void drawApple() {                                     // Methode pour dessiner une pomme aleatoirement, qui utilise des bonds de 20 pour que ca puisse rester dans le
        	points = points + 7;                                        // meme quadrant que le serpent
	    	randX = ((int)(Math.random() * 24)) * 20;
			randY = ((int)(Math.random() * 24)) * 20;
			for(int i = circleList.size() - 1; i >=  1; i-- ){
				if (randX == circleList.get(i).getX() && randY == circleList.get(i).getY()){
	    		drawApple();
				}
			}
	  }
	  
	  void painter(){                                        // Methode de peinture
		    clear();
		    if(presentX == randX && presentY == randY){             // Si les coordonnes du snake sont les memes que celles de la pomme, il ajoute au points et ajoute 3 cercles
		    	drawApple();
		    	frame.timer.setDelay(frame.delay - points);	
		    	circleAdder(3);
		    }
		    if(presentX < 0 || presentX >= 500 || presentY < 0 || presentY >= 500){ // Si le snake depasse les limites 
		    	    frame.timer.stop();
		    		frame.goLabel.setText("game over");
		    }
		    for(int i = circleList.size() - 1; i >=  1; i-- ){
		    	if (presentX == circleList.get(i).getX() && presentY == circleList.get(i).getY()){ // Si le snake touche elle meme
		    		frame.timer.stop();
		    		frame.goLabel.setText("game over");
		    	}
		    }
		    circleList.get(0).setX(presentX);                                               // Set les coordonnes presentes au premier cercle
		    circleList.get(0).setY(presentY);
		    graphic.fillOval(randX, randY, 20, 20);
		    graphic.setPaint(Color.red);                                                    // Peint le premier cercle rouge
		    graphic.fillOval(circleList.get(0).getX(), circleList.get(0).getY(), 20, 20);
		    graphic.setPaint(Color.black);
	        for(int i = 1; i <= circleList.size() - 1; i++ ){                                   // Peint tout les cercles dans la liste
	       		graphic.fillOval(circleList.get(i).getX(), circleList.get(i).getY(), 20, 20);
	        }
	       	for(int i = circleList.size() - 1; i >=  1; i-- ){                                  // Bouge les coordonnes de chaque cercle a un plus tard dans la list
	    	    circleList.get(i).setX(circleList.get(i - 1).getX());
	   		    circleList.get(i).setY(circleList.get(i - 1).getY());
	       	}
	        repaint();
	  }
}

