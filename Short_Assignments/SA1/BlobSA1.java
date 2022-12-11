public class BlobSA1 extends Blob{
    private double step, currentStep;  //variables for step and counter for number of steps

    //constructor for this instance
    public BlobSA1(double x, double y){
        super(x, y);
        this.step = 12 + 18 *Math.random();     //step between 12(min) and 30(max)
        this.currentStep = 0;
    }

    @Override
    public void step(){
        if (currentStep < step){
            currentStep += 1;
        }
        else{
            dx = 5 * (Math.random()-0.5);       // changing velocity randomly between 2.5 to -2.5
            dy = 5 * (Math.random()-0.5);
            currentStep = 0;                    //reset currentstep if step is out of range
        }
        x += dx;                                //shift in position of blob
        y += dy;
    }
}
