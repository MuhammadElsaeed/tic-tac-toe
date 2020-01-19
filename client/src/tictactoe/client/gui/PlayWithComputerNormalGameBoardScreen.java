/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.gui;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class PlayWithComputerNormalGameBoardScreen extends Pane {

    private final App app;
    Random rand;
    boolean turn, fullBoardFlag;
    int counter, cpupos;
    String line;
    Vector<Label> l = new Vector<>();
    boolean[] textLabelflag;
    private boolean isEnded = false;
    
    public PlayWithComputerNormalGameBoardScreen(App app) {
        this.app = app;
        setId("stackGameboard");
        for (int i = 0; i < 9; i++) {
            l.add(new Label("_"));
        }
        turn = true;
        rand = new Random();
        counter = 0;
        resetGame();
        GridPane stack = new GridPane();
        stack.setId("stack");
        stack.setPadding(new Insets(40, 0, 0, 50));
        stack.setHgap(150);
        stack.setVgap(-20);
        stack.setPrefSize(750, 700);
        checkWinner();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i * 3 + j;
                l.get(x).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (isEnded) {
                            return;
                        }
                        if (turn && textLabelflag[x]) {
                            l.get(x).setText("X");
                            l.get(x).setId("X");
                            turn = false;
                            textLabelflag[x] = false;
                            counter++;
                            checkWinner();
                        }
                        if (turn == false) {
                            cpu();
                        }
                        checkWinner();
                    }
                });
                stack.add(l.get(x), j, i);
            }
        }
        //
        setId("stackGameboard");
       Label label1 = new Label("PLAYER1");
        Label label2 = new Label("COMPUTER");

        HBox hbox = new HBox(385, label1, label2);
        hbox.setLayoutX(70);
        hbox.setLayoutY(25);

        TextArea ta = new TextArea(" ");
        ta.setId("ta");
        ta.setLayoutX(890);
        ta.setLayoutY(400);
        ta.setMaxWidth(220.0);
        ta.setMaxHeight(150.0);

        TextArea text = new TextArea("");
        text.setId("text");
        text.setPromptText("Enter your Msg ");
        text.setLayoutX(890);
        text.setLayoutY(600);
        text.setMaxWidth(220.0);
        text.setMaxHeight(10.5);

        Button send = new Button();
        send.setText("send");
        send.setId("send");
        send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                JsonObject response = new JsonObject();
                JsonObject data = new JsonObject();
                response.add("data", data);
                response.addProperty("type", "Message_sent");
                data.addProperty("msg", ta.getText());
                try {
                    app.getDataOutputStream().writeUTF(response.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            
        });
        send.setLayoutX(1140);
        send.setLayoutY(600);
        getChildren().addAll(stack, hbox, text, ta, send);
        stack.setId("stacklolo");
        if (!turn) {
            cpu();
        }
    }

    void cpu() {
        PauseTransition pause = new PauseTransition(Duration.seconds(.5));
        pause.setOnFinished((ActionEvent event) -> {
            if(counter == 1 && textLabelflag[4]){
                cpupos = 4;
            }
            else while (!textLabelflag[cpupos] && counter < 9) {
                cpupos = generateCpuPos();
            }
            if (turn == false && textLabelflag[cpupos]) {
                counter++;
                l.get(cpupos).setText("O");
                l.get(cpupos).setId("O");
                turn = true;
                textLabelflag[cpupos] = false;
                checkWinner();
            }
        });
        pause.play();
    }

    private int checkCpuPos(String line, int index1, int index2, int index3) {
        int index = 4;
        switch (line) {
            case "XX_":
                index = index3;
                break;
            case "X_X":
                index = index2;
                break;
            case "_XX":
                index = index1;
                break;
        }
        System.out.println(index);
        return index;
    }
    private int generateCpuPos() {
        String lineGenerator;
        int cpu = rand.nextInt(9);
        for (int x = 0; x < 8; x++) {
            lineGenerator = null;
            switch (x) {
                case 0:
                    lineGenerator = l.get(0).getText() + l.get(1).getText() + l.get(2).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 0, 1, 2);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 1:
                    lineGenerator = l.get(3).getText() + l.get(4).getText() + l.get(5).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 3, 4, 5);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 2:
                    lineGenerator = l.get(6).getText() + l.get(7).getText() + l.get(8).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 6, 7, 8);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 3:
                    lineGenerator = l.get(0).getText() + l.get(3).getText() + l.get(6).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 0, 3, 6);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 4:
                    lineGenerator = l.get(1).getText() + l.get(4).getText() + l.get(7).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 1, 4, 7);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 5:
                    lineGenerator = l.get(2).getText() + l.get(5).getText() + l.get(8).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 2, 5, 8);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 6:
                    lineGenerator = l.get(0).getText() + l.get(4).getText() + l.get(8).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 0, 4, 8);
                        System.out.println(lineGenerator + x);
                    }
                    break;
                case 7:
                    lineGenerator = l.get(2).getText() + l.get(4).getText() + l.get(6).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 2, 4, 6);
                        System.out.println(lineGenerator + x);
                    }
                    break;
            }
        }
        return cpu;
    }

    private void resetGame() {
        for (int i = 0; i < l.size(); i++) {
            l.get(i).setText("_");
            l.get(i).setId("label");
        }
        textLabelflag = new boolean[]{true, true, true, true, true, true, true, true, true};
        counter = 0;
        fullBoardFlag = true;
        isEnded = false;
        if (!turn) {
            cpu();
        }
    }

    private void checkWinner() {
        for (int x = 0; x < 8; x++) {
            line = null;
            switch (x) {
                case 0:
                    line = l.get(0).getText() + l.get(1).getText() + l.get(2).getText();

                    break;
                case 1:
                    line = l.get(3).getText() + l.get(4).getText() + l.get(5).getText();
                    break;
                case 2:
                    line = l.get(6).getText() + l.get(7).getText() + l.get(8).getText();
                    break;
                case 3:

                    line = l.get(0).getText() + l.get(3).getText() + l.get(6).getText();
                    break;
                case 4:
                    line = l.get(1).getText() + l.get(4).getText() + l.get(7).getText();
                    break;
                case 5:
                    line = l.get(2).getText() + l.get(5).getText() + l.get(8).getText();
                    break;
                case 6:
                    line = l.get(0).getText() + l.get(4).getText() + l.get(8).getText();
                    break;
                case 7:
                    line = l.get(2).getText() + l.get(4).getText() + l.get(6).getText();
                    break;
            }
            switch (line) {
                case "XXX": {
                    fullBoardFlag = false;
                    turn = true;
                    isEnded = true;
                    for (int i = 0; i < 9; i++) {
                        if (textLabelflag[i]) {
                            textLabelflag[i] = false;
                        }
                    }
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished((ActionEvent event) -> {
                        app.setScreen("youWin");
                        counter = 0;
                        resetGame();
                    });
                    pause.play();
                    return;
                }
                case "OOO": {
                    fullBoardFlag = false;
                    turn = true;
                    isEnded = true;
                    for (int i = 0; i < 9; i++) {
                        if (textLabelflag[i]) {
                            textLabelflag[i] = false;
                        }
                    }
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished((ActionEvent event) -> {
                        app.setScreen("hardLuck");
                        counter = 0;
                        resetGame();
                    });
                    pause.play();
                    return;
                }
            }
        }
        if (counter == 9 && fullBoardFlag) {
            
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished((ActionEvent event) -> {
                app.setScreen("nooneIsTheWinner");
                counter = 0;
                resetGame();
            });
            pause.play();
        }
    }
}