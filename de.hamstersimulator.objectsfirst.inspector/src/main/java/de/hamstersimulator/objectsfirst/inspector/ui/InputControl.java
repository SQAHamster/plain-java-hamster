package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import de.hamstersimulator.objectsfirst.inspector.model.TypeCategory;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InstanceViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.controlsfx.control.SearchableComboBox;
import java.util.*;
import java.util.function.Consumer;

public class InputControl extends HBox {

    private final Type type;
    private Control currentInputControl;
    private Consumer<Object> onValueChanged;
    private ListChangeListener<InstanceViewModel> instanceViewModelListChangeListener = null;
    private boolean changeFromInputControl = false;
    private final List<ChangeListener<?>> currentChangeListeners = new ArrayList<>();
    private final ComboBox<Type> multiTypeComboBox;

    private final InspectionViewModel inspectionViewModel;
    private final SimpleObjectProperty<Type> currentType = new SimpleObjectProperty<>(this, "currentType");
    private final SimpleObjectProperty<Object> value = new SimpleObjectProperty<>(this, "value");
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(this, "isValid");
    private final SimpleBooleanProperty isNewObjectValue = new SimpleBooleanProperty(this, "isNewObjectValue", false);
    private final SimpleBooleanProperty isReadOnly = new SimpleBooleanProperty(this, "isReadOnly", false);

