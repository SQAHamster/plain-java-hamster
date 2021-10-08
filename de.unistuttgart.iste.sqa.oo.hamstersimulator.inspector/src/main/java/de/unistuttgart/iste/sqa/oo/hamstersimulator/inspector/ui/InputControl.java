package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Instance;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Primitives;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
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

    private final InspectionViewModel viewModel;
    private final SimpleObjectProperty<Object> value = new SimpleObjectProperty<>(this, "value");
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(this, "isValid");
    private final SimpleBooleanProperty isNewObjectValue = new SimpleBooleanProperty(this, "isNewObjectValue", false);

    public InputControl(final Type type, final InspectionViewModel viewModel) {
        this.type = type;
        this.viewModel = viewModel;
        this.value.addListener((observable, oldValue, newValue) -> {
            this.handleValueChanged(newValue);
        });
        final Primitives primitiveType = type.getPrimitiveType();

        /*if (primitiveType == Primitives.COMPLEX || primitiveType == Primitives.OPTIONAL) {
            this.multiTypeComboBox = this.createMultiTypeComboBox();
            this.getChildren().add(this.multiTypeComboBox);
            this.currentType = multiTypeComboBox.getValue();
        } else {*/
            this.currentType = type;
            this.multiTypeComboBox = null;
        //}
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
            if (this.type.getPrimitiveType() == Primitives.OPTIONAL) {
                assert value instanceof Optional;
                value = ((Optional<?>)value).orElse(null);
            }
            if (value != null
                    && (this.type.getPrimitiveType() == Primitives.COMPLEX || this.type.getPrimitiveType() == Primitives.OPTIONAL)) {
                final Type newType = Type.typeForClass(value.getClass());
                final Primitives newPrimitiveType = newType.getPrimitiveType();
                final Primitives currentPrimitiveType = this.currentType.getPrimitiveType();
                if (newPrimitiveType == Primitives.ENUM && !this.currentType.getType().isAssignableFrom(newType.getType())) {
                    boolean foundMatchingEnumType = false;
                    //TODO: Understand
                    /*for (final Type inputType : this.classObjectCollection.multiInputTypesProperty()) {
                        if (inputType.getPrimitiveType() == Primitives.ENUM && inputType.getType().isAssignableFrom(newType.getType())) {
                            this.multiTypeComboBox.setValue(inputType);
                            foundMatchingEnumType = true;
                            break;
                        }
                    }*/
                    if (!foundMatchingEnumType) {
                        this.multiTypeComboBox.setValue(Type.typeForClass(Object.class));
                    }
                }
                if (newPrimitiveType != currentPrimitiveType && currentPrimitiveType != Primitives.COMPLEX) {
                    this.multiTypeComboBox.setValue(newType);
                }
            }
            this.onValueChanged.accept(value);
        }
    }

    private ValidationResult validateString(final String textValue) {
        assert textValue != null;

        if (this.currentType.getPrimitiveType() == Primitives.STRING) {
            return ValidationResult.OK;
        }
        if (!this.currentType.isPrimitive() && textValue.isBlank()) {
            if (this.type.getPrimitiveType() == Primitives.OPTIONAL) {
                return ValidationResult.OK;
            } else {
                return ValidationResult.WARNING;
            }
        }
        if (this.currentType.getPrimitiveType() == Primitives.CHAR) {
            if (textValue.length() == 1) {
                return ValidationResult.OK;
            } else {
                return ValidationResult.ERROR;
            }
        }
        try {
            switch (this.currentType.getPrimitiveType()) {
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

        if (this.currentType.getPrimitiveType() == Primitives.STRING) {
            this.setValue(textValue);
        } else if (this.currentType.getPrimitiveType() == Primitives.CHAR && textValue.length() == 1) {
            this.setValue(textValue.charAt(0));
        } else if (textValue.isBlank()) {
            this.setValue(null);
        } else {
            switch (this.currentType.getPrimitiveType()) {
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
        if (this.type.getPrimitiveType() == Primitives.OPTIONAL) {
            this.value.set(Optional.ofNullable(value));
        } else {
            this.value.set(value);
        }
        this.changeFromInputControl = false;
    }

    private String sanitizeString(final String textValue) {
        final Primitives primitiveType = this.currentType.getPrimitiveType();
        if (primitiveType == Primitives.BYTE
                || primitiveType == Primitives.SHORT
                || primitiveType == Primitives.INTEGER
                || primitiveType == Primitives.LONG) {
            return textValue.replaceAll("[^0-9]", "");
        } else if (primitiveType == Primitives.FLOAT || primitiveType == Primitives.DOUBLE) {
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
        addButton.setOnMouseClicked(event -> {
            System.out.println("Clicked this shit");
        });
        addButton.prefWidthProperty().bind(addButton.heightProperty());
        addButton.minWidthProperty().bind(addButton.prefWidthProperty());
        return addButton;
    }

    /**
     * TODO: Understand
     */
    /*private ComboBox<Type> createMultiTypeComboBox() {
        final ComboBox<Type> multiTypeComboBox = new SearchableComboBox<>(
                this.classObjectCollection.multiInputTypesProperty());
        multiTypeComboBox.setValue(multiTypeComboBox.getItems().get(0));
        multiTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.onMultiTypeChanged(newValue);
            }
        });
        multiTypeComboBox.setPrefWidth(100);
        multiTypeComboBox.minWidthProperty().bind(multiTypeComboBox.prefWidthProperty());
        return multiTypeComboBox;
    }

    private void onMultiTypeChanged(final Type newInputType) {
        if (this.currentType != newInputType) {
            this.isNewObjectValue.set(false);
            this.currentType = newInputType;
            this.createSimpleInputControl();
            this.getChildren().set(1, this.currentInputControl);
            if (this.type.getPrimitiveType() == Primitives.OPTIONAL) {
                this.value.set(Optional.empty());
            } else {
                this.value.set(null);
            }
            this.onValueChanged.accept(null);
        }
    }*/

    private void createSimpleInputControl() {
        this.currentChangeListeners.clear();
        this.currentListChangeListeners.clear();
        final Primitives category = this.currentType.getPrimitiveType();
        switch (category) {
            case ENUM -> this.createEnumComboBox();
            case BOOLEAN -> this.createBooleanComboBox();
            case COMPLEX/*, OF_CLASS*/ -> this.createObjectComboBox();
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
                this.currentType.isNullable() ? Arrays.asList("true", "false") : Arrays.asList("true", "false", "null")));
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
        assert this.currentType.getPrimitiveType() == Primitives.ENUM;

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

    /**
     * TODO: Check if changes broke fancy selection
     */
    private void createObjectComboBox() {
        final ObservableList<Instance> itemsList = FXCollections.observableList(new ArrayList<>());
        itemsList.add(Instance.instanceForObject(null));
        for (final Instance objectInfo : this.type.getKnownInstances()) {
            this.addToObjectList(itemsList, objectInfo);
        }

        final ComboBox<Instance> comboBox = new SearchableComboBox<>(itemsList);
        this.currentInputControl = comboBox;

        final ListChangeListener<Instance> objectInfoListChangeListener = change -> {
            final Instance selectedInstance = comboBox.getValue();
            for (final Instance addedObjectInfo : change.getAddedSubList()) {
                this.addToObjectList(itemsList, addedObjectInfo);
                if (selectedInstance.getValue() != null && addedObjectInfo.getValue() == selectedInstance.getValue()) {
                    itemsList.remove(selectedInstance);
                    comboBox.setValue(itemsList.get(0));
                }
            }
            if (change.getRemovedSize() > 0) {
                itemsList.removeIf(instance -> instance.getValue() != null
                        && change.getRemoved().contains(instance));
            }
        };
        this.viewModel.allInstancesProperty().addListener(objectInfoListChangeListener);
        this.currentListChangeListeners.add(objectInfoListChangeListener);

        this.onValueChanged = value -> {
            if (value != null) {
                final Instance valueInstance = Instance.instanceForObject(value);
                if (valueInstance.getValue() != null) {
                    if (itemsList.stream().anyMatch(instance -> instance == valueInstance)) {
                        comboBox.setValue(valueInstance);
                    } else {
                        this.addToObjectList(itemsList, valueInstance);
                    }
                } else {
                    //TODO: Offer "new"/"create" (because no instance is known
                    /*final OptionalObjectInfo newObjectInfo = new OptionalObjectInfo(Optional.of(
                            new ObjectInfo<>("«new»", new ClassInfo<>("",
                                    Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                                    Object.class), Collections.emptyList(), Collections.emptyList(), value)), true);
                    itemsList.add(0, newObjectInfo);
                    comboBox.setValue(newObjectInfo);*/
                }
            } else {
                comboBox.setValue(itemsList.get(itemsList.size() - 1));
            }
        };
        final ChangeListener<Instance> changeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.validateComboBoxValue(newValue);
                if (newValue.equals("null")) {
                    this.setValue(null);
                } else {
                    this.setValue(newValue);
                }
                //this.isNewObjectValue.set(newValue.isUnlisted());
            }
        };
        comboBox.valueProperty().addListener(new WeakChangeListener<>(changeListener));
        this.currentChangeListeners.add(changeListener);
        this.validateComboBoxValue(comboBox.getValue());
    }

    private void addToObjectList(final ObservableList<Instance> itemsList, final Instance instance) {
        if (this.currentType.getPrimitiveType() != Primitives.COMPLEX
                || (instance.getType() != null && this.currentType.getType().isAssignableFrom(instance.getType().getType()))) {
            itemsList.add(0, instance);
        }
    }

    private void validateComboBoxValue(final Object value) {
        if (value == null) {
            this.updateIsValid(ValidationResult.ERROR);
        } else if (value.toString().equals("null") && this.type.getPrimitiveType() != Primitives.OPTIONAL) {
            this.updateIsValid(ValidationResult.WARNING);
        } else {
            this.updateIsValid(ValidationResult.OK);
        }
    }
}

/*class OptionalObjectInfo {

    private final Optional<ObjectInfo<?>> objectInfo;
    private final boolean unlisted;

    public OptionalObjectInfo(final Optional<ObjectInfo<?>> objectInfo, final boolean isUnlisted) {
        this.objectInfo = objectInfo;
        this.unlisted = isUnlisted;
    }

    public Optional<ObjectInfo<?>> getObjectInfo() {
        return objectInfo;
    }

    public boolean isUnlisted() {
        return unlisted;
    }

    @Override
    public String toString() {
        if (this.objectInfo.isPresent()) {
            return this.objectInfo.get().nameProperty().get();
        } else {
            return "null";
        }
    }
}*/