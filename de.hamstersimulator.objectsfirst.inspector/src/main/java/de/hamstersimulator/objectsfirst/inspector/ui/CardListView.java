package de.hamstersimulator.objectsfirst.inspector.ui;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.PopOver;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class CardListView<T> extends FlowPane {

    private final SimpleListProperty<T> items;

    private final Map<T, Node> cardLookUp = new IdentityHashMap<>();

    public CardListView() {
        this.items = new SimpleListProperty<>(this, "items");
        this.items.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                for (final T addedElement: change.getAddedSubList()) {
                    final Node newCard = this.createCard(addedElement);
                    this.cardLookUp.put(addedElement, newCard);
                    this.getChildren().add(newCard);
                }
                for (final T removedElement: change.getRemoved()) {
                    this.getChildren().remove(this.cardLookUp.remove(removedElement));
                }
            }
        });
        this.setVgap(5);
        this.setHgap(5);
        this.minWidthProperty().set(0);
    }

    public ListProperty<T> itemsProperty() {
        return this.items;
    }

    private Node createCard(final T item) {
        final StackPane stackPane = new StackPane();
        final Rectangle rectangle = new Rectangle();
        rectangle.setWidth(120);
        rectangle.setHeight(50);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setFill(Color.LIGHTGRAY);
        stackPane.getChildren().add(rectangle);
        stackPane.maxWidthProperty().bind(rectangle.widthProperty());
        final Node content = this.createCardContent(item);
        stackPane.getChildren().add(content);
        stackPane.setOnMouseClicked(e -> {
            addPopOver(rectangle, item);
        });
        return stackPane;
    }

    private void addPopOver(final Node owner, final T item) {
        final Region content = this.createPopOverContent(item);
        content.setMaxWidth(360);
        content.minWidthProperty().bind(content.maxWidthProperty());
        final PopOver popOver = new PopOver(content);
        popOver.setDetachable(false);
        popOver.show(owner);
    }

    protected abstract Node createCardContent(final T item);

    protected abstract Region createPopOverContent(final T item);

}
