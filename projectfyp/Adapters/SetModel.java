package com.example.projectfyp.Adapters;

public class SetModel {
    private String setName;

    public SetModel(String setName) {
        if (setName == null || setName.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName = setName;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        if (setName == null || setName.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName = setName;
    }

    @Override
    public String toString() {
        return "SetModel{" +
                "setName='" + setName + '\'' +
                '}';
    }
}
