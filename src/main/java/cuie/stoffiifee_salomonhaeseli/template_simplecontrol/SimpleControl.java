package cuie.stoffiifee_salomonhaeseli.template_simplecontrol;

import java.util.List;
import java.util.Locale;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * Dieses Dashboard zeigt die produzierte Strommenge des selektierten Windrades in Relation zu der gesamt
 * produzierten Strommgende an.
 * Jeder Kreis kann die produzierte Strommenge für das entsprechende Jahr bearbeiten.
 * Zentriert wird der in einem Button der Status des Windrads angezeigt (in Betrieb, ausser Betrieb) und kann über selbigen Button bearbeitet werden.
 *
 * @author Sophie-Marie Ordelman
 * @author Salomon Häseli
 */
//ToDo: Umbenennen.
public class SimpleControl extends Region {
    // wird gebraucht fuer StyleableProperties
    private static final StyleablePropertyFactory<SimpleControl> FACTORY = new StyleablePropertyFactory<>(
            Region.getClassCssMetaData());

    @Override public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    private static final Locale CH = new Locale("de", "CH");

    private static final double ARTBOARD_WIDTH = 800;  // ToDo: Breite der "Zeichnung" aus dem Grafik-Tool übernehmen
    private static final double ARTBOARD_HEIGHT = 800;  // ToDo: Anpassen an die Breite der Zeichnung

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH = 200;    // ToDo: Anpassen
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 3000;    // ToDo: Anpassen

    // ToDo: diese Parts durch alle notwendigen Parts der gewünschten CustomControl ersetzen
    private Circle backgroundCircle;
    private Circle thumb2015;
    private Circle thumb2016;
    private Circle thumb2017;
    private Circle thumb2018;
    private Arc maxMwhArc;
    private Arc currentMwh2015Arc;
    private Arc currentMwh2016Arc;
    private Arc currentMwh2017Arc;
    private Arc currentMwh2018Arc;
    private Button operatingButton;

    // ToDo: ersetzen durch alle notwendigen Properties der CustomControl
    private final DoubleProperty currentMwh2015 = new SimpleDoubleProperty();
    private final DoubleProperty currentMwh2016 = new SimpleDoubleProperty();
    private final DoubleProperty currentMwh2017 = new SimpleDoubleProperty();
    private final DoubleProperty currentMwh2018 = new SimpleDoubleProperty();
    private final DoubleProperty maxMwh = new SimpleDoubleProperty();
    private final BooleanProperty operating = new SimpleBooleanProperty();

    // ToDo: ergänzen mit allen CSS stylable properties
    private static final CssMetaData<SimpleControl, Color> BASE_COLOR_META_DATA = FACTORY
            .createColorCssMetaData("-base-color", s -> s.baseColor);

    private final StyleableObjectProperty<Color> baseColor = new SimpleStyleableObjectProperty<Color>(
            BASE_COLOR_META_DATA) {
        @Override protected void invalidated() {
            setStyle(String.format("%s: %s;", getCssMetaData().getProperty(), colorToCss(get())));
            applyCss();
        }
    };

    // ToDo: Loeschen falls keine getaktete Animation benoetigt wird

    // ToDo: alle Animationen und Timelines deklarieren

    //private final Timeline timeline = new Timeline();

    // fuer Resizing benoetigt
    private Pane drawingPane;

