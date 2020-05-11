package cuie.stoffiifee_salomonhaeseli.template_simplecontrol.demo;

import javafx.beans.property.*;

public class PresentationModel {
    private final StringProperty demoTitle      =   new SimpleStringProperty("Spinning Control Demo");
    private final DoubleProperty currentMwh2015 =   new SimpleDoubleProperty(1);
    private final DoubleProperty currentMwh2016 =   new SimpleDoubleProperty(1);
    private final DoubleProperty currentMwh2017 =   new SimpleDoubleProperty(1);
    private final DoubleProperty currentMwh2018 =   new SimpleDoubleProperty(10000);
    private final DoubleProperty maxMwh         =   new SimpleDoubleProperty(300000);
    private final BooleanProperty operating     =   new SimpleBooleanProperty(false);

    // all getters and setters (generated via "Code -> Generate -> Getter and Setter)

    public String getDemoTitle() {
        return demoTitle.get();
    }

    public StringProperty demoTitleProperty() {
        return demoTitle;
    }

    public void setDemoTitle(String demoTitle) {
        this.demoTitle.set(demoTitle);
    }

    public double getMaxMwh() {
        return maxMwh.get();
    }

    public DoubleProperty maxMwhProperty() {
        return maxMwh;
    }

    public void setMaxMwh(double maxMwh) {
        this.maxMwh.set(maxMwh);
    }

    public double getCurrentMwh2015() {
        return currentMwh2015.get();
    }

    public DoubleProperty currentMwh2015Property() {
        return currentMwh2015;
    }

    public void setCurrentMwh2015(double currentMwh2015) {
        this.currentMwh2015.set(currentMwh2015);
    }

    public double getCurrentMwh2016() {
        return currentMwh2016.get();
    }

    public DoubleProperty currentMwh2016Property() {
        return currentMwh2016;
    }

    public void setCurrentMwh2016(double currentMwh2016) {
        this.currentMwh2016.set(currentMwh2016);
    }

    public double getCurrentMwh2017() {
        return currentMwh2017.get();
    }

    public DoubleProperty currentMwh2017Property() {
        return currentMwh2017;
    }

    public void setCurrentMwh2017(double currentMwh2017) {
        this.currentMwh2017.set(currentMwh2017);
    }

    public double getCurrentMwh2018() {
        return currentMwh2018.get();
    }

    public DoubleProperty currentMwh2018Property() {
        return currentMwh2018;
    }

    public void setCurrentMwh2018(double currentMwh2018) {
        this.currentMwh2018.set(currentMwh2018);
    }

    public boolean getOperating() {
        return operating.get();
    }

    public BooleanProperty operatingProperty() {
        return operating;
    }

    public void setOperating(boolean operating) {
        this.operating.set(operating);
    }
}
