package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.TypeCategory;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InstanceViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.SearchableComboBox;
import java.util.*;
import java.util.function.Consumer;

public class InputControl extends HBox {

    private final Type type;
    private Type currentType;
    private Control currentInputControl;
    private Consumer<Object> onValueChanged;
    private boolean changeFromInputControl = false;
    private final List<ChangeListener<?>> currentChangeListeners = new ArrayList<>();
    private final List<ListChangeListener<?>> currentListChangeListeners = new ArrayList<>();
    private final ComboBox<Type> multiTypeComboBox;

    private final InspectionViewModel inspectionViewModel;
    private final SimpleObjectProperty<Object> value = new SimpleObjectProperty<>(this, "value");
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(this, "isValid");
    private final SimpleBooleanProperty isNewObjectValue = new SimpleBooleanProperty(this, "isNewObjectValue", false);

    public InputControl(final Type type, final InspectionViewModel hamsterUI) {
        this.type = type;
        this.inspectionViewModel = hamsterUI;
        this.value.addListener((observable, oldValue, newValue) -> {
            this.handleValueChanged(newValue);
        });
        final TypeCategory category = type.getCategory();

        if (category == TypeCategory.OBJECT || category == TypeCategory.OPTIONAL) {
            final ComboBox<Type> multiTypeComboBox = this.createMultiTypeComboBox();
            this.multiTypeComboBox = multiTypeComboBox;
            this.getChildren().add(multiTypeComboBox);
            this.currentType = multiTypeComboBox.getValue();
        } else {
            this.currentType = type;
            this.multiTypeComboBox = null;
        }
        this.createSimpleInputControl();
        this.getChildren().add(this.currentInputControl);
        this.getChildren().add(this.createAddObjectButton());
        this.setSpacing(5);
    }

    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }

    public BooleanProperty isValidProperty() {
        return this.isValid;
    }

    private void handleValueChanged(Object value) {
        if (!this.changeFromInputControl) {
            if (this.type.getCategory() == TypeCategory.OPTIONAL) {
                assert value instanceof Optional;
                value = ((Optional<?>)value).orElse(null);
            }
            if (value != null
                    && (this.type.getCategory() == TypeCategory.OBJECT || this.type.getCategory() == TypeCategory.OPTIONAL)) {
                final Type newType = new Type(value.getClass());
                final TypeCategory newCategory = newType.getCategory();
                final TypeCategory currentCategory = this.currentType.getCategory();
                if (newCategory == TypeCategory.ENUM && !this.currentType.getType().isAssignableFrom(newType.getType())) {
                    boolean foundMatchingEnumType = false;
                    for (final Type type : this.inspectionViewModel.multiInputTypesProperty()) {
                        if (type.getCategory() == TypeCategory.ENUM && type.getType().isAssignableFrom(newType.getType())) {
                            this.multiTypeComboBox.setValue(type);
                            foundMatchingEnumType = true;
                            break;
                        }
                    }
                    if (!foundMatchingEnumType) {
                        this.multiTypeComboBox.setValue(Type.OBJECT_TYPE);
                    }
                }
                if (newCategory != currentCategory && currentCategory != TypeCategory.OBJECT) {
                    this.multiTypeComboBox.setValue(newType);
                }
            }
            this.onValueChanged.accept(value);
        }
    }

    private ValidationResult validateString(final String textValue) {
        assert textValue != null;

        if (this.currentType.getCategory() == TypeCategory.STRING) {
            return ValidationResult.OK;
        }
        if (!this.currentType.isPrimitive() && textValue.isBlank()) {
            if (this.type.getCategory() == TypeCategory.OPTIONAL) {
                return ValidationResult.OK;
            } else {
                return ValidationResult.WARNING;
            }
        }
        if (this.currentType.getCategory() == TypeCategory.CHARACTER) {
            if (textValue.length() == 1) {
                return ValidationResult.OK;
            } else {
                return ValidationResult.ERROR;
            }
        }
        try {
            switch (this.currentType.getCategory()) {
                case BYTE -> Byte.parseByte(textValue);
                case SHORT -> Short.parseShort(textValue);
                case INTEGER -> Integer.parseInt(textValue);
                case LONG -> Long.parseLong(textValue);
                case FLOAT -> Float.parseFloat(textValue);
                case DOUBLE -> Double.parseDouble(textValue);
                default -> throw new IllegalStateException("Illegal input category");
            }
            return ValidationResult.OK;
        } catch (Exception e) {
            return ValidationResult.ERROR;
        }
    }

    private void updateIsValid(final ValidationResult validationResult) {
        final ObservableList<String> styleClass = this.currentInputControl.getStyleClass();
        switch (validationResult) {
            case OK -> {
                styleClass.remove("error");
                styleClass.remove("warning");
                this.isValid.set(true);
            }
            case WARNING -> {
                this.isValid.set(true);
                styleClass.remove("error");
                if (!styleClass.contains("warning")) {
                    styleClass.add("warning");
                }
            }
            case ERROR -> {
                this.isValid.set(false);
                styleClass.remove("warning");
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                }
            }
        }
    }

    private void setFromString(final String textValue) {
        assert this.validateString(textValue) == ValidationResult.OK;

        if (this.currentType.getCategory() == TypeCategory.STRING) {
            this.setValue(textValue);
        } else if (this.currentType.getCategory() == TypeCategory.CHARACTER && textValue.length() == 1) {
            this.setValue(textValue.charAt(0));
        } else if (textValue.isBlank()) {
            this.setValue(null);
        } else {
            switch (this.currentType.getCategory()) {
                case BYTE -> this.setValue(Byte.parseByte(textValue));
                case SHORT -> this.setValue(Short.parseShort(textValue));
                case INTEGER -> this.setValue(Integer.parseInt(textValue));
                case LONG -> this.setValue(Long.parseLong(textValue));
                case FLOAT -> this.setValue(Float.parseFloat(textValue));
                case DOUBLE -> this.setValue(Double.parseDouble(textValue));
                default -> throw new IllegalStateException("Illegal input category");
            }
        }
    }

    private void setValue(final Object value) {
        this.changeFromInputControl = true;
        if (this.type.getCategory() == TypeCategory.OPTIONAL) {
            this.value.set(Optional.ofNullable(value));
        } else {
            this.value.set(value);
        }
        this.changeFromInputControl = false;
    }

    private String sanitizeString(final String textValue) {
        final TypeCategory category = this.currentType.getCategory();
        if (category == TypeCategory.BYTE
                || category == TypeCategory.SHORT
                || category == TypeCategory.INTEGER
                || category == TypeCategory.LONG) {
            return textValue.replaceAll("[^0-9]", "");
        } else if (category == TypeCategory.FLOAT || category == TypeCategory.DOUBLE) {
            final String partialSanitized = textValue.replaceAll("[^.0-9]", "");
            if (partialSanitized.indexOf('.') != partialSanitized.lastIndexOf('.')) {
                final int firstDotIndex = partialSanitized.indexOf('.');
                final String firstPart = partialSanitized.substring(0, firstDotIndex + 1);
                final String lastPart = partialSanitized.substring(firstDotIndex + 1).replaceAll("\\.", "");
                return firstPart + lastPart;
            } else {
                return partialSanitized;
            }
        } else {
            return textValue;
        }
    }

    private Button createAddObjectButton() {
        final Button addButton = new Button();
        addButton.setText("+");
        addButton.managedProperty().bind(this.isNewObjectValue);
        addButton.visibleProperty().bind(this.isNewObjectValue);
        //TODO handler functionality
        addButton.prefWidthProperty().bind(addButton.heightProperty());
        addButton.minWidthProperty().bind(addButton.prefWidthProperty());
        return addButton;
    }

    private ComboBox<Type> createMultiTypeComboBox() {
        final ComboBox<Type> multiTypeComboBox = new SearchableComboBox<>(
                this.inspectionViewModel.multiInputTypesProperty());
        multiTypeComboBox.setValue(multiTypeComboBox.getItems().get(0));
        multiTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.onMultiTypeChanged(newValue);
            }
        });
        multiTypeComboBox.setPrefWidth(85);
        multiTypeComboBox.minWidthProperty().bind(multiTypeComboBox.prefWidthProperty());
        return multiTypeComboBox;
    }

    private void onMultiTypeChanged(final Type newType) {
        if (this.currentType != newType) {
            this.isNewObjectValue.set(false);
            this.currentType = newType;
            this.createSimpleInputControl();
            this.getChildren().set(1, this.currentInputControl);
            if (this.type.getCategory() == TypeCategory.OPTIONAL) {
                this.value.set(Optional.empty());
            } else {
                this.value.set(null);
            }
            this.onValueChanged.accept(null);
        }
    }

    private void createSimpleInputControl() {
        this.currentChangeListeners.clear();
        this.currentListChangeListeners.clear();
        final TypeCategory category = this.currentType.getCategory();
        switch (category) {
            case ENUM -> this.createEnumComboBox();
            case BOOLEAN -> this.createBooleanComboBox();
            case OBJECT, COMPLEX -> this.createObjectComboBox();
            default -> this.createTextField();
        }
        this.currentInputControl.setPrefWidth(1000);
    }

    private void createTextField() {
        final TextField textField = new TextField();
        this.currentInputControl = textField;
        this.onValueChanged = (value) -> {
            if (value != null) {
                textField.setText(value.toString());
            } else {
                textField.setText("");
            }
        };
        final ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            final String sanitizedString = this.sanitizeString(newValue);
            if (!newValue.equals(sanitizedString)) {
                textField.setText(sanitizedString);
            }
            final ValidationResult validationResult = this.validateString(sanitizedString);
            this.updateIsValid(validationResult);
            if (validationResult != ValidationResult.ERROR) {
                this.setFromString(sanitizedString);
            }
        };
        textField.textProperty().addListener(changeListener);
        this.currentChangeListeners.add(new WeakChangeListener<>(changeListener));
        final ValidationResult validationResult = this.validateString(textField.getText());
        this.updateIsValid(validationResult);
    }

    private void createBooleanComboBox() {
        final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableList(
                this.currentType.isPrimitive() ? Arrays.asList("true", "false") : Arrays.asList("true", "false", "null")));
        this.currentInputControl = comboBox;
        this.onValueChanged = (value) -> {
            if (value == null) {
                comboBox.setValue("null");
            } else if ((boolean)value) {
                comboBox.setValue("true");
            } else {
                comboBox.setValue("false");
            }
        };
        final ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            this.validateComboBoxValue(newValue);
            switch (newValue) {
                case "null" -> this.setValue(null);
                case "true" -> this.setValue(true);
                case "false" -> this.setValue(false);
                default -> throw new IllegalStateException("unknown enum state");
            }
        };
        comboBox.valueProperty().addListener(new WeakChangeListener<>(changeListener));
        this.currentChangeListeners.add(changeListener);
        this.validateComboBoxValue(comboBox.getValue());
    }

    private void createEnumComboBox() {
        assert this.currentType.getCategory() == TypeCategory.ENUM;

        final ArrayList<Object> enumValues = new ArrayList<>(List.of(this.currentType.getType().getEnumConstants()));
        enumValues.add("null");
        final ComboBox<Object> comboBox = new SearchableComboBox<>(FXCollections.observableList(enumValues));
        this.currentInputControl = comboBox;
        this.onValueChanged = (value) -> {
            if (value == null) {
                comboBox.setValue("null");
            } else {
                comboBox.setValue(value);
            }
        };
        final ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.validateComboBoxValue(newValue);
                if (newValue.equals("null")) {
                    this.setValue(null);
                } else {
                    this.setValue(newValue);
                }
            }
        };
        comboBox.valueProperty().addListener(new WeakChangeListener<>(changeListener));
        this.currentChangeListeners.add(changeListener);
        this.validateComboBoxValue(comboBox.getValue());
    }

    private void createObjectComboBox() {
        final ObservableList<OptionalInstance> itemsList = FXCollections.observableList(new ArrayList<>());
        final OptionalInstance nullValue = new OptionalInstance(Optional.empty(), "null", false);
        itemsList.add(nullValue);
        for (final InstanceViewModel instanceViewModel : this.inspectionViewModel.instancesProperty()) {
            this.addToObjectList(itemsList, instanceViewModel);
        }

        final ComboBox<OptionalInstance> comboBox = new SearchableComboBox<>(itemsList);
        comboBox.setValue(nullValue);
        this.currentInputControl = comboBox;

        final ListChangeListener<InstanceViewModel> instanceViewModelListChangeListener = change -> {
            while(change.next()) {
                final OptionalInstance selectedOptionalInstance = comboBox.getValue();
                for (final InstanceViewModel addedInstanceViewModel : change.getAddedSubList()) {
                    final Optional<OptionalInstance> newInstance = this.addToObjectList(itemsList, addedInstanceViewModel);
                    if (selectedOptionalInstance != null
                            && selectedOptionalInstance.isNew()
                            && newInstance.isPresent()
                            && selectedOptionalInstance.equals(newInstance.get())) {
                        itemsList.remove(selectedOptionalInstance);
                        comboBox.setValue(newInstance.get());
                    }
                }
                for (final InstanceViewModel removedInstanceViewModel : change.getRemoved()) {
                    itemsList.remove(new OptionalInstance(Optional.of(removedInstanceViewModel.valueProperty().get()), "", false));
                }
            }
        };
        this.inspectionViewModel.instancesProperty().addListener(instanceViewModelListChangeListener);
        this.currentListChangeListeners.add(instanceViewModelListChangeListener);

        this.onValueChanged = value -> {
            itemsList.removeIf(OptionalInstance::isNew);
            if (value != null) {
                if (this.inspectionViewModel.hasViewModelForObject(value)) {
                    final OptionalInstance toSelect = itemsList.stream().filter(optionalInstance -> {
                        if (optionalInstance.instance().isEmpty()) {
                            return false;
                        } else {
                            return optionalInstance.instance().get() == value;
                        }
                    }).findFirst().orElseThrow();
                    comboBox.setValue(toSelect);
                } else {
                    final OptionalInstance newInstanceViewModel = new OptionalInstance(Optional.of(value), "«new»", true);
                    itemsList.add(0, newInstanceViewModel);
                    comboBox.setValue(newInstanceViewModel);
                }
            } else {
                comboBox.setValue(itemsList.get(itemsList.size() - 1));
            }
        };
        final ChangeListener<OptionalInstance> changeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.validateComboBoxValue(newValue);
                if (newValue.instance().isEmpty()) {
                    this.setValue(null);
                } else {
                    this.setValue(newValue);
                }
                this.isNewObjectValue.set(newValue.isNew());
            }
        };
        comboBox.valueProperty().addListener(new WeakChangeListener<>(changeListener));
        this.currentChangeListeners.add(changeListener);
        this.validateComboBoxValue(comboBox.getValue());
    }

    private Optional<OptionalInstance> addToObjectList(final ObservableList<OptionalInstance> itemsList, final InstanceViewModel instanceViewModel) {
        if (this.currentType.getCategory() != TypeCategory.COMPLEX
                || this.currentType.getType().isAssignableFrom(instanceViewModel.valueProperty().get().getClass())) {
            final OptionalInstance newInstance = new OptionalInstance(Optional.of(instanceViewModel.valueProperty().get()), instanceViewModel.nameProperty().get(), false);
            itemsList.add(0, newInstance);
            return Optional.of(newInstance);
        } else {
            return Optional.empty();
        }
    }

    private void validateComboBoxValue(final Object value) {
        if (value == null) {
            this.updateIsValid(ValidationResult.ERROR);
        } else if (value.toString().equals("null") && this.type.getCategory() != TypeCategory.OPTIONAL) {
            this.updateIsValid(ValidationResult.WARNING);
        } else {
            this.updateIsValid(ValidationResult.OK);
        }
    }

    private static record OptionalInstance(Optional<Object> instance, String text, boolean isNew) {

        @Override
        public String toString() {
            return this.text;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != OptionalInstance.class) {
                return false;
            } else {
                final Optional<Object> otherInstance = ((OptionalInstance)obj).instance();
                if (this.instance.isPresent()) {
                    return this.instance.get() == otherInstance.orElse(null);
                } else {
                    return otherInstance.isEmpty();
                }
            }
        }
    }
}