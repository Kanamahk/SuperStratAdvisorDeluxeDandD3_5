package models.bonus;

public abstract class Bonus 
{
	private NomBonus origine;
	private String nom;
	private int bonus;
	
	public Bonus(NomBonus origine_, String nom_, int bonus_)
	{
		this.origine = origine_;
		this.nom = nom_;
		this.bonus = bonus_;
	}

	public NomBonus getOrigine() 
	{
		return origine;
	}

	public void setOrigine(NomBonus origine)
	{
		this.origine = origine;
	}

	
	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom) 
	{
		this.nom = nom;
	}

	
	public int getBonus() 
	{
		return bonus;
	}

	public void setBonus(int bonus)
	{
		this.bonus = bonus;
	}
}
