package ValueObjects;
 import Enum.*;
public class Parameter
    // Sub intrebare
{
    // Common size parameters
    private double width;
    private double height;
    private double depth;

    // Elevator-specific parameters
    // Changed to ElevatorDetails CLASS ?
    private Double carWidth; // Car means (interiorul)
    private Double carHeight;
    private Double carDepth;
    private Double doorWidth;
    private Double doorHeight;
    private Double hoistwayWidth; //The width of the shaft that houses the elevator
    private Double hoistwayDepth;
    private Speed speed; // ???
    private int maxPassengersLoad;
    private int maxWeightLoad;

}
