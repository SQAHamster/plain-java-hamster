package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.ui.javafx.HamsterGameStage;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;

import java.io.IOException;

/**
 * HamsterGameStage with an additional InspectControl to allow inspection
 */
public class InpectableHamsterGameStage extends HamsterGameStage {

    private final InspectionViewModel inspectionViewModel;
    private final InspectControl inspectControl;

    /**
     * Creates a new HamsterGameStage
     *
     * @param hamsterGameViewModel represents the HamsterGame, provided to parent HamsterGameStage
     * @param inspectionViewModel represents inspection support
     * @throws IOException if the fxml of the parent control cannot be loaded
     */
    public InpectableHamsterGameStage(final HamsterGameViewModel hamsterGameViewModel,
                                      final InspectionViewModel inspectionViewModel) throws IOException {
        super(hamsterGameViewModel);

        this.inspectionViewModel = inspectionViewModel;
        final SplitPane root = new SplitPane();
        root.getItems().add(this.getScene().getRoot());
        this.inspectControl = new InspectControl(inspectionViewModel);
        root.getItems().add(this.inspectControl);
        root.setDividerPosition(0,0.65);
        final Scene newScene = new Scene(root, 1280, 720);
        this.inspectControl.prefHeightProperty().bind(newScene.heightProperty());
        newScene.getStylesheets().add("style.css");
        newScene.getStylesheets().addAll(this.getScene().getStylesheets());
        this.setScene(newScene);
    }

    @Override
    protected void onCloseRequest() {
        super.onCloseRequest();
        this.inspectionViewModel.stopExecution();
        this.inspectControl.onClose();
    }
}
