package be.webtechie.resistors.views;

import be.webtechie.resistorcalculator.definition.ColorCode;
import be.webtechie.resistorcalculator.definition.ColorValue;
import be.webtechie.resistorcalculator.util.CalculateColorValue;
import be.webtechie.resistorcalculator.util.Convert;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class BandCalculator extends VBox {

    private final ComboBox<ColorCode> band1;
    private final ComboBox<ColorCode> band2;
    private final ComboBox<ColorCode> band3;
    private final ComboBox<ColorCode> band4;
    private final ComboBox<ColorCode> band5;
    private final ComboBox<ColorCode> band6;

    private final Label result;

    public BandCalculator() {
        setSpacing(10);

        Label title = new Label("Resistor value calculator (3, 4, 5 or 6 bands)");
        getChildren().add(title);

        HBox colorSelection = new HBox();
        colorSelection.setSpacing(10);
        getChildren().add(colorSelection);

        band1 = new ComboBox<>();
        band1.setCellFactory(cellFactory);
        band1.getItems().setAll(ColorCode.values());
        band1.setOnAction(this::calculateValue);
        band2 = new ComboBox<>();
        band2.setCellFactory(cellFactory);
        band2.getItems().setAll(ColorCode.values());
        band2.setOnAction(this::calculateValue);
        band3 = new ComboBox<>();
        band3.setCellFactory(cellFactory);
        band3.getItems().setAll(ColorCode.values());
        band3.setOnAction(this::calculateValue);
        band4 = new ComboBox<>();
        band4.setCellFactory(cellFactory);
        band4.getItems().setAll(ColorCode.values());
        band4.setOnAction(this::calculateValue);
        band5 = new ComboBox<>();
        band5.setCellFactory(cellFactory);
        band5.getItems().setAll(ColorCode.values());
        band5.setOnAction(this::calculateValue);
        band6 = new ComboBox<>();
        band6.setCellFactory(cellFactory);
        band6.getItems().setAll(ColorCode.values());
        band6.setOnAction(this::calculateValue);

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(this::clear);

        colorSelection.getChildren().addAll(band1, band2, band3,
                band4, band5, band6, clearButton);

        result = new Label();
        getChildren().add(result);
    }

    Callback<ListView<ColorCode>, ListCell<ColorCode>> cellFactory = new Callback<ListView<ColorCode>, ListCell<ColorCode>>() {
        @Override
        public ListCell<ColorCode> call(ListView<ColorCode> l) {
            return new ListCell<ColorCode>() {
                @Override
                protected void updateItem(ColorCode item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        HBox holder = new HBox();
                        holder.setSpacing(10);
                        setGraphic(holder);

                        VBox colorBox = new VBox();
                        colorBox.setMinWidth(20);
                        if (item.getColor() != null) {
                            colorBox.setStyle(
                                    "-fx-background-color: " + Convert.toHexColorString(item.getColor()) + ";");
                        }
                        holder.getChildren().add(colorBox);

                        holder.getChildren().add(new Label(item.name()));
                    }
                }
            } ;
        }
    };

    private void calculateValue(ActionEvent actionEvent) {
        List<ColorCode> colors = new ArrayList<>();

        setAllComboBoxColors();

        if (band1.getValue() != null) {
            colors.add(band1.getValue());
        } else {
            return;
        }

        if (band2.getValue() != null) {
            colors.add(band2.getValue());
        } else {
            return;
        }

        if (band3.getValue() != null) {
            colors.add(band3.getValue());
        } else {
            return;
        }

        if (band4.getValue() != null) {
            colors.add(band4.getValue());
        } else {
            calculateValue(colors);
        }

        if (band5.getValue() != null) {
            colors.add(band5.getValue());
        } else {
            calculateValue(colors);
        }

        if (band6.getValue() != null) {
            colors.add(band6.getValue());
            calculateValue(colors);
        } else {
            calculateValue(colors);
        }
    }

    private void setAllComboBoxColors() {
        setComboBoxColor(band1);
        setComboBoxColor(band2);
        setComboBoxColor(band3);
        setComboBoxColor(band4);
        setComboBoxColor(band5);
        setComboBoxColor(band6);
    }

    private void setComboBoxColor(ComboBox<ColorCode> comboBox) {
        if (comboBox.getValue() == null || comboBox.getValue().getColor() == null) {
            comboBox.setStyle("-fx-border-width: 0px;");
        } else {
            comboBox.setStyle("-fx-border-width: 0 0 10px 0; -fx-border-color: "
                    + Convert.toHexColorString(comboBox.getValue().getColor()) + ";");
        }
    }

    private void calculateValue(List<ColorCode> colors) {
        try {
            ColorValue value = CalculateColorValue.from(colors);

            result.setText(
                    "Resistor value is "
                            + Convert.toOhmString(value.getOhm())
                            + (value.getTolerance() == 0 ? "" : " with tolerance " + value.getTolerance() + "%")
                            + (value.getTemperatureCoefficient() == null ? ""
                            : ", temperature coefficient " + value.getTemperatureCoefficient() + "ppm/K")
            );
        } catch (IllegalArgumentException ex) {
            result.setText(ex.getMessage());
        }
    }

    private void clear(ActionEvent actionEvent) {
        band1.setValue(null);
        band2.setValue(null);
        band3.setValue(null);
        band4.setValue(null);
        band5.setValue(null);
        band6.setValue(null);

        setAllComboBoxColors();

        result.setText("");
    }
}
