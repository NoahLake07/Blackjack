import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static java.awt.MouseInfo.getPointerInfo;

public class Blackjack extends GraphicsProgram {

    // data about the game
    private int wager = 0;
    private int balance = 10000;
    private int bank = 10000;
    private boolean onStartPage = false;
    private boolean onWagerPage = false;

    // labels to display info to the player
    private GLabel bankLabel, wagerLabel, balanceLabel;

    // buttons for controls
    JButton hitButton, stayButton, quitButton;
    GImage playButton, dealBtn;

    // objects we are playing with
    private Deck deck;
    private GHand dealerHand;
    private GHand playerHand;

    // components used to input and display prompts
    GImage dealer, player, title, wagerBox;
    JTextField wagerField;
    JLabel invalidEntry = new JLabel("Invalid entry");
    NumberLabel playerBalance;

    // colors for different backgrounds
    Color playBackground = new Color(0, 115, 0);
    Color background = new Color(0, 150, 40);

    // threads for doing concurrent actions
    Runnable wagerValidityCheck = new Runnable() {
                public void run() {
                    add(invalidEntry,wagerField.getX(),wagerField.getY() + wagerBox.getHeight()/10);
                    // loop will run until onWagerPage = false
                    while(onWagerPage) {

                        // check for a wager invalid entry
                        try{
                            if (onWagerPage && (!(wagerField.getText().isEmpty()) && !(Integer.valueOf(wagerField.getText()) < 1 && !((Integer.valueOf(wagerField.getText())) > balance)))) {

                                if(Integer.valueOf(wagerField.getText())<=balance){
                                    dealBtn.setVisible(true);
                                    invalidEntry.setVisible(false);
                                } else {
                                    dealBtn.setVisible(false);
                                    invalidEntry.setVisible(true);
                                }
                            } else {
                                dealBtn.setVisible(false);
                                invalidEntry.setVisible(true);
                            }
                        }catch (Exception e){
                            dealBtn.setVisible(false);
                            dealBtn.setVisible(false);
                            invalidEntry.setVisible(true);
                        }
                    }
                }
            };
    Runnable startResizing = new Runnable() {
        public void run() {
            while(true){
                // set bounds of window height

                // set bounds of the title page components
                if(onStartPage == false && onWagerPage == false){
                    title.setBounds(getWidth() / 2 - title.getWidth() / 2,getHeight() / 2 - title.getHeight(),title.getWidth(),title.getHeight());
                }

                // set bounds of the wager page components

                // set bounds of the game page components
            }
        }
    };

    @Override
    public void init() {
        this.getMenuBar().setVisible(false);
        this.setBackground(playBackground);

        deck = new Deck();

        // set up buttons
        playButton = new GImage("buttons/playBtn.png");
        hitButton = new JButton("Hit");
        stayButton = new JButton("Stay");

        // add buttons to the screen
        add(playButton, getWidth() / 2, getHeight() / 2);
        playButton.scale(0.24);
        playButton.setLocation(getWidth() / 2 - playButton.getWidth() / 2, getHeight() / 2 + playButton.getHeight() / 2);

        // creating title
        title = new GImage("pngmessages/title.png");
        title.scale(.8);
        add(title, getWidth() / 2 - title.getWidth() / 2, getHeight() / 2 - title.getHeight());

        // creating wager input area
        wagerBox = new GImage("pngmessages/wagerBox.png");
        wagerBox.scale(.121);
        add(wagerBox, getWidth() / 2 - wagerBox.getWidth() / 2, getHeight() / 2 - wagerBox.getHeight() / 2);
        wagerBox.setVisible(false);

        addActionListeners();
        addMouseListeners();
        startClickFlash();
        wagerValidityCheck.run();
    }

    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Hit":
                hit();
                break;

            case "Stay":
                stay();
                break;

            case "Quit":
                System.exit(0);
                break;

