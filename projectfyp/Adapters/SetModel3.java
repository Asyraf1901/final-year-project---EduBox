package com.example.projectfyp.Adapters;

public class SetModel3 {
    private String setName3;

    public SetModel3(String setName) {
        if (setName == null || setName.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName3 = setName;
    }

    public String getSetName3() {
        return setName3;
    }

    public void setSetName3(String setName3) {
        if (setName3 == null || setName3.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName3 = setName3;
    }

    @Override
    public String toString() {
        return "SetModel3{" +
                "setName3='" + setName3 + '\'' +
                '}';
    }
}
