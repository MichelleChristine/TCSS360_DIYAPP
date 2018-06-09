package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Utility;
import model.Project;
import model.ProjectManager;
import model.ShopSim;
import view.panel.AboutPanel;
import view.panel.DisplayPanel;
import view.panel.HomePanel;
import view.panel.ProjectPanel;
import view.panel.ReportPanel;
import view.panel.SettingsPanel;
import view.panel.ShopPanel;


/**
 * The Main frame the user sees. It houses the home screen and its buttons.
 *
 * @author Jim Phan phanjim2@hotmail.com
 * @author Michelle Brown - added little things like button images and panel creation once something was complete
 * 
 * @version May 29, 2018
 */
public class MainFrame extends JFrame {

    /**
     * Serial code of the class.
     */
    private static final long serialVersionUID = -7259295803716311757L;
    
    /**
     * The pages that the panel can switch to.
     * 
     * @author Caleb 
     * @version 6/7/2018
     */
    public static enum PAGE {
        HOME, PROJECT, REPORT, SHOP, ABOUT, SETTING;
    }
    
    /**
     * The reduction value used on the computer's screen resolution.
     */
    private static final double REDUCTION = 0.75;
    
    /**
     * Reduction used for the side panel.
     */
    private static final double SIDE = 0.10;
    
    private static final List<PAGE> PAGE_HISTORY = new ArrayList<PAGE>(); 
    
    /**
     * The panel that is held to the frame.
     */
    private JPanel displayPanel;
    
    /**
     * The current panel that's displayed to the user.
     */
    private JPanel dynamicPanel;
    
    /**
     * The Side Panel that houses the buttons.
     */
    private JPanel sidePanel;
    
    /**
     * The dimension of the frame.
     */
    private Dimension frameDimension;
    
    /**
     * The constructor. Initialize the values of the frame and sets up the panels.
     * 
     * @param width Width of the frame.
     * @param height Height of the frame.
     * @throws IOException
     * 
     * @author Jim
     */
    public MainFrame(int width, int height) throws IOException {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        frameDimension = new Dimension((int)(size.getWidth() * REDUCTION),
                (int)(size.getHeight() * 0.833));
        setSize(new Dimension((int)(frameDimension.getWidth()),
                (int)frameDimension.getHeight()));
        dynamicPanel = new HomePanel();
        displayPanel = new JPanel(); //Replace with Caleb's code.
        sidePanel = createSidePanel((int)(frameDimension.width * SIDE),
                (int)(frameDimension.height * REDUCTION));
        add(sidePanel, BorderLayout.WEST);
        add(displayPanel, BorderLayout.EAST);
        displayPanel.add(dynamicPanel);
        PAGE_HISTORY.add(PAGE.HOME);
    }
    
    /**
     * Creates the panel that will hold all of the buttons to navigate to different pages.
     * 
     * @param width
     * @param height
     * @return the side panel that was created
     * @throws IOException
     * 
     * @author Jim
     * @author Michelle - added images and paths
     */
    private JPanel createSidePanel(int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(width, height));
        
