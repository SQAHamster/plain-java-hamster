package de.unistuttgart.iste.rss.oo.examples.figures;

public class FigureTest {

    Figure fig; // = ...
    ClosedFigure closedFig; // = ...
    Rectangle rect; // = ...
    Square square; // = ...
    double result = 0.0d;

    void runTest() {
        result = rect.perimeter();
        result = square.perimeter();
        fig = rect;
        fig.draw();
        
        // Not permitted
        rect = fig;
        fig.perimeter();
    }
}
