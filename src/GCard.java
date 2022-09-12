import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GRect;

import java.awt.*;

public class GCard extends GCompound{

    private Card card;
    private GRect back;

    public GCard(Card card) {
        this.card = card;

        // make a String for the location of the card picture
        String imageFileName = "cardgifs/" + card.getsuit().toString().substring(0,1).toLowerCase() +
                                (card.getFACE().ordinal() + 2) + ".gif";

        // instantiate a GImage using that String
        GImage image = new GImage(imageFileName);
        // add the GImage to the compound
        add(image, 1,1);
        // make a GRect for the border of the card
        GRect cardBorder = new GRect(109,152);
        // add the border to the compound
        add(cardBorder);
        // make a GRect for the back of the card
        GRect cardBack = new GRect(107,150);
        // add the back to the compound
        add(cardBack);
        cardBack.setFilled(true);
        cardBack.setFillColor(new Color(0, 135, 220));
        // make the visibility of the card back depend on faceUp
        cardBack.setVisible(!card.isFaceUp());
        // scale down the card (too big)
        this.scale(.75);
    }

    public void setFaceUp(boolean faceUp) {
        card.setFaceUp(faceUp);
    }

    public boolean getFaceUp() {
        return card.isFaceUp();
    }

    public void flip() { card.flip(); }

    public int getValue(){
        return card.getValue();
    }
}
