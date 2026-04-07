package com.backend.legisloop.enums;

/**
 * An enum to relate vote_id's from LegiScan to their string representation.
 */
public enum StateEnum {
	
	AK(2),
	AL(1),
	AR(4),
	AZ(3),
	CA(5),
	CO(6),
	CT(7),
	DE(8),
	FL(9),
	GA(10),
	HI(11),
	IA(15),
	ID(12),
	IL(13),
	IN(14),
	KS(16),
	KY(17),
	LA(18),
	MA(21),
	MD(20),
	ME(19),
	MI(22),
	MN(23),
	MO(25),
	MS(24),
	MT(26),
	NC(33),
	ND(34),
	NE(27),
	NH(29),
	NJ(30),
	NM(31),
	NV(28),
	NY(32),
	OH(35),
	OK(36),
	OR(37),
	PA(38),
	RI(39),
	SC(40),
	SD(41),
	TN(42),
	TX(43),
	UT(44),
	VA(46),
	VT(45),
	WA(47),
	WI(49),
	WV(48),
	WY(50),
    DC(51),
    US(52);

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
