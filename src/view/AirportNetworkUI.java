package view;

import controller.AirportNetworkController;
import observer.Observer;

public interface AirportNetworkUI extends Observer {


    String getNameAirportAdd();
    String getIATAAirportAdd();
    String getXadd();
    String getYadd();
    String getCombo1();
    String getCombo2();
    String getDistanceAdd();

    void setTxtPathResult(String result);
    void displayError(String msg);
    void clearError();
    void clearControls();

    void setTriggers(AirportNetworkController controller);
}
