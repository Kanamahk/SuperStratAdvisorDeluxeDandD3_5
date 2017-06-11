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
         // chargement des propri�t�s
         Properties prop = PropertyLoader.load("fichier.txt");

         // Affichage des propri�t�s
         // R�cup�re la propri�t� ma.cle
         // Si la propri�t� n'existe pas, retourne la valeur par d�faut "vide"
         System.out.println("une cle: "+ prop.getProperty("unecle"));
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }*/
}