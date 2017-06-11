package models.bonus;

import models.CategorieTaille;

public class BonusDeTouche extends Bonus
{
	public BonusDeTouche(NomBonus origine_, String nom_, int bonus_)
	{
		super(origine_, nom_, bonus_);
	}
	
	public static BonusDeTouche bonusDeTaille(CategorieTaille categorieTaille)
	{
		int bonus = 0;
		
		switch(categorieTaille)
		{
		case Infime : bonus = 8; break;
		case Minuscule : bonus = 4; break;
		case TresPetit : bonus = 2; break;
		case Petit : bonus = 1; break;
		case Moyen : bonus = 0; break;
		case Grand : bonus = -1; break;
		case TresGrand : bonus = -2; break;
		case Gigantesque : bonus = -4; break;
		case Colossale : bonus = -8; break;
		}
		
		return new BonusDeTouche(NomBonus.Taille, "Bonus de taille", bonus);
	}
}
