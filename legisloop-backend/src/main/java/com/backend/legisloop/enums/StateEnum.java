package com.backend.legisloop.enums;

/**
 * An enum to relate vote_id's from LegiScan to their string representation.
 */
public enum StateEnum {
	
	YEA(1),
    NAY(2),
    NV(3),
    ABSENT(4);

    private final int state_id;
	
    StateEnum(int state_id)
	{
		this.state_id = state_id;
	}
    
    public int getStateID()
    {
        return state_id;
    }

    /**
     * From the vote_id, get the enum.
     * @param StateID from LegiScan
     * @return Enum, if the vote_id is valid
     */
    public static StateEnum fromStateID(int stateID)
    {
        for (StateEnum state : values())
        {
            if (state.state_id == stateID)
            {
                return state;
            }
        }
        throw new IllegalArgumentException(stateID + " does not match any known state_id");
    }
}
	
