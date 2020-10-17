/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Максим Усков
 */
public class Main {
    public static void main(String[] args) {
      Parser p =new Parser();
      String s = "-3+4*2/(1-5)^2"; // = -2.5
      System.out.print(p.calculations(s));
    }
}



