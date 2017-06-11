package models.bonus;

public class BonusToucheDegats extends Bonus
{
	private BonusDeTouche bTouche;
	private BonusAuxDegats bDegats;
	
	public BonusToucheDegats(NomBonus origine_, String nom_, int bonus_)
	{
		super(origine_, nom_, bonus_);
		
		bTouche = new BonusDeTouche(origine_, nom_, bonus_);
		bDegats = new BonusAuxDegats(origine_, nom_, bonus_);
	}

	public BonusDeTouche getbTouche()
	{
		return bTouche;
	}

	public void setbTouche(BonusDeTouche bTouche) 
	{
		this.bTouche = bTouche;
	}

	
	public BonusAuxDegats getbDegats() 
	{
		return bDegats;
	}

	public void setbDegats(BonusAuxDegats bDegats) 
	{
		this.bDegats = bDegats;
	}
}
