package common.utility;

/**
 * Simple encryptor just so that passwords are not viewable in the config file.
 * 
 * @author Tom Hewitt
 * @since 2.1.0
 */
public class Encryptor
{
	public static String encrypt(String password)
	{
		String encrypted = "";
		
		char[] letters = password.toCharArray();

		for (Character letter : letters)
		{
			encrypted += letter - 'a' + 5;
			encrypted += ".";
		}

		return encrypted;
	}
	
	public static String decrypt(String encryptedPassword)
	{
		String unencrypted = "";
		String[] encryptedNumbers = encryptedPassword.split("\\.");

		for (int i = 0; i < encryptedNumbers.length; i++)
		{
			int anint = Integer.valueOf(encryptedNumbers[i]);
			unencrypted += Character.toString((char) (anint + 'a' - 5));
		}

		return unencrypted;
	}
}
