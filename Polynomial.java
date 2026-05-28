import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Polynomial {
	
	private double[] coefficients;
    private int[] exponents;
	
    public Polynomial() {
    	coefficients = new double[1];
        exponents = new int[1];
    }
    
    public Polynomial(double[] coefficients, int[] exponents) {
    	this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public Polynomial(File file) {
        Scanner sc = new Scanner(file);
        String line = sc.nextLine();
        
        String[] tmpArr = line.split("(?=[-+])");
        int len = tmpArr.length;
        coefficients = new double[len];
        exponents = new int[len];

        for (int i = 0; i < len; i++) {
            String tmp = tmpArr[i];
            int idx = tmp.indexOf("x");
            if (idx == -1) {
                exponents[i] = 0;
            } else {
                exponents[i] = Integer.parseInt(tmp.substring(idx + 1, tmp.length));
            }
            coefficients[i] = Double.parseDouble(tmp.substring(0, idx));
        }
    }
    
    public double[] getCoefficients() {
    	return coefficients;
    }
    
    public void setCoefficients(double[] coefficients) {
    	this.coefficients = coefficients;
    }

    public int[] getExponents() {
        return exponents;
    }

    public void setExponents(int[] exponents) {
        this.exponents = exponents;
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

    public Polynomial multiply(Polynomial p) {
        int maxL = p.getCoefficients().length * coefficients.length;
        int tempExp[] = new int[maxL];

        // find exponents
        int idx = 0;
        for (int i = 0; i < coefficients.length; i++) {
            for (int j = 0; j < p.getCoefficients().length; j++) {
                int tmp = Array.binarySearch(tempExp, exponents[i] * p.getExponents()[j]);
                if (tmp != -1) {
                    tempExp[idx] = exponents[i] * p.getExponents()[j];
                    tmp++;
                }
            }
        }
        Arrays.sort(tempExp);
        int newExp[] = new int[idx];
        // copy exponents to correct array length
        for (int i = 0; i < idx; i++) {
            newExp[i] = tempExp[i];
        }

        // find coefficients
        int newCoeffs[] = new int[idx];
        for (int i = 0; i < coefficients.length; i++) {
            for (int j = 0; j < p.getCoefficients().length; j++) {
                int coeff = coefficients[i] * p.getCoefficients()[j];
                int tmp = Array.binarySearch(tempExp, exponents[i] * p.getExponents()[j]);
                newCoeffs[tmp] += coeff;
            }
        }
        Polynomial newP = new Polynomial(newCoeffs, newExp);
        return newP;
    }

    public void saveToFile(String fileName) {
        String s = "";
        int len = coefficients.length;
        for (int i = 0; i < len; i++) {
            if (exponents[i] == 0) {
                s += "" + coefficients[i] + "";
            } else {
                if (coefficients[i] > 0) s += "+" + coefficients[i] + "x" + exponents[i];
                else s += "" + coefficients[i] + "x" + exponents[i];
            }
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        out.write(s);
        out.close();
    }
}
