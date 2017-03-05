/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdes;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author anony
 */

/*class Print {
    static void array(int[] arr,int len) {
        for(int i=0; i < len; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println("");
    }
}*/

class KeyGen {
    private int[] key = new int[10];
    private int[] k1 = new int[8];
    private int[] k2 = new int[8];
    
    void GetInput(String in) {
        for (int i = 0; i < in.length(); i++) {
            key[i] = Integer.parseInt(Character.toString(in.charAt(i)));
        }
        /*System.out.print("Key is: ");
        Print.array(key,key.length);
        System.out.println("");*/
    }
    
    void P10 () {
        int[] temp = new int[10];
        temp[0] = key[2];
        temp[1] = key[4];
        temp[2] = key[1];
        temp[3] = key[6];
        temp[4] = key[3];
        temp[5] = key[9];
        temp[6] = key[0];
        temp[7] = key[8];
        temp[8] = key[7];
        temp[9] = key[5];
        key = temp;
        /*System.out.print("P10: ");
        Print.array(key,key.length);
        System.out.println("");*/
    }
    
    void LS1 () {
        int[] temp = new int[10];
        temp[0] = key[1];
        temp[1] = key[2];
        temp[2] = key[3];
        temp[3] = key[4];
        temp[4] = key[0];
        temp[5] = key[6];
        temp[6] = key[7];
        temp[7] = key[8];
        temp[8] = key[9];
        temp[9] = key[5];
        key = temp;
        /*System.out.print("LS1: ");
        Print.array(key,key.length);
        System.out.println("");*/
    }
    
    int[] P8 () {
        int[] temp = new int[8];
        temp[0] = key[5];
        temp[1] = key[2];
        temp[2] = key[6];
        temp[3] = key[3];
        temp[4] = key[7];
        temp[5] = key[4];
        temp[6] = key[9];
        temp[7] = key[8];
        return temp;
    }
    
    void LS2 () {
        int[] temp = new int[10];
        temp[0] = key[2];
        temp[1] = key[3];
        temp[2] = key[4];
        temp[3] = key[0];
        temp[4] = key[1]; 
        temp[5] = key[7];
        temp[6] = key[8];
        temp[7] = key[9];
        temp[8] = key[5];
        temp[9] = key[6];
        key = temp;
        /*System.out.print("LS2: ");
        Print.array(key,key.length);
        System.out.println("");*/
    }
    
    int[] getKey(boolean flag) {
        if (!flag) {
            k1 = P8();
            /*System.out.print("k1: ");
            Print.array(k1,k1.length);
            System.out.println("");*/
            return k1;
        }
        else {
            k2 = P8();
            /*System.out.print("k2: ");
            Print.array(k2,k2.length);
            System.out.println("");*/
            return k2;
        }
    }
}

class CBC {
        String CBCEncrypt(String plaintext, int[] k1, int[] k2, String IV) {
            Encryption enc  = new Encryption();
            String encrypt_mes = "";
            int count = 0;
            String temp = plaintext;
            //System.out.println("Binary text: " + temp);
            List<String> t = new ArrayList<String>();
            for (int i = 0; i <= temp.length() - 8; i+=8) {
                t.add(temp.substring(i, i+8));
                count++;
            }
            //System.out.println(t.get(0));
            
            int[] a = BinaryOp.StringToArrayInt(t.get(0));
            int[] b = BinaryOp.StringToArrayInt(IV);
            t.set(0,BinaryOp.StrBuilder(BinaryOp.XOR(a,b)));
            //System.out.println(t.get(0));
            int ct[] = new int[8];
            ct = enc.encrypt(t.get(0),k1,k2);
            t.set(0, BinaryOp.StrBuilder(ct));
            encrypt_mes += t.get(0);
            
            for (int i = 1; i < count; i++) {
                a = BinaryOp.StringToArrayInt(t.get(i));
                b = BinaryOp.StringToArrayInt(t.get(i-1));
                t.set(i,BinaryOp.StrBuilder(BinaryOp.XOR(a,b)));
                int tp[] = new int[8];
                tp = enc.encrypt(t.get(i),k1,k2);
                 t.set(i, BinaryOp.StrBuilder(tp));
            encrypt_mes += t.get(i);
            }
            return encrypt_mes;
    }
    String CBCDecrypt(String encrypt_mes, int[] k1, int[] k2, String IV) {
            Encryption enc  = new Encryption();
            String decrypt_mes = "";
            List<String> t = new ArrayList<String>();
            int count = 0;
            for (int i = 0; i <= encrypt_mes.length() - 8; i+=8) {
                t.add(encrypt_mes.substring(i, i+8));
                count++;
            }
            
            int[] b = BinaryOp.StringToArrayInt(IV);
            int[] cn = new int[8];
            cn = enc.encrypt(t.get(0),k2,k1); 
            decrypt_mes += BinaryOp.StrBuilder(BinaryOp.XOR(cn,b));
                 
            for (int i = 1; i < count; i++) {
                int tn[] = new int[8];
                tn = enc.encrypt(t.get(i),k2,k1);
                b = BinaryOp.StringToArrayInt(t.get(i-1));
                decrypt_mes += BinaryOp.StrBuilder(BinaryOp.XOR(tn,b));
            } 
            return decrypt_mes;
    }
}