    public SimpleControl() {
        initializeSelf();
        initializeParts();
        initializeDrawingPane();
        initializeAnimations();
        layoutParts();
        setupEventHandlers();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeSelf() {
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/Lato/Lato-Reg.ttf");
        addStylesheetFiles("style.css");

        getStyleClass().add("simple-control");  // ToDo: an den Namen der Klasse (des CustomControls) anpassen
    }

    private void initializeParts() {
        //ToDo: alle deklarierten Parts initialisieren
        //ToDo: alle deklarierten Parts initialisieren
        double centerX = ARTBOARD_WIDTH * 0.5;
        double centerY = ARTBOARD_HEIGHT * 0.5;
        int width = 15;
        double radius = centerX - width;
        double radius2015 = ((centerX - width) - 40);
        double radius2016 = radius2015 - 40;
        double radius2017 = radius2016 - 40;
        double radius2018 = radius2017 - 40;

        backgroundCircle = new Circle(centerX, centerX, centerX);
        backgroundCircle.getStyleClass().add("background-circle");

        maxMwhArc = new Arc(centerX, centerX, radius, radius, 0, 360.0);
        maxMwhArc.getStyleClass().add("maxMwhArc");
        maxMwhArc.setType(ArcType.CHORD);

        currentMwh2015Arc = new Arc(centerX, centerX, radius2015, radius2015, +90, 180);
        currentMwh2015Arc.getStyleClass().add("currentMwh2015Arc");
        currentMwh2015Arc.setType(ArcType.OPEN);

        currentMwh2016Arc = new Arc(centerX, centerX, radius2016, radius2016, +90, 180);
        currentMwh2016Arc.getStyleClass().add("currentMwh2016Arc");
        currentMwh2016Arc.setType(ArcType.OPEN);

        currentMwh2017Arc = new Arc(centerX, centerX, radius2017, radius2017, +90, 180);
        currentMwh2017Arc.getStyleClass().add("currentMwh2017Arc");
        currentMwh2017Arc.setType(ArcType.OPEN);

        currentMwh2018Arc = new Arc(centerX, centerX, radius2018, radius2018, +90, 180);
        currentMwh2018Arc.getStyleClass().add("currentMwh2018Arc");

        thumb2015 = new Circle(centerX, centerX + centerX - width, 13);
        thumb2015.getStyleClass().add("thumb2015");
        thumb2016 = new Circle(centerX, centerX + centerX - width, 13);
        thumb2016.getStyleClass().add("thumb2016");
        thumb2017 = new Circle(centerX, centerX + centerX - width, 13);
        thumb2017.getStyleClass().add("thumb2017");
        thumb2018 = new Circle(centerX, centerX + centerX - width, 13);
        thumb2018.getStyleClass().add("thumb2018");

        operatingButton = createCenteredButton(centerX, centerY, "");
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void initializeAnimations() {
        //ToDo: alle deklarierten Animationen initialisieren
    }

    private void layoutParts() {
        //ToDo: alle Parts zur drawingPane hinzufügen
        drawingPane.getChildren()
                .addAll(backgroundCircle, maxMwhArc, currentMwh2015Arc, currentMwh2016Arc, currentMwh2017Arc,
                        currentMwh2018Arc, thumb2015, thumb2016, thumb2017, thumb2018, operatingButton);

        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {
        //ToDo: bei Bedarf ergänzen
        thumb2015.setOnMouseDragged(event -> {
            double newValue = radialMousePositionToValue(event.getX(), event.getY(), ARTBOARD_WIDTH * 0.5,
                    ARTBOARD_HEIGHT * 0.5, 0, getMaxMwh());
            setCurrentMwh2015(newValue);
        });

        thumb2016.setOnMouseDragged(event -> {
            double newValue = radialMousePositionToValue(event.getX(), event.getY(), ARTBOARD_WIDTH * 0.5,
                    ARTBOARD_HEIGHT * 0.5, 0, getMaxMwh());
            setCurrentMwh2016(newValue);
        });

        thumb2017.setOnMouseDragged(event -> {
            double newValue = radialMousePositionToValue(event.getX(), event.getY(), ARTBOARD_WIDTH * 0.5,
                    ARTBOARD_HEIGHT * 0.5, 0, getMaxMwh());
            setCurrentMwh2017(newValue);
        });

        thumb2018.setOnMouseDragged(event -> {
            double newValue = radialMousePositionToValue(event.getX(), event.getY(), ARTBOARD_WIDTH * 0.5,
                    ARTBOARD_HEIGHT * 0.5, 0, getMaxMwh());
            setCurrentMwh2018(newValue);
        });

        operatingButton.setOnMouseClicked(event -> {
            if (isOperating()) {
                setOperating(false);
            } else {
                setOperating(true);
            }
        });
    }

    private void setupValueChangeListeners() {
        //ToDo: durch die Listener auf die Properties des Custom Controls ersetzen
        currentMwh2015Property().addListener(((observable, oldValue, newValue) -> {
            double arcSize = valueToAngle(newValue.doubleValue(), 0, getMaxMwh());
            currentMwh2015Arc.setLength(-arcSize);

            Point2D p = pointOnCircle(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, (ARTBOARD_WIDTH * 0.5) - 16,
                    arcSize);
            thumb2015.setCenterX(p.getX());
            thumb2015.setCenterY(p.getY());
        }));

        currentMwh2016Property().addListener(((observable, oldValue, newValue) -> {
            double arcSize = valueToAngle(newValue.doubleValue(), 0, getMaxMwh());
            currentMwh2016Arc.setLength(-arcSize);

            Point2D p = pointOnCircle(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, (ARTBOARD_WIDTH * 0.5) - 16,
                    arcSize);
            thumb2016.setCenterX(p.getX());
            thumb2016.setCenterY(p.getY());
        }));

        currentMwh2017Property().addListener(((observable, oldValue, newValue) -> {
            double arcSize = valueToAngle(newValue.doubleValue(), 0, getMaxMwh());
            currentMwh2017Arc.setLength(-arcSize);

            Point2D p = pointOnCircle(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, (ARTBOARD_WIDTH * 0.5) - 16,
                    arcSize);
            thumb2017.setCenterX(p.getX());
            thumb2017.setCenterY(p.getY());
        }));

        currentMwh2018Property().addListener(((observable, oldValue, newValue) -> {
            double arcSize = valueToAngle(newValue.doubleValue(), 0, getMaxMwh());
            currentMwh2018Arc.setLength(-arcSize);

            Point2D p = pointOnCircle(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, (ARTBOARD_WIDTH * 0.5) - 16,
                    arcSize);
            thumb2018.setCenterX(p.getX());
            thumb2018.setCenterY(p.getY());
        }));

        operatingProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                operatingButton.getStyleClass().removeAll();
                operatingButton.getStyleClass().addAll("operationButtonOn");
            } else {
                operatingButton.getStyleClass().removeAll();
                operatingButton.getStyleClass().addAll("operationButtonOff");
            }
        });

    }

