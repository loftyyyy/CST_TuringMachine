package org.example.project.turingmachineproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class TuringMachineLogic {
    private List<StackPane> tapeCells = new ArrayList<>();
    private int currentIndex = 0;
    private HBox tape;
    private ScrollPane scrollPane;
    private Timeline timeline;
    private Diagram stateDiagram;
    private TransitionLines transitionLines;
    private Pane diagram;

    public TuringMachineLogic(HBox tape, ScrollPane scrollPane, Pane diagram, Diagram stateDiagram) {
        this.tape = tape;
        this.scrollPane = scrollPane;
        this.stateDiagram = stateDiagram;
        this.diagram = diagram;

        transitionLines = new TransitionLines(diagram, 30);


        this.diagram.getChildren().addAll(stateDiagram.getState("q0"),stateDiagram.getState("q1"), stateDiagram.getState("q2"), stateDiagram.getState("q3"), stateDiagram.getState("q4"), stateDiagram.getState("q5"));

        //Initial Line
        transitionLines.addInitialStateArrow(stateDiagram.getState("q0"));

        // Linear Line
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q0"), stateDiagram.getState("q1"), "0, X / R");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q1"), stateDiagram.getState("q2"), "C, C / R");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q2"), stateDiagram.getState("q3"), "B, 0 / L");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q3"), stateDiagram.getState("q4"), "C, C / L");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q0"), stateDiagram.getState("q5"), "C, B / R");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q4"), stateDiagram.getState("q0"), "X, X / R");

        // Loop Lines
        transitionLines.addLoopArrow(stateDiagram.getState("q1"), 30,50, "0, 0 / R", 20000);
        transitionLines.addLoopArrow(stateDiagram.getState("q2"), 30,50, "0, 0 / R", 20000);
        transitionLines.addLoopArrow(stateDiagram.getState("q3"), 30,50, "0, 0 / L", 20000);
        transitionLines.addLoopArrowBelow(stateDiagram.getState("q4"), 30,50, "0, 0 / L", 20000);





    }

    public void createInputTape(String input, String input2) {
        char[] inputChars = input.toCharArray();
        char[] input2Chars = input.toCharArray();

        // Generate Empty Cells
        for (int i = 0; i < 4; i++) {
            StackPane emptyCell = createTapeCell(' ');
            tapeCells.add(emptyCell);
            tape.getChildren().add(emptyCell);
        }
        for (char s : inputChars) {
            StackPane cell = createTapeCell(s);
            tapeCells.add(cell);
            tape.getChildren().add(cell);
        }
        StackPane separator = createTapeCell('+');
        tapeCells.add(separator);
        tape.getChildren().add(separator);

        for (char s : input2Chars) {
            StackPane cell = createTapeCell(s);
            tapeCells.add(cell);
            tape.getChildren().add(cell);
        }
        // Generate Empty Cells
        for (int i = 0; i < 4; i++) {
            StackPane emptyCell = createTapeCell(' ');
            tapeCells.add(emptyCell);
            tape.getChildren().add(emptyCell);
        }
    }

    public StackPane createTapeCell(char symbol) {
        StackPane cell = new StackPane();
        Rectangle rect = new Rectangle(90, 90);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        Label label = new Label(Character.toString(symbol));
        cell.getChildren().addAll(rect, label);
        return cell;
    }

    // Automatically move the cursor to the next cell in a given interval
    public void startAutomaticCursorMovement() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            moveCursorRight();
        }));
        timeline.setCycleCount(tapeCells.size());
        timeline.play();
    }

    public void stopAutomaticCursorMovement() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void moveCursorRight() {
        // Reset the border of the previous cell
        if (currentIndex > 0) {
            StackPane prevCell = tapeCells.get(currentIndex - 1);
            Rectangle prevRect = (Rectangle) prevCell.getChildren().get(0);
            prevRect.setStroke(Color.BLACK);  // Reset to default black border
            prevRect.setStrokeWidth(1);
        }

        // Highlight the current cell
        StackPane currentCell = tapeCells.get(currentIndex);
        Rectangle currentRect = (Rectangle) currentCell.getChildren().get(0);
        currentRect.setStroke(Color.RED);  // Change border color to red to highlight
        currentRect.setStrokeWidth(2);

        executeLogicAndMoveCursor();



        // Scroll to the current index
        scrollToCurrentIndex();

        // Increment the index and stop if it's the last cell
        if (currentIndex < tapeCells.size() - 1) {
            currentIndex++;
        } else {
            stopAutomaticCursorMovement();
        }
    }

    private void executeLogicAndMoveCursor(){
        int indexToUpdate = this.currentIndex;
        String currentInput = readCurrentInput();

        if(readCurrentInput().equals("1")){
            writeToCurrentCell('0', currentIndex);
        }



    }

    private String readCurrentInput() {
        StackPane currentCell = tapeCells.get(currentIndex);
        Label currentLabel = (Label) currentCell.getChildren().get(1);  // Label is at index 1 in StackPane
        return currentLabel.getText();
    }

    public void writeToCurrentCell(char newSymbol, int index) {
        Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            StackPane currentCell = tapeCells.get(index);
            Label currentLabel = (Label) currentCell.getChildren().get(1);  // Label is at index 1 in StackPane
            currentLabel.setText(Character.toString(newSymbol));  // Update the symbol in the current cell
            System.out.println("Wrote " + newSymbol + " to the current cell.");
        }));
        delayTimeline.setCycleCount(1);  // Run only once
        delayTimeline.play();

    }

    private void scrollToCurrentIndex() {
        double totalWidth = tape.getWidth();
        double cellWidth = 90;  // Assuming the width of each cell is 90
        double scrollPos = (currentIndex * cellWidth) / totalWidth;
        scrollPane.setHvalue(scrollPos);  // Scroll horizontally to the current index
    }
}