    public InputControl(final Type type, final InspectionViewModel hamsterUI) {
        this.type = type;
        this.inspectionViewModel = hamsterUI;
        this.value.addListener((observable, oldValue, newValue) -> {
            this.handleValueChanged(newValue);
        });
        this.inspectionViewModel.instancesProperty().addListener((ListChangeListener<InstanceViewModel>) change -> {
            if (this.instanceViewModelListChangeListener != null) {
                this.instanceViewModelListChangeListener.onChanged(change);
            }
        });
        final TypeCategory category = type.getCategory();

        if (category == TypeCategory.OBJECT || category == TypeCategory.OPTIONAL) {
            final ComboBox<Type> multiTypeComboBox = this.createMultiTypeComboBox();
            multiTypeComboBox.managedProperty().bind(this.isReadOnly.not());
            multiTypeComboBox.visibleProperty().bind(this.isReadOnly.not());
            this.multiTypeComboBox = multiTypeComboBox;
            this.getChildren().add(multiTypeComboBox);
            this.currentType.set(multiTypeComboBox.getValue());
            this.isReadOnly.addListener((observable, oldValue, newValue) -> {
                this.createSimpleInputControl();
                this.getChildren().set(1, this.currentInputControl);
            });
        } else {
            this.currentType.set(type);
            this.multiTypeComboBox = null;
            this.isReadOnly.addListener((observable, oldValue, newValue) -> {
                this.createSimpleInputControl();
                this.getChildren().set(0, this.currentInputControl);
            });
        }
        this.createSimpleInputControl();
        this.getChildren().add(this.currentInputControl);
        this.getChildren().add(this.createAddObjectButton());
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
    }

    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }

    public BooleanProperty isValidProperty() {
        return this.isValid;
    }

    public BooleanProperty isReadOnlyProperty() {
        return this.isReadOnly;
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
                final TypeCategory currentCategory = this.currentType.get().getCategory();
                if (newCategory == TypeCategory.ENUM && !this.currentType.get().getType().isAssignableFrom(newType.getType())) {
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
        if (this.currentType.get().getCategory() == TypeCategory.STRING) {
            if (textValue == null) {
                return ValidationResult.WARNING;
            } else {
                return ValidationResult.OK;
            }
        }
        final String nonNullTextValue = Objects.requireNonNullElse(textValue, "");
        if (!this.currentType.get().isPrimitive() && nonNullTextValue.isBlank()) {
            if (this.type.getCategory() == TypeCategory.OPTIONAL) {
                return ValidationResult.OK;
            } else {
                return ValidationResult.WARNING;
            }
        }
        if (this.currentType.get().getCategory() == TypeCategory.CHARACTER) {
            if (nonNullTextValue.length() == 1) {
                return ValidationResult.OK;
            } else {
                return ValidationResult.ERROR;
            }
        }
        try {
            switch (this.currentType.get().getCategory()) {
                case BYTE -> Byte.parseByte(nonNullTextValue);
                case SHORT -> Short.parseShort(nonNullTextValue);
                case INTEGER -> Integer.parseInt(nonNullTextValue);
                case LONG -> Long.parseLong(nonNullTextValue);
                case FLOAT -> Float.parseFloat(nonNullTextValue);
                case DOUBLE -> Double.parseDouble(nonNullTextValue);
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

        final String nonNullTextValue = Objects.requireNonNullElse(textValue, "");
        if (this.currentType.get().getCategory() == TypeCategory.STRING) {
            this.setValue(nonNullTextValue);
        } else if (this.currentType.get().getCategory() == TypeCategory.CHARACTER && nonNullTextValue.length() == 1) {
            this.setValue(nonNullTextValue.charAt(0));
        } else if (nonNullTextValue.isBlank()) {
            this.setValue(null);
        } else {
            switch (this.currentType.get().getCategory()) {
                case BYTE -> this.setValue(Byte.parseByte(nonNullTextValue));
                case SHORT -> this.setValue(Short.parseShort(nonNullTextValue));
                case INTEGER -> this.setValue(Integer.parseInt(nonNullTextValue));
                case LONG -> this.setValue(Long.parseLong(nonNullTextValue));
                case FLOAT -> this.setValue(Float.parseFloat(nonNullTextValue));
                case DOUBLE -> this.setValue(Double.parseDouble(nonNullTextValue));
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

    private Button createAddObjectButton() {
        final Button addButton = new Button();
        addButton.setText("+");
        addButton.managedProperty().bind(this.isNewObjectValue);
        addButton.visibleProperty().bind(this.isNewObjectValue);
        addButton.setOnMouseClicked(e -> {
            AddInstanceDialogWrapper.showAndWait(this.inspectionViewModel, this.value.get());
        });
        addButton.setMinWidth(Button.USE_PREF_SIZE);
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
        multiTypeComboBox.managedProperty().bind(this.isReadOnly.not());
        return multiTypeComboBox;
    }

    private void onMultiTypeChanged(final Type newType) {
        if (this.currentType.get() != newType) {
            this.isNewObjectValue.set(false);
            this.currentType.set(newType);
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
        final TypeCategory category = this.currentType.get().getCategory();
        if (this.isReadOnly.get()) {
            this.createReadOnlyLabel();
        } else {
            switch (category) {
                case ENUM -> this.createEnumComboBox();
                case BOOLEAN -> this.createBooleanComboBox();
                case OBJECT, COMPLEX -> this.createObjectComboBox();
                default -> this.createTextField();
            }
        }
        this.currentInputControl.setPrefWidth(1000);
        this.onValueChanged.accept(this.value.get());
    }

    private void createReadOnlyLabel() {
        final Label label = new Label();
        this.onValueChanged = (value) -> {
            if (value != null) {
                switch (this.currentType.get().getCategory()) {
                    case OBJECT, COMPLEX -> {
                        final Optional<InstanceViewModel> instanceViewModel = this.inspectionViewModel.getViewModelForObject(value);
                        if (instanceViewModel.isPresent()) {
                            label.setText(instanceViewModel.get().nameProperty().get());
                            this.isNewObjectValue.set(false);
                        } else {
                            label.setText("«new»");
                            this.isNewObjectValue.set(true);
                        }
                    }
                    case ENUM -> {
                        label.setText(((Enum<?>)value).name());
                    }
                    default -> {
                        label.setText(value.toString());
                    }
                }
            } else {
                label.setText("null");
            }
        };
        this.instanceViewModelListChangeListener = change -> {
            this.onValueChanged.accept(this.value.get());
        };
        this.currentInputControl = label;
    }

    private void createTextField() {
        final TextField textField = new TextField();
        this.currentInputControl = textField;
        this.onValueChanged = (value) -> {
            if (value != null) {
                textField.setText(value.toString());
            } else {
                textField.setText(null);
            }
        };
        textField.promptTextProperty().bind(Bindings.createStringBinding(() -> {
            final String currentValue = textField.getText();
            final TypeCategory currentCategory = this.currentType.get().getCategory();
            final boolean isPrimitive = this.currentType.get().isPrimitive();
            if (!isPrimitive && (currentValue == null || (currentValue.isBlank() && currentCategory != TypeCategory.STRING))) {
                return "null";
            } else {
                return "";
            }
        }, this.currentType, textField.textProperty()));
        final ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            final ValidationResult validationResult = this.validateString(newValue);
            this.updateIsValid(validationResult);
            if (validationResult != ValidationResult.ERROR) {
                this.setFromString(newValue);
            }
        };
        textField.textProperty().addListener(changeListener);
        this.currentChangeListeners.add(new WeakChangeListener<>(changeListener));
        final ValidationResult validationResult = this.validateString(textField.getText());
        this.updateIsValid(validationResult);
    }

    private void createBooleanComboBox() {
        final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableList(
                this.currentType.get().isPrimitive() ? Arrays.asList("true", "false") : Arrays.asList("true", "false", "null")));
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
        assert this.currentType.get().getCategory() == TypeCategory.ENUM;

        final ArrayList<Object> enumValues = new ArrayList<>(List.of(this.currentType.get().getType().getEnumConstants()));
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
        final ComboBox<OptionalInstance> comboBox = new SearchableComboBox<>();
        this.currentInputControl = comboBox;
        this.updateObjectComboBox(comboBox, null);

        this.instanceViewModelListChangeListener = change -> {
            this.updateObjectComboBox(comboBox,
                    comboBox.getValue().instance().orElse(null));
        };

        this.onValueChanged = value -> {
            this.updateObjectComboBox(comboBox, value);
        };
        final ChangeListener<OptionalInstance> changeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.validateComboBoxValue(newValue);
                if (newValue.instance().isEmpty()) {
                    this.setValue(null);
                } else {
                    this.setValue(newValue.instance().get());
                }
                this.isNewObjectValue.set(newValue.isNew());
            }
        };
        comboBox.valueProperty().addListener(new WeakChangeListener<>(changeListener));
        this.currentChangeListeners.add(changeListener);
        this.validateComboBoxValue(comboBox.getValue());
    }

    private void updateObjectComboBox(final ComboBox<OptionalInstance> comboBox, final Object value) {
        final ObservableList<OptionalInstance> itemsList = FXCollections.observableList(new ArrayList<>());
        final OptionalInstance nullValue = new OptionalInstance(Optional.empty(), "null", false);
        itemsList.add(nullValue);
        for (final InstanceViewModel instanceViewModel : this.inspectionViewModel.instancesProperty()) {
            this.addToObjectList(itemsList, instanceViewModel);
        }

        final Map<Object, OptionalInstance> itemLookup = new IdentityHashMap<>();
        for (final OptionalInstance optionalInstance : itemsList) {
            itemLookup.put(optionalInstance.instance().orElse(null), optionalInstance);
        }

        if (!itemLookup.containsKey(value)) {
            final OptionalInstance newValue = new OptionalInstance(Optional.of(value), "«new»", true);
            itemsList.add(0, newValue);
            itemLookup.put(value, newValue);
        }
        comboBox.setItems(itemsList);
        comboBox.setValue(itemLookup.get(value));
    }

    private Optional<OptionalInstance> addToObjectList(final ObservableList<OptionalInstance> itemsList, final InstanceViewModel instanceViewModel) {
        if (this.currentType.get().getCategory() != TypeCategory.COMPLEX
                || this.currentType.get().getType().isAssignableFrom(instanceViewModel.valueProperty().get().getClass())) {
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
    }
}