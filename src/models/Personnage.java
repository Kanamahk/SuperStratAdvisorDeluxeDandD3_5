package models;

import java.util.ArrayList;
import java.util.Hashtable;

import models.bonus.*;
import models.attaque.*;

public class Personnage 
{
	private String nom;
	private int bba, force, dexterite, constitution, intelligence, sagesse, charisme;
	private CategorieTaille categorieTaille;
	
	private Hashtable<String, BonusDeCarac> bFor;
	private Hashtable<String, BonusDeCarac> bDex;
	private Hashtable<String, BonusDeCarac> bCon;
	private Hashtable<String, BonusDeCarac> bInt;
	private Hashtable<String, BonusDeCarac> bSag;
	private Hashtable<String, BonusDeCarac> bChar;
	private Hashtable<String, BonusChant> bChant;
	private Hashtable<String, BonusDeTouche> bTouche;
	private Hashtable<String, BonusAuxDegats> bDegats;
	private BonusDeTouche bTTaille;
	
	private ArrayList<Attaque> attaques;

	public Personnage(String nom, int bba, int force, int dexterite, int constitution, int intelligence, int sagesse, int charisme, CategorieTaille categorieTaille)
	{
		super();
		this.nom = nom;
		this.bba = bba;
		this.force = force;
		this.dexterite = dexterite;
		this.constitution = constitution;
		this.intelligence = intelligence;
		this.sagesse = sagesse;
		this.charisme = charisme;
		this.categorieTaille = categorieTaille;
		
		bFor = new Hashtable<String, BonusDeCarac>();
		bDex = new Hashtable<String, BonusDeCarac>();
		bCon = new Hashtable<String, BonusDeCarac>();
		bInt = new Hashtable<String, BonusDeCarac>();
		bSag = new Hashtable<String, BonusDeCarac>();
		bChar = new Hashtable<String, BonusDeCarac>();
		bChant = new Hashtable<String, BonusChant>();
		bTouche = new Hashtable<String, BonusDeTouche>();
		bDegats = new Hashtable<String, BonusAuxDegats>();
		bTTaille = BonusDeTouche.bonusDeTaille(categorieTaille);
		
		attaques = new ArrayList<Attaque>();
		// TODO : rajouter de base une attaque a main nu
	}

	public String getNom() 
	{
		return nom;
	}

	public void setNom(String nom) 
	{
		this.nom = nom;
	}

	
	public int getBba() 
	{
		return bba;
	}

	public void setBba(int bba) 
	{
		this.bba = bba;
	}

	
	public int getForce() 
	{
		return force;
	}

	public void setForce(int force) 
	{
		this.force = force;
	}

	
	public int getDexterite()
	{
		return dexterite;
	}

	public void setDexterite(int dexterite)
	{
		this.dexterite = dexterite;
	}
	

	public int getConstitution()
	{
		return constitution;
	}

	public void setConstitution(int constitution)
	{
		this.constitution = constitution;
	}
	

	public int getIntelligence() 
	{
		return intelligence;
	}

	public void setIntelligence(int intelligence)
	{
		this.intelligence = intelligence;
	}

	
	public int getSagesse() 
	{
		return sagesse;
	}

	public void setSagesse(int sagesse) 
	{
		this.sagesse = sagesse;
	}

	
	public int getCharisme() 
	{
		return charisme;
	}
	
	public void setCharisme(int charisme)
	{
		this.charisme = charisme;
	}

	
	public CategorieTaille getCategorieTaille()
	{
		return categorieTaille;
	}
	
	public void setCategorieTaille(CategorieTaille categorieTaille) 
	{
		this.categorieTaille = categorieTaille;
		//TODO : changer bTTaille
	}

	
	public Hashtable<String, BonusDeCarac> getbFor()
	{
		return bFor;
	}
	
	public void ajouterBonusForce(BonusDeCarac bonus)
	{
		bFor.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusForce(String nom)
	{
		bFor.remove(nom);
	}
	
	
	public Hashtable<String, BonusDeCarac> getbDex()
	{
		return bDex;
	}
	
	public void ajouterBonusDex(BonusDeCarac bonus)
	{
		bDex.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusDex(String nom)
	{
		bDex.remove(nom);
	}
	
	
	public Hashtable<String, BonusDeCarac> getbCon()
	{
		return bCon;
	}
	
	public void ajouterBonusCon(BonusDeCarac bonus)
	{
		bCon.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusCon(String nom)
	{
		bCon.remove(nom);
	}
	
	
	public Hashtable<String, BonusDeCarac> getbInt() 
	{
		return bInt;
	}
	
	public void ajouterBonusInt(BonusDeCarac bonus)
	{
		bInt.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusInt(String nom)
	{
		bInt.remove(nom);
	}
	

	public Hashtable<String, BonusDeCarac> getbSag() 
	{
		return bSag;
	}
	
	public void ajouterBonusSag(BonusDeCarac bonus)
	{
		bSag.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusSag(String nom)
	{
		bSag.remove(nom);
	}
	

	public Hashtable<String, BonusDeCarac> getbChar()
	{
		return bChar;
	}
	
	public void ajouterBonusChar(BonusDeCarac bonus)
	{
		bChar.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusChar(String nom)
	{
		bChar.remove(nom);
	}
	

	public Hashtable<String, BonusChant> getbChant()
	{
		return bChant;
	}
	
	public void ajouterBonusChant(BonusChant bonus)
	{
		bChant.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusChant(String nom)
	{
		bChant.remove(nom);
	}

	
	public Hashtable<String, BonusDeTouche> getbTouche() 
	{
		return bTouche;
	}
	
	public void ajouterBonusTouche(BonusDeTouche bonus)
	{
		bTouche.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusTouche(String nom)
	{
		bTouche.remove(nom);
	}
	

	public Hashtable<String, BonusAuxDegats> getbDegats() 
	{
		return bDegats;
	}
	
	public void ajouterBonusDegats(BonusAuxDegats bonus)
	{
		bDegats.put(bonus.getNom(), bonus);
	}
	
	public void retirerBonusDegats(String nom)
	{
		bDegats.remove(nom);
	}
	

	public void ajouterBonusToucheDegats(BonusToucheDegats bonus)
	{
		bTouche.put(bonus.getNom(), bonus.getbTouche());
		bDegats.put(bonus.getNom(), bonus.getbDegats());
	}
	
	public void retirerBonusToucheDegats(String nom)
	{
		bTouche.remove(nom);
		bDegats.remove(nom);
	}
	
	
	public ArrayList<Attaque> getAttaques() 
	{
		return attaques;
	}
	
	
}
