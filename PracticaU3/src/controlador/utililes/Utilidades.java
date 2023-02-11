/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.utililes;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;
import javax.crypto.SecretKey;

/**
 *
 * @author sebastian
 */
public class Utilidades {

    public static String capitalizar(String nombre) {
        char[] aux = nombre.toCharArray();
        aux[0] = Character.toUpperCase(aux[0]);
        return new String(aux);
    }

    public static Field obtenetAtributo(Class clase, String nombre) {
        Field atributo = null;
        for (Field aux : clase.getDeclaredFields()) {
            if (nombre.equalsIgnoreCase(aux.getName())) {
                atributo = aux;
                break;
            }
        }
        return atributo;
    }

    public static Object transformarDato(Field atributo, String dato) {
        Object transformar = null;
        if (atributo.getType().getSuperclass().getSimpleName().equalsIgnoreCase("Number")) {
            if (atributo.getType().getSimpleName().equals("Integer")) {
                transformar = Integer.parseInt(dato);
            }
        } else if (atributo.getType().isEnum()) {
            Enum enumeracion = Enum.valueOf((Class) atributo.getType(), dato.toString());
            transformar = enumeracion;
        }  else if(atributo.getType().getSimpleName().equalsIgnoreCase("Boolean")) {
            transformar = Boolean.parseBoolean(dato);
        }
        else {
            transformar = dato;
        }
        return transformar;
    }

    public static String encriptar(String dato) {

        return Base64.getEncoder().encodeToString(dato.getBytes());
        //return Base64.encodeBase64URLSafeString(dato.getBytes());
    }

    public static String desencriptar(String dato) {
        return new String(Base64.getDecoder().decode(dato));
        //return new String(Base64.decodeBase64(dato));
    }

    //****************otra forma
    public static SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] claveEncription = clave.getBytes("UTF-8");
        // MessageDigest sha = MessageDigest.getInstance("SHA-1");
        //claveEncription = sha.digest(claveEncription);
        claveEncription = Arrays.copyOf(claveEncription, 16);
        SecretKeySpec keySpec = new SecretKeySpec(claveEncription, "AES");
        return keySpec;
    }

    //admin123
    public static String encriptarClave(final String datos, final String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] decodeKey = Base64.getDecoder().decode(claveSecreta);
        SecretKey secretKeySpec = crearClave(new String(decodeKey));
        //System.out.println(secretKeySpec.getFormat());
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] bytesEncriptados = cipher.doFinal(datos.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(bytesEncriptados);
    }

    public static String decencriptarClave(final String datos, final String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decodeKey = Base64.getDecoder().decode(claveSecreta);
        SecretKey secretKeySpec = crearClave(new String(decodeKey));
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] bytesEncriptados = Base64.getDecoder().decode(datos);//desencriptar(datos).getBytes();
        byte[] datosDessencriptados = cipher.doFinal(bytesEncriptados);
        return new String(datosDessencriptados);
    }

    public static boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;
        cedula = (cedula.length() == 13) ? cedula.substring(0, 10) : cedula;
        
        //String baj = cedula.substring(10, 13);
        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
// Coeficientes de validación cédula
// El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int verificador = Integer.parseInt(cedula.substring(9, 10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    } else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
            System.out.println("La Cédula ingresada es Incorrecta");
        }
        return cedulaCorrecta;
    }

    public static void main(String[] args) {
        // System.out.println(Utilidades.encriptar("viviana"));
//        System.out.println(Utilidades.desencriptar("dml2aWFuYQ=="));
//        try {
//            String claveSecreta = "XABC345";
//            String dato = "Marylin";
//            System.out.println(Utilidades.encriptarClave(dato, claveSecreta));
//            System.out.println(Utilidades.decencriptarClave("crth6PAR67eXoOjBn+0RiQ==", "XABC345"));
//            //qKG4D/oBUq3NwSX1tZPDxg==
//        } catch (Exception e) {
//
//            System.out.println(e);
//            e.printStackTrace();
//        }
        String aux = "9999999998001";
        String ced = aux.substring(0, 10);
        String baj = aux.substring(10, 13);
        System.out.println(ced+" "+baj);
    }

    public static Boolean isNumbre(Class clase) {
        return clase.getSuperclass().getSimpleName().equalsIgnoreCase("Number");
    }
    public static Boolean isString(Class clase) {
        return clase.getSimpleName().equalsIgnoreCase("String");
    }
    public static Boolean isCharacter(Class clase) {
        return clase.getSimpleName().equalsIgnoreCase("Character");
    }

    public static Boolean isBoolean(Class clase) {
        return clase.getSimpleName().equalsIgnoreCase("Boolean");
    }
    public static Boolean isPrimitive(Class clase) {
        return clase.isPrimitive();
    }
    public static Boolean isObject(Class clase) {
        return (!isPrimitive(clase) && !isBoolean(clase) && !isCharacter(clase) && !isNumbre(clase) && !isString(clase));
    }
}
