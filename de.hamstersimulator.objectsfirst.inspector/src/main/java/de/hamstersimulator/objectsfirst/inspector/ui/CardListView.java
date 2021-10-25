package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.HideableViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class CardListView<T extends HideableViewModel> extends FlowPane {

    private final SimpleListProperty<T> items;
    private final Map<T, ToggleButton> cardLookUp = new IdentityHashMap<>();
    private PopOver currentPopOver;

    public CardListView() {
        this.items = new SimpleListProperty<>(this, "items");
        final ToggleGroup toggleGroup = new ToggleGroup();
        this.items.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                for (final T addedElement : change.getAddedSubList()) {
                    final ToggleButton newCard = this.createCard(addedElement);
                    this.cardLookUp.put(addedElement, newCard);
                    this.getChildren().add(newCard);
                    newCard.setToggleGroup(toggleGroup);
                }
                for (final T removedElement : change.getRemoved()) {
                    final ToggleButton removedCard = this.cardLookUp.remove(removedElement);
                    this.getChildren().remove(removedCard);
                    removedCard.setToggleGroup(null);
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

    private ToggleButton createCard(final T item) {
        final ToggleButton card = new ToggleButton();
        card.setPrefWidth(120);
        card.setPrefHeight(50);
        card.maxWidthProperty().bind(card.prefWidthProperty());
        card.maxHeightProperty().bind(card.prefHeightProperty());
        card.textProperty().bind(this.getCardText(item));
        card.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                item.isVisibleProperty().set(true);
                this.addPopOver(card, item);
            } else {
                item.isVisibleProperty().set(false);
                if (this.currentPopOver != null) {
                    this.currentPopOver.hide();
                }
            }
        });
        return card;
    }

    private void addPopOver(final ToggleButton owner, final T item) {
        final Region content = this.createPopOverContent(item);
        content.minWidthProperty().bind(content.maxWidthProperty());
        final ScrollPane contentScrollPane = new ScrollPane(content);
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setMaxWidth(360);
        contentScrollPane.setMaxHeight(Screen.getPrimary().getBounds().getHeight() / 2);
        final PopOver popOver = new PopOver(contentScrollPane);
        popOver.setDetachable(false);
        popOver.setConsumeAutoHidingEvents(false);
        popOver.show(owner);
        popOver.setOnHidden(event -> owner.setSelected(false));
        this.currentPopOver = popOver;
    }

    protected abstract ObservableStringValue getCardText(final T item);

    protected abstract Region createPopOverContent(final T item);

    public void onClose() {
        if (this.currentPopOver != null) {
            this.currentPopOver.hide(Duration.ZERO);
        }
    }

}
