import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalogueClock extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setId("anchor-pane");
        AnchorPane stackPane = new AnchorPane();
        stackPane.setId("stack-pane");
        stackPane.setLayoutX(200);
        stackPane.setLayoutY(100);
        Rectangle hr = new Rectangle(6,120);
        hr.setLayoutX(104);
        hr.setLayoutY(100);
        hr.setTranslateX(100);
        hr.setTranslateY(100);
        hr.setId("hr");
        Rectangle mn = new Rectangle(4,140);
        mn.setId("mn");
        mn.setLayoutX(98);
        mn.setLayoutY(97);
        mn.setTranslateX(100);
        mn.setTranslateY(100);
        Rectangle sc = new Rectangle(2,160);
        sc.setId("sc");
        sc.setLayoutY(100);
        sc.setLayoutX(102);
        sc.setTranslateY(100);
        sc.setTranslateX(100);
        Circle circle = new Circle(10);
        circle.setLayoutX(200);
        circle.setLayoutY(200);
        circle.setId("circle");
        stackPane.getChildren().addAll(hr,mn,sc,circle);
        Label clock = new Label("Analogue Minimalist Clock");
        clock.setId("clock");
        clock.setLayoutX(280);
        clock.setLayoutY(530);

        FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon.setId("close-icon");
        Label close = new Label("",closeIcon);
        close.setId("close");
        close.setLayoutY(5);
        close.setLayoutX(775);

        close.setOnMouseClicked(e -> primaryStage.close());


        anchorPane.setOnMousePressed(e ->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        anchorPane.setOnMouseDragged( e ->{
            primaryStage.setX(e.getScreenX() - xOffset);
            primaryStage.setY(e.getScreenY() - yOffset);
        });


        anchorPane.getChildren().addAll(stackPane,clock,close);
        Scene scene = new Scene(anchorPane,800,600);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        primaryStage.show();


        // determine the starting time
        Calendar calendar = GregorianCalendar.getInstance();
        final double seedSecondDegrees = calendar.get(Calendar.SECOND) * (360.0 / 60);
        final double seedMinuteDegree = (calendar.get(Calendar.MINUTE) + seedSecondDegrees / 360.0 ) * (360.0 / 60);
        final double seedHourDegree = (calendar.get(Calendar.HOUR) + seedMinuteDegree / 360.0) * (360.0 / 12);

        //define rotations to map the analogue to the current time
        final Rotate hourRotate = new Rotate(seedHourDegree);
        final Rotate minuteRotate = new Rotate(seedMinuteDegree);
        final Rotate secondRotate = new Rotate(seedSecondDegrees);
        hr.getTransforms().add(hourRotate);
        mn.getTransforms().add(minuteRotate);
        sc.getTransforms().add(secondRotate);

        // hour hand rotate twice a day
        final Timeline hourTime = new Timeline(
                new KeyFrame(
                        Duration.hours(12),
                        new KeyValue(
                                hourRotate.angleProperty(),
                                 360+seedHourDegree,
                                Interpolator.LINEAR
                        )
                )
        );

        // the minute hand rotate once an hour
        final Timeline minuteTime = new Timeline(
                new KeyFrame(
                        Duration.minutes(60),
                        new KeyValue(
                                minuteRotate.angleProperty(),
                                360+seedMinuteDegree,
                                Interpolator.LINEAR
                        )
                )
        );

        //the second hand rotates once a minute
        final Timeline secondTime = new Timeline(
                new KeyFrame(
                        Duration.seconds(60),
                        new KeyValue(
                                secondRotate.angleProperty(),
                                360+seedSecondDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // time never ends
        hourTime.setCycleCount(Animation.INDEFINITE);
        minuteTime.setCycleCount(Animation.INDEFINITE);
        secondTime.setCycleCount(Animation.INDEFINITE);

        // start the analogue clock
        hourTime.play();
        minuteTime.play();
        secondTime.play();

        stackPane.setRotate(180);
    }
    public static void main(String[] args){
        launch();
    }
}
