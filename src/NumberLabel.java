import acm.graphics.GCompound;

public class NumberLabel extends GCompound {

    int labelX, labelY;
    GNumberLabel[] numArr;
    public NumberLabel(int number, int x, int y){
        // get details about the number
        String sNumLength = String.valueOf(number);
        int numLength = sNumLength.length();
        x = labelX;
        y = labelY;

        // create pre populated array
        numArr = new GNumberLabel[sNumLength.length()];

        // creating individual digits
        for (int i = 0; i < numLength; i++) {
            numArr[i] = new GNumberLabel(number,i,x + (i*10),y);
            add(numArr[i],numArr[i].getNumX(), numArr[i].getNumY());
        }
    }

    public void setValue(int value){
        String stringValue = String.valueOf(value);
        int valueLength = stringValue.length();

        // update individual digits
        for (int i = 0; i < valueLength; i++) {
            this.numArr[i].setValue(Integer.parseInt(String.valueOf( stringValue.charAt(i) )));
        }
    }

    public GNumberLabel[] getNumArr() {
        return numArr;
    }
}
