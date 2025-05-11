package com.gdg.planpal.domain.gemini.functionCall.Spot;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"explain", "tourSpot"})
public record TourSpot(String explain, List<String> tourSpot) {}
