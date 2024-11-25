package com.example.projectfyp.Adapters;

public class SetModel4 {
    private String setName4;

    public SetModel4(String setName4) {
        if (setName4 == null || setName4.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName4 = setName4;
    }

    public String getSetName4() {
        return setName4;
    }

    public void setSetName4(String setName4) {
        if (setName4 == null || setName4.isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be null or empty");
        }
        this.setName4 = setName4;
    }

    @Override
    public String toString() {
        return "SetModel4{" +
                "setName4='" + setName4 + '\'' +
                '}';
    }
}
