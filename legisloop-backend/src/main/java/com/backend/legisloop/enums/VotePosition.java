package com.backend.legisloop.enums;

/**
 * An enum to relate vote_id's from LegiScan to their string representation.
 */
public enum VotePosition {
	
	YEA(1),
    NAY(2),
    NV(3),
    ABSENT(4);

    private final int id;
	
    VotePosition(int id)
	{
		this.id = id;
	}
    
    public int getID()
    {
        return id;
    }

    /**
     * From the vote_id, get the enum.
     * @param voteID from LegiScan
     * @return Enum, if the vote_id is valid
     */
    public static VotePosition fromVoteID(int voteID)
    {
        for (VotePosition voteType : values())
        {
            if (voteType.id == voteID)
            {
                return voteType;
            }
        }
        throw new IllegalArgumentException(voteID + " does not match any known vote ID");
    }
}
	
