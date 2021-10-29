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
import java.util.List;
import java.util.Map;

public abstract class CardListView<T extends HideableViewModel> extends FlowPane {

    private final SimpleListProperty<T> items;
    private final Map<T, ToggleButton> cardLookUp = new IdentityHashMap<>();
    private PopOver currentPopOver;
    private final ToggleGroup toggleGroup;

    public CardListView() {
        this.items = new SimpleListProperty<>(this, "items");
        this.toggleGroup = new ToggleGroup();
        this.items.addListener(this::onItemsChange);
        this.setVgap(5);
        this.setHgap(5);
        this.minWidthProperty().set(0);
    }

    /**
     * Adds a toggleable card for each of the view models in the given list to this list view
     * by creating a card for every entry using {@link CardListView#createCard} for every one.
     * <p>
     * WARNING: If a view model already has a card but is included in the list (or is present twice or more) multiple cards will be created for it.
     *
     * @param addedViewModels A non-null list of view models to add to the list view
     */
    private void addNewCardsFor(final List<? extends T> addedViewModels) {
        if (addedViewModels == null) {
            throw new IllegalArgumentException("List of added view models was null");
        }
        for (final T addedElement : addedViewModels) {
            final ToggleButton newCard = this.createCard(addedElement);
            this.cardLookUp.put(addedElement, newCard);
            this.getChildren().add(newCard);
            newCard.setToggleGroup(this.toggleGroup);
        }
    }

    /**
     * Removes the cards for all view models included in the list given from the list view
     *
     * @param removedViewModels Non-null List of view models.
     *                          If a view model is included in the list but isn't on the list view, it will be ignored
     */
    private void removeOldCards(final List<? extends T> removedViewModels) {
        if (removedViewModels == null) {
            throw new IllegalArgumentException("List of removed view models was null");
        }
        for (final T removedElement : removedViewModels) {
            final ToggleButton removedCard = this.cardLookUp.remove(removedElement);
            this.getChildren().remove(removedCard);
            removedCard.setToggleGroup(null);
        }
    }

    /**
     * Listener for a list of view models.
     * On change refreshes the cards in this card list view by adding/removing the appropriate ones
     *
     * @param change All changes to be reflected with the cards in the card list view. Can't be null
     */
    private void onItemsChange(final ListChangeListener.Change<? extends T> change) {
        if (change == null) {
            throw new IllegalArgumentException("Changes can't be null");
        }
        while (change.next()) {
            this.addNewCardsFor(change.getAddedSubList());
            this.removeOldCards(change.getRemoved());
        }
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
        content.requestFocus();
    }

    protected abstract ObservableStringValue getCardText(final T item);

    protected abstract Region createPopOverContent(final T item);

    public void onClose() {
        if (this.currentPopOver != null) {
            this.currentPopOver.hide(Duration.ZERO);
        }
    }

}
