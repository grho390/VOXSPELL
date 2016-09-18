package VoxspellApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import models.SpellingQuiz;
import models.Status;
import models.WordModel;

import java.util.ArrayList;

/**
 * Created by ratterz on 16/09/16.
 */
public class SpellingQuizScene {

    private SpellingQuiz _quiz = new SpellingQuiz();
    private WordModel _wordModel;

    //SCENE
    private Scene _mainScene;

    //PANES
    private VBox _mainLayout = new VBox();
    private HBox _textArea = new HBox();
    private HBox _statusArea = new HBox();
    private HBox _buttonArea = new HBox();
    private HBox _resultsArea = new HBox();

    //TEXT
    private TextField _inputText = new TextField();

    //BUTTONS
    private Button _submitButton = new Button("Submit");
    private Button _startQuizButton = new Button("Start Quiz");
    private Button _settingsButton = new Button("Settings");
    private Button _repeatButton = new Button("Repeat");
    private Button _nextWordButton = new Button("Next Word");

    //STORAGE
    private ArrayList<Circle> _circleList = new ArrayList<Circle>();
    private int _position = 0;

    /**
     * This is the constructor for the spelling quiz scene. This will call the set up gui method
     * so the gui is set up and also it will call a method to start a new spelling quiz. The wordModel
     * will be passed onto the new spelling quiz.
     * @param wordModel
     */
    public SpellingQuizScene(WordModel wordModel) {
        setUpGui();
        setUpEventHandelers();
        this._wordModel = wordModel;
    }

    private void setUpGui() {
        setUpStatusArea();
        setUpTextArea();
        setUpButtonArea();
        setUpResultsArea();
        _mainLayout.setPadding(new Insets(20));
        _mainLayout.getChildren().addAll(_statusArea,_resultsArea,_buttonArea,_textArea);

        _mainScene = new Scene(_mainLayout, 1040, 640);
        _mainScene.getStylesheets().add("VoxspellApp/LayoutStyles");
    }

    private void setUpTextArea() {
        _textArea.setSpacing(20);
        _textArea.setPadding(new Insets(20));

        _inputText.setMinWidth(840);
        _inputText.setMinHeight(50);
        _inputText.setText("Press Start Quiz To Start Your Quiz!!");
        _inputText.setStyle("-fx-font: 18 arial");
        _inputText.setDisable(true);

        _submitButton.setMinWidth(100);
        _submitButton.setMinHeight(50);
        _submitButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        _submitButton.setDisable(true);

        _textArea.getChildren().addAll(_inputText,_submitButton);
    }

    private void setUpStatusArea() {
        _statusArea.setSpacing(10);
        _statusArea.setPadding(new Insets(20));

        _startQuizButton.setMinWidth(120);
        _startQuizButton.setMinHeight(50);
        _startQuizButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _statusArea.getChildren().addAll(_startQuizButton);
    }

    private void setUpButtonArea() {
        _buttonArea.setSpacing(100);
        _buttonArea.setPadding(new Insets(50,200,50,175));
        _buttonArea.setPrefHeight(200);

        _settingsButton.setMinWidth(150);
        _settingsButton.setMinHeight(150);
        _settingsButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _nextWordButton.setMinWidth(150);
        _nextWordButton.setMinHeight(150);
        _nextWordButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _repeatButton.setMinWidth(150);
        _repeatButton.setMinHeight(150);
        _repeatButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");


        _buttonArea.getChildren().addAll(_repeatButton,_nextWordButton,_settingsButton);
    }

    private void setUpResultsArea() {
        _resultsArea.setSpacing(20);
        _resultsArea.setPadding(new Insets(50));
        _resultsArea.setPrefHeight(100);
        _resultsArea.setAlignment(Pos.CENTER);

        createCircles();

        _resultsArea.getChildren().addAll(_circleList);


    }

    private void createCircles() {
        for (int i = 0; i < Voxspell.COUNT; i++) {
            Circle circle = new Circle(20);
            circle.setStyle("-fx-fill: #c2c2c2;");
            _circleList.add(circle);
        }
    }

    private void updateCircle(Status status) {
        if (status.equals(Status.Mastered)) {
            _circleList.get(_position).setStyle("-fx-fill: rgb(90,175,90);");
            _position++;
        } else if (status.equals(Status.Faulted)) {
            _circleList.get(_position).setStyle("-fx-fill: rgb(230,160,40);");
            _position++;
        } else if (status.equals(Status.Failed)) {
            _circleList.get(_position).setStyle("-fx-fill: rgb(225,100,50);");
            _position++;
        }
    }

    private void setUpEventHandelers() {
        _inputText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    String text = _inputText.getText();
                    _inputText.clear();
                    _quiz.spellingLogic(text);
                    updateCircle(_quiz.getStatus());
                }
            }
        });

        _submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = _inputText.getText();
                _inputText.clear();
                _quiz.spellingLogic(text);
                updateCircle(_quiz.getStatus());
            }
        });

        _startQuizButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _startQuizButton.setDisable(true);
                _inputText.setDisable(false);
                _inputText.clear();
                _submitButton.setDisable(false);
                _quiz.setUpSpellingQuiz(_wordModel);
            }
        });
    }

    public Scene createScene() {
        return this._mainScene;
    }

}