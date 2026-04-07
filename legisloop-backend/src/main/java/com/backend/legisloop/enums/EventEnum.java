package com.backend.legisloop.enums;

/**
 * An enum to classify a vote and a sponsor event when serving to the frontend
 */
public enum EventEnum {
	
	VOTE(1),
    SPONSORED(2);
	
    private final int id;
	
    EventEnum(int id)
	{
		this.id = id;
	}
    
    public int getID()
    {
        return id;
    }

    /**
     * From the id, get the enum.
     * @param int id
     * @return Enum, if the id is valid
     */
    public static EventEnum fromID(int id)
    {
        for (EventEnum type : values())
        {
            if (type.id == id)
            {
                return type;
            }
        }
        throw new IllegalArgumentException(id + " does not match any known event enum ID");
    }
}
	