class BinaryOp {
    
    static int[] StringToArrayInt(String str) {
        int[] t = new int[8];
        for(int i = 0; i < str.length(); i++) {
            t[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
        }
        return t;
    }
    
    public static String TextToBinary(String str, int bits) {
        String result = "";
        String tmpStr;
        int tmpInt;
        char[] messChar = str.toCharArray();

        for (int i = 0; i < messChar.length; i++) {
            tmpStr = Integer.toBinaryString(messChar[i]);
            tmpInt = tmpStr.length();
            if(tmpInt != bits) {
                tmpInt = bits - tmpInt;
                if (tmpInt == bits) {
                    result += tmpStr;
                } else if (tmpInt > 0) {
                    for (int j = 0; j < tmpInt; j++) {
                        result += "0";
                    }
                    result += tmpStr;
                } else {
                    System.err.println("argument 'bits' is too small");
                }
            } else {
                result += tmpStr;
            }
            result += ""; // separator
        }
        return result;
    }
    
    public static String BinaryToText(String str){
        String result = "";
        for(int i = 0; i <= str.length() - 8; i+=8) {
            int k = Integer.parseInt(str.substring(i, i+8), 2);
            result += (char) k;
        }   
        return result;
    }
    
    static int BinToDec(int...bits) {    
     int temp=0;
     int base = 1;
     for(int i=bits.length-1 ; i>=0;i--)
     {
        temp = temp + (bits[i]*base);
        base = base * 2 ;
     }
      
      return temp;
  }

  static int[] DecToBinArr(int no) {
	if(no==0)
	{
		int[] zero = new int[2];
		zero[0] = 0;
		zero[1] = 0;
		return zero;	
	}
        int[] temp = new int[10] ;
	int count = 0 ;
        for(int i= 0 ; no!= 0 ; i++) {
            temp[i] = no % 2;
            no = no/2;
            count++;
        }
	int[] temp2 = new int[count];
	for(int i=count-1, j=0;i>=0 && j<count;i--,j++) {
		temp2[j] = temp[i];
	}
        if(count<2) {
		temp = new int[2];
		temp[0] = 0;
		temp[1] = temp2[0];
		return temp;
	}
	 
	return temp2;
    }
  
    static String StrBuilder(int[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i : array) {
            builder.append(i);
        }
        String text = builder.toString();
        return text;
    }
    
    static int[] XOR(int[] x, int[] y) {
	for (int i = 0; i < x.length; i++) {
		x[i] = x[i] ^ y[i];
	}
	return x;
    }
}

class Encryption {
    private int[] K1 = new int[8];
    private int[] K2 = new int[8];
    private int[] pt = new int[8];
    void SaveParameters(String plaintext, int[] k1, int[] k2){
	char c1;
	String ts ;
	try {
            for(int i=0;i<8;i++) {
                c1 = plaintext.charAt(i);
                ts = Character.toString(c1);
                pt[i] = Integer.parseInt(ts);
	   
                if(pt[i] !=0 && pt[i]!=1) {
                    //System.out.println("\n .. Invalid Plaintext ..");
                    System.exit(0);
                    return ;
                }
            }
	}
	catch(Exception e) {
		//System.out.println("\n .. Invalid Plaintext .. ");
		System.exit(0);
		return ;
		
        }
        this.pt = pt;	
        this.K1 = k1;
        this.K2 = k2;
    }
    
