package HW_2.task_ComplexNumbers;

public class ComplexNumber {
    private double real;
    private double imagin;

    public ComplexNumber(double real) {
        this.real = real;
        this.imagin = 0;
    }

    public ComplexNumber(double real, double imagin) {
        this.real = real;
        this.imagin = imagin;
    }

    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(this.real + c.real, this.imagin + c.imagin);
    }

    public ComplexNumber add(double real, double imagin) {
        return new ComplexNumber(this.real + real, this.imagin + imagin);
    }

    public ComplexNumber minus(ComplexNumber c) {
        return new ComplexNumber(this.real - c.real, this.imagin - c.imagin);
    }

    public ComplexNumber minus(double real, double imagin) {
        return new ComplexNumber(this.real - real, this.imagin - imagin);
    }

    public ComplexNumber multiply(ComplexNumber c) {
        double realPart = this.real * c.real - this.imagin * c.imagin;
        double imaginPart = this.real * c.imagin + this.imagin * c.real;
        return new ComplexNumber(realPart, imaginPart);
    }

    public double abs() {
        return Math.sqrt(this.real * this.real + this.imagin * this.imagin);
    }

    @Override
    public String toString() {
        return "ComplexNumber{"
                + real + ((imagin > 0) ? " + " : " - ")
                + Math.abs(imagin)
                + "*i"
                + '}';
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImagin() {
        return imagin;
    }

    public void setImagin(double imagin) {
        this.imagin = imagin;
    }
}
