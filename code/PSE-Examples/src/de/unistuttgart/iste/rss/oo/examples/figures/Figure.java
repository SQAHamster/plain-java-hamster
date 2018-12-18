package de.unistuttgart.iste.rss.oo.examples.figures;

import javafx.geometry.Dimension2D;

abstract class Figure {
    public abstract void draw();
}
abstract class ClosedFigure extends Figure {
    public abstract double perimeter();
}
class Rectangle extends ClosedFigure {

    final Dimension2D dimension;
    
    @Override
    public double perimeter() {
        return 2 * (dimension.getHeight() + dimension.getWidth());
    }

    @Override
    public void draw() {
        // Draw a rectangle
    }
}
class Square extends Rectangle {

    double sideLength;
    
    @Override
    public double perimeter() {
        return 4*sideLength;
    }

    @Override
    public void draw() {
        // Draw a square
    }
}