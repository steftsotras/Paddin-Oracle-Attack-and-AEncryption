/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package padding_attack;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import static padding_attack.Padding_Attack.startAttack;

/**
 *
 * @author Stefanos
 */
public class Main implements ActionListener{
    
    //DHLWSH METABLHTWN
    
    //JCOMPONENTS
    private JFrame frame;
    private JPanel wholepanel;
    private JButton attack,enc,save,exit;
    private JLabel label,label2,label3,label4;
    private JProgressBar progressBar;
    private JFileChooser fc;
    
    public Main(){
        
        //GRAFIKO PERIVALON 
        frame = new JFrame("Crypto");
        
        //DHMIOYRGIA TWN APARAITHTWN PANEL
        wholepanel = new JPanel();
         
        //ORISMOS LAYOUT TOU PANEL 
        //WS BOXLAYOUT GIA NA MPOYN TO ENA KATW APTO ALLO
        BoxLayout bl = new BoxLayout(wholepanel,BoxLayout.PAGE_AXIS);
        wholepanel.setLayout(bl);
        
        //DHMIOURGIA KENOY BORDER GIA THN APOSTASH APTO ORIO TOU FRAME
        javax.swing.border.Border padding = BorderFactory.createEmptyBorder(30, 60, 30, 60);
        wholepanel.setBorder(padding);
        
        //DHMIOURGIA TWN BUTTONS KAI ORISMOS TOYS MESA STO PANEL
        //PROSTHESH ACTIONLISTENER
        attack = new JButton("Start Attack");
        wholepanel.add(attack);
        attack.setAlignmentX(Component.CENTER_ALIGNMENT);
        attack.addActionListener(this);
        
        wholepanel.add(Box.createRigidArea(new Dimension(0,5)));
        
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);
        wholepanel.add(progressBar);
        
        wholepanel.add(Box.createRigidArea(new Dimension(0,5)));
        
        label = new JLabel("");
        wholepanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        label3 = new JLabel("");
        wholepanel.add(label3);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        save = new JButton("Save Result");
        save.setVisible(false);
        wholepanel.add(save);
        save.setAlignmentX(Component.CENTER_ALIGNMENT);
        save.addActionListener(this);
        
        
        wholepanel.add(Box.createRigidArea(new Dimension(0,10)));
         
        enc = new JButton("Encrypt File");
        wholepanel.add(enc);
        enc.setAlignmentX(Component.CENTER_ALIGNMENT);
        enc.addActionListener(this);
        
        wholepanel.add(Box.createRigidArea(new Dimension(0,5)));
        
        label2 = new JLabel("");
        wholepanel.add(label2);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        label4 = new JLabel("");
        wholepanel.add(label4);
        label4.setAlignmentX(Component.CENTER_ALIGNMENT);
         
        wholepanel.add(Box.createRigidArea(new Dimension(0,45)));
        
        exit = new JButton("Exit");
        wholepanel.add(exit);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(this);
        
        fc = new JFileChooser();
        
        //PROSTHESH TOU TELIKOU PANEL STO FRAME
        frame.add(wholepanel);
        
        //SYNARTHSEIS GIA TO FRAME
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //frame.setSize(300 , 350);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        if(e.getSource() == attack){
            
            save.setVisible(false);
            label.setText("");
            label3.setText("");
            label4.setText("");
            label2.setText("");
            
            attack.setEnabled(false);
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisible(true);
                    // do my stuff here...
                    String words = "";
                    try {
                      words = startAttack();
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    progressBar.setVisible(false);
                    attack.setEnabled(true);
                    label.setText(words);
                    save.setVisible(true);
                  
                }
            }).start();
            
        }
        else if (e.getSource() == save){
            
            save.setVisible(false);
            label.setText("");
            
            try (PrintWriter out = new PrintWriter("padding_attack.txt")) {
                out.println(label.getText());
                label3.setText("Saved in padding_attack.txt");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(e.getSource() == enc){
            
            save.setVisible(false);
            label.setText("");
            label3.setText("");
            label4.setText("");
            label2.setText("");
            
            int returnVal = fc.showOpenDialog(wholepanel);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(fc.getSelectedFile().getAbsolutePath()));
                
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append("\n");
                        line = br.readLine();
                    }
                    
                    br.close();
                    
                    String plaintext = sb.toString();
                    String k1 = generateKey();
                    String k2 = generateKey();
                    
                    
                    label4.setText("Encryption key: " + k1+"\nAuthorisation key: " + k2+"\nPlaintext: " + plaintext);
                    
                    AuthenticatedEncryption aE = new AuthenticatedEncryption(k1, k2);
                    String encrypted = aE.encrypt(plaintext);
                    
                    label2.setText("Authenticated and encrypted plaintext: " + encrypted);
                    
                     
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        else if(e.getSource() == exit){
            System.exit(0);
        }
    }
    
    
    
    
    
    public static void main(String[] args) throws IOException ,Exception{
		
           new Main();
    }
    
    
    private static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        byte[] secretKeyEncoded = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(secretKeyEncoded);
    }
    
}


