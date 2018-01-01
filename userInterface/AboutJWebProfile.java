/*
 * Year: 2017
 */
package userInterface;

/**
 *
 * @author nandan
 */
public class AboutJWebProfile extends javax.swing.JFrame {

    /**
     * Creates new form AboutJWebProfile
     */
    public AboutJWebProfile() {
        initComponents();
        String license = "<html><p>MIT License\n"
                + "\n"
                + "Copyright (c) 2017 Nandan Desai\n"
                + "\n"
                + "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
                + "of this software and associated documentation files (the \"Software\"), to deal\n"
                + "in the Software without restriction, including without limitation the rights\n"
                + "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
                + "copies of the Software, and to permit persons to whom the Software is\n"
                + "furnished to do so, subject to the following conditions:\n"
                + "\n"
                + "The above copyright notice and this permission notice shall be included in all\n"
                + "copies or substantial portions of the Software.\n"
                + "\n"
                + "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
                + "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
                + "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
                + "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
                + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
                + "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n"
                + "SOFTWARE.</p></html>";
        license = license.replace("\n", "<br>");
        String credits = "<html><p>Thanks to Sourabh Daroji for the logo design. <br>Thanks to the entire open source community and especially to the developers of the libraries used to make this software.<br>The libraries belong to their rightful owners.</p></html>";
        jLabel9.setText(credits);
        jLabel4.setText(license);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lato Thin", 0, 36)); // NOI18N
        jLabel1.setText("jWebProfile");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Lato Thin", 0, 12)); // NOI18N
        jLabel2.setText("beta");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, -1, -1));

        jLabel3.setFont(new java.awt.Font("Lato Thin", 0, 14)); // NOI18N
        jLabel3.setText("v0.1");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        jLabel4.setText("jLabel4");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 740, 380));

        jLabel5.setFont(new java.awt.Font("Lato Thin", 0, 14)); // NOI18N
        jLabel5.setText("This software is licensed under MIT LIcense on following terms:  ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel6.setFont(new java.awt.Font("Lato Thin", 0, 14)); // NOI18N
        jLabel6.setText("First build: 24 th July 2017. ");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/userInterface/Resources/logo60.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel8.setText("Credits");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, -1, -1));

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setText("jLabel9");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 730, 80));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
