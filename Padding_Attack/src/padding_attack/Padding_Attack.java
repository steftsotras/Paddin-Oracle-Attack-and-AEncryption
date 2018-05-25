/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package padding_attack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
/**
 *
 * @author Stefanos
 */
public class Padding_Attack {
    
    private static String hex_digits = "0123456789abcdef";
    private static final int bytes_per_block = 16;
    private static final String url = "http://crypto-class.appspot.com/po?er=";
    
    //Original url
    private static final String enc_url = "f20bdba6ff29eed7b046d1df9fb7000058b1ffb4210a580f748b4ac714c001bd4a61044426fb515dad3f21f18aa577c0bdf302936266926ff37dbf7035d5eeb4";
    
    //url encrypted with AE
    //private static final String enc_url = "A22whnwkJOHBtbebPSBCft3nq50Ogxjy/kvSHGcGXnaBK+Ahu9oGyxXMDN9uyu5PAJSzKLspgc01nASJkkYZ8yfSFo8REzSrlomYmY5Wnvlu8hUr7CtI3i5b1lr/80NEZlXkrXkw8N24hlUe/sK5jw==";
    
    //METATROPH TOU 16DIKOU STRING SE BYTES KAI EISAGWGH SE PINAKA 
    private static final byte[] enc_bytes = hexToBytes(enc_url);
    
    //DHLWSH PINAKA POU THA EXEI XWRISMENA TA BYTE SE BLOCKS TWN (enc_bytes.length / 16)
    private static final byte[][] blocks = new byte[enc_bytes.length / 16][];
    
    
    public static String startAttack()throws IOException{
        
        //DIAXWRISMOS TOU PINAKA ANA BLOCK 
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = Arrays.copyOfRange(enc_bytes, i * bytes_per_block, (i + 1) * bytes_per_block);
        }
        
        //PINAKAS POU KRATAME TA SWSTA PADDINGS
        byte[] p = new byte[enc_bytes.length - bytes_per_block];
        
        //GIA OLA TA BLOCKS TWN 16 BYTE
        for (int i = blocks.length - 1; i > 0; i--) {
            System.out.println("Block number : " + i);
            
            //KRATAME TO PROHGOUMENO BLOCK KAI TO TWRINO
            byte[] previous_block = blocks[i - 1];
            byte[] current_block = Arrays.copyOf(enc_bytes, (i + 1) * bytes_per_block);
            
            //GIA OLA TA PADDING
            for (int padding = 1; padding <= bytes_per_block; padding++) {
                System.out.println("Padding : " + padding);
                
                //EISAGWGH STO TWRINO BLOCK TO PROHGOUMENO SWSTA MANTEMENO BYTE
                for (int j = 1; j < padding; j++) {
                    int index = current_block.length - bytes_per_block - j;
                    current_block[index] = (byte) (previous_block[bytes_per_block - j] ^ p[index] ^ padding);
                }
                
                //DOKIMES STO SYGKEKRIMENO PADDING ME BYTE APO 0-256
                for (int guessed_byte = 0; guessed_byte < 256; guessed_byte++) {
                    
                    //OTAN TO BYTE EINAI IDIO ME TO PADDING TOTE SKIP STO EPOMENO
                    if (guessed_byte == 1 && padding == 1 && i == blocks.length - 1)
                            continue;
                    
                    //XOR TO PROHGOUMENO CIPHERTEXT BLOCK ME TO BYTE KAI TO PADDING
                    current_block[current_block.length - bytes_per_block - padding] = (byte) (previous_block[bytes_per_block - padding] ^ guessed_byte ^ padding);
                    //METATROPH SE DEKAEKSADIKO
                    String hexed_guess = toHex(current_block,current_block.length);
                    
                    //STELNW HTTP REQUEST ME THN MANTEPSIA
                    int stat = sendGuessRequest(hexed_guess);
                    
                    //AN O SERVER STHLEI 404 ANTI GIA 403
                    //KSEROUME OTI TO PADDING EINAI EGKYRO OPOTE 
                    //KRATAME TO BYTE 
                    if (stat == HttpURLConnection.HTTP_NOT_FOUND || stat == HttpURLConnection.HTTP_OK) {
                            p[i * bytes_per_block - padding] = (byte) guessed_byte;
                            System.out.println(guessed_byte);
                            break;
                    }
                }
            }
        }
        //EPISTROFH TOY PLAINTEXT
        return new String(p);
    }
    
    //AITHMATA STON SERVER
    private static int sendGuessRequest(String qs) throws IOException {
            URL url = new URL(Padding_Attack.url + qs);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Integer.MAX_VALUE);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            int serverResponseCode = conn.getResponseCode();
            System.out.println(serverResponseCode);
            conn.disconnect();
            return serverResponseCode;
    }
    
    //METATROPH PINAKA BYTE SE DEKAEKSADIKO
    public static String toHex(byte[] data, int length) {
            StringBuffer buf = new StringBuffer();

            for (int i = 0; i != length; i++) {
                    int v = data[i] & 0xff;

                    buf.append(hex_digits.charAt(v >> 4));
                    buf.append(hex_digits.charAt(v & 0xf));
            }

            return buf.toString();
    }
    
    //METATROPH DEKAEKSADIKOY SE PINAKA BYTE
    public static byte[] hexToBytes(String hexString) {
            char[] hex = hexString.toCharArray();
            int length = hex.length / 2;
            byte[] raw = new byte[length];
            for (int i = 0; i < length; i++) {
                    int high = Character.digit(hex[i * 2], 16);
                    int low = Character.digit(hex[i * 2 + 1], 16);
                    int value = (high << 4) | low;
                    if (value > 127)
                            value -= 256;
                    raw[i] = (byte) value;
            }
            return raw;
    }
}