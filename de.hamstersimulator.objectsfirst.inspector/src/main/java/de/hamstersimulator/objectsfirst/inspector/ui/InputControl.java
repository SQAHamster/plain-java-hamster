package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import de.hamstersimulator.objectsfirst.inspector.model.TypeCategory;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InstanceViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
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

/**
 * Input Control which supports the following input types:
 * - all primitive types
 * - all wrapper types
 * - String
 * - Optional (automatically unwrapped)
 * - Enums
 * - Instances of specific class
 * The value is only updated, if the current input is valid.
 * If readOnly is true, the value cannot be updated
 * If the current value is not a primitive, wrapper type or Enum,
 * and if it is not an element of the known instances list,
 * an add button is displayed beside the value.
 */
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
    private final ReadOnlyBooleanWrapper isValid = new ReadOnlyBooleanWrapper(this, "isValid");
    private final ReadOnlyBooleanWrapper isNewObjectValue = new ReadOnlyBooleanWrapper(this, "isNewObjectValue", false);
    private final SimpleBooleanProperty isReadOnly = new SimpleBooleanProperty(this, "isReadOnly", false);

    /**
     * Creates a new InputControl
     * The type this control supports cannot be changed
     *
     * @param type                The type of the input this control should support
     * @param inspectionViewModel the ViewModel which contains the instances list
     */
    public InputControl(final Type type, final InspectionViewModel inspectionViewModel) {
        this.type = type;
        this.inspectionViewModel = inspectionViewModel;
        this.initChangeListeners();

        final TypeCategory category = type.getCategory();
        if (category == TypeCategory.OBJECT || category == TypeCategory.OPTIONAL) {
            this.multiTypeComboBox = this.initTypeSwitchingUI();
        } else {
            this.multiTypeComboBox = null;
            this.initSimpleUI();
        }
        this.createSimpleInputControl();
        this.getChildren().add(this.currentInputControl);
        this.getChildren().add(this.createAddInstanceButton());
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Property for the current value of this InputControl
     *
     * @return a mutable property for the value
     */
    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }

    /**
     * Property if the current input is valid
     *
     * @return a readonly property for isValid
     */
    public ReadOnlyBooleanProperty isValidProperty() {
        return this.isValid.getReadOnlyProperty();
    }

    /**
     * Property if the user can update the value of this control
     *
     * @return a property for isReadOnly
     */
    public BooleanProperty isReadOnlyProperty() {
        return this.isReadOnly;
    }

    /**
     * Inits the change listeners which watch for changes on the value and
     * the instance list of the InspectionViewModel.
     * Should only be called once from the constructor
     */
    private void initChangeListeners() {
        this.value.addListener((observable, oldValue, newValue) -> {
            this.handleValueChanged(newValue);
        });
        this.inspectionViewModel.instancesProperty().addListener((ListChangeListener<InstanceViewModel>) change -> {
            if (this.instanceViewModelListChangeListener != null) {
                this.instanceViewModelListChangeListener.onChanged(change);
            }
        });
    }

    /**
     * Creates the children for the complex UI which is displayed if all input
     * types must be possible (OBJECT and OPTIONAL)
     *
     * @return The ComboBox used too switch between the different
     * input possibilities
     */
    private ComboBox<Type> initTypeSwitchingUI() {
        final ComboBox<Type> multiTypeComboBox = this.createMultiTypeComboBox();
        multiTypeComboBox.managedProperty().bind(this.isReadOnly.not());
        multiTypeComboBox.visibleProperty().bind(this.isReadOnly.not());
        this.getChildren().add(multiTypeComboBox);
        this.currentType.set(multiTypeComboBox.getValue());
        this.isReadOnly.addListener((observable, oldValue, newValue) -> {
            this.createSimpleInputControl();
            this.getChildren().set(1, this.currentInputControl);
        });
        return multiTypeComboBox;
    }

    /**
     * Inits the UI in simple mode (all modes except OBJECT and OPTIONAL)
     */
    private void initSimpleUI() {
        this.currentType.set(type);
        this.isReadOnly.addListener((observable, oldValue, newValue) -> {
            this.createSimpleInputControl();
            this.getChildren().set(0, this.currentInputControl);
        });
    }

    /**
     * Parses a value
     * If the currentType is OPTIONAL, the content of the optional is returned
     * Otherwise, value is returned
     *
     * @param value the value to escape
     * @return the escaped value
     */
    private Object escapeValue(final Object value) {
        if (this.type.getCategory() == TypeCategory.OPTIONAL) {
            assert value instanceof Optional;
            return ((Optional<?>) value).orElse(null);
        } else {
            return value;
        }
    }

    /**
     * Called when the value changed
     * Guards for changes which are caused internally
     *
     * @param value the new value
     */
    private void handleValueChanged(final Object value) {
        if (!this.changeFromInputControl) {
            final Object escapedValue = this.escapeValue(value);
            final TypeCategory category = this.type.getCategory();
            final boolean isComplexMode = (category == TypeCategory.OBJECT) || (category == TypeCategory.OPTIONAL);
            if (escapedValue != null && isComplexMode) {
                this.handleValueChangedComplexMode(escapedValue);
            }
            this.onValueChanged.accept(escapedValue);
        }
    }

    /**
     * Handles value changed logic specific to the complex UI mode
     * Updates the current input mode depending on the dynamic type of the new value
     *
     * @param escapedValue the escaped new value, must be != null
     */
    private void handleValueChangedComplexMode(final Object escapedValue) {
        assert escapedValue != null;

        final Type newType = new Type(escapedValue.getClass());
        final TypeCategory newCategory = newType.getCategory();
        final TypeCategory currentCategory = this.currentType.get().getCategory();
        if (newCategory != currentCategory && currentCategory != TypeCategory.OBJECT) {
            this.multiTypeComboBox.setValue(newType);
        }
    }

    /**
     * Validates a String input based on the currentType
     *
     * @param textValue the text to validate
     * @return the ValidationResult
     */
    private ValidationResult validateString(final String textValue) {
        final String nonNullTextValue = Objects.requireNonNullElse(textValue, "");
        if (this.type.getCategory() == TypeCategory.OPTIONAL && nonNullTextValue.isBlank()) {
            return ValidationResult.OK;
        }

        if (this.currentType.get().getCategory() == TypeCategory.STRING) {
            return this.validateStringInput(textValue);
        } else if (this.currentType.get().getCategory() == TypeCategory.CHARACTER) {
            return this.validateCharacterInput(nonNullTextValue);
        } else if (!this.currentType.get().isPrimitive() && nonNullTextValue.isBlank()) {
            return ValidationResult.WARNING;
        }
        return this.validatePrimitiveInput(nonNullTextValue);
    }

    /**
     * Validates a String input
     *
     * @param textValue the new value (might be null)
     * @return WARNING if textValue is null, otherwise OK
     */
    private ValidationResult validateStringInput(final String textValue) {
        if (textValue == null) {
            return ValidationResult.WARNING;
        } else {
            return ValidationResult.OK;
        }
    }

    /**
     * Validates a character input
     *
     * @param textValue the input for the character, must be != null
     * @return OK if the input consists of exactly one character, otherwise ERROR
     */
    private ValidationResult validateCharacterInput(final String textValue) {
        assert textValue != null;

        if (textValue.length() == 1) {
            return ValidationResult.OK;
        } else if (textValue.isBlank() && !this.currentType.get().isPrimitive()) {
            return ValidationResult.WARNING;
        } else {
            return ValidationResult.ERROR;
        }
    }

    /**
     * Validates input for a primitive except char.
     * Tries to parse the input, and returns ERROR if the parsing failed
     *
     * @param textValue the value to parse
     * @return OK if the input is valid, otherwise ERROR
     */
    private ValidationResult validatePrimitiveInput(final String textValue) {
        assert textValue != null;

        try {
            switch (this.currentType.get().getCategory()) {
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

    /**
     * Updates isValid and sets style classes based on a ValidationResult
     *
     * @param validationResult the result of the latest input validation
     */
    private void updateIsValid(final ValidationResult validationResult) {
        final ObservableList<String> styleClass = this.currentInputControl.getStyleClass();
        styleClass.remove("error");
        styleClass.remove("warning");
        switch (validationResult) {
            case OK -> {
                this.isValid.set(true);
            }
            case WARNING -> {
                this.isValid.set(true);
                styleClass.add("warning");
            }
            case ERROR -> {
                this.isValid.set(false);
                styleClass.add("error");
            }
        }
    }

    /**
     * Updates the current value based on a text field input
     * Requires that the input is valid
     *
     * @param textValue the text input to parse
     */
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

    /**
     * Sets the current value
     * changeFromInputControl is temporarily set to true, to
     * prevent the listener.
     * If the current type requires an Optional, the value
     * is wrapped in an Optional
     *
     * @param value the new value
     */
    private void setValue(final Object value) {
        this.changeFromInputControl = true;
        if (this.type.getCategory() == TypeCategory.OPTIONAL) {
            this.value.set(Optional.ofNullable(value));
        } else {
            this.value.set(value);
        }
        this.changeFromInputControl = false;
    }

    /**
     * Creates an add instance button
     * If the button is clicked, the current value is added to the instances list
     *
     * @return the created button
     */
    private Button createAddInstanceButton() {
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

    /**
     * Creates the ComboBox which is used if multiple input types are possible
     *
     * @return the created ComboBox
     */
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

    /**
     * Handles a change of the input method in case of complex input method
     * Creates a new simpleInputControl, and updates the children of this control
     *
     * @param newType the new input method, must be != null
     */
    private void onMultiTypeChanged(final Type newType) {
        assert newType != null;

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

    /**
     * Creates a simple input control
     * This control can handle all primitives, their wrappers, enums, and subtypes of object.
     * It cannot handle object or optional on its own, as these require an input type dropdown.
     * After a call to
     */
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

    /**
     * Creates the label which is displayed if the control is in readonly mode
     * The content of the label depends on the current value and type
     * Sets currentInputControl to the created label
     */
    private void createReadOnlyLabel() {
        final Label label = new Label();
        this.onValueChanged = (value) -> {
            if (value != null) {
                updateLabelText(label, value);
            } else {
                label.setText("null");
            }
        };
        this.instanceViewModelListChangeListener = change -> {
            this.onValueChanged.accept(this.value.get());
        };
        this.currentInputControl = label;
    }

    /**
     * Updates the text of the label based on the provided value Object
     * If currentType is OBJECT or COMPLEX, also updates isNewObjectValue
     *
     * @param label the Label to set the text of
     * @param value the current value
     */
    private void updateLabelText(final Label label, final Object value) {
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
                label.setText(((Enum<?>) value).name());
            }
            default -> {
                label.setText(value.toString());
            }
        }
    }

    /**
     * Creates a TextField used for all simple (primitive and wrapper type) inputs.
     * Adds a ChangeListener, which updates the current value on TextField updates
     * Also adds a promptText which is displayed if the text field is empty
     */
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
        createTextFieldPromptBinding(textField);
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

    /**
     * Creates the binding for the prompt text fo a simple type text field.
     * Adds the binding to the provided TextField
     * The prompt text display null if the current type is a wrapper and the current value is null or the
     * current value is blank and the category is not STRING
     *
     * @param textField the TextField to add the prompt binding to
     */
    private void createTextFieldPromptBinding(TextField textField) {
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
    }

    /**
     * Creates a ComboBox if the current category is BOOLEAN
     * Adds true and false to the possible values.
     * Also adds null, if the current type is a wrapper type
     * Adds a changeListener which updates the current value on ComboBox selected item changes.
     * Sets currentInputControl to the generated ComboBox
     */
    private void createBooleanComboBox() {
        final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableList(
                this.currentType.get().isPrimitive() ? Arrays.asList("true", "false") : Arrays.asList("true", "false", "null")));
        this.currentInputControl = comboBox;
        this.onValueChanged = (value) -> {
            if (value == null) {
                comboBox.setValue("null");
            } else if ((boolean) value) {
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

    /**
     * Creates a ComboBox if the current category is ENUM.
     * Adds all possible enum values, including null to the items list.
     * Adds a changeListener which updates the current value on ComboBox selected item changes.
     * Sets currentInputControl to the generated ComboBox
     */
    private void createEnumComboBox() {
        assert this.currentType.get().getCategory() == TypeCategory.ENUM;

        final ArrayList<Object> enumValues = new ArrayList<>(List.of(this.currentType.get().getType().getEnumConstants()));
        enumValues.add("null");
        final ComboBox<Object> comboBox = new SearchableComboBox<>(FXCollections.observableList(enumValues));
        this.currentInputControl = comboBox;
        this.onValueChanged = (value) -> {
            comboBox.setValue(Objects.requireNonNullElse(value, "null"));
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
     *  Creates a ComboBox if the current category is any object category.
     *  Adds a changeListener which updates the current value on ComboBox selected item changes.
     *  Sets currentInputControl to the generated ComboBox
     */
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
        final ChangeListener<OptionalInstance> changeListener = createObjectComboBoxChangeHandler();
        comboBox.valueProperty().addListener(new WeakChangeListener<>(changeListener));
        this.currentChangeListeners.add(changeListener);
        this.validateComboBoxValue(comboBox.getValue());
    }

    /**
     * Creates a ChangeHandler called when the value of the objectComboBox changes
     * which updates the current value, and isNewObjectValue
     *
     * @return the generated ChangeListener
     */
    private ChangeListener<OptionalInstance> createObjectComboBoxChangeHandler() {
        return (observable, oldValue, newValue) -> {
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
    }

    /**
     * Updates the items list of the provided ComboBox based on the instances list on the
     * inspectionViewModel. Each instance is added to the ComboBox if it has a compatible type.
     * Additionally, a null entry is added.
     * If the current value of the ComboBox is not already present in the generated items list,
     * it is added too.
     * The current value of the ComboBox is set to the OptionalInstance representing the provided value
     *
     * @param comboBox the ComboBox to update
     * @param value the current value
     */
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

    /**
     * Checks that the value of the instanceViewModel is assignable to the current type, and creates
     * a new OptionalInstance with the provided instanceViewModel.
     * Also adds it to the beginning of itemsList
     *
     * @param itemsList a list of all items, if an OptionalInstance was created, it is added at the beginning
     *                  ot this list
     * @param instanceViewModel the view model representing the instance to convert
     * @return An optional with the created OptionalInstance if one was created, otherwise an empty Optional
     */
    private Optional<OptionalInstance> addToObjectList(final ObservableList<OptionalInstance> itemsList,
                                                       final InstanceViewModel instanceViewModel) {
        if (this.currentType.get().getCategory() != TypeCategory.COMPLEX
                || this.currentType.get().getType().isAssignableFrom(instanceViewModel.valueProperty().get().getClass())) {
            final OptionalInstance newInstance = new OptionalInstance(
                    Optional.of(instanceViewModel.valueProperty().get()),
                    instanceViewModel.nameProperty().get(),
                    false);
            itemsList.add(0, newInstance);
            return Optional.of(newInstance);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Validates the value of an object ComboBox
     * If it is null, the result is ERROR.
     * If it is an OptionalInstance with text null, it displays a warning if the current category is not OPTIONAL
     * Otherwise, the validation result is OK
     *
     * @param value the current value of the object ComboBox to validate
     */
    private void validateComboBoxValue(final Object value) {
        if (value == null) {
            this.updateIsValid(ValidationResult.ERROR);
        } else if (value.toString().equals("null") && this.type.getCategory() != TypeCategory.OPTIONAL) {
            this.updateIsValid(ValidationResult.WARNING);
        } else {
            this.updateIsValid(ValidationResult.OK);
        }
    }

    /**
     * Entry for the object ComboBox
     *
     * @param instance the actual instance of the object
     * @param text     the displayed text
     * @param isNew    is the value new and not saved to the object bench?
     */
    private record OptionalInstance(Optional<Object> instance, String text, boolean isNew) {

        @Override
        public String toString() {
            return this.text;
        }
    }
}