/*
 * Copyright (C) 2016 SamuelCoral
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jfractal;

/**
 *
 * @author SamuelCoral
 */
public class Complejo {
    
    public double real;
    public double imaginario;
    
    public static final Complejo MAS_I = new Complejo(0, 1);
    public static final Complejo MENOS_I = new Complejo(0, -1);
    public static final Complejo MAS_1 = new Complejo(1);
    public static final Complejo MENOS_1 = new Complejo(-1);
    public static final Complejo CERO = new Complejo();
    
    public Complejo() {
        real = imaginario = 0;
    }
    
    public Complejo(double real) {
        this.real = real;
        imaginario = 0;
    }
    
    public Complejo(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }
    
    public Complejo(Complejo origen) {
        real = origen.real;
        imaginario = origen.imaginario;
    }
    
    public static double encerrar(double val, double inicio, double rango) {
        return val + rango * Math.ceil((inicio - val) / rango);
    }
    
    public static Complejo desdeCoordenadasPolares(double radio, double angulo) {
        return new Complejo(radio * Math.cos(angulo), radio * Math.sin(angulo));
    }
    
    
    public double radio() {
        return Math.hypot(real , imaginario);
    }
    
    public double angulo(double rama) {
        return encerrar(Math.atan2(imaginario, real), Math.PI * (2 * rama - 1), 2 * Math.PI);
    }
    
    
    public Complejo mas(final Complejo comp2) {
        
        return new Complejo(
            real + comp2.real,
            imaginario + comp2.imaginario
        );
    }
    
    public Complejo menos(final Complejo comp2) {
        
        return new Complejo(
            real - comp2.real,
            imaginario - comp2.imaginario
        );
    }
    
    public Complejo por(final Complejo comp2) {
        
        return new Complejo(
            real * comp2.real - imaginario * comp2.imaginario,
            real * comp2.imaginario + comp2.real * imaginario
        );
    }
    
    public Complejo entre(final Complejo comp2) {
        
        double divisor = comp2.real * comp2.real + comp2.imaginario * comp2.imaginario;
        return new Complejo(
            (real * comp2.real + imaginario * comp2.imaginario) / divisor,
            (imaginario * comp2.real - real * comp2.imaginario) / divisor
        );
    }
    
    public static Complejo sumar(final Complejo comp1, final Complejo comp2) { return comp1.mas(comp2); }
    public static Complejo restar(final Complejo comp1, final Complejo comp2) { return comp1.menos(comp2); }
    public static Complejo multiplicar(final Complejo comp1, final Complejo comp2) { return comp1.por(comp2); }
    public static Complejo dividir(final Complejo comp1, final Complejo comp2) { return comp1.entre(comp2); }
    
    public Complejo inverso() {
        return MAS_1.entre(this);
    }
    
    public Complejo log(double rama) {
        return new Complejo(Math.log(radio()), angulo(rama));
    }
    
    public Complejo exp() {
        return Complejo.desdeCoordenadasPolares(Math.exp(real), imaginario);
    }
    
    public static Complejo exponencial(Complejo base, Complejo exponente, double rama) {
        return base.log(rama).por(exponente).exp();
    }
    
    public static Complejo logaritmica(Complejo base, Complejo logaritmo, double rama) {
        return logaritmo.log(rama).entre(base.log(rama));
    }
    
    
    public void sumar(Complejo comp2) {
        
        Complejo res = mas(comp2);
        real = res.real;
        imaginario = res.imaginario;
    }
    
    public void restar(Complejo comp2) {
        
        Complejo res = menos(comp2);
        real = res.real;
        imaginario = res.imaginario;
    }
    
    public void multiplicar(Complejo comp2) {
        
        Complejo res = por(comp2);
        real = res.real;
        imaginario = res.imaginario;
    }
    
    public void dividir(Complejo comp2) {
        
        Complejo res = entre(comp2);
        real = res.real;
        imaginario = res.imaginario;
    }
    
    
    public String aCadena() {
        
        String res = String.valueOf(real);
        if(imaginario != 0) {
            
            res += ' ' + (imaginario > 0 ? '+' : '-') + ' ';
            if(Math.abs(imaginario) != 1) res += String.valueOf(Math.abs(imaginario));
            res += 'i';
        }
        return res;
    }
}
