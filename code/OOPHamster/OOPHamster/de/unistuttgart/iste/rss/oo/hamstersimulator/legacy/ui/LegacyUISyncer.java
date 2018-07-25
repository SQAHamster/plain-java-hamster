package de.unistuttgart.iste.rss.oo.hamstersimulator.legacy.ui;
//package de.unistuttgart.iste.rss.oo.hamster.legacy.ui;
//
//import de.hamster.simulation.controller.SimulationController;
//import de.hamster.simulation.model.SimulationModel;
//import de.hamster.simulation.model.SimulationModelSimOnly;
//import de.hamster.simulation.view.LogPanel;
//import de.hamster.simulation.view.SimulationTools;
//import de.hamster.workbench.Workbench;
//import de.unistuttgart.iste.rss.oo.hamster.state.HamsterStateChangedEvent;
//import de.unistuttgart.iste.rss.oo.hamster.state.HamsterStateListener;
//
//public class LegacyUISyncer implements HamsterStateListener {
//
//    private Workbench _intern_simulationWorkbench;
//    private SimulationController _intern_simulationController;
//    private SimulationTools _intern_simulationTools;
//    private SimulationModel _intern_simulationModel;
//    private LogPanel _intern_logPanel;
//
//    public LegacyUISyncer() {
//        super();
//        initWorkbench();
//    }
//
//    @Override
//    public void onStateChanged(final HamsterStateChangedEvent e) {
//    }
//
//    private void initWorkbench() {
//        _intern_simulationWorkbench = Workbench.getSimWorkbench(new SimulationModelSimOnly());
//        _intern_simulationWorkbench.getView().getSimulationFrame().toFront();
//        // simulationWorkbench.getView().getSimulationFrame().setAlwaysOnTop(true);
//        _intern_simulationWorkbench.getView().getSimulationFrame().setTitle("Hamster-Simulator");
//        _intern_simulationController = _intern_simulationWorkbench.getSimulationController();
//        _intern_simulationTools = _intern_simulationController.getSimulationTools();
//        _intern_simulationTools.getResetAction().setEnabled(false);
//        _intern_simulationModel = _intern_simulationController.getSimulationModel();
//        _intern_logPanel = _intern_simulationController.getLogPanel();
//    }
//}
