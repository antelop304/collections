package org.antelop.geoserver.security;

import static org.geoserver.security.SecurityUtils.scramble;
import static org.geoserver.security.SecurityUtils.toChars;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.io.IOUtils;
import org.geoserver.security.SecurityUtils;
import org.jasypt.commons.CommonUtils;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.exceptions.EncryptionInitializationException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.normalization.Normalizer;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;

/**
 * geoserver连接mysql数据库密码生成器.
 * 
 * @author antelop
 * @version 2014.08.19
 */
public class EncodeGeoserverMysqlPassword
{
    protected KeyStore ks;

    private boolean stringOutputTypeBase64 = true;
    private char[] password = null;
    private int saltSizeBytes = 8;
    private int keyObtentionIterations = 1000;
    private String algorithm = "PBEWithMD5AndDES";
    private static final String MESSAGE_CHARSET = "UTF-8";
    private static final String ENCRYPTED_MESSAGE_CHARSET = "US-ASCII";
    static final char[] BASE = new char[] { 'U', 'n', '6', 'd', 'I', 'l', 'X', 'T', 'Q', 'c', 'L', ')', '$', '#', 'q', 'J', 'U', 'l', 'X', 'Q', 'U', '!', 'n', 'n', 'p', '%', 'U', 'r', '5', 'U', 'u', '3', '5', 'H', '`', 'x', 'P', 'F', 'r', 'X' };
    static final int[] PERM = new int[] { 32, 19, 30, 11, 34, 26, 3, 21, 9, 37, 38, 13, 23, 2, 18, 4, 20, 1, 29, 17, 0, 31, 14, 36, 12, 24, 15, 35, 16, 39, 25, 5, 10, 8, 7, 6, 33, 27, 28, 22 };
    private SecretKey key = null;
    private Cipher encryptCipher = null;
    private final Base64 base64 = new Base64();
    private SaltGenerator saltGenerator = new RandomSaltGenerator();

