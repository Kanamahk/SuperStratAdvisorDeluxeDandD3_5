package config;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class PropertyLoader
{
	public static Properties load(String filename) throws IOException 
	{
	    InputStream input = new FileInputStream(filename);
	    try 
	    {
	        Properties properties = new Properties();
	        properties.load(input);
	        return properties;
	    } 
	    finally 
	    {
	        input.close();
	    }
	}

   /*public static void main(String[] args)
   {
      try
      {
         // chargement des propriétés
         Properties prop = PropertyLoader.load("fichier.txt");

         // Affichage des propriétés
         // Récupère la propriété ma.cle
         // Si la propriété n'existe pas, retourne la valeur par défaut "vide"
         System.out.println("une cle: "+ prop.getProperty("unecle"));
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }*/
}