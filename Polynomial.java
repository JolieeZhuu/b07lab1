public class Polynomial {
	
	private double[] coefficients;
	
    public Polynomial() {
    	coefficients = new double[1];
    }
    
    public Polynomial(double[] coefficients) {
    	this.coefficients = coefficients;
    }
    
    public double[] getCoefficients() {
    	return coefficients;
    }
    
    public void setCoefficients(double[] coefficients) {
    	this.coefficients = coefficients;
    }
    
    public Polynomial add(Polynomial p) {
    	double pLen = (double)p.getCoefficients().length;
    	double cLen = (double)coefficients.length;
    	double[] newCoeffs = new double[(int)Math.max(cLen, pLen)];
    	double[] biggerArr, smallerArr;
    	if (cLen > pLen) {
    		biggerArr = coefficients;
    		smallerArr = p.getCoefficients();
    	} else {
    		biggerArr = p.getCoefficients();
    		smallerArr = coefficients;
    		
    	}
    	for (int i = 0; i < newCoeffs.length; i++) {
    		if (i >= smallerArr.length) {
    			newCoeffs[i] = biggerArr[i];
    		} else {
        		newCoeffs[i] = smallerArr[i] + biggerArr[i];
    		}
    	}
    	
    	Polynomial newP = new Polynomial(newCoeffs);
    	return newP;
    }
    
    public double evaluate(double x) {
    	double val = 0;
    	for (int i = 0; i < coefficients.length; i++) {
    		val += coefficients[i] * Math.pow(x, i);
    	}
    	return val;
    }
    
    public boolean hasRoot(double x) {
    	double res = evaluate(x);
    	return res == 0;
    }
}
