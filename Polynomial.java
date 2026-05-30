import java.io.*;
import java.util.*;

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

    public Polynomial(File file) throws FileNotFoundException {
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
                coefficients[i] = Double.parseDouble(tmp.substring(0, tmp.length()));
            } else {
                exponents[i] = Integer.parseInt(tmp.substring(idx + 1, tmp.length()));
                coefficients[i] = Double.parseDouble(tmp.substring(0, idx));
            }
        }
        sc.close();
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
        int maxL = p.getCoefficients().length + coefficients.length;
        int tempExp[] = new int[maxL];
        for (int i = 0; i < maxL; i++) tempExp[i] = -1;
        // find exponents
        int idx = 0;
        for (int i = 0; i < exponents.length; i++) {
            int tmp = linearSearch(tempExp, exponents[i]);
            if (tmp < 0) {
                tempExp[idx] = exponents[i];
                idx++;
            }
        }
        for (int i = 0; i < p.getExponents().length; i++) {
            int tmp = linearSearch(tempExp, p.getExponents()[i]);
            if (tmp < 0) {
                tempExp[idx] = p.getExponents()[i];
                idx++;
            }
        }
        
        Arrays.sort(tempExp, 0, idx);
        int newExp[] = new int[idx];

        for (int i = 0; i < idx; i++) newExp[i] = tempExp[i];
        
        // find coefficients
        double newCoeffs[] = new double[idx];
        for (int i = 0; i < coefficients.length; i++) {
            int tmp = linearSearch(newExp, exponents[i]);
            newCoeffs[tmp] += coefficients[i];
        }
        for (int i = 0; i < p.getCoefficients().length; i++) {
            int tmp = linearSearch(newExp, p.getExponents()[i]);
            newCoeffs[tmp] += p.getCoefficients()[i];
        }
        Polynomial newP = new Polynomial(newCoeffs, newExp);
        return newP;
    }
    
    public double evaluate(double x) {
    	double val = 0;
    	for (int i = 0; i < coefficients.length; i++) val += coefficients[i] * Math.pow(x, i);
    	return val;
    }
    
    public boolean hasRoot(double x) {
    	double res = evaluate(x);
    	return res == 0;
    }

    private int linearSearch(int[] arr, int key) {
        for (int k = 0; k < arr.length; k++) if (arr[k] == key) return k;
        return -1;
    }

    public Polynomial multiply(Polynomial p) {
        int maxL = p.getCoefficients().length * coefficients.length;
        int tempExp[] = new int[maxL];

        // find exponents
        int idx = 0;
        for (int i = 0; i < exponents.length; i++) {
            for (int j = 0; j < p.getExponents().length; j++) {
                int tmp = linearSearch(tempExp, exponents[i] + p.getExponents()[j]);

                if (tmp >= 0 && exponents[i] + p.getExponents()[j] == 0) {
                    idx++;
                }
                else if (tmp < 0) {
                    tempExp[idx] = exponents[i] + p.getExponents()[j];
                    idx++;
                }
            }
        }

        Arrays.sort(tempExp, 0, idx);
        int newExp[] = new int[idx];
        // copy exponents to correct array length
        for (int i = 0; i < idx; i++) newExp[i] = tempExp[i];

        // find coefficients
        double newCoeffs[] = new double[idx];
        for (int i = 0; i < coefficients.length; i++) {
            for (int j = 0; j < p.getCoefficients().length; j++) {
                double coeff = coefficients[i] * p.getCoefficients()[j];
                int tmp = linearSearch(tempExp, exponents[i] + p.getExponents()[j]);
                // System.out.println("tmp: " + tmp);
                newCoeffs[tmp] += coeff;
            }
        }
        Polynomial newP = new Polynomial(newCoeffs, newExp);
        return newP;
    }

    public void saveToFile(String fileName) throws IOException {
        String s = toString();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        out.write(s);
        out.close();
    }

    public String toString() {
        String s = "";
        int len = coefficients.length;
        for (int i = 0; i < len; i++) {
            if (exponents[i] == 0) {
                if ((int)coefficients[i] == coefficients[i]) s += "" + (int)coefficients[i] + "";
                else s += "" + (int)coefficients[i] + "";
            } else {
                if (coefficients[i] > 0) {
                    if ((int)coefficients[i] == coefficients[i]) s += "+" + (int)coefficients[i] + "x" + exponents[i];
                    else s += "+" + coefficients[i] + "x" + exponents[i];
                }
                else {
                    if ((int)coefficients[i] == coefficients[i]) s += "" + (int)coefficients[i] + "x" + exponents[i];
                    else s += "" + coefficients[i] + "x" + exponents[i];
                }
            }
        }
        return s;
    }
}
