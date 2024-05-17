package com.homegravity.Odi.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class MapResponseDTO {
    private int code;
    private String message;
    private LocalDateTime currentDateTime;
    private MapRouteDTO route;

    @Getter
    @Setter
    @ToString
    public static class MapRouteDTO {
        private List<Traoptimal> traoptimal;

        @Getter
        @Setter
        @ToString
        public static class Traoptimal {
            private Summary summary;
            private List<double[]> path;
            private List<Section> section;
            private List<Guide> guide;

            @Getter
            @Setter
            @ToString
            public static class Summary {
                private Location start;
                private Goal goal;
                private int distance;
                private long duration;
                private int etaServiceType;
                private String departureTime;
                private double[][] bbox;
                private int tollFare;
                private int taxiFare;
                private int fuelPrice;

                @Getter
                @Setter
                @ToString
                public static class Location {
                    private double[] location;
                }

                @Getter
                @Setter
                @ToString
                public static class Goal {
                    private double[] location;
                    private int dir;
                }
            }

            @Getter
            @Setter
            @ToString
            public static class Section {
                private int pointIndex;
                private int pointCount;
                private int distance;
                private String name;
                private int congestion;
                private int speed;
            }

            @Getter
            @Setter
            @ToString
            public static class Guide {
                private int pointIndex;
                private int type;
                private String instructions;
                private int distance;
                private long duration;
            }
        }
    }
}

