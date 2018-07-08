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

import java.awt.*;

/**
 *
 * @author SamuelCoral
 */
public class DibujarMandelbrot extends Thread {
    
    public static Color colorHSL(double angulo, double saturacion, double iluminacion) {
        
        angulo *= 3 / Math.PI;
        double C = (1 - Math.abs(2 * iluminacion - 1)) * saturacion;
        double X = C * (1 - Math.abs(angulo % 2 - 1));
        double m = iluminacion - C / 2;
        
        double r_prima =
            angulo < 1 || angulo >= 5 ? C :
            angulo < 2 || angulo >= 4 ? X : 0;
        double g_prima =
            angulo >= 1 && angulo < 3 ? C :
            angulo >= 4 ? 0 : X;
        double b_prima =
            angulo >= 3 && angulo < 5 ? C :
            angulo < 2 ? 0 : X;
        
        return new Color(
            (int)((r_prima + m) * 0xFF),
            (int)((g_prima + m) * 0xFF),
            (int)((b_prima + m) * 0xFF)
        );
    }
    
    public static Color colorComplejo(Complejo z, double separacion, double brillo) {
        
        double rad = (Math.log1p(z.radio()) % separacion) / separacion;
        double b_p = rad < 0.5 ? rad * 2 : 2 - rad * 2;
        return colorHSL(z.angulo(0) + (z.angulo(0) < 0 ? 2 * Math.PI : 0), 1.0, brillo * (b_p - 0.5d) + 0.5d);
    }
    
    public static Color colorMandelbrot(Complejo valor, long iteraciones, Color[] paleta) {
        
        Complejo inicial = valor;
        
        long c;
        for(c = 0; c < iteraciones; c++) {
            
            if(Thread.currentThread().isInterrupted()) break;
            valor = valor.por(valor).mas(inicial);
            
            if(valor.radio() > 2) return paleta[(int)(c % paleta.length)];
        }
        
        return Color.BLACK;
    }
    
    public static Color funcionCompleja(Complejo z, double[] ramas) {
        
        Complejo chido = Complejo.dividir(
            Complejo.exponencial(z, new Complejo(2, 0), 0).mas(Complejo.MENOS_1).por(
                Complejo.exponencial(z.menos(new Complejo(2, 1)), new Complejo(2, 0), 0)
            ),
            Complejo.exponencial(z, new Complejo(2, 0), 0).mas(new Complejo(2, 2))
        );
        
        /*
        Complejo phi = new Complejo((1 + Math.sqrt(5)) / 2);
        Complejo chido = Complejo.dividir(Complejo.restar(
            Complejo.exponencial(phi, z, 0),
            Complejo.exponencial(new Complejo(-phi.real), Complejo.multiplicar(z, Complejo.MENOS_1), 0)
        ), new Complejo(Math.sqrt(5)));
        */
        
        // arctan(z)
        /*
        Complejo chido = new Complejo(1 - z.imaginario, z.real)
            .entre(new Complejo(1 + z.imaginario, -z.real))
            .log(ramas[0])
            .por(new Complejo(0, -0.5));
        */
        
        return colorComplejo(chido, 1d, 0.75);
        //return colorHSL(chido.angulo() + (chido.angulo() < 0 ? 2 * Math.PI : 0), 1, Math.pow(2, -chido.radio()));
    }
    
    private final Graphics2D g;
    private final Dimension areaPintar;
    private final double x1, y1, ramas[];
    private final long pixelesXUnidad, iteraciones;
    private final boolean dibujarCoordenadas;
    private final Color[] paleta;
    
    public DibujarMandelbrot(Graphics2D g,
        Dimension areaPintar,
        double x1, double y1,
        long pixelesXUnidad,
        long iteraciones,
        Color[] paleta,
        boolean dibujarCoordenadas,
        double[] ramas) {
        
        this.g = g;
        this.areaPintar = areaPintar;
        this.x1 = x1;
        this.y1 = y1;
        this.pixelesXUnidad = pixelesXUnidad;
        this.iteraciones = iteraciones;
        this.paleta = paleta;
        this.dibujarCoordenadas = dibujarCoordenadas;
        this.ramas = ramas;
    }
    
    @Override
    public void run() {
        
        int x, y;
        for(y = 0; y < areaPintar.getHeight(); y++) for(x = 0; x < areaPintar.getWidth(); x++) {
            
            if(Thread.currentThread().isInterrupted()) break;
            //g.setColor(colorMandelbrot(new Complejo(x1 + (double)x / pixelesXUnidad, y1 - (double)y / pixelesXUnidad), iteraciones, paleta));
            
            g.setColor(
                funcionCompleja(
                    new Complejo(
                        x1 + (double)x / pixelesXUnidad,
                        y1 - (double)y / pixelesXUnidad
                    ),
                    ramas
                )
            );
            
            g.drawLine(x, y, x, y);
        }
        
        if(dibujarCoordenadas) {
            
            g.setColor(new Color(0xC07F7F7F, true));
            g.fillRect(10, 35, 215, 65);
            Font fuente = new Font(g.getFont().getFontName(), Font.BOLD, 16);
            g.setFont(fuente);
            g.setColor(Color.WHITE);
            g.drawString("X = " + String.valueOf(x1), 15, 50);
            g.drawString("Y = " + String.valueOf(y1), 15, 65);
            g.drawString("PPU = " + String.valueOf(pixelesXUnidad), 15, 80);
            g.drawString(String.valueOf(iteraciones) + " iteraciones", 15, 95);
        }
    }
}
