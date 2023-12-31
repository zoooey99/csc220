package prog03;

public class ConstantFib extends PowerFib{
    public double O(int n) {
        return Math.log(n);
    }

    protected double pow(double x, int n) {
        return Math.pow(x,n);
    }
}