    public static String createPasswd(String passwd, String geoserverPath)
    {
        try {
            EncodeGeoserverMysqlPassword eGMysqlPassword = new EncodeGeoserverMysqlPassword();
            String passwdfile = geoserverPath + "/data/security/masterpw/default/passwd";
            String geoserverJckes = geoserverPath + "/data/security/geoserver.jceks";

            eGMysqlPassword.initialize(passwdfile, geoserverJckes);

            String result = eGMysqlPassword.encrypt(passwd);
            return "crypt1:" + result;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public synchronized
        void initialize(String passwdfile, String geoserverJckes)
    {
        // Double-check to avoid synchronization issues

        try {
            KeyStore ks = assertActivatedKeyStore(passwdfile, geoserverJckes);

            SecretKey skey = (SecretKey) ks.getKey(
                "config:password:key", doGetMasterPassword(passwdfile));
            this.password = SecurityUtils.toChars(skey.getEncoded());
            // Normalize password to NFC form
            final char[] normalizedPassword = Normalizer
                .normalizeToNfc(this.password);
            /*
             * Encryption and decryption Ciphers are created the usual way.
             */
            PBEKeySpec pbeKeySpec = new PBEKeySpec(normalizedPassword);

            // We don't need the char[] passwords anymore -> clean!
            SecretKeyFactory factory = SecretKeyFactory
                .getInstance(this.algorithm);
            this.key = factory.generateSecret(pbeKeySpec);
            this.encryptCipher = Cipher.getInstance(this.algorithm);
        }
        catch (EncryptionInitializationException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new EncryptionInitializationException(t);
        }

        // The salt size for the chosen algorithm is set to be equal
        // to the algorithm's block size (if it is a block algorithm).
        int algorithmBlockSize = this.encryptCipher.getBlockSize();
        if (algorithmBlockSize > 0) {
            this.saltSizeBytes = algorithmBlockSize;
        }
    }

    public synchronized byte[] setPasswordCharArray(byte[] passwd)
    {
        // decrypt the password
        StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor();
        char[] key = key();
        try {
            encryptor.setPasswordCharArray(key);
            return encryptor.decrypt(org.apache.commons.codec.binary.Base64
                .decodeBase64(passwd));
        }
        finally {
            scramble(key);
        }
    }

    /**
     * <p>
     * Encrypts a message using the specified configuration.
     * </p>
     * </p> The Strings returned by this method are BASE64-encoded (default) or
     * HEXADECIMAL ASCII Strings. </p>
     * <p>
     * The mechanisms applied to perform the encryption operation are described
     * in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
     * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
     * </p>
     * <p>
     * This encryptor uses a salt for each encryption operation. The size of the
     * salt depends on the algorithm being used. This salt is used for creating
     * the encryption key and, if generated by a random generator, it is also
     * appended unencrypted at the beginning of the results so that a decryption
     * operation can be performed.
     * </p>
     * <p>
     * <b>If a random salt generator is used, two encryption results for the
     * same message will always be different (except in the case of random salt
     * coincidence)</b>. This may enforce security by difficulting brute force
     * attacks on sets of data at a time and forcing attackers to perform a
     * brute force attack on each separate piece of encrypted data.
     * </p>
     * 
     * @param message
     *            the String message to be encrypted
     * @return the result of encryption
     * @throws EncryptionOperationNotPossibleException
     *             if the encryption operation fails, ommitting any further
     *             information about the cause for security reasons.
     * @throws EncryptionInitializationException
     *             if initialization could not be correctly done (for example,
     *             no password has been set).
     */

    public String encrypt(final String message)
    {

        if (message == null) {
            return null;
        }

        try {

            // The input String is converted into bytes using MESSAGE_CHARSET
            // as a fixed charset to avoid problems with different platforms
            // having different default charsets (see MESSAGE_CHARSET doc).
            final byte[] messageBytes = message.getBytes(MESSAGE_CHARSET);

            // The StandardPBEByteEncryptor does its job.
            byte[] encryptedMessage = encrypt(messageBytes);

            // We encode the result in BASE64 or HEXADECIMAL so that we obtain
            // the safest result String possible.
            String result = null;
            if (this.stringOutputTypeBase64) {
                encryptedMessage = this.base64.encode(encryptedMessage);
                result = new String(encryptedMessage, ENCRYPTED_MESSAGE_CHARSET);
            }
            else {
                result = CommonUtils.toHexadecimal(encryptedMessage);
            }

            return result;

        }
        catch (EncryptionInitializationException e) {
            throw e;
        }
        catch (EncryptionOperationNotPossibleException e) {
            throw e;
        }
        catch (Exception e) {
            // If encryption fails, it is more secure not to return any
            // information about the cause in nested exceptions. Simply fail.
            throw new EncryptionOperationNotPossibleException();
        }
    }

    /**
     * <p>
     * Encrypts a message using the specified configuration.
     * </p>
     * <p>
     * The mechanisms applied to perform the encryption operation are described
     * in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
     * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
     * </p>
     * <p>
     * This encryptor uses a salt for each encryption operation. The size of the
     * salt depends on the algorithm being used. This salt is used for creating
     * the encryption key and, if generated by a random generator, it is also
     * appended unencrypted at the beginning of the results so that a decryption
     * operation can be performed.
     * </p>
     * <p>
     * <b>If a random salt generator is used, two encryption results for the
     * same message will always be different (except in the case of random salt
     * coincidence)</b>. This may enforce security by difficulting brute force
     * attacks on sets of data at a time and forcing attackers to perform a
     * brute force attack on each separate piece of encrypted data.
     * </p>
     * 
     * @param message
     *            the byte array message to be encrypted
     * @return the result of encryption
     * @throws EncryptionOperationNotPossibleException
     *             if the encryption operation fails, ommitting any further
     *             information about the cause for security reasons.
     * @throws EncryptionInitializationException
     *             if initialization could not be correctly done (for example,
     *             no password has been set).
     */

    public byte[] encrypt(final byte[] message)
        throws EncryptionOperationNotPossibleException
    {
        if (message == null) {
            return null;
        }

        try {
            // Create salt
            final byte[] salt = this.saltGenerator
                .generateSalt(this.saltSizeBytes);

            /*
             * Perform encryption using the Cipher
             */
            final PBEParameterSpec parameterSpec = new PBEParameterSpec(
                salt, this.keyObtentionIterations);

            byte[] encryptedMessage = null;
            synchronized (this.encryptCipher) {
                this.encryptCipher.init(
                    Cipher.ENCRYPT_MODE, this.key, parameterSpec);
                encryptedMessage = this.encryptCipher.doFinal(message);
            }
            // Finally we build an array containing both the unencrypted salt
            // and the result of the encryption. This is done only
            // if the salt generator we are using specifies to do so.
            if (this.saltGenerator.includePlainSaltInEncryptionResults()) {
                // Insert unhashed salt before the encryption result
                return CommonUtils.appendArrays(salt, encryptedMessage);
            }

            return encryptedMessage;
        }
        catch (InvalidKeyException e) {
            // The problem could be not having the unlimited strength policies
            // installed, so better give a usefull error message.
            handleInvalidKeyException(e);
            throw new EncryptionOperationNotPossibleException();
        }
        catch (Exception e) {
            // If encryption fails, it is more secure not to return any
            // information about the cause in nested exceptions. Simply fail.
            throw new EncryptionOperationNotPossibleException();
        }
    }

    public KeyStore assertActivatedKeyStore(
        String passwdfile,
        String geoserverJceksFile) throws IOException
    {
        char[] passwd = null;
        try {

            passwd = doGetMasterPassword(passwdfile);
            ks = KeyStore.getInstance("JCEKS");

            FileInputStream fis = new FileInputStream(new File(
                geoserverJceksFile));
            ks.load(fis, passwd);
            fis.close();
            return ks;
        }
        catch (Exception ex) {
            if (ex instanceof IOException) // avoid useless wrapping
                throw (IOException) ex;
            throw new IOException(ex);
        }
        finally {
            SecurityUtils.scramble(passwd);
        }
    }

    public char[] doGetMasterPassword(String file) throws Exception
    {
        try {
            InputStream in = new FileInputStream(new File(file));
            try {
                // JD: for some reason the decrypted passwd comes back sometimes
                // with null chars
                // tacked on
                // MCR, was a problem with toBytes and toChar in SecurityUtils
                // return
                // trimNullChars(toChars(decode(IOUtils.toByteArray(in))));
                return toChars(decode(IOUtils.toByteArray(in)));
            }
            finally {
                in.close();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Method used to provide an useful error message in the case that the
     * user tried to use a strong PBE algorithm like TripleDES and he/she
     * has not installed the Unlimited Strength Policy files (the default
     * message for this is simply "invalid key size", which does not provide
     * enough clues for the user to know what is really going on).
     */
    private void handleInvalidKeyException(InvalidKeyException e)
    {
        if ((e.getMessage() != null) && ((e
            .getMessage().toUpperCase().indexOf("KEY SIZE") != -1))) {
            throw new EncryptionOperationNotPossibleException(
                "Encryption raised an exception. A possible cause is " + "you are using strong encryption algorithms and " + "you have not installed the Java Cryptography " + "Extension (JCE) Unlimited Strength Jurisdiction " + "Policy Files in this Java Virtual Machine");
        }
    }

    byte[] decode(byte[] passwd)
    {

        // decrypt the password
        StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor();
        char[] key = key();
        try {
            encryptor.setPasswordCharArray(key);
            return encryptor.decrypt(Base64.decodeBase64(passwd));
        }
        finally {
            scramble(key);
        }
    }

    char[] key()
    {
        // generate the key
        return SecurityUtils.permute(BASE, 32, PERM);
    }
}