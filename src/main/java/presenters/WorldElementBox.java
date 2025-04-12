package presenters;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import world_elements.WorldElement;
import world_elements.animals.Animal;

public class WorldElementBox {
    private final Label label;
    private final StackPane stackPane;

    public WorldElementBox(WorldElement worldElement) {
        Image image = new Image(String.format("/%s", worldElement.getImageName()));
        ImageView view = new ImageView(image);
        view.setFitHeight(20);
        view.setFitWidth(20);

        if (worldElement instanceof Animal) {
            double energyPercentage = ((Animal) worldElement).getEnergyPercentage();
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setHue(this.energyToHue(energyPercentage));
            colorAdjust.setSaturation(1);
            view.setEffect(colorAdjust);
        }
        this.label = new Label(worldElement.getVisualLabel());
        VBox vbox = new VBox(this.label, view);
        vbox.setAlignment(Pos.CENTER);

        this.stackPane = new StackPane(vbox);
    }

    public Label getLabel() {
        return label;
    }

    public StackPane getStackPane() {
        return this.stackPane;
    }

    private double energyToHue(double energyPercentage) {
        // Zakresy HUE:
        // Żółty (0,33) - Pomarańczowy (0,15) - Czerwony (0)
        return Math.max(0.0, 0.33 - energyPercentage * 0.18);
    }
}
