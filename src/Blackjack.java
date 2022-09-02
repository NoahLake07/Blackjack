import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import svu.csc213.Dialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Blackjack extends GraphicsProgram {

    // data about the game
    private int wager = 0;
    private int balance = 10000;
    private int bank = 10000;

    // labels to display info to the player
    private GLabel bankLabel, wagerLabel, balanceLabel;

    // buttons for controls
    JButton wagerButton, playButton, hitButton, stayButton, quitButton;

    // objects we are playing with
    private Deck deck;
    private GHand dealer;

    @Override
    public void init(){
        this.getMenuBar().setVisible(false);

        Color background = new Color(0, 150, 0);
        this.setBackground(background);

        deck = new Deck();

        // set up our buttons
        wagerButton = new JButton("Wager");
        playButton = new JButton("Play");
        hitButton = new JButton("Hit");
        stayButton = new JButton("Stay");
        quitButton = new JButton("Quit");

        // add buttons to the screen
        add(hitButton, SOUTH);
        add(stayButton, SOUTH);
        add(quitButton, SOUTH);
        add(playButton, SOUTH);

        addActionListeners();
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        switch(ae.getActionCommand()){

            case "Play":
                wager();
                play();
                break;

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

    private void wager() {
        wager = (int) Dialog.getDouble("Make a wager:");
        play();
    }

    private void play(){

    }

    private void hit(){

    }

    private void stay(){

    }

    @Override
    public void run(){
        GHand hand = new GHand(new Hand(deck, true));
        add(hand, 100, 100);
        hand.hit();
    }

    public static void main(String[] args) {
        new Blackjack().start();
    }

}
