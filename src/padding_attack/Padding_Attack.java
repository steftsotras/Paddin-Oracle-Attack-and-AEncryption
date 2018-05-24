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
    private static final String enc_url = "f20bdba6ff29eed7b046d1df9fb7000058b1ffb4210a580f748b4ac714c001bd4a61044426fb515dad3f21f18aa577c0bdf302936266926ff37dbf7035d5eeb4";
    
    //METATROPH TOU 16DIKOU STRING SE BYTES KAI EISAGWGH SE PINAKA 
    private static final byte[] enc_bytes = hexToBytes(enc_url);
    
    //DHLWSH PINAKA POU THA EXEI XWRISMENA TA BYTE SE BLOCKS TWN (enc_bytes.length / 16)
    private static final byte[][] blocks = new byte[enc_bytes.length / 16][];
    
    
    public static String startAttack()throws IOException{
        
        //DIAXWRISMOS TOU PINAKA ANA BLOCK 
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = Arrays.copyOfRange(enc_bytes, i * bytes_per_block, (i + 1) * bytes_per_block);
        }
        
        //
        byte[] p = new byte[enc_bytes.length - bytes_per_block];
        
        // cycle for all 16 byte blocks except IV
        for (int i = blocks.length - 1; i > 0; i--) {
            System.out.println("block number: " + i);
            byte[] previous_block = blocks[i - 1];
            byte[] current_block = Arrays.copyOf(enc_bytes, (i + 1) * bytes_per_block);
            // cycle for all paddings
            for (int padding = 1; padding <= bytes_per_block; padding++) {
                System.out.println("pad: " + padding);
                // apply previously founded LSB
                for (int j = 1; j < padding; j++) {
                    int index = current_block.length - bytes_per_block - j;
                    current_block[index] = (byte) (previous_block[bytes_per_block - j] ^ p[index] ^ padding);
                }
                // cycle for all guesses
                for (int guessed_byte = 0; guessed_byte < 256; guessed_byte++) {
                    // skipping iteration when guessed byte and pad are the same
                    // and cancels each other
                    if (guessed_byte == 1 && padding == 1 && i == blocks.length - 1)
                            continue;
                    // XORing previous cipher text block with guess byte and pad
                    current_block[current_block.length - bytes_per_block - padding] = (byte) (previous_block[bytes_per_block - padding] ^ guessed_byte ^ padding);
                    String hexed_guess = toHex(current_block,current_block.length);
                    // send guess to server
                    int stat = sendGuessRequest(hexed_guess);
                    if (stat == HttpURLConnection.HTTP_NOT_FOUND || stat == HttpURLConnection.HTTP_OK) {
                            p[i * bytes_per_block - padding] = (byte) guessed_byte;
                            System.out.println(guessed_byte);
                            break;
                    }
                }
            }
        }
        System.out.println(p);
        return new String(p);
    }

    private static int sendGuessRequest(String qs) throws IOException {
            URL url = new URL(Padding_Attack.url + qs);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Integer.MAX_VALUE);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            int serverResponseCode = conn.getResponseCode();
            conn.disconnect();
            return serverResponseCode;
    }

    public static String toHex(byte[] data, int length) {
            StringBuffer buf = new StringBuffer();

            for (int i = 0; i != length; i++) {
                    int v = data[i] & 0xff;

                    buf.append(hex_digits.charAt(v >> 4));
                    buf.append(hex_digits.charAt(v & 0xf));
            }

            return buf.toString();
    }

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