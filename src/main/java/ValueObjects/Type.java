package ValueObjects;

public class Type
{
    public enum ElevatorType
    {
        PASSENGER,
        FREIGHT,
        SERVICE,
        PANORAMIC,
        HOME
    }

    public enum AirSystemType
    {
        HVAC,
        AIR_CONDITIONER,
        AIR_PURIFIER,
        VENTILATION
    }

    public enum LampType
    {
        LED,
        FLUORESCENT,
        INCANDESCENT,
        HALOGEN,
        SMART
    }

    public enum EmergencySystemType
    {
        FIRE_ALARM,
        EMERGENCY_PHONE,
        EMERGENSY_STOP_BUTTON,
        AUTOMATIC_RESCUE_DEVICE,
        EARTHQUAKE_DETECTION_SYSTEM,
        OVERSPEED_GOVERNOR,
        VISUAL_AUDIABLE_INDCATORS
    }

    public enum MirrorType
    {
        WALL_MIRROR,
        VANITY_MIRROR,
        FULL_LENGTH_MIRROR,
        DECORATIVE_MIRROR
    }
}