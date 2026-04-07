package com.backend.legisloop.enums;

/**
 * An enum to relate status_id's from LegiScan to their string representation.
 */
public enum StatusEnum {
	
	PreIntroduction(0),
	Introduced(1),
	Engrossed(2),
	Enrolled(3),
	Passed(4),
	Vetoed(5),
	Failed(6),
	Override(7),
	Chaptered(8),
	Refer(9),
	ReportPass(10),
	ReportDNP(11),
	Draft(12);

    private final int status_id;
	
    StatusEnum(int status_id)
	{
		this.status_id = status_id;
	}
    
    public int getStatusID()
    {
        return status_id;
    }

    /**
     * From the status_ud, get the enum.
     * @param StatusID from LegiScan
     * @return Enum, if the status_id is valid
     */
    public static StatusEnum fromStatusID(int statusID)
    {
        for (StatusEnum status : values())
        {
            if (status.status_id == statusID)
            {
                return status;
            }
        }
        throw new IllegalArgumentException(statusID + " does not match any known status_id");
    }
}
