package com.dmm.ecommerceapp.utils;
import java.util.ArrayList;
import java.util.List;

public class FeedbackAndRatings {
    private List<String> feedback;
    private List<Double> ratings;

    public FeedbackAndRatings(List<String> feedback, List<Double> ratings) {
        this.feedback = feedback;
        this.ratings = ratings;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public List<Double> getRatings() {
        return ratings;
    }
}

