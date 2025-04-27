package model;

import java.io.Serializable;

public interface Payable extends Serializable {
        double calculateTotalPrice();
    }

