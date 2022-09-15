import acm.graphics.GImage;
import acm.program.GraphicsProgram;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.MouseInfo.getPointerInfo;

/*
BLACKJACK - A Card Game
Completed 9-13-2022 by Noah Lake

All Images and Icons were designed with Adobe Illustrator.
All GitHub Files can be viewed with: github.com/noahlake07/blackjack/
No JButtons, or any images from the internet were used for the UI. Every button, title, and icon is custom designed and original.
 */

public class Blackjack extends GraphicsProgram {

    // data about the game
    private int wager = 0;
    private int balance = 10000;
    private int bank = 10000;
    private boolean onStartPage = false;
    private boolean onWagerPage = false;
    private boolean playingGame = false;
    private boolean gameFinished = false;

    // buttons for controls
    GImage playButton, dealBtn, stayButton, hitButton,newGameBtn;

    // icons for UI
    GImage wagerIcon, balanceIcon, bankIcon;


    // objects we are playing with
    private Deck deck;
    private GHand dealerHand;
    private GHand playerHand;

    // components used to input and display prompts
    GImage dealer, player, title, wagerBox, inGameTitle;
    JTextField wagerField;
    JLabel invalidEntry = new JLabel("Invalid entry");
    NumberLabel playerBalance,inGameWager,inGamePlayerBalance,inGameBankBalance;

    // colors for different page backgrounds
    Color playBackground = new Color(0, 115, 19);
    Color defaultBackground = new Color(0, 150, 40);
    Color gameBackground = new Color(77, 136, 100);

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
                if(!onStartPage && !onWagerPage){
                    title.setBounds(getWidth() / 2 - title.getWidth() / 2,getHeight() / 2 - title.getHeight(),title.getWidth(),title.getHeight());
                }

                // set bounds of the wager page components

