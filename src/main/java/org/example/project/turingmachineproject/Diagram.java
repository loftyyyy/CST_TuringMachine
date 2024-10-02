package org.example.project.turingmachineproject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diagram {
    private Map<String, StateNode> states = new HashMap<>();
    private String currentState;

    private String finalState = "q5";

    public Diagram(){

        states.put("q0", new StateNode(false, "q0",138,198.5));
        states.put("q1", new StateNode(false, "q1", 336, 198.5));
        states.put("q2", new StateNode(false, "q2", 534, 198.5));
        states.put("q3", new StateNode(false, "q3", 732, 198.5));
        states.put("q4", new StateNode(false,"q4", 732,358.5));
        states.put("q5", new StateNode(true,"q5",138, 358.5));

        this.currentState = "q0";

    }
    public StateNode getState(String stateName) {
        return states.get(stateName);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String state) {
        currentState = state;
    }

    public String getFinalState(){
        return finalState;
    }

    public void resetToStartState(){
        setCurrentState("q0");

    }


    public List<StateNode> getAllStates(){
        return new ArrayList<>(states.values());



    }
}