    private void setupBindings() {
        //ToDo: dieses Binding ersetzen
    }

    private void updateUI() {
        //ToDo : ergaenzen mit dem was bei einer Wertaenderung einer Status-Property im UI upgedated werden muss
    }

    private void performPeriodicTask() {
        //ToDo: ergaenzen mit dem was bei der getakteten Animation gemacht werden muss
        //normalerweise: den Wert einer der Status-Properties aendern
    }

    private void startClockedAnimation(boolean start) {

    }

    @Override protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    //ToDo: ueberpruefen ob dieser Resizing-Ansatz anwendbar ist.
    private void resize() {
        Insets padding = getPadding();
        double availableWidth = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math
                .max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            //ToDo: ueberpruefen ob die drawingPane immer zentriert werden soll (eventuell ist zum Beispiel linksbuendig angemessener)
            relocateDrawingPaneCentered();
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    private void relocateDrawingPaneCentered() {
        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
    }

    private void relocateDrawingPaneCenterBottom(double scaleY, double paddingBottom) {
        double visualHeight = ARTBOARD_HEIGHT * scaleY;
        double visualSpace = getHeight() - visualHeight;
        double y = visualSpace + (visualHeight - ARTBOARD_HEIGHT) * 0.5 - paddingBottom;

        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, y);
    }