    void InitialPermutation() {
        int[] temp = new int[8]; 
        temp[0] = pt[1];
        temp[1] = pt[5];
        temp[2] = pt[2];
        temp[3] = pt[0];
        temp[4] = pt[3];
        temp[5] = pt[7];
        temp[6] = pt[4];
        temp[7] = pt[6];
        pt = temp;
	/*System.out.print("Initial Permutaion(IP): ");
        Print.array(this.pt,8);
	System.out.println("\n");*/
    } 
    
    void InverseInitialPermutation() {
        int[] temp = new int[8];
        temp[0] = pt[3];
        temp[1] = pt[0];
        temp[2] = pt[2];
        temp[3] = pt[4];
        temp[4] = pt[6];
        temp[5] = pt[1];
        temp[6] = pt[7];
        temp[7] = pt[5];
        pt = temp;
        /*System.out.print("Inverse Initial Permutaion(IP-1): ");
        Print.array(this.pt,8);
	System.out.println("\n");*/
    }    
    
    int[] mappingF(int[] R, int[] SK) {
        int[] temp = new int[8];
    
        temp[0]  = R[3];
        temp[1]  = R[0];
        temp[2]  = R[1];
        temp[3]  = R[2];
        temp[4]  = R[1];
        temp[5]  = R[2];
        temp[6]  = R[3];
        temp[7]  = R[0];
    
        /*System.out.println("EXPANSION/PERMUTATION on RH : ");
        Print.array(temp,8);
        System.out.println("\n");*/
	 
        temp[0] = temp[0] ^ SK[0];
        temp[1] = temp[1] ^ SK[1];
        temp[2] = temp[2] ^ SK[2];
        temp[3] = temp[3] ^ SK[3];
        temp[4] = temp[4] ^ SK[4];
        temp[5] = temp[5] ^ SK[5];
        temp[6] = temp[6] ^ SK[6];
        temp[7] = temp[7] ^ SK[7];
    
	/*System.out.println("XOR With Key : ");
        Print.array(temp,8);
	System.out.println("\n");*/
	 
        final int[][] S0 = { {1,0,3,2} , {3,2,1,0} , {0,2,1,3} , {3,1,3,2} } ;
        final int[][] S1 = { {0,1,2,3},  {2,0,1,3}, {3,0,1,0}, {2,1,0,3}} ;
    
   
        int d11 = temp[0];
        int d14 = temp[3];
      
	int row1 = BinaryOp.BinToDec(d11,d14);
      
	  
        int d12 = temp[1];
        int d13 = temp[2];    
        int col1 = BinaryOp.BinToDec(d12,d13);
 
        int o1 = S0[row1][col1]; 
	      
	int[] out1 = BinaryOp.DecToBinArr(o1);
	 
	/*System.out.println("S-Box S0: ");
        Print.array(out1,2);
 	System.out.println("\n");*/

	int d21 = temp[4];
        int d24 = temp[7];
        int row2 = BinaryOp.BinToDec(d21,d24);
	  
	int d22 = temp[5];
	int d23 = temp[6];
	int col2 = BinaryOp.BinToDec(d22,d23);
	  
	int o2 = S1[row2][col2];
	  	 
	int[] out2 = BinaryOp.DecToBinArr(o2); 

	/*System.out.println("S-Box S1: ");
        Print.array(out2,2);
	System.out.println("\n");*/
		
	int[] out = new int[4];
	out[0] = out1[0];
        out[1] = out1[1];
	out[2] = out2[0];
	out[3] = out2[1];
	  	  
	int [] O_Per = new int[4];
	O_Per[0] = out[1];
	O_Per[1] = out[3];
	O_Per[2] = out[2];
        O_Per[3] = out[0];
	  
        /*System.out.println("Output of mappingF : ");
        Print.array(O_Per,4);
	System.out.println("\n");*/
	 
	return O_Per;
  }

    int[] functionFk(int[] L, int[] R,int[] SK) {	
	int[] temp = new int[4];
	int[] out = new int[8];
	
	temp = mappingF(R,SK);
	
	out[0] = L[0] ^ temp[0];
	out[1] = L[1] ^ temp[1];
	out[2] = L[2] ^ temp[2];
	out[3] = L[3] ^ temp[3];
	
	out[4] = R[0];
	out[5] = R[1];
	out[6] = R[2];
	out[7] = R[3];
	
	return out;
    }
  
    int[] switchSW(int[] in) {
	
	int[] temp = new int[8];
	
	temp[0] = in[4];
	temp[1] = in[5];
	temp[2] = in[6];
	temp[3] = in[7];
        temp[4] = in[0];
	temp[5] = in[1];
	temp[6] = in[2];
	temp[7] = in[3];	
	
	return temp;
  }

    int[] encrypt(String plaintext , int[] LK, int[] RK) {
	
	SaveParameters(plaintext,LK,RK);
	
	/*System.out.println("\n---------------------------------------\n");
	System.out.println("\n---------------------------------------\n");
	*/
        InitialPermutation();
	int[] LH = new int[4];
	int[] RH = new int[4];
	LH[0] = pt[0];
	LH[1] = pt[1];
	LH[2] = pt[2];
	LH[3] = pt[3];
	

	RH[0] = pt[4];
	RH[1] = pt[5];
	RH[2] = pt[6];
	RH[3] = pt[7];
	
	/*System.out.println("First Round LH : ");
        Print.array(LH,4);
	System.out.println("\n");
	 
	System.out.println("First Round RH: ");
        Print.array(RH,4);
	System.out.println("\n");*/
	 
	int[] r1 = new int[8];
	r1 = functionFk(LH,RH,K1);
	
	/*System.out.println("After First Round : ");
        Print.array(r1,8);
	System.out.println("\n");
	System.out.println("\n---------------------------------------\n");
        */
        
	int[] temp = new int[8];
	temp = switchSW(r1);
	
        /*
	System.out.println("After Switch Function : ");
        Print.array(temp,8);
	System.out.println("\n");
	System.out.println("\n---------------------------------------\n");
        */
        
	LH[0] = temp[0];
	LH[1] = temp[1];
	LH[2] = temp[2];
	LH[3] = temp[3];
	RH[0] = temp[4];
	RH[1] = temp[5];
	RH[2] = temp[6];
	RH[3] = temp[7];

        /*
	System.out.println("Second Round LH : ");
        Print.array(LH,4);
	System.out.println("\n");
	 
	System.out.println("Second Round RH: ");
        Print.array(RH,4);
	System.out.println("\n");
	*/
	 
	int[] r2 = new int[8];
	r2 = functionFk(LH,RH,K2);
	
	pt = r2;
	
        /*
	System.out.println("After Second Round : ");
        Print.array(this.pt,8);
	System.out.println("\n");
	System.out.println("\n---------------------------------------\n");
	*/
        
	InverseInitialPermutation();
	
        /*
	System.out.println("After Inverse IP (Result) : ");
        Print.array(this.pt,8);
	System.out.println("\n");
	*/
	
	return pt;
    }
}

public class SDESForm extends javax.swing.JFrame {

    /**
     * Creates new form SDESForm
     */
    public SDESForm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        BrowseTab1 = new javax.swing.JButton();
        Encrypt = new javax.swing.JButton();
        SaveTab1 = new javax.swing.JButton();
        key = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        BrowseTab2 = new javax.swing.JButton();
        Decrypt = new javax.swing.JButton();
        SaveTab2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jScrollPane2.setViewportView(jTextPane2);

