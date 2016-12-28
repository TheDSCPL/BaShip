package sharedlib.enums;

public enum UserStatus {
    Offline,
    Online,
    Waiting,
    Playing;
    
    public UserStatus getFromString(String s)
    {
        for(UserStatus us : UserStatus.values())
            if(us.name().equals(s))
                return us;
        return null;
    }
}
