package me.ixwavey.utilities;

import static me.ixwavey.utilities.Formatting.Formatter.formatRoundDouble;

public class Main {
    public static void main(String[] args) {
        double X = 2.65, Y = 6.72, Z;
        System.out.println("Initial values, X: %s Y: %s".formatted(X,Y));
        X = formatRoundDouble(X);
        Y = formatRoundDouble(Y);
        System.out.println("Modified values, X: %s Y: %s".formatted(X,Y));
        Z = X + Y;
        System.out.println("X + Y = Z, " + X + " + " + Y + " = " + Z);
    }
}
