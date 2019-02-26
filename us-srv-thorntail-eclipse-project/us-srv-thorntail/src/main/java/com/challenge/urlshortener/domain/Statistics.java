package com.challenge.urlshortener.domain;

public class Statistics {

    private String timeTaken;

    public Statistics() {
    }

    public Statistics(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	@Override
    public String toString() {
        return "Statistics{timeTaken=[" + timeTaken + ']' + '}';
    }
}
