package com.example.projectfyp.Adapters;

public class SetModel2 {
    private String setName2;

    public SetModel2(String setName2) {
        if (setName2 == null || setName2.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName2 = setName2;
    }

    public String getSetName() {
        return setName2;
    }

    public void setSetName(String setName2) {
        if (setName2 == null || setName2.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName2 = setName2;
    }

    @Override
    public String toString() {
        return "SetModel2{" +
                "setName2='" + setName2 + '\'' +
                '}';
    }
}
