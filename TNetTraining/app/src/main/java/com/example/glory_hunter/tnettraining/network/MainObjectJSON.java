package com.example.glory_hunter.tnettraining.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glory-hunter on 17/01/2018.
 */

public class MainObjectJSON {
    ArrayList<ResultJSON> results;


    public MainObjectJSON(ArrayList<ResultJSON> results) {
        this.results = results;
    }

    public ArrayList<ResultJSON> getResults() {
        return results;
    }

    public class ResultJSON {
        private String formatted_address;

        public ResultJSON(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getFormatted_address() {
            return formatted_address;
        }
    }
}