            default:
                System.out.println("I do not recognize that command. Check your button text.");
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (onStartPage == false) {
            onStartPage = true;
            title.setVisible(false);
            playButton.setVisible(false);
            setupWagerPage();
        }
        if ((onWagerPage) && (getPointerInfo().getLocation().getX() > dealBtn.getX()) && (getPointerInfo().getLocation().getY() > dealBtn.getY())){

            if(dealBtn.isVisible()) {
                System.out.println("deal button clicked");
                wagerMade();
            }
        }
    }

    public void keyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (onWagerPage == true) {
                wager = Integer.parseInt(wagerField.getText());
                System.out.println("wager made");
                wagerMade();
            }
        }
    }

    private void setupWagerPage() {
        // change data values
        onWagerPage = true;

        // change window appearance
        this.setBackground(new Color(27, 82, 115));

        // add the wager input box and field
        wagerBox.setVisible(true);
        wagerField = new JTextField("",15);
        add(wagerField, getWidth()/2-getWidth()/10, (getHeight()/6)*2.575);
        wagerField.setText("");

        // adding the deal button
        dealBtn = new GImage("buttons/continueBtn.png");
        dealBtn.scale(0.185);
        add(dealBtn,getWidth() - dealBtn.getWidth() - getWidth()/25, getHeight() - dealBtn.getHeight() - getHeight()/18);
        dealBtn.setVisible(false);
        System.out.println(dealBtn.getImage());

        // adding the player balance label
        playerBalance = new NumberLabel(balance,getWidth()/2,getHeight()/2);
        add(playerBalance,getWidth()/2 - playerBalance.getWidth()/2,wagerBox.getY() + (wagerBox.getHeight()/6 * 4.56));

        // add event listeners
        addActionListeners();
        addKeyListeners();
        addMouseListeners();
    }

    private void wagerMade(){
        // hide the components of the wager page
        onWagerPage = false;
        wagerBox.setVisible(false);
        wagerField.setVisible(false);
        wagerField.setText("");
        dealBtn.setVisible(false);
        playerBalance.setVisible(false);
        invalidEntry.setVisible(false);


        // set up the game page
        setBackground(background);

        // begin the game
        play();
    }

    private void play () {
            deck.deal();

            playerHand = new GHand(new Hand(deck, false));
            add(playerHand, getWidth() / 20, getHeight() / 2 + playerHand.getHeight() / 2);

            dealerHand = new GHand(new Hand(deck, true));
            add(dealerHand, getWidth() / 20, getHeight() / 2 - dealerHand.getHeight() - getHeight() / 30);

            dealer = new GImage("labelovals/LC" + dealerHand.getTotal() + ".png");
            player = new GImage("labelovals/LC" + playerHand.getTotal() + ".png");

            dealer.scale(0.17);
            player.scale(0.17);
            add(dealer, getWidth() - getWidth() / 3, getHeight() - (getHeight() / 3) * 2);
            add(player, getWidth() - getWidth() / 3, getHeight() - getHeight() / 3);

            dealerHand.flipCard(0);
        }

        private void startClickFlash () {
            Runnable myRunnable =
                    new Runnable() {
                        public void run() {
                            // run while start page is true
                            while (onStartPage == false) {
                                // toggle on and off, while pausing
                                playButton.setVisible(false);
                                pause(700);
                                playButton.setVisible(true);
                                pause(700);
                            }
                            playButton.setVisible(false);
                        }
                    };
            myRunnable.run();
        }

        private void hit () {
            playerHand.hit();
            player.setImage(getFilename(playerHand));
            player.scale(0.17);

            // check for a bust
            if (playerHand.getTotal() > 17) {
                bust();
            }
        }

        private void stay () {
            while (!(dealerHand.getTotal() > 17 || dealerHand.getTotal() > playerHand.getTotal())) {
                dealerHand.hit();
            }
        }

        private void bust () {
            System.out.println("bust");
            GImage bust = new GImage("pngmessages/Bust - bj.png");
            add(bust, getWidth() / 2 - bust.getWidth() / 2, getHeight() / 2 - bust.getHeight() / 2);
            bust.setVisible(true);
            for (int i = 0; i < 1000; i++) {
                bust.scale(1.1);
            }
            remove(bust);
        }

        private String getFilename(GHand h){

        if (h.getTotal() <= 30) {
            return "labelovals/LC" + h.getTotal() + ".png";
        } else {
            return "labelovals/LC30.png";
        }
    }

        private int getWindowSize(){
        return (int) Math.sqrt( (getWidth() * getWidth()) + (getHeight() * getHeight()));
    }

        public static void main (String[]args){
            new Blackjack().start();
        }

    }