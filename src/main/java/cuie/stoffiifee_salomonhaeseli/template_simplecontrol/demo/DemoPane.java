package cuie.stoffiifee_salomonhaeseli.template_simplecontrol.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.stoffiifee_salomonhaeseli.template_simplecontrol.SimpleControl;
import javafx.util.converter.NumberStringConverter;

public class DemoPane extends BorderPane {

    private final PresentationModel pm;

    // declare the custom control
    private SimpleControl cc;

    // all controls
    private TextField maxMwh;
    private TextField currentMwh2015;
    private TextField currentMwh2016;
    private TextField currentMwh2017;
    private TextField currentMwh2018;

    public DemoPane(PresentationModel pm) {
        this.pm = pm;
        initializeControls();
        layoutControls();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        cc = new SimpleControl();
        maxMwh = new TextField();
        currentMwh2015 = new TextField();
        currentMwh2016 = new TextField();
        currentMwh2017 = new TextField();
        currentMwh2018 = new TextField();
    }

    private void layoutControls() {
        VBox controlPane = new VBox(
                new Label("Gesamtproduktion"), maxMwh,
                new Label("Produktion 2015 (MwH)"), currentMwh2015,
                new Label("Produktion 2016 (MwH)"), currentMwh2016,
                new Label("Produktion 2017 (MwH)"), currentMwh2017,
                new Label("Produktion 2018 (MwH)"), currentMwh2018);

        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(cc);
        setRight(controlPane);
    }

    private void setupBindings() {
        //bindings for the "demo controls"
        maxMwh.textProperty().bindBidirectional(pm.maxMwhProperty(), new NumberStringConverter());
        currentMwh2015.textProperty().bindBidirectional(pm.currentMwh2015Property(), new NumberStringConverter());
        currentMwh2016.textProperty().bindBidirectional(pm.currentMwh2016Property(), new NumberStringConverter());
        currentMwh2017.textProperty().bindBidirectional(pm.currentMwh2017Property(), new NumberStringConverter());
        currentMwh2018.textProperty().bindBidirectional(pm.currentMwh2018Property(), new NumberStringConverter());



        //bindings for the Custom Control
        cc.maxMwhProperty().bindBidirectional(pm.maxMwhProperty());
        cc.currentMwh2015Property().bindBidirectional(pm.currentMwh2015Property());
        cc.currentMwh2016Property().bindBidirectional(pm.currentMwh2016Property());
        cc.currentMwh2017Property().bindBidirectional(pm.currentMwh2017Property());
        cc.currentMwh2018Property().bindBidirectional(pm.currentMwh2018Property());
        cc.operatingProperty().bindBidirectional(pm.operatingProperty());
    }

}
