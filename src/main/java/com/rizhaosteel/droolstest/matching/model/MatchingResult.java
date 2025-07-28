package com.rizhaosteel.droolstest.matching.model;

import java.util.List;
import java.util.ArrayList;

public class MatchingResult {
    private Employee employee;
    private Position position;
    private int matchScore; // 匹配分数 (0-100)
    private boolean isMatch; // 是否匹配
    private List<String> matchReasons; // 匹配原因
    private List<String> mismatchReasons; // 不匹配原因
    private String recommendation; // 推荐建议
    
    public MatchingResult() {
        this.matchReasons = new ArrayList<>();
        this.mismatchReasons = new ArrayList<>();
        this.matchScore = 0;
        this.isMatch = false;
    }
    
    public MatchingResult(Employee employee, Position position) {
        this.employee = employee;
        this.position = position;
        this.matchReasons = new ArrayList<>();
        this.mismatchReasons = new ArrayList<>();
        this.matchScore = 0;
        this.isMatch = false;
    }
    
    // Getters and Setters
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    
    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { 
        this.matchScore = matchScore;
        this.isMatch = matchScore >= 60; // 60分以上认为匹配
    }
    
    public boolean isMatch() { return isMatch; }
    public void setMatch(boolean match) { this.isMatch = match; }
    
    public List<String> getMatchReasons() { return matchReasons; }
    public void setMatchReasons(List<String> matchReasons) { this.matchReasons = matchReasons; }
    
    public List<String> getMismatchReasons() { return mismatchReasons; }
    public void setMismatchReasons(List<String> mismatchReasons) { this.mismatchReasons = mismatchReasons; }
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public void addMatchReason(String reason) {
        this.matchReasons.add(reason);
    }
    
    public void addMismatchReason(String reason) {
        this.mismatchReasons.add(reason);
    }
    
    public void addScore(int score) {
        this.matchScore += score;
        if (this.matchScore > 100) {
            this.matchScore = 100;
        }
        this.isMatch = this.matchScore >= 60;
    }
}
