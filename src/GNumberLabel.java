import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GNumberLabel extends GCompound {

    GImage label;

    int numX,numY;

    public GNumberLabel(int number,int numPlacement, int x, int y){
        String mySNumber = String.valueOf(number);
        int myNumber = Integer.parseInt(String.valueOf(mySNumber.charAt(numPlacement)));
        numX = x;
        numY = y;

        label = new GImage("gnumbers/pkg-gnum-" + myNumber + ".png");
        label.scale(.1);
        add(label,x,y);
    }

    public void setValue(int value){
        label.setImage("gnumbers/pkg-gnum-" + value + ".png");
        label.scale(.1);
        label.setLocation(numX,numY);
    }

    public int getNumX(){
        return numX;
    }

    public int getNumY(){
        return numY;
    }

}