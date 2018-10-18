/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OPT_UI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmcorej.CMMCore;
import org.micromanager.Studio;

import org.micromanager.data.Coords;
import org.micromanager.data.Image;
import org.micromanager.data.Datastore;
import org.micromanager.display.DisplayWindow;

/**
 *
 * @author Fogim
 */
public class OPT_hostframe extends javax.swing.JFrame {
    static OPT_hostframe frame_;  
    public static Studio gui_ = null;
    private CMMCore core_ = null;   
    Gson gson = new GsonBuilder().create();
    FileWriter fw;
    boolean aborted_ = false;
    
    /**
     * Creates new form OPT_hostframe
     */
    public OPT_hostframe(Studio gui_ref) {
        frame_ = this;            
//        frame_.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("HCAicon.png")));
//        frame_.setTitle("OpenHCA controller for Micro-manager 2");        
//        frame_.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        frame_.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent we) {
//                if (true == confirmQuit()){
//                    dispose();
//                } else {
//                    frame_.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//                }
//            }
//        });          
        gui_ = gui_ref;
        core_ = gui_.getCMMCore();
        initComponents();
        //Pass reference to this frame to children to allow callbacks
        rotation_control1.initialise(frame_);
        save_details1.initialise(frame_);
        abort1.initialise(frame_);
        
        try {
            core_.setExposure(100.0);
        } catch (Exception ex) {
            Logger.getLogger(OPT_hostframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        gui_.live().setLiveMode(true);
    }
    
    public void abort(){
        progress_indicator1.set_working(false);
        //Set to 100%, or leave at progress when aborted?
        //progress_indicator1.set_progress(100);
        progress_indicator1.set_aborted(true);
        aborted_ = true;
        //Maybe go live?
        gui_.live().setLiveMode(true);
        //###Reenable acq and calib buttons here
    }
    
    public void reset_abort(){
        progress_indicator1.set_aborted(false);
        aborted_ = false;
    }
    
    public void set_working(boolean working){
        progress_indicator1.set_progress(0);
        progress_indicator1.set_working(working);
    }

    public void run_acquisition() throws IOException, Exception{
        reset_abort();
        int numproj = rotation_control1.get_numproj();
        //Setup co-ordinates
        Coords.CoordsBuilder builder = gui_.data().getCoordsBuilder();
        //###Can't use this until arbitrary axes can be saved...
        //builder.index("projection",numproj);
        builder.z(numproj);
        Coords coords = builder.build();      

        //Kill live mode if running
        gui_.live().setLiveMode(false);
        
        //Make a datastore/window for the acquisition
        String dir = "C:\\TMP\\MMtest\\";
        String file = "X";
        String type = ".tif";
        String fullpath  = dir+file+type;
        File saveloc = new File(fullpath);
        while(saveloc.exists()){
            file = file + "X";
            fullpath  = dir+file+type;
            saveloc = new File(fullpath);
        }
        Datastore store = gui_.data().createRAMDatastore();
        DisplayWindow OPT_display = gui_.displays().createDisplay(store);
        Image curr_img;
        int stepsize = (rotation_control1.steps_per_revolution/rotation_control1.get_numproj());
        
        for(int pos = 0; pos<numproj; pos++){
            if(aborted_){
                break;
            } else {
                //###Can't use this until arbitrary axes can be saved...
                //coords = coords.copy().index("projection", pos).build();
                coords = coords.copy().z(pos).build();
                double oldpos = core_.getPosition();
                core_.setPosition(oldpos+stepsize);
                //core_.waitForDevice("RotStage");
                core_.snapImage();
                //convertTaggedImage takes (IMG/COORDS/METADATA)
                curr_img = gui_.data().convertTaggedImage(core_.getTaggedImage(),coords,null);
                store.putImage(curr_img);
                //gui_.displays().
            }
        }
        store.freeze();
        //If aborted, continue rotation to original position directly
        //Reset abort
        //Rename datastore/window
        //If not aborted, save data
    }
    
    public void run_calibration(){
        set_working(true);
        //Disable 
        //Kill live mode if running
        //Make a datastore/window for the calibation
        //Loop
            //Check abort
            //Snap image
            //Step rotation ("Z")
            //
        //If aborted, continue rotation to original position directly
        //Reset abort        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rotation_control1 = new OPT_UI.Rotation_control();
        save_details1 = new OPT_UI.Save_details();
        abort1 = new OPT_UI.Abort();
        progress_indicator1 = new OPT_UI.progress_indicator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(save_details1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rotation_control1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(progress_indicator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(abort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rotation_control1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(abort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(progress_indicator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(save_details1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(OPT_hostframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OPT_hostframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OPT_hostframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OPT_hostframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OPT_hostframe(gui_).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private OPT_UI.Abort abort1;
    private OPT_UI.progress_indicator progress_indicator1;
    private OPT_UI.Rotation_control rotation_control1;
    private OPT_UI.Save_details save_details1;
    // End of variables declaration//GEN-END:variables
}