                // set bounds of the game page components
            }
        }
    };
    Runnable startTitleInsFlash = new Runnable() {
                public void run() {
                    // run while start page is true
                    while (!onStartPage) {
                        // toggle on and off, while pausing
                        playButton.setVisible(false);
                        pause(700);
                        playButton.setVisible(true);
                        pause(700);
                    }
                    playButton.setVisible(false);
                }
            };
    Runnable timedNGButtonReveal = new Runnable() {
        @Override
        public void run() {
            pause(5000);
            newGameBtn.setVisible(true);
        }
    };

    @Override
    public void init() {
        // initialize all game components
        initComponents();

        // setup all game components
        setupComponents();

        // start the flashing of the title screen
        startClickFlash();

        // start checking for wager input validity
        wagerValidityCheck.run();

        // TODO: start the thread that moves and resizes objects when the window is resized
    }

    private void initComponents(){
        // change GraphicsProgram Window settings
        this.getMenuBar().setVisible(false);
        this.setBackground(playBackground);

        // create a new deck
        deck = new Deck();

        // instantiate hands for player and dealer
        playerHand = new GHand(new Hand(deck, false));
        dealerHand = new GHand(new Hand(deck, true));

        // assign pathname to buttons
        playButton = new GImage("buttons/playBtn.png");
        hitButton = new GImage("buttons/hitBtn2.png");
        stayButton = new GImage("buttons/stayBtn2.png");
        newGameBtn = new GImage("buttons/playAgainBtn.png");
        dealBtn = new GImage("buttons/continueBtn.png");

        // creating titles
        title = new GImage("pngmessages/title.png");
        title.scale(.8);
        add(title, getWidth() / 2 - title.getWidth() / 2, getHeight() / 2 - title.getHeight());
        inGameTitle = new GImage("pngmessages/title-ingame.png");
        inGameTitle.scale(.8);
        add(inGameTitle,0,0);
        inGameTitle.setVisible(false);

        // creating wager input area
        wagerBox = new GImage("pngmessages/wagerBox.png");
        wagerBox.scale(.121);

        // creating wager icon
        wagerIcon = new GImage("pngmessages/wagerIcon.png");
        wagerIcon.scale(.16);

    }

    private void setupComponents(){
        // assign temp images to the dealer and player card total icons
        dealer = new GImage("labelovals/LC1.png");
        player = new GImage("labelovals/LC1.png");

        // add the card total icons to the screen
        dealer.scale(0.17);
        player.scale(0.17);
        add(dealer, getWidth() - (getWidth() / 4), getHeight()/2 - getHeight()/5);
        add(player, getWidth() - (getWidth() / 4), getHeight()/2 + getHeight() / 7);

        // hide the icons
        dealer.setVisible(false);
        player.setVisible(false);

        // scale all buttons before adding them to screen
        playButton.scale(0.24);
        hitButton.scale(.25);
        stayButton.scale(.25);
        newGameBtn.scale(.3);
        dealBtn.scale(0.185);

        // add buttons
        add(playButton,getWidth() / 2 - playButton.getWidth() / 2, getHeight() / 2 + playButton.getHeight() / 2);
        add(hitButton,getWidth()/2 - hitButton.getWidth(),getHeight() - hitButton.getHeight() - getWidth()/50);
        add(stayButton,getWidth()/2 + stayButton.getWidth()/2,getHeight() - stayButton.getHeight() - getWidth()/50);
        add(newGameBtn,getWidth()*.885 - newGameBtn.getWidth()/2,getHeight() - newGameBtn.getHeight() - getWidth()/50);
        add(dealBtn,getWidth() - dealBtn.getWidth() - getWidth()/25, getHeight() - dealBtn.getHeight() - getHeight()/18);

        // set button visibility
        newGameBtn.setVisible(false);
        stayButton.setVisible(false);
        hitButton.setVisible(false);
        dealBtn.setVisible(false);

        // adding wager input area
        add(wagerBox, getWidth() / 2 - wagerBox.getWidth() / 2, getHeight() / 2 - wagerBox.getHeight() / 2);
        wagerBox.setVisible(false);

        // adding wager icon
        add(wagerIcon,getWidth() * .05,getHeight() - getHeight()/10);
        wagerIcon.setVisible(false);

        // creating player balance icon
        balanceIcon = new GImage("pngmessages/balanceIcon.png");
        balanceIcon.scale(.12);
        add(balanceIcon,getWidth() * .565,getHeight()/16);
        balanceIcon.setVisible(false);

        // creating bank balance icon
        bankIcon = new GImage("pngmessages/bankIcon.png");
        bankIcon.scale(.11);
        add(bankIcon,getWidth() * .7556,getHeight()/16);
        bankIcon.setVisible(false);


        // adding mouse listeners to the hit and stay buttons
        hitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                hitButton.setImage("buttons/hitBtn.png");
                hitButton.scale(.25);
            }

            public void mouseExited(MouseEvent evt) {
                hitButton.setImage("buttons/hitBtn2.png");
                hitButton.scale(.25);
            }

            public void mousePressed(MouseEvent e){
                if(playingGame) {
                    hit();
                }
            }
        });
        stayButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                stayButton.setImage("buttons/stayBtn.png");
                stayButton.scale(.25);
            }

            public void mouseExited(MouseEvent evt) {
                stayButton.setImage("buttons/stayBtn2.png");
                stayButton.scale(.25);
            }

            public void mousePressed(MouseEvent e){
                if(playingGame) {
                    stay();
                }
            }
        });
        newGameBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                newGameBtn.setImage("buttons/playAgainBtn2.png");
                newGameBtn.scale(.3);
            }

            public void mouseExited(MouseEvent evt) {
                newGameBtn.setImage("buttons/playAgainBtn.png");
                newGameBtn.scale(.3);
            }

            public void mousePressed(MouseEvent e){
                if (gameFinished == true) {
                    nextGame();
                }
            }
        });
        dealBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                dealBtn.setImage("buttons/continueBtn2.png");
                dealBtn.scale(0.185);
            }

            public void mouseExited(MouseEvent evt) {
                dealBtn.setImage("buttons/continueBtn.png");
                dealBtn.scale(0.185);
            }

            public void mousePressed(MouseEvent e){
                if ((onWagerPage) && (getPointerInfo().getLocation().getX() > dealBtn.getX()) && (getPointerInfo().getLocation().getY() > dealBtn.getY())){
                    if(dealBtn.isVisible()) {
                        wagerMade();
                    }
                }
            }
        });

        // adding action and mouse listeners
        addActionListeners();
        addMouseListeners();

    }

    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Stay" -> stay();
            case "Quit" -> System.exit(0);
            case "hitBtn.png" -> hit();
            default -> System.out.println("I do not recognize that command. Check your button text.");
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (!onStartPage) {
            onStartPage = true;
            title.setVisible(false);
            playButton.setVisible(false);
            setupWagerPage();
        }
    }

    public void keyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (onWagerPage) {
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

        // adding the player balance label
        playerBalance = new NumberLabel(balance,getWidth()/2,getHeight()/2);
        add(playerBalance,getWidth()/2 - playerBalance.getWidth()/2,wagerBox.getY() + (wagerBox.getHeight()/6 * 4.56));

        // add event listeners
        addActionListeners();
        addKeyListeners();
        addMouseListeners();
    }

    private void wagerMade(){
        // set the wager value
        wager = Integer.parseInt(wagerField.getText());

        // hide the components of the wager page
        onWagerPage = false;
        wagerBox.setVisible(false);
        wagerField.setVisible(false);
        wagerField.setText("");
        dealBtn.setVisible(false);
        playerBalance.setVisible(false);
        invalidEntry.setVisible(false);

        // begin the game
        play();
    }

    private void play () {
        // changing page data
        playingGame = true;

        // add the in-game bank balance
        inGameBankBalance = new NumberLabel(bank,0,0);
        inGameBankBalance.scale(.7);
        add(inGameBankBalance,(int) bankIcon.getX() + bankIcon.getWidth() + getWidth()/45, (int) bankIcon.getY() + inGameBankBalance.getHeight()/10);

        // add the in-game player balance
        inGamePlayerBalance = new NumberLabel(balance,0,0);
        inGamePlayerBalance.scale(.7);
        add(inGamePlayerBalance,(int) balanceIcon.getX() + balanceIcon.getWidth() + getWidth()/45, (int) balanceIcon.getY() + balanceIcon.getHeight()/10);

        // add the in-game wager
        inGameWager = new NumberLabel(wager, 0,0);
        add(inGameWager, (int) wagerIcon.getX() + wagerIcon.getWidth() + getWidth()/25, (int) wagerIcon.getY() + wagerIcon.getWidth()/7);
        inGameWager.scale(.7);

        // set the background to the game background
        this.setBackground(gameBackground);

        // deal the cards
        deck.deal();

        // add the player hand
        add(playerHand, getWidth() - getWidth() * .6, getHeight() / 2 + playerHand.getHeight() / 2);

        // add the dealer hand
        add(dealerHand, getWidth() - getWidth() * .6, getHeight() / 2 - dealerHand.getHeight());

        // update the card total icons
        player.setImage(getFilename(playerHand));
        dealer.setImage(getFilename(dealerHand));
        player.scale(0.17);
        dealer.scale(0.17);

        // set button visibility
        hitButton.setVisible(true);
        stayButton.setVisible(true);
        inGameTitle.setVisible(true);
        wagerIcon.setVisible(true);
        balanceIcon.setVisible(true);
        newGameBtn.setVisible(false);

        // set icon visibility
        bankIcon.setVisible(true);
        player.setVisible(true);
        dealer.setVisible(true);

        // check for a blackjack
        if (playerHand.getTotal() == 21){
            win();
        }
    }

    private void startClickFlash () {
        startTitleInsFlash.run();
    }

    private void hit () {
        // tell the playerHand to hit
        playerHand.hit();
        playerHand.setLocation(playerHand.getX() - 100,playerHand.getY());

        // update the player's card total icon
        player.setImage(getFilename(playerHand));
        player.scale(0.17);

        // check for a bust
        if (playerHand.getTotal() > 21) {
            bust();
        }

        // check for a blackjack
        if (playerHand.getTotal() == 21){
            win();
        }
    }

    private void stay () {
        // reveal dealer's cards
        dealerHand.revealAllCards();

        // if the dealer's total is less than 17 and not greater than the player's total, the dealer hits.
        if (dealerHand.getTotal() < 17) {
            dealerHand.hit();
            System.out.println("dealer hit");
            dealerHand.setLocation(dealerHand.getX()-100,dealerHand.getY());
        }

        // update the totals of the dealer's hand total
        dealer.setImage(getFilename(dealerHand));
        dealer.scale(0.17);

        // calculate the distance each hand is from 21
        int playerDiff = 21 - playerHand.getTotal();
        int dealerDiff = 21 - dealerHand.getTotal();

        // check for dealer bust
        if(dealerHand.getTotal()>21){
            win();
        } else {
            // check for the winner
            if (playerDiff - dealerDiff < 0) {
                win();
            } else if (dealerDiff < playerDiff) {
                lose();
            } else if (dealerDiff == playerDiff){
                push();
            }
        }
        playingGame = false;
        gameFinished = true;
    }

    private void bust () {
        System.out.println("bust");
        hitButton.setVisible(false);
        stayButton.setVisible(false);
        playingGame = false;

        // add the bust ribbon on top of player cards
        GImage bustRib = new GImage("ribbons/bustRibbon.png");
        bustRib.scale(.5);
        add(bustRib,playerHand.getX() + playerHand.getWidth()/2 - bustRib.getWidth()/2,playerHand.getY() + playerHand.getHeight()/2 + bustRib.getHeight()/3);

        // add the dealer wins ribbon on top of dealer cards
        GImage dealerWins = new GImage("ribbons/dealerWinRibbon.png");
        dealerWins.scale(.5);
        add(dealerWins,bustRib.getX(),dealerHand.getY() + dealerHand.getHeight()/2 + dealerWins.getHeight()/3);

        // distribute money
        balance = balance - wager;
        bank = bank + wager;

        gameFinished();
        gameFinished = true;
        playingGame = false;
    }

    private void lose(){
        System.out.println("lose");
        hitButton.setVisible(false);
        stayButton.setVisible(false);

        // add the bust ribbon on top of player cards
        GImage bustRib = new GImage("ribbons/playerLoseRibbon.png");
        bustRib.scale(.5);
        add(bustRib,playerHand.getX() + playerHand.getWidth()/2 - bustRib.getWidth()/2,playerHand.getY() + playerHand.getHeight()/2 + bustRib.getHeight()/3);

        // add the dealer wins ribbon on top of dealer cards
        GImage dealerWins = new GImage("ribbons/dealerWinRibbon.png");
        dealerWins.scale(.5);
        add(dealerWins,bustRib.getX(),dealerHand.getY() + dealerHand.getHeight()/2 + dealerWins.getHeight()/3);

        // distribute money
        balance = balance - wager;
        bank = bank + wager;

        gameFinished();

        gameFinished = true;
        playingGame = false;
    }

    private void push(){
        // TODO add ribbons that say both the dealer and player "pushed"
        gameFinished = true;
        playingGame = false;
        gameFinished();
    }

    private void gameFinished(){
        // update labels
        inGamePlayerBalance.setValue(balance);
        inGameBankBalance.setValue(bank);
        playingGame = false;
        player.setImage("labelovals/LC"+ playerHand.getTotal() +".png");
        dealer.setImage("labelovals/LC"+ dealerHand.getTotal() +".png");

        // resize labels after change
        inGamePlayerBalance.scale(.7);
        inGameBankBalance.scale(.7);
        player.scale(0.17);
        dealer.scale(0.17);

        // hide game control buttons
        stayButton.setVisible(false);
        hitButton.setVisible(false);
        wagerIcon.setVisible(false);
        inGameWager.setVisible(false);

        // show the new game button
        newGameBtn.setVisible(true);
    }

    private void win(){
        System.out.println("win");
        hitButton.setVisible(false);
        stayButton.setVisible(false);

        // add the bust ribbon on top of player cards
        GImage winRib = new GImage("ribbons/playerWinRibbon.png");
        winRib.scale(.5);
        add(winRib,playerHand.getX() + playerHand.getWidth()/2 - winRib.getWidth()/2,playerHand.getY() + playerHand.getHeight()/2 + winRib.getHeight()/3);

        // add the dealer wins ribbon on top of dealer cards
        GImage dealerLoses = new GImage("ribbons/dealerLoseRibbon.png");
        dealerLoses.scale(.5);
        add(dealerLoses,winRib.getX(),dealerHand.getY() + dealerHand.getHeight()/2 + dealerLoses.getHeight()/3);

        // distribute money
        balance = balance + wager;
        bank = bank - wager;

        gameFinished();
    }

    private void nextGame(){

        // REMOVE ALL OBJECTS FROM SCREEN
        refreshGameData();

        // SETUP NEW COMPONENTS
        setupComponents();

        // REDIRECT TO THE WAGER PAGE
        setupWagerPage();
    }

    private void refreshGameData(){
        // remove and hide used components
        title.setVisible(false);
        playButton.setVisible(false);
        newGameBtn.setVisible(false);
        inGameBankBalance.setVisible(false);
        inGamePlayerBalance.setVisible(false);
        inGameTitle.setVisible(false);
        inGameWager.setVisible(false);

        remove(dealerHand);
        remove(playerHand);

        // clear wager field
        wagerField.setText("");

        // create a fresh deck
        deck = new Deck();

        // create new player and dealer hands
        playerHand = new GHand(new Hand(deck, false));
        dealerHand = new GHand(new Hand(deck, true));

        // setup data for UI
        gameFinished = false;
        onWagerPage = true;
    }

    private String getFilename(GHand h){

        if (h.getTotal() <= 30) {
            return "labelovals/LC" + h.getRevealedTotal() + ".png";
        } else {
            return "labelovals/LC30.png";
        }
    }

    public static void main (String[]args){
        new Blackjack().start();
    }

}