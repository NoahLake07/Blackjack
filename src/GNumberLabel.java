import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GNumberLabel extends GCompound {

    int numX,numY;

    public GNumberLabel(int number,int numPlacement, int x, int y){
        String mySNumber = String.valueOf(number);
        int myNumber = Integer.parseInt(String.valueOf(mySNumber.charAt(numPlacement)));
        numX = x;
        numY = y;

        GImage label = new GImage("gnumbers/pkg-gnum-" + myNumber + ".png");
        label.scale(.1);
        add(label,x,y);
    }

    public int getNumX(){
        return numX;
    }

    public int getNumY(){
        return numY;
    }

}
