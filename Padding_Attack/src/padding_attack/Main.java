/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package padding_attack;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
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
    private JButton attack,enc,save,show,exit;
    private JLabel label,label2,label3,label4;
    private JProgressBar progressBar;
    private JFileChooser fc;
    private File encrypted_data;
    JScrollPane jScrollPane;
    
    public Main(){
        
        encrypted_data = new File("encrypted.txt");
        
        
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
        
        show = new JButton("Show Encrypted");
        wholepanel.add(show);
        show.setAlignmentX(Component.CENTER_ALIGNMENT);
        show.addActionListener(this);
         
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
            
            //THREAD POU KANEI TO ATTACK
            //ETSI O XRHSTHS MPOREI NA XRHSIMOPOIEI THN UPOLOIPH
            //EFARMOGH XWRIS NA XREIAZETAI NA PERIMENEI NA TELIWSEI
            //H XRONOVORA EPITHESH
            new Thread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisible(true);
                    
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
                    
                    //DHMIOURGIA KLEIDIWN
                    String k1 = generateKey();
                    String k2 = generateKey();
                    
                    
                    String message = "Encryption key: " + k1+"\nAuthorisation key: " + k2+"\nPlaintext: " + plaintext;
                    JOptionPane.showMessageDialog(wholepanel, message,"info", JOptionPane.INFORMATION_MESSAGE);
                    
                    
                    String encrypted_text = (new AuthenticatedEncryption(k1, k2)).encrypt(plaintext);
                    
                    try (PrintWriter out = new PrintWriter(new FileWriter(encrypted_data, true))) {
                        out.append("Plaintext : "+plaintext);
                        out.append("Encrypted : "+encrypted_text);
                        out.append("\n\n");
                        out.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    showData();
                     
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        else if (e.getSource() == show){
            
            try {
                showData();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadLocationException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(e.getSource() == exit){
            System.exit(0);
        }
    }
    
    
    public void showData() throws IOException, BadLocationException{
        
        String data = "";
        
        data = new String(Files.readAllBytes(Paths.get(encrypted_data.getAbsolutePath())));
        
        JTextPane text = new JTextPane();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        Document doc = text.getStyledDocument();  
        doc.insertString(doc.getLength(), data,attributeSet);
        text.setPreferredSize(new java.awt.Dimension(500, 400));
        
        jScrollPane = new JScrollPane(text);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setViewportBorder(new LineBorder(Color.RED));
        
        JOptionPane.showMessageDialog(wholepanel, jScrollPane,"Encrypted texts", JOptionPane.INFORMATION_MESSAGE);
        
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


