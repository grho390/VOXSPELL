package VoxspellApp;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Festival;
import models.WordModel;

/**
 * Created by edson on 15/09/16.
 *
 * responsible for creating and handling interaction with the initial window of the game.
 */
public class InitialScene {
    private Stage _window;
    private Scene _mainScene;//background scene of primary window
    private BorderPane _mainLayout;

    //Buttons
    private ToggleGroup _menuGroup;
    private ToggleButton _newGameButton;
    private ToggleButton _reviewGameButton;
    private ToggleButton _statisticsButton;
    private ToggleButton _resetButton;
    private Button playButton;


    private int _level;
    private Mode _mode = Mode.NEW;
    protected enum Mode{NEW, REVIEW};

    private WordModel _model;

    private ComboBox _voiceOptionCombo;

    public InitialScene(Stage window, WordModel model){
        _model = model;
        _window = window;

        playButton = new Button("PLAY");
        playButton.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;");

        VBox menuSceneLayout = setMenuScene();//sets the left-hand side menu panel
        GridPane gameSceneLayout = setGameScene();//sets the right-hand side main

        //set all scenes into the main scene
        _mainLayout = new BorderPane();
        _mainLayout.setLeft(menuSceneLayout);
        _mainLayout.setCenter(gameSceneLayout);
        //mainLayout.setRight()

        BackgroundImage menuBackground = new BackgroundImage(new Image("MediaResources/background.png", 1040, 640, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        _mainLayout.setBackground(new Background(menuBackground));

        _mainScene = new Scene(_mainLayout, 1040, 640);
        _mainScene.getStylesheets().add("VoxspellApp/LayoutStyles");//add the css style-sheet to the main menu scene



        setupEventHandlers();

    }

    protected Scene createScene(){
        return _mainScene;
    }
    /**
     * Logic for setting up the left-side menu scene (specific for main entry menu only).
     * @return menu vbox layout
     */
    private VBox setMenuScene(){
        VBox menuSceneLayout = new VBox();
        menuSceneLayout.setPrefWidth(150);//set width of menu buttons
        _menuGroup = new ToggleGroup();
        //http://docs.oracle.com/javafx/2/ui_controls/button.htm
        _newGameButton = createMenuButtons("MediaResources/newGame.png", "New Game");
        _reviewGameButton = createMenuButtons("MediaResources/newGame.png", "Review Game");
        _statisticsButton = createMenuButtons("MediaResources/newGame.png", "Statistics");
        _resetButton = createMenuButtons("MediaResources/newGame.png", "Reset");

        menuSceneLayout.setPadding(new Insets(20, 10, 10, 20));//insets: top right bottom left
        menuSceneLayout.getChildren().addAll(_newGameButton, _reviewGameButton, _statisticsButton, _resetButton);
        menuSceneLayout.getStyleClass().add("vbox");//add the custom vbox layout style


        return menuSceneLayout;
    }



    /**
     * Create menu buttons for the menu scene
     * @param imageName image filepath
     * @param caption button caption
     * @return button node
     */
    private ToggleButton createMenuButtons(String imageName, String caption){
        Image newGameIcon = new Image(imageName, 120, 100, false, true);//size of image
        ToggleButton newButton = new ToggleButton(caption, new ImageView(newGameIcon));
        newButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        newButton.setContentDisplay(ContentDisplay.TOP);
        newButton.setToggleGroup(_menuGroup);
        if (caption.equals("New Game")){
            newButton.setSelected(true);
        }
        return newButton;
    }



    /**
     * Logic for the main game scene of the main entry window
     * thinking of reusing for settings popup window
     * @return main game scene as a gridPane
     */
    //we may want to reuse this for settings page
    private GridPane setGameScene(){
        GridPane gameGrid = new GridPane();
        gameGrid.setPadding(new Insets(20,20,20,50));
        gameGrid.setVgap(40);
        gameGrid.setHgap(10);

        Label levelLabel = new Label("Level");
        levelLabel.setStyle("-fx-font: 22 arial;");
        GridPane.setConstraints(levelLabel, 0, 0);

        ToggleGroup levelToggles = setLevelButtons(_model.getTotalLevels(), gameGrid);

        Label voiceLabel = new Label("Voice");
        voiceLabel.setStyle("-fx-font: 22 arial;");
        GridPane.setConstraints(voiceLabel, 0, 1);

        //set up combo box for choosing levels
        _voiceOptionCombo = new ComboBox<String>();
        _voiceOptionCombo.getItems().addAll(
                Festival.getVoiceList()
        );
        _voiceOptionCombo.setStyle("-fx-font: 22 arial;");
        _voiceOptionCombo.setValue("kal_diphone");
        GridPane.setConstraints(_voiceOptionCombo, 1, 1);

        gameGrid.getChildren().addAll(levelLabel, voiceLabel, _voiceOptionCombo);

        GridPane.setConstraints(playButton, 0, 3);
        gameGrid.getChildren().add(playButton);
        return gameGrid;

    }

    public Button getPlayButton(){
        return playButton;
    }

    private ToggleGroup setLevelButtons(int maxLevel, GridPane gameGrid){
        HBox levelHBox = new HBox();
        levelHBox.setSpacing(5);
        ToggleGroup levelGroup = new ToggleGroup();
        for (int i = 1; i <maxLevel+1 ; i++){
            ToggleButton levelButton = new ToggleButton("" + i);
            levelButton.setUserData(i);
            levelButton.setStyle("-fx-font: 22 arial;");
            //upon button click, update model's level
            levelButton.setOnAction(e->{
                _model.updateLevel(Integer.parseInt(levelButton.getText()));
            });

            if (i ==1){
                levelButton.setSelected(true);
                _model.updateLevel(1);
            }
            //disable if user has no access to level
            //if (i > _model.getAccessLevel()){
            //    levelButton.setDisable(true);
            //}

            levelButton.setToggleGroup(levelGroup);
            levelHBox.getChildren().add(levelButton);

        }
        levelGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue==null){
                    _level=1;//default level
                } else {
                    _level = (int) levelGroup.getSelectedToggle().getUserData();//set level via button select
                }
            }
        });
        GridPane.setConstraints(levelHBox, 1, 0);
        gameGrid.getChildren().add(levelHBox);
        return levelGroup;
    }

    private void setupEventHandlers(){
        _newGameButton.setOnAction(event -> {
            _mainLayout.setCenter(setGameScene());
            _mode=Mode.NEW;
        });
        _reviewGameButton.setOnAction(event -> {
            _mainLayout.setCenter(setGameScene());
            _mode=Mode.REVIEW;
        });
        _statisticsButton.setOnAction(event -> {
            StatisticsScene graphScene = new StatisticsScene(_model);
            _mainLayout.setCenter(graphScene.createScene());//set center pane to the StatisticsScene's layout node
        });
        _resetButton.setOnAction(event -> {
            final VBox resetVbox = new VBox(20);
            resetVbox.setPadding(new Insets(40,50,40,40));
            resetVbox.setAlignment(Pos.TOP_CENTER);
            Label title = new Label("Clear History");
            Image resetImage = new Image("MediaResources/newGame.png", 150, 150, false, true);
            ImageView rsImageContainer = new ImageView(resetImage);
            Label caption1 = new Label("Clearing the history will remove all history statistics.");
            Label caption2 = new Label("Your highest level will be reset to level 1.");
            Label caption3 = new Label("Are you sure you want to clear the history?");
            final Label caption4 = new Label("History Successfully Cleared.");
            caption4.setVisible(false);
            Button confirmButton = new Button("Clear History");

            confirmButton.setOnAction(e->{
                _model.recreate();
                caption4.setVisible(true);
            });
            resetVbox.getChildren().addAll(title,  rsImageContainer,caption1,caption2,caption3, confirmButton, caption4);
            _mainLayout.setCenter(resetVbox);
        });
        playButton.setOnAction(event ->{
            SpellingQuizScene newGameSceneCreator = new SpellingQuizScene(_model, _window);
            Scene newGameScene = newGameSceneCreator.createScene();
            _window.setScene(newGameScene);
        });
        _voiceOptionCombo.setOnAction(event -> {
            String option = (String)_voiceOptionCombo.getValue();
            Festival.changeVoice(option);
        });
    }




}
