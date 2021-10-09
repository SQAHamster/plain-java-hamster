package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.testdata.B;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.javafx.HamsterGameStage;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;

import java.io.IOException;

public class InpectableHamsterGameStage extends HamsterGameStage {

    public InpectableHamsterGameStage(HamsterGameViewModel hamsterGameViewModel, InspectionViewModel inspect) throws IOException {
        super(hamsterGameViewModel);

        final SplitPane root = new SplitPane();
        root.getItems().add(this.getScene().getRoot());
        final InspectControl inspectControl = new InspectControl(inspect);
        root.getItems().add(inspectControl);
        root.setDividerPosition(0,0.65);
        final Scene newScene = new Scene(root, 1280, 720);
        inspectControl.prefHeightProperty().bind(newScene.heightProperty());
        newScene.getStylesheets().add("style.css");
        newScene.getStylesheets().addAll(this.getScene().getStylesheets());
        this.setScene(newScene);

        inspect.createInstanceViewModel(new B(), "b");
    }

}
