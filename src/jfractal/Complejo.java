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
    
    
    public void sumar(Complejo a) {
        
        real += a.real;
        imaginario += a.imaginario;
    }
    
    public void multiplicar(Complejo a) {
        
        Complejo resultado = new Complejo(real * a.real - imaginario * a.imaginario, real * a.imaginario + imaginario * a.real);
        real = resultado.real;
        imaginario = resultado.imaginario;
    }
}