    private void relocateDrawingPaneCenterTop(double scaleY, double paddingTop) {
        double visualHeight = ARTBOARD_HEIGHT * scaleY;
        double y = (visualHeight - ARTBOARD_HEIGHT) * 0.5 + paddingTop;

        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, y);
    }

    // Sammlung nuetzlicher Funktionen

    //ToDo: diese Funktionen anschauen und für die Umsetzung des CustomControls benutzen

    private void loadFonts(String... font) {
        for (String f : font) {
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    private void addStylesheetFiles(String... stylesheetFile) {
        for (String file : stylesheetFile) {
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    /**
     * Umrechnen einer Prozentangabe, zwischen 0 und 100, in den tatsaechlichen Wert innerhalb des angegebenen Wertebereichs.
     *
     * @param percentage Wert in Prozent
     * @param minValue   untere Grenze des Wertebereichs
     * @param maxValue   obere Grenze des Wertebereichs
     * @return value der akuelle Wert
     */
    private double percentageToValue(double percentage, double minValue, double maxValue) {
        return ((maxValue - minValue) * percentage) + minValue;
    }

    /**
     * Umrechnen des angegebenen Werts in eine Prozentangabe zwischen 0 und 100.
     *
     * @param value    der aktuelle Wert
     * @param minValue untere Grenze des Wertebereichs
     * @param maxValue obere Grenze des Wertebereichs
     * @return Prozentangabe des aktuellen Werts
     */
    private double valueToPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue);
    }

    /**
     * Berechnet den Winkel zwischen 0 und 360 Grad, 0 Grad entspricht "Nord", der dem value
     * innerhalb des Wertebereichs zwischen minValue und maxValue entspricht.
     *
     * @param value    der aktuelle Wert
     * @param minValue untere Grenze des Wertebereichs
     * @param maxValue obere Grenze des Wertebereichs
     * @return angle Winkel zwischen 0 und 360 Grad
     */
    private double valueToAngle(double value, double minValue, double maxValue) {
        return percentageToAngle(valueToPercentage(value, minValue, maxValue));
    }

    /**
     * Umrechnung der Maus-Position auf den aktuellen Wert.
     * <p>
     * Diese Funktion ist sinnvoll nur fuer radiale Controls einsetzbar.
     * <p>
     * Lineare Controls wie Slider müssen auf andere Art die Mausposition auf den value umrechnen.
     *
     * @param mouseX   x-Position der Maus
     * @param mouseY   y-Position der Maus
     * @param cx       x-Position des Zentrums des radialen Controls
     * @param cy       y-Position des Zentrums des radialen Controls
     * @param minValue untere Grenze des Wertebereichs
     * @param maxValue obere Grenze des Wertebereichs
     * @return value der dem Winkel entspricht, in dem die Maus zum Mittelpunkt des radialen Controls steht
     */
    private double radialMousePositionToValue(double mouseX, double mouseY, double cx, double cy, double minValue,
            double maxValue) {
        double percentage = angleToPercentage(angle(cx, cy, mouseX, mouseY));

        return percentageToValue(percentage, minValue, maxValue);
    }

    /**
     * Umrechnung eines Winkels, zwischen 0 und 360 Grad, in eine Prozentangabe.
     * <p>
     * Diese Funktion ist sinnvoll nur fuer radiale Controls einsetzbar.
     *
     * @param angle der Winkel
     * @return die entsprechende Prozentangabe
     */
    private double angleToPercentage(double angle) {
        return angle / 360.0;
    }

    /**
     * Umrechnung einer Prozentangabe, zwischen 0 und 100, in den entsprechenden Winkel.
     * <p>
     * Diese Funktion ist sinnvoll nur fuer radiale Controls einsetzbar.
     *
     * @param percentage die Prozentangabe
     * @return der entsprechende Winkel
     */
    private double percentageToAngle(double percentage) {
        return 360.0 * percentage;
    }

    /**
     * Berechnet den Winkel zwischen einem Zentrums-Punkt und einem Referenz-Punkt.
     *
     * @param cx x-Position des Zentrums
     * @param cy y-Position des Zentrums
     * @param x  x-Position des Referenzpunkts
     * @param y  y-Position des Referenzpunkts
     * @return winkel zwischen 0 und 360 Grad
     */
    private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx = deltaX / radius;
        double ny = deltaY / radius;
        double theta = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }

    /**
     * Berechnet den Punkt auf einem Kreis mit gegebenen Radius im angegebenen Winkel
     *
     * @param cX     x-Position des Zentrums
     * @param cY     y-Position des Zentrums
     * @param radius Kreisradius
     * @param angle  Winkel zwischen 0 und 360 Grad
     * @return Punkt auf dem Kreis
     */
    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
                cY + (radius * Math.cos(Math.toRadians(angle - 180))));
    }

    /**
     * Erzeugt eine Text-Instanz in der Mitte des CustomControls.
     * Der Text bleibt zentriert auch wenn der angezeigte Text sich aendert.
     *
     * @param styleClass mit dieser StyleClass kann der erzeugte Text via css gestyled werden
     * @return Text
     */
    private Button createCenteredButton(String styleClass) {
        return createCenteredButton(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
    }

    /**
     * Erzeugt eine Text-Instanz mit dem angegebenen Zentrum.
     * Der Text bleibt zentriert auch wenn der angezeigte Text sich aendert.
     *
     * @param cx         x-Position des Zentrumspunkt des Textes
     * @param cy         y-Position des Zentrumspunkt des Textes
     * @param styleClass mit dieser StyleClass kann der erzeugte Text via css gestyled werden
     * @return Text
     */
    private Button createCenteredButton(double cx, double cy, String styleClass) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setPrefWidth(67);
        button.setPrefHeight(67);
        button.setLayoutY(cy - button.getPrefHeight() / 2);
        button.setLayoutX(cx - button.getPrefHeight() / 2);

        return button;
    }

    /**
     * Erzeugt eine Group von Lines, die zum Beispiel fuer Skalen oder Zifferblaetter verwendet werden koennen.
     * <p>
     * Diese Funktion ist sinnvoll nur fuer radiale Controls einsetzbar.
     *
     * @param cx            x-Position des Zentrumspunkts
     * @param cy            y-Position des Zentrumspunkts
     * @param radius        radius auf dem die Anfangspunkte der Ticks liegen
     * @param numberOfTicks gewuenschte Anzahl von Ticks
     * @param startingAngle Wickel in dem der erste Tick liegt, zwischen 0 und 360 Grad
     * @param overallAngle  gewuenschter Winkel zwischen den erzeugten Ticks, zwischen 0 und 360 Grad
     * @param tickLength    Laenge eines Ticks
     * @param styleClass    Name der StyleClass mit der ein einzelner Tick via css gestyled werden kann
     * @return Group mit allen Ticks
     */
    private Group createTicks(double cx, double cy, double radius, int numberOfTicks, double startingAngle,
            double overallAngle, double tickLength, String styleClass) {
        Group group = new Group();

        double degreesBetweenTicks =
                overallAngle == 360 ? overallAngle / numberOfTicks : overallAngle / (numberOfTicks - 1);
        double innerRadius = radius - tickLength;

        for (int i = 0; i < numberOfTicks; i++) {
            double angle = startingAngle + i * degreesBetweenTicks;

            Point2D startPoint = pointOnCircle(cx, cy, radius, angle);
            Point2D endPoint = pointOnCircle(cx, cy, innerRadius, angle);

            Line tick = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
            tick.getStyleClass().add(styleClass);
            group.getChildren().add(tick);
        }

        return group;
    }

    private String colorToCss(final Color color) {
        return color.toString().replace("0x", "#");
    }

    // compute sizes

    @Override protected double computeMinWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override protected double computeMinHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override protected double computePrefWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override protected double computePrefHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // alle getter und setter  (generiert via "Code -> Generate... -> Getter and Setter)

    // ToDo: ersetzen durch die Getter und Setter Ihres CustomControls
    public Color getBaseColor() {
        return baseColor.get();
    }

    public StyleableObjectProperty<Color> baseColorProperty() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor.set(baseColor);
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

    public boolean isOperating() {
        return operating.get();
    }

    public BooleanProperty operatingProperty() {
        return operating;
    }

    public void setOperating(boolean operating) {
        this.operating.set(operating);
    }
}
