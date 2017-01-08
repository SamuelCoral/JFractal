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

import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
 *
 * @author SamuelCoral
 */
public class PantallaFractales extends JFrame {
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PantallaFractales().setVisible(true);
            }
        });
    }
    
    long pixelesXUnidad = 200, iteraciones = 50;
    double x1 = -2, y1 = -1;
    boolean dibujarCoordenadas = true;
    String nombre = "Mandelbrot";
    Thread hiloDibujar;
    Color[] paleta;
    
    public static Color[] crearPaletaStandar(int numColores) {
        
        Color[] paleta = new Color[numColores];
        
        int c, i;
        for(c = i = numColores * 0 / 6; c < numColores * 1 / 6; c++) paleta[c] = new Color(255, (c - i) * 6 * 256 / numColores, 0);
        for(c = i = numColores * 1 / 6; c < numColores * 2 / 6; c++) paleta[c] = new Color(255 - (c - i) * 6 * 256 / numColores, 255, 0);
        for(c = i = numColores * 2 / 6; c < numColores * 3 / 6; c++) paleta[c] = new Color(0, 255, (c - i) * 6 * 256 / numColores);
        for(c = i = numColores * 3 / 6; c < numColores * 4 / 6; c++) paleta[c] = new Color(0, 255 - (c - i) * 6 * 256 / numColores, 255);
        for(c = i = numColores * 4 / 6; c < numColores * 5 / 6; c++) paleta[c] = new Color((c - i) * 6 * 256 / numColores, 0, 255);
        for(c = i = numColores * 5 / 6; c < numColores * 6 / 6; c++) paleta[c] = new Color(255, 0, 255 - (c - i) * 6 * 256 / numColores);
        
        return paleta;
    }
    
    public PantallaFractales() {
        
        this.paleta = crearPaletaStandar(128);
        Inicializar();
    }
    
    @Override
    public void paint(Graphics g) {
        
        setTitle(nombre + " - " + String.valueOf(iteraciones) + " iteraciones");
        if(hiloDibujar != null) if(hiloDibujar.isAlive()) {
            try {
                hiloDibujar.interrupt();
                hiloDibujar.join();
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        hiloDibujar = new DibujarMandelbrot((Graphics2D)getGraphics(), getSize(), x1, y1, pixelesXUnidad, iteraciones, paleta, dibujarCoordenadas);
        hiloDibujar.start();
    }
    
    private void Inicializar() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 480);
        
        Dimension areaVentana = getSize();
        Dimension areaPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((areaPantalla.width - areaVentana.width) / 2, (areaPantalla.height - areaVentana.height) / 2);
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                
                if(e.getKeyCode() == KeyEvent.VK_PLUS) { iteraciones += e.isShiftDown() ? e.isControlDown() ? e.isAltDown() ? 500 : 50 : 10 : 1; repaint(); }
                if(e.getKeyCode() == KeyEvent.VK_MINUS) { iteraciones -= e.isShiftDown() ? e.isControlDown() ? e.isAltDown() ? 500 : 50 : 10 : 1; repaint(); }
                if(e.getKeyCode() == KeyEvent.VK_P) {
                    
                    try {
                        
                        setTitle(nombre + " - Guardando - " + String.valueOf(iteraciones) + " iteraciones");
                        BufferedImage guardar = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                        Thread hiloGuardar = new DibujarMandelbrot(guardar.createGraphics(), getSize(), x1, y1, pixelesXUnidad, iteraciones, paleta, dibujarCoordenadas);
                        hiloGuardar.start();
                        hiloGuardar.join();
                        ImageIO.write(guardar, "png", new File(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date()) + ".png"));
                        setTitle(nombre + " - " + String.valueOf(iteraciones) + " iteraciones");
                    }
                    catch(IOException | InterruptedException ex) {
                        
                        System.err.println(ex.getMessage());
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_H) { dibujarCoordenadas = !dibujarCoordenadas; repaint(); }
                if(e.getKeyCode() == KeyEvent.VK_I) {
                    
                    try {
                        
                        double nuevoX1 = Double.parseDouble(JOptionPane.showInputDialog("Coordenada en X", x1));
                        double nuevoY1 = Double.parseDouble(JOptionPane.showInputDialog("Coordenada en Y", y1));
                        long nuevoPixelesXUnidad = Long.parseLong(JOptionPane.showInputDialog("Pixeles por unidad", pixelesXUnidad));
                        long nuevoIteraciones = Long.parseLong(JOptionPane.showInputDialog("Iteraciones", iteraciones));
                        
                        x1 = nuevoX1;
                        y1 = nuevoY1;
                        pixelesXUnidad = nuevoPixelesXUnidad;
                        iteraciones = nuevoIteraciones;
                        repaint();
                    }
                    catch(NumberFormatException | NullPointerException ex) {
                        
                    }
                }
            }
        });
        
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                
                if(e.getWheelRotation() < 0) {
                    
                    pixelesXUnidad = pixelesXUnidad * 3 / 2;
                    x1 += (((double)e.getX() / getWidth()) / 2) * ((double)getWidth() / pixelesXUnidad);
                    y1 += (((double)e.getY() / getHeight()) / 2) * ((double)getHeight() / pixelesXUnidad);
                }
                else {
                    
                    x1 -= (((double)e.getX() / getWidth()) / 2) * ((double)getWidth() / pixelesXUnidad);
                    y1 -= (((double)e.getY() / getHeight()) / 2) * ((double)getHeight() / pixelesXUnidad);
                    pixelesXUnidad = pixelesXUnidad * 2 / 3;
                }
                
                repaint();
            }
        });
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                
                x1 += (e.getX() - (getWidth() / 2d)) / pixelesXUnidad;
                y1 += (e.getY() - (getHeight() / 2d)) / pixelesXUnidad;
                repaint();
            }
        });
    }
}
