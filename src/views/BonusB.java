package views;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;
import java.awt.Toolkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import config.*;


public class BonusB extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenuBar menuBar;
	private JMenu mnFichier, mnChargeConfig;
	private JPanel contentPane;
	private JTextField textField_AEP, textField_NomPJ, textField_BArme, textField_Chant, textField_BFrenesie, textField_BDivers ,textField_CritArme, textField_BRage, textField_BBA, textField_CaracFor, textField_degatsBonus, textField_FacteurCharge, textField_CA;
	private JCheckBox chckbxRage, chckbxFrnsie, chckbxTenaille, chckbxArme2M, chckbxAEP,chckbxArmeMagique, chckbxCalculeDuMeilleur, chckbxCharge, chckbxDegatsBonus, chckbxSautDeCombat, chckbxAttaqueSaut;
	private ArrayList<TextArea> textAreas; 
	private ArrayList<JMenuItem> myConfigFiles; 
	private JTabbedPane tabbedPane;
	private JLabel lblBonusDeLarme, lblBonusDeRage, lblBonusDeFrnsie, lblDegatsBonusAEP, lblFacteurCharge;
	
	private String nomPJ, extension = ".perso";
	private int bForce, bBBA, bRage, bFrenesie, bAM, bDivers, modCritArme, ca, degatsBonus, chant, bbaDansAP, modCritCharge;
	private double bAEP;
	private boolean rage, frenesie, aM, tenaille, a2Mains, charge, bDegatsBonus, bSautCombat, bAttaqueSaut, AEP;
	private ArrayList<String> listFile;
	
	private FindCertainExtension fce;

	public double proba(int touche)
	{
		double proba  = (20 - (ca - touche))/20.0;
		
		if(proba < 0.05)
			proba = 0.05;
		
		if(proba > 0.95)
			proba = 0.95;
		
		return proba;
	}
	
	public String CreatTab(int nbAttaque)
	{
		StringBuilder sb = new StringBuilder();
		
		int bbaEff = bBBA - nbAttaque*5;
		
		int bonusForce = (bForce + (rage? bRage : 0) + (frenesie? bFrenesie : 0) - 10) / 2, degatsBBA, degats, touche, meilleurTouche = 0, modCritTotal = 1;
		double meilleurRatio = 0.0, tempRatio, proba, facteurAEP = 0;
		String s = "";
		
		
		if(modCritCharge<1)
			modCritCharge = 1;
		
		if(bSautCombat)
			modCritCharge += 1;
		
		if(modCritArme < 2)
			modCritArme = 2;
		
		
		facteurAEP = (AEP? bAEP : 0) * (bAttaqueSaut? 2 : 1) * (a2Mains? 2 : 1);
		modCritTotal += (modCritArme - 1) + (charge? modCritCharge - 1 : 0);
		
		sb.append("BBA dans l'AEP\tDégats bonus de l'AEP\tBonus de touché\tDégats\tDégats en critique\n");
		for(int i = 0; i <= (AEP? bbaEff : 0); i++)
		{
			degatsBBA =(int) (i * facteurAEP);
			degats = ((int) ((a2Mains? (int) bonusForce*1.5 : bonusForce) + degatsBBA + (aM? bAM : 0) + (bDegatsBonus? degatsBonus : 0)));
			touche = (bonusForce + bbaEff - i + (aM? bAM : 0) + chant + bDivers + (tenaille? 2 : 0) + (charge? 2 : 0));
			
			sb.append("\n" + i 
					+ "\t\t" + degatsBBA
					+ "\t\t\t" + touche
					+ "\t\t" + (degats * (charge? modCritCharge: 1) + chant)
					+ "\t" + (degats * modCritTotal + chant)
					);
			
			if(chckbxCalculeDuMeilleur.isSelected())
			{	
				proba = proba(touche);
				tempRatio = proba *  (degats * (charge? modCritCharge: 1));
				
				if(nbAttaque == 0)
				{
					if(meilleurRatio < tempRatio)
					{
						meilleurRatio = tempRatio;
						bbaDansAP = i;
						meilleurTouche = touche;
						s = "Pour une CA de " + ca + ", il est conseillé de mettre " + i + " points de BBA (proba de toucher : " + proba + "(" + (touche<0? "" : "+") + touche + ")" + " pour " + degats + "  degats)\n\n\n";
					}
					else if (meilleurRatio == tempRatio)
						if(meilleurTouche < touche)
						{
							meilleurRatio = tempRatio;
							bbaDansAP = i;
							meilleurTouche = touche;
							s = "Pour une CA de " + ca + ", il est conseillé de mettre " + i + " points de BBA (proba de toucher : " + proba + "(" + (touche<0? "" : "+") + touche  + ")"+ " pour " + degats + " degats)\n\n\n";
						}
				}
				else
				{
					if(bbaDansAP < bbaEff)
						s = "Pour une CA de " + ca + ", il est conseillé de mettre " + bbaDansAP + " points de BBA (proba de toucher : " + proba + "(" + (touche<0? "" : "+") + touche + ")" + " pour " + degats + "  degats)\n\n\n";
					else
						s = "Pour une CA de " + ca + ", il est conseillé de mettre " + bbaEff + " points de BBA (proba de toucher : " + proba + "(" + (touche<0? "" : "+") + touche + ")" + " pour " + degats + "  degats)\n\n\n";
				}
				
				sb.append("\t" + proba
						+ "\t" + tempRatio
						);
			}
		}
		
		return s + sb.toString();
	}
	
	public void updateAll()
	{
		try
		{
			bForce = Integer.parseInt(textField_CaracFor.getText());
		}
		catch (NumberFormatException e)
		{
			bForce = 0;
		}
		
		try
		{
			bBBA = Integer.parseInt(textField_BBA.getText());
		}
		catch (NumberFormatException e)
		{
			bBBA = 0;
		}
		
		try
		{
			bRage = Integer.parseInt(textField_BRage.getText());
		}
		catch (NumberFormatException e)
		{
			bRage = 4;
		}
				
		try
		{
			bFrenesie = Integer.parseInt(textField_BFrenesie.getText());
		}
		catch (NumberFormatException e)
		{
			bFrenesie = 6;
		}
		
		try
		{
			bAM = Integer.parseInt(textField_BArme.getText());
		}
		catch (NumberFormatException e)
		{
			bAM = 0;
		}
		
		try
		{
			bDivers = Integer.parseInt(textField_BDivers.getText());
		}
		catch (NumberFormatException e)
		{
			bDivers = 0;
		}
		
		try
		{
			modCritArme = Integer.parseInt(textField_CritArme.getText());
		}
		catch (NumberFormatException e)
		{
			modCritArme = 2;
		}
		
		try
		{
			ca = Integer.parseInt(textField_CA.getText());
		}
		catch (NumberFormatException e)
		{
			ca = 0;
		}
		
		try
		{
			degatsBonus = Integer.parseInt(textField_degatsBonus.getText());
		}
		catch (NumberFormatException e)
		{
			degatsBonus = 0;
		}
		
		try
		{
			chant = Integer.parseInt(textField_Chant.getText());
		}
		catch (NumberFormatException e)
		{
			chant = 0;
		}
		
		try
		{
			modCritCharge = Integer.parseInt(textField_FacteurCharge.getText());
		}
		catch (NumberFormatException e)
		{
			modCritCharge = 1;
		}
		
		try
		{
			bAEP = Double.parseDouble(textField_AEP.getText());
		}
		catch (NumberFormatException e)
		{
			bAEP = 1;
		}
		
		
		nomPJ = textField_NomPJ.getText();
		
		rage = chckbxRage.isSelected();
		frenesie = chckbxFrnsie.isSelected();
		aM = chckbxArmeMagique.isSelected();
		tenaille = chckbxTenaille.isSelected();
		a2Mains = chckbxArme2M.isSelected();
		charge = chckbxCharge.isSelected();
		bDegatsBonus = chckbxDegatsBonus.isSelected();
		bAttaqueSaut = chckbxAttaqueSaut.isSelected();
		bSautCombat = chckbxSautDeCombat.isSelected();
		AEP = chckbxAEP.isSelected();
		
		if(!charge)
		{
			bAttaqueSaut = false;
			bSautCombat = false;
		}
		
		textAreas = new ArrayList<TextArea>();
		tabbedPane.removeAll();
		
		
		StringBuilder sb;
		int i, nbAttaque = (int) Math.ceil((bBBA/5.0));
		
		if(nbAttaque > 4)
			nbAttaque = 4;
		
		for(i = textAreas.size() ; i < nbAttaque ; i++)
		{
			sb = new StringBuilder();
			switch (i)
			{
				case 0 :
						sb.append("Premiere ");
						break;
				case 1 :
						sb.append("Seconde ");
						break;
				case 2 :
						sb.append("Troisieme ");
						break;
				case 3 :
						sb.append("Quatrieme ");
						break;
			}
			sb.append("attaque");
			
			textAreas.add(new TextArea());
			tabbedPane.addTab(sb.toString(), null, textAreas.get(i), null);
		}
		
		for(i = 0; i < textAreas.size() ; i++)
		{
			textAreas.get(i).setText(this.CreatTab(i));
		}
	}
	
	public void initConfig(String FichierConfig)
	{
		Properties prop;
		try
		{
			prop = PropertyLoader.load(FichierConfig);
		}
		catch(Exception e)
		{
			return;
		}
		
		textField_NomPJ.setText(prop.getProperty("lblNomPJ"));
		textField_CaracFor.setText(prop.getProperty("textField_CaracFor"));
		textField_BBA.setText(prop.getProperty("textField_BBA"));
		textField_Chant.setText(prop.getProperty("textField_Chant"));
		textField_BDivers.setText(prop.getProperty("textField_BDivers"));
		textField_CritArme.setText(prop.getProperty("textField_Crit"));
		textField_BRage.setText(prop.getProperty("textField_BRage"));
		textField_BFrenesie.setText(prop.getProperty("textField_BFrenesie"));
		textField_BArme.setText(prop.getProperty("textField_BArme"));
		textField_degatsBonus.setText(prop.getProperty("textField_degatsBonus"));
		textField_AEP.setText(prop.getProperty("textField_AEP"));
		textField_FacteurCharge.setText(prop.getProperty("textField_FacteurCharge"));
		
		
		String s = prop.getProperty("chckbxAEP");
		if(s.equals("true"))
		{
			chckbxAEP.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxAEP.setSelected(false);
			}
			else
			{
				chckbxAEP.setSelected(false);
			}
		}
		lblDegatsBonusAEP.setVisible(chckbxAEP.isSelected());
		textField_AEP.setVisible(chckbxAEP.isSelected());
		
		s = prop.getProperty("chckbxAttaqueSaut");
		if(s.equals("true"))
		{
			chckbxAttaqueSaut.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxAttaqueSaut.setSelected(false);
			}
			else
			{
				chckbxAttaqueSaut.setSelected(false);
			}
		}
		
		s = prop.getProperty("chckbxSautDeCombat");
		if(s.equals("true"))
		{
			chckbxSautDeCombat.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxSautDeCombat.setSelected(false);
			}
			else
			{
				chckbxSautDeCombat.setSelected(false);
			}
		}
		
		s = prop.getProperty("chckbxCharge");
		if(s.equals("true"))
		{
			chckbxCharge.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxCharge.setSelected(false);
			}
			else
			{
				chckbxCharge.setSelected(false);
				System.out.println("!charge");
			}
			
			chckbxSautDeCombat.setSelected(false);
			chckbxAttaqueSaut.setSelected(false);
		}
		chckbxSautDeCombat.setVisible(false);
		chckbxAttaqueSaut.setVisible(false);
		textField_FacteurCharge.setVisible(chckbxCharge.isSelected());
		lblFacteurCharge.setVisible(chckbxCharge.isSelected());
		
		
		s = prop.getProperty("chckbxArme2M");
		if(s.equals("true"))
		{
			chckbxArme2M.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxArme2M.setSelected(false);
			}
			else
			{
				chckbxArme2M.setSelected(false);
			}
		}
		
		s = prop.getProperty("chckbxTenaille");
		if(s.equals("true"))
		{
			chckbxTenaille.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxTenaille.setSelected(false);
			}
			else
			{
				chckbxTenaille.setSelected(false);
			}
		}
		
		s = prop.getProperty("chckbxArmeMagique");
		if(s.equals("true"))
		{
			chckbxArmeMagique.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxArmeMagique.setSelected(false);
			}
			else
			{
				chckbxArmeMagique.setSelected(false);
			}
		}
		lblBonusDeLarme.setVisible(chckbxArmeMagique.isSelected());
    	textField_BArme.setVisible(chckbxArmeMagique.isSelected());
    	
    	s = prop.getProperty("chckbxRage");
		if(s.equals("true"))
		{
			chckbxRage.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxRage.setSelected(false);
			}
			else
			{
				chckbxRage.setSelected(false);
			}
		}
		lblBonusDeRage.setVisible(chckbxRage.isSelected());
		textField_BRage.setVisible(chckbxRage.isSelected());
		
		s = prop.getProperty("chckbxFrnsie");
		if(s.equals("true"))
		{
			chckbxFrnsie.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxFrnsie.setSelected(false);
			}
			else
			{
				chckbxFrnsie.setSelected(false);
			}
		}
		lblBonusDeFrnsie.setVisible(chckbxFrnsie.isSelected());
    	textField_BFrenesie.setVisible(chckbxFrnsie.isSelected());
    	
    	s = prop.getProperty("chckbxDegatsBonus");
		if(s.equals("true"))
		{
			chckbxDegatsBonus.setSelected(true);
		}
		else
		{
			if(s.equals("false"))
			{
				chckbxDegatsBonus.setSelected(false);
			}
			else
			{
				chckbxDegatsBonus.setSelected(false);
			}
		}
		textField_degatsBonus.setVisible(chckbxDegatsBonus.isSelected());
	}
	
	public void ChargerFichier()
	{
		mnChargeConfig.removeAll();
		
		myConfigFiles = new ArrayList<JMenuItem>();
		
		listFile = fce.listFile(".", extension);
		for(String t : listFile)
		{
			myConfigFiles.add(new JMenuItem(t));
		}
		
		
		for(JMenuItem j : myConfigFiles)
		{
			j.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{	
					initConfig(j.getText());
					updateAll();
				}
	        });
			mnChargeConfig.add(j);
		}
	}
	
	public void saveProperty()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(nomPJ + extension);
		
		String nameFile = sb.toString();
		
		sb = new StringBuilder();
		
		sb.append("#Nom du personnage\nlblNomPJ\t\t\t\t=\t" + nomPJ + "\n\n");
		sb.append("#Bonus de force (un entier est attendu, 0 par defaut)\ntextField_CaracFor\t\t=\t" + bForce + "\n\n");
		sb.append("#Bonus de Base a l'attaque (un entier est attendu, 0 par defaut)\ntextField_BBA\t\t\t=\t" + bBBA + "\n\n");
		sb.append("#Bonus de chant (un entier est attendu, 0 par defaut)\ntextField_Chant\t\t\t=\t" + chant + "\n\n");
		sb.append("#Bonus divers (un entier est attendu, 0 par defaut)\ntextField_BDivers\t\t=\t" + bDivers + "\n\n");
		sb.append("#Modificateur de critique de l'arme (un entier est attendu, 0 par defaut)\ntextField_Crit\t\t\t=\t" + modCritArme + "\n\n");
		sb.append("#Tenaille? (true ou false attendu, false par defaut)\nchckbxTenaille\t\t\t=\t" + tenaille + "\n\n");
		sb.append("#Possede le don Attaque en puissance (true ou false attendu, false par defaut)\nchckbxAEP\t\t\t\t=\t" + AEP + "\n\n");
		sb.append("#Degats bonus obtenus par point de bba utilise pour une arme a une main (un reel est attendu, 1.0 par defaut)\ntextField_AEP\t\t\t=\t" + bAEP + "\n\n");
		sb.append("#Arme a deux main? (true ou false attendu, false par defaut)\nchckbxArme2M\t\t\t=\t" + a2Mains + "\n\n");
		sb.append("#Est ce que l'arme est magique? (true ou false attendu, false par defaut)\nchckbxArmeMagique\t\t=\t" + aM + "\n\n");
		sb.append("#Bonus d'alteration de l'arme (un entier est attendu, 0 par defaut)\ntextField_BArme\t\t\t=\t" + bAM + "\n\n");
		sb.append("#En rage? (true ou false attendu, false par defaut)\nchckbxRage\t\t\t\t=\t" + rage + "\n\n");
		sb.append("#Bonus lors de la Rage (un entier est attendu, 0 par defaut)\ntextField_BRage\t\t\t=\t" + bRage + "\n\n");
		sb.append("#En frenesie? (true ou false attendu, false par defaut)\nchckbxFrnsie\t\t\t=\t" + frenesie + "\n\n");
		sb.append("#Bonus lors de la Frenesie (un entier est attendu, 0 par defaut)\ntextField_BFrenesie\t\t=\t" + bFrenesie + "\n\n");
		sb.append("#Charge? (true ou false attendu, false par defaut)\nchckbxCharge\t\t\t=\t" + charge + "\n\n");
		sb.append("#Les prerequis d'un Saut de Combat sont atteint(true ou false attendu, false par defaut)\nchckbxSautDeCombat\t\t=\t" + bSautCombat + "\n\n");
		sb.append("#Les prerequis d'une attaque saute sont atteint(true ou false attendu, false par defaut)\nchckbxAttaqueSaut\t\t=\t" + bAttaqueSaut + "\n\n");
		sb.append("#Facteur multiplicatoire des degats lors d'une charge (un entier est attendu, 1 par defaut)\ntextField_FacteurCharge\t=\t" + modCritCharge + "\n\n");
		sb.append("#Degats bonus d'une autre source? (true ou false attendu, false par defaut)\nchckbxDegatsBonus\t\t=\t" + bDegatsBonus + "\n\n");
		sb.append("#Degats bonus d'une autre source (un entier est attendu, 0 par defaut)\ntextField_degatsBonus\t=\t" + degatsBonus + "\n\n");
		
		
		String property = sb.toString();
		
		File f = new File(nameFile);
		
		try 
		{
			FileWriter fw = new FileWriter(f);
			
			fw.write(property);
			
			fw.close();
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public BonusB() 
	{
		setTitle("StratAdvisorD&D3.5");
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1056, 620);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFichier = new JMenu("Fichier");
		mnFichier.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnFichier);
		
		mnChargeConfig = new JMenu("Charger un fichier de Configuration");
		mnFichier.add(mnChargeConfig);
		
		JMenuItem mntmSauvConfig = new JMenuItem("Sauvegarder la Configuration");
		mnFichier.add(mntmSauvConfig);
		mntmSauvConfig.setHorizontalAlignment(SwingConstants.LEFT);
		
		mntmSauvConfig.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				saveProperty();
			}
        });
		
		JMenuItem mntmRechargerLesFichiers = new JMenuItem("Recharger les fichiers");
		mnFichier.add(mntmRechargerLesFichiers);
		
		mntmRechargerLesFichiers.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ChargerFichier();
			}
        });
		
		setJMenuBar(menuBar);
			
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		fce = FindCertainExtension.getInstance();
		
		ChargerFichier();

		JLabel lblNomDuPersonnage = new JLabel("Nom du personnage :");
		lblNomDuPersonnage.setBounds(8, 13, 129, 16);
		contentPane.add(lblNomDuPersonnage);
		
		textField_NomPJ = new JTextField("");
		textField_NomPJ.setBounds(149, 10, 204, 22);
		contentPane.add(textField_NomPJ);
		
		textField_NomPJ.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		JLabel lblCaracForce = new JLabel("Caract\u00E9ristique de force : ");
		lblCaracForce.setBounds(8, 42, 150, 16);
		contentPane.add(lblCaracForce);
		
		textField_CaracFor = new JTextField();
		textField_CaracFor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField_CaracFor.setBounds(296, 39, 57, 22);
		contentPane.add(textField_CaracFor);
		textField_CaracFor.setColumns(10);
		textField_CaracFor.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		JLabel lblBBA = new JLabel("Bonus de base \u00E0 l'attaque :");
		lblBBA.setBounds(8, 71, 156, 16);
		contentPane.add(lblBBA);
		
		textField_BBA = new JTextField();
		textField_BBA.setBounds(296, 68, 57, 22);
		contentPane.add(textField_BBA);
		textField_BBA.setColumns(10);	
		textField_BBA.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		JLabel labelChant = new JLabel("Bonus de chant :");
		labelChant.setBounds(8, 100, 105, 16);
		contentPane.add(labelChant);
		
		textField_Chant = new JTextField();
		textField_Chant.setColumns(10);
		textField_Chant.setBounds(296, 97, 57, 22);
		contentPane.add(textField_Chant);
		textField_Chant.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		JLabel lblBonusDivers = new JLabel("Bonus divers :");
		lblBonusDivers.setBounds(8, 129, 81, 16);
		contentPane.add(lblBonusDivers);
		
		textField_BDivers = new JTextField();
		textField_BDivers.setBounds(296, 126, 57, 22);
		contentPane.add(textField_BDivers);
		textField_BDivers.setColumns(10);
		textField_BDivers.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		JLabel lblModificateurDeCritique = new JLabel("Modificateur de critique de l'arme :");
		lblModificateurDeCritique.setBounds(8, 158, 201, 16);
		contentPane.add(lblModificateurDeCritique);
		
		textField_CritArme = new JTextField();
		textField_CritArme.setBounds(296, 155, 57, 22);
		contentPane.add(textField_CritArme);
		textField_CritArme.setColumns(10);
		textField_CritArme.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		chckbxArme2M = new JCheckBox("Arme \u00E0 2 mains");
		chckbxArme2M.setBounds(59, 192, 129, 25);
		contentPane.add(chckbxArme2M);
		chckbxArme2M.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				updateAll();
			}
        });
		
		chckbxTenaille = new JCheckBox("Tenaille");
		chckbxTenaille.setBounds(205, 192, 81, 25);
		contentPane.add(chckbxTenaille);
		chckbxTenaille.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				updateAll();
			}
        });
		
		chckbxCharge = new JCheckBox("Charge");
		chckbxCharge.setBounds(8, 386, 81, 25);
		contentPane.add(chckbxCharge);
		chckbxCharge.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				chckbxAttaqueSaut.setVisible(chckbxCharge.isSelected());
				chckbxSautDeCombat.setVisible(chckbxCharge.isSelected());
				textField_FacteurCharge.setVisible(chckbxCharge.isSelected());
				lblFacteurCharge.setVisible(chckbxCharge.isSelected());
				updateAll();
			}
        });
		
		chckbxArmeMagique = new JCheckBox("Arme magique");
		chckbxArmeMagique.setBounds(8, 266, 113, 25);
		contentPane.add(chckbxArmeMagique);
		
		lblBonusDeLarme = new JLabel("Bonus d'alteration :");
		lblBonusDeLarme.setBounds(170, 270, 116, 16);
		lblBonusDeLarme.setVisible(chckbxArmeMagique.isSelected());
		contentPane.add(lblBonusDeLarme);
		
		chckbxArmeMagique.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				lblBonusDeLarme.setVisible(chckbxArmeMagique.isSelected());
            	textField_BArme.setVisible(chckbxArmeMagique.isSelected());
				
				updateAll();
			}
        });
		
		textField_BArme = new JTextField();
		textField_BArme.setBounds(296, 267, 57, 22);
		textField_BArme.setVisible(chckbxArmeMagique.isSelected());
		contentPane.add(textField_BArme);
		textField_BArme.setColumns(10);
		textField_BArme.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		chckbxRage = new JCheckBox("Rage");
		chckbxRage.setBounds(8, 296, 57, 25);
		
		contentPane.add(chckbxRage);
		
		lblBonusDeRage = new JLabel("Bonus de Rage :");
		lblBonusDeRage.setBounds(170, 300, 94, 16);
		contentPane.add(lblBonusDeRage);
		
		lblBonusDeRage.setVisible(false);
		
		chckbxRage.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				lblBonusDeRage.setVisible(chckbxRage.isSelected());
				textField_BRage.setVisible(chckbxRage.isSelected());
				
				updateAll();
			}
        });
		
		textField_BRage = new JTextField();
		textField_BRage.setBounds(296, 297, 57, 22);
		contentPane.add(textField_BRage);
		textField_BRage.setColumns(10);
		textField_BRage.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		textField_BRage.setVisible(false);
		
		chckbxFrnsie = new JCheckBox("Fr\u00E9n\u00E9sie");
		chckbxFrnsie.setBounds(8, 326, 81, 25);
		contentPane.add(chckbxFrnsie);
		
		lblBonusDeFrnsie = new JLabel("Bonus de Fr\u00E9n\u00E9sie :");
		lblBonusDeFrnsie.setBounds(170, 330, 116, 16);
		contentPane.add(lblBonusDeFrnsie);
		
		lblBonusDeFrnsie.setVisible(false);
		
		chckbxFrnsie.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				lblBonusDeFrnsie.setVisible(chckbxFrnsie.isSelected());
            	textField_BFrenesie.setVisible(chckbxFrnsie.isSelected());
				
				updateAll();
			}
        });
		
		textField_BFrenesie = new JTextField();
		textField_BFrenesie.setBounds(296, 327, 57, 22);
		contentPane.add(textField_BFrenesie);
		textField_BFrenesie.setColumns(10);
		textField_BFrenesie.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		textField_BFrenesie.setVisible(false);
		
		chckbxDegatsBonus = new JCheckBox("D\u00E9gats bonus");
		chckbxDegatsBonus.setBounds(8, 356, 150, 25);
		contentPane.add(chckbxDegatsBonus);
		
		textField_degatsBonus = new JTextField();
		textField_degatsBonus.setBounds(296, 357, 57, 22);
		contentPane.add(textField_degatsBonus);
		textField_degatsBonus.setColumns(10);
		textField_degatsBonus.setVisible(false);
		textField_degatsBonus.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		chckbxDegatsBonus.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				textField_degatsBonus.setVisible(chckbxDegatsBonus.isSelected());
				
				updateAll();
			}
        });
		
		lblFacteurCharge = new JLabel("Facteur :");
		lblFacteurCharge.setBounds(170, 390, 56, 16);
		contentPane.add(lblFacteurCharge);
		lblFacteurCharge.setVisible(false);
		
		textField_FacteurCharge = new JTextField();
		textField_FacteurCharge.setBounds(296, 387, 57, 22);
		contentPane.add(textField_FacteurCharge);
		textField_FacteurCharge.setColumns(10);
		textField_FacteurCharge.setVisible(false);
		
		textField_FacteurCharge.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		chckbxSautDeCombat = new JCheckBox("Saut de Combat");
		chckbxSautDeCombat.setBounds(170, 415, 135, 25);
		contentPane.add(chckbxSautDeCombat);
		chckbxSautDeCombat.setVisible(false);
		
		chckbxSautDeCombat.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				updateAll();
			}
        });
		
		chckbxAttaqueSaut = new JCheckBox("Attaque saut\u00E9");
		chckbxAttaqueSaut.setBounds(170, 445, 134, 25);
		contentPane.add(chckbxAttaqueSaut);
		chckbxAttaqueSaut.setVisible(false);
		
		chckbxAttaqueSaut.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				updateAll();
			}
        });
		
		chckbxAEP = new JCheckBox("Attaque en puissance");
		chckbxAEP.setBounds(8, 236, 156, 25);
		contentPane.add(chckbxAEP);
		
		chckbxAEP.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				lblDegatsBonusAEP.setVisible(chckbxAEP.isSelected());
				textField_AEP.setVisible(chckbxAEP.isSelected());
				updateAll();
			}
        });
		
		lblDegatsBonusAEP = new JLabel("Bonus :");
		lblDegatsBonusAEP.setBounds(170, 240, 93, 16);
		lblDegatsBonusAEP.setVisible(false);
		contentPane.add(lblDegatsBonusAEP);
		
		textField_AEP = new JTextField();
		textField_AEP.setBounds(296, 237, 57, 22);
		contentPane.add(textField_AEP);
		textField_AEP.setColumns(10);
		textField_AEP.setVisible(false);
		
		textField_AEP.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		
		chckbxCalculeDuMeilleur = new JCheckBox("Calcule du meilleur ratio touch\u00E9/degats");
		chckbxCalculeDuMeilleur.setBounds(8, 482, 261, 25);
		contentPane.add(chckbxCalculeDuMeilleur);
		
		JLabel lblCaDeLadversaire = new JLabel("CA de l'adversaire : ");
		lblCaDeLadversaire.setBounds(149, 516, 129, 16);
		contentPane.add(lblCaDeLadversaire);
		
		textField_CA = new JTextField();
		textField_CA.setText("10");
		textField_CA.setBounds(296, 513, 57, 22);
		contentPane.add(textField_CA);
		textField_CA.setColumns(10);
		textField_CA.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateAll();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				updateAll();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				updateAll();	
			}
		});
		
		lblCaDeLadversaire.setVisible(false);
    	textField_CA.setVisible(false);
		
		chckbxCalculeDuMeilleur.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				lblCaDeLadversaire.setVisible(chckbxCalculeDuMeilleur.isSelected());
            	textField_CA.setVisible(chckbxCalculeDuMeilleur.isSelected());
				
				updateAll();
			}
        });
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(370, 6, 664, 536);
		contentPane.add(tabbedPane);
		
		this.updateAll();
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run()
			{
				try 
				{
					BonusB frame = new BonusB();
					frame.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