        panel.add(createBackButton("images/back.png"));
        panel.add(createButton(PAGE.HOME, "images/home_button.png"));
        panel.add(createButton(PAGE.PROJECT, "images/projects_button.png"));
        panel.add(createButton(PAGE.REPORT, "images/efficiency_button.png"));
        panel.add(createButton(PAGE.SHOP, "images/shop_button.png"));
        panel.add(createButton(PAGE.ABOUT, "images/about_button.png"));
        panel.add(createButton(PAGE.SETTING, "images/settings_button.png"));
        return panel;
    }
    
    /**
     * Creates a specific button for the SidePanel menu that will be used
     * to access its corresponding "page".
     * 
     * @param name the name if the page to be attached to the button
     * @param icon the picture for the button
     * @return the button that was created
     * @throws IOException
     * 
     * @author Jim
     * @author Michelle - added icons
     */
    private JButton createButton(PAGE name, String icon) throws IOException {
        ProjectManager.loadProjects();
        Image image = ImageIO.read(new File(icon));
        image = image.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
        ImageIcon button_image = new ImageIcon(image);
        JButton button = new JButton(button_image);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanel(name);
            }
        });
        return button;
    }
    
    /**
     * Creates a back button for the user.
     * 
     * @param icon the picture for the button
     * @return the button that was created
     * @throws IOException Throws an IOException if the image is linked wrong.
     * @author Jim
     */
    private JButton createBackButton(String icon) throws IOException {
        ProjectManager.loadProjects();
        Image image = ImageIO.read(new File(icon));
        image = image.getScaledInstance(75, 40, java.awt.Image.SCALE_SMOOTH);
        ImageIcon button_image = new ImageIcon(image);
        JButton button = new JButton(button_image);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(PAGE_HISTORY.size() > 1) {
                    PAGE page = PAGE_HISTORY.get(PAGE_HISTORY.size() - 2);
                    displayPanel.remove(dynamicPanel);
                    dynamicPanel = createPanel(page);
                    displayPanel.add(dynamicPanel, BorderLayout.CENTER);
                    validate();
                    PAGE_HISTORY.remove(PAGE_HISTORY.size() - 1);
                }
            }
        });
        return button;
    }
    
    /**
     * Changes the dynamic panel
     * 
     * @param name name of the page to switch to
     * 
     * @author Jim
     */
    public void changePanel(PAGE name) {
        if(name != PAGE.REPORT && name != PAGE.SHOP ||
                        ProjectManager.getCurrentProjectIndex() != null) {
            displayPanel.remove(dynamicPanel);
            dynamicPanel = createPanel(name);
            displayPanel.add(dynamicPanel, BorderLayout.CENTER);
            if(PAGE_HISTORY.size() >= 0 &&
                            PAGE_HISTORY.get(PAGE_HISTORY.size() - 1) != (name)) {
                if(PAGE_HISTORY.size() >= 30) {
                    PAGE_HISTORY.remove(0);
                }
                PAGE_HISTORY.add(name);
            }
            validate();
        } else {
            //project hasn't been selected yet so can't open shop or report page
            System.out.println("No project selected yet!");
        }
    }
    
    /**
     * A pseudo DisplayPanel factory that creates the panel to display
     * based on the button pressed in the SidePanel.
     * 
     * @param name of the page to create
     * @return the panel that was created
     * 
     * @author Jim
     * @author Michelle - added home and about page
     */
     private JPanel createPanel(PAGE name) {
        JPanel panel = null;
        
        if(name.equals(PAGE.HOME)) {
            panel = new HomePanel();
        } else if(name.equals(PAGE.PROJECT)) {
            panel = new ProjectPanel(new Dimension((int)(frameDimension.getWidth()*(1-SIDE*1.4)),
                                                  (int)(frameDimension.getHeight()*(REDUCTION))));
        } else if(name.equals(PAGE.REPORT)){
            Project p = ProjectManager.getProject(ProjectManager.getCurrentProjectIndex());
            if(p != null)
                panel = new ReportPanel(p);
        } else if(name.equals(PAGE.SHOP)) {
            
            ShopSim testStore = new ShopSim();
            panel = new ShopPanel(testStore);
            
        } else if(name.equals(PAGE.ABOUT)){ //Michelle's code
            String aboutText = "";
            try {
                aboutText = Utility.ReadTextFile("testAbout.txt");
            } catch (FileNotFoundException e) {
                aboutText = "file not found";
                e.printStackTrace();
            }
            
            panel = new AboutPanel(aboutText);
            
        } else if(name.equals(PAGE.SETTING)){
            panel = new SettingsPanel(frameDimension);
        } else {
            panel = new DisplayPanel(Color.ORANGE, frameDimension);
        }
        return panel;
    }
    
    /**
     * change the title of the frame to reflect the current project.
     * 
     * @param title
     * 
     * @author Caleb
     */
    public void SetTitle(String title) {
        this.setTitle("DIY Project\t -" + title);
    }
    
}