        BrowseTab1.setText("Browse");
        BrowseTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowseTab1ActionPerformed(evt);
            }
        });

        Encrypt.setText("Encrypt");
        Encrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncryptActionPerformed(evt);
            }
        });

        SaveTab1.setText("Save As");
        SaveTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveTab1ActionPerformed(evt);
            }
        });

        key.setText("Key (10bits)");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel3.setText("IV (8bits)");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(key)
                            .addComponent(jLabel3))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                            .addComponent(jTextField2)))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SaveTab1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Encrypt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BrowseTab1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(key)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BrowseTab1))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Encrypt)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SaveTab1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        jTabbedPane1.addTab("Encrypt", jPanel1);

        BrowseTab2.setText("Browse");
        BrowseTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowseTab2ActionPerformed(evt);
            }
        });

        Decrypt.setText("Decrypt");
        Decrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecryptActionPerformed(evt);
            }
        });

        SaveTab2.setText("Save As");
        SaveTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveTab2ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jTextPane1);

        jLabel4.setText("Key (10bits)");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel5.setText("IV (8bits)");

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(66, 66, 66)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(jTextField4)))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(BrowseTab2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Decrypt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SaveTab2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BrowseTab2)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Decrypt)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SaveTab2)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Decrypt", jPanel2);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("S-DES with CBC");

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel2.setText("Made by Nhu Dinh Toan (1414060)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(11, 11, 11))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BrowseTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowseTab1ActionPerformed
        // TODO add your handling code here:
        jFileChooser1.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        jFileChooser1.setFileFilter(filter); 
        int result = jFileChooser1.showOpenDialog(this);
        if (result == jFileChooser1.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            if(selectedFile.getName().toLowerCase().endsWith(".txt")) {
                try {
                    String text = "";
                    JOptionPane.showMessageDialog(null,"Selected file: " + selectedFile.getAbsolutePath());   
                    Path path = Paths.get(selectedFile.getAbsolutePath());
                    fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);
                    for(int i=0; i<fileLines.size(); i++){
                        text += fileLines.get(i) + "\r\n";
                        jTextPane2.setText(text);
                    } 
                    
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_BrowseTab1ActionPerformed

    private void BrowseTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowseTab2ActionPerformed
        // TODO add your handling code here:
        jFileChooser1.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        jFileChooser1.setFileFilter(filter); 
        int result = jFileChooser1.showOpenDialog(this);
        if (result == jFileChooser1.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            if(selectedFile.getName().toLowerCase().endsWith(".txt")) {
                try {
                    String text = "";
                    JOptionPane.showMessageDialog(null,"Selected file: " + selectedFile.getAbsolutePath());     
                    Path path = Paths.get(selectedFile.getAbsolutePath());
                    fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);
                    for(int i=0; i<fileLines.size(); i++){
                        text += fileLines.get(i) + "\n";
                        getText = text;
                        jTextPane1.setText(BinaryOp.BinaryToText(text));
                    } 
                    
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_BrowseTab2ActionPerformed

    private void SaveTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveTab1ActionPerformed
        // TODO add your handling code here:
        jFileChooser1.setCurrentDirectory(new File("user.dir"));
        int retrival = jFileChooser1.showSaveDialog(null);
        if (textEn.isEmpty() == false) {
            if (retrival == jFileChooser1.APPROVE_OPTION) {
                try {
                    FileWriter fw = new FileWriter(jFileChooser1.getSelectedFile()+".txt");
                    fw.write(textEn.toString());
                    fw.close();
                    JOptionPane.showMessageDialog(null,"Save file: " + jFileChooser1.getSelectedFile().getAbsolutePath() + ".txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else JOptionPane.showMessageDialog(null,"You haven't encrypted any files");
    }//GEN-LAST:event_SaveTab1ActionPerformed

    private void EncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncryptActionPerformed
        // TODO add your handling code here:
        if (jTextField1.getText() != null && jTextField2.getText() != null && jTextPane2.getText() != null) {
            String key = jTextField1.getText();
            String IV = jTextField2.getText();
            
            KeyGen k = new KeyGen();
            CBC cbc = new CBC();
            
            k.GetInput(key);
            k.P10();
            k.LS1();
            int[] k1 = k.getKey(false);
            k.LS2();
            int[] k2 = k.getKey(true);          
            
            String plaintext = BinaryOp.TextToBinary(jTextPane2.getText(),8);
            String encrypt_mes = cbc.CBCEncrypt(plaintext, k1, k2, IV);
            JOptionPane.showMessageDialog(null,"Encrypting Done. Please Save File!");
            textEn = encrypt_mes;
            jTextPane2.setText(BinaryOp.BinaryToText(textEn));
        }
        else {
            JOptionPane.showMessageDialog(null,"Please input key, IV and browse text file before encrypting");
        }
    }//GEN-LAST:event_EncryptActionPerformed
    public String getText = "";
    private void DecryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DecryptActionPerformed
        // TODO add your handling code here:
         String t = getText;
         if (jTextField3.getText() != null && jTextField4.getText() != null && jTextPane1.getText() != null) {
            String key = jTextField3.getText();
            String IV = jTextField4.getText();
            
            KeyGen k = new KeyGen();
            CBC cbc = new CBC();
            
            k.GetInput(key);
            k.P10();
            k.LS1();
            int[] k1 = k.getKey(false);
            k.LS2();
            int[] k2 = k.getKey(true);          
            
            String decrypt_mes = cbc.CBCDecrypt(t, k1, k2, IV);
            JOptionPane.showMessageDialog(null,"Decrypting Done. Please Save File!");
            
            textEn = BinaryOp.BinaryToText(decrypt_mes);
            jTextPane1.setText(textEn);
         }
         else {
            JOptionPane.showMessageDialog(null,"Please input key, IV and browse text file before decrypting");
        }
    }//GEN-LAST:event_DecryptActionPerformed

    private void SaveTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveTab2ActionPerformed
        // TODO add your handling code here:
        jFileChooser1.setCurrentDirectory(new File("user.dir"));
        int retrival = jFileChooser1.showSaveDialog(null);
        if (textEn.isEmpty() == false) {
            if (retrival == jFileChooser1.APPROVE_OPTION) {
                try {
                    FileWriter fw = new FileWriter(jFileChooser1.getSelectedFile()+".txt");
                    fw.write(textEn.toString());
                    fw.close();
                    JOptionPane.showMessageDialog(null,"Save file: " + jFileChooser1.getSelectedFile().getAbsolutePath() + ".txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else JOptionPane.showMessageDialog(null,"You haven't encrypted any files");
    }//GEN-LAST:event_SaveTab2ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        if (jTextField1.getText().length()>=10) {  
            evt.consume();
        }
        /*char c = evt.getKeyChar();
        if(!((c >= '0') && (c <= '1') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }*/
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (textEn.isEmpty() == false) {
                try {
                    FileWriter fw = new FileWriter(System.getProperty("user.dir")  + "//plaintext.txt");
                    fw.write(textEn.toString());
                    fw.close();
                    JOptionPane.showMessageDialog(null,"Save file: " + System.getProperty("user.dir") + "\\plaintext.txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (textEn.isEmpty() == false) {
            try {
                FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//result_ciphertext.txt");
                fw.write(textEn.toString());
                fw.close();
                JOptionPane.showMessageDialog(null,"Save file: " + System.getProperty("user.dir") + "\\result_ciphertext.txt");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        if (jTextField2.getText().length()>=8) {  
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        if (jTextField3.getText().length()>=10) {  
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        if (jTextField4.getText().length()>=8) {  
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    /**
     * @param args the command line arguments
     */
    List<String> fileLines;
    public String textEn = "";
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SDESForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SDESForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SDESForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SDESForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /*KeyGen k = new KeyGen();
        try {
            Scanner keyboard = new Scanner(System.in);
            Encryption enc  = new Encryption();
            CBC cbc = new CBC();
            //System.out.println("Enter a key: ");
            //String key = keyboard.next();
            String key = "0010010111";
            //System.out.println("Enter an input: ");
            //String p = keyboard.next();
            String p = "Toan is stupid";
            
            String IV = "10011011";
            
            k.GetInput(key);
            k.P10();
            k.LS1();
            int[] k1 = k.getKey(false);
            k.LS2();
            int[] k2 = k.getKey(true);          
            
            String plaintext = BinaryOp.TextToBinary(p,8);
            String encrypt_mes = cbc.CBCEncrypt(plaintext, k1, k2, IV);
                       
            String t = encrypt_mes;
            String decrypt_mes = cbc.CBCDecrypt(t, k1, k2, IV);
                    
            System.out.println("Original: " + BinaryOp.BinaryToText(plaintext));
            System.out.println("Decrypt Mes is:" + decrypt_mes);
            String temp2 = BinaryOp.BinaryToText(decrypt_mes);
            System.out.println("Plain text: " + temp2);
                  
        }
        catch(InputMismatchException e)
        {
            System.out.println("Error Occured : Invalid Input ");
        }
        catch(Exception e)
        {
            System.out.println("Error Occured : "+e);
        }*/

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SDESForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BrowseTab1;
    private javax.swing.JButton BrowseTab2;
    private javax.swing.JButton Decrypt;
    private javax.swing.JButton Encrypt;
    private javax.swing.JButton SaveTab1;
    private javax.swing.JButton SaveTab2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JLabel key;
    // End of variables declaration//GEN-END:variables
}
