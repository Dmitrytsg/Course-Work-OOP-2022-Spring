package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cinemaProject.Data;
import cinemaProject.Film;
import cinemaProject.Person;
import cinemaProject.Session;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.log4j.BasicConfigurator;
import org.jdesktop.swingx.JXDatePicker;
import org.slf4j.LoggerFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/** @author Dmitry Tsygankov */

/**
 * Interface creating class
 * */
public class Interface{
    /** interface width*/
    private int gui_width = 500;
    /** interface height*/
    private int gui_height = 500;
    /** interface pupup width*/
    private int popup_width = 500;
    /** interface pupup height*/
    private int popup_height = 500;
    /** ClassType object for Selected menu*/
    private int ClassType = 0;
    /** Data from database*/
    private Data data = new Data();
    // Logger
    private static final Logger logger = Logger.getLogger(Interface.class);

    class XMLFileNotFoundExeption extends Exception {
        public XMLFileNotFoundExeption() {
            super ("No open file");
        }
    }

    private void checkXMLFile(String datasource) throws XMLFileNotFoundExeption {
        File TestFile = new File(datasource);
        if (!TestFile.exists()) throw new XMLFileNotFoundExeption();
    }

    public void GeneratePDF(String datasource, String resultpath) throws FileNotFoundException, XMLFileNotFoundExeption {

        checkXMLFile(datasource);
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(resultpath));
        pdfDoc.addNewPage();

        Document doc = new Document(pdfDoc);

        Paragraph ReportType = new Paragraph("Report: Films");
        ReportType.setFontSize(20);

        float[] ColumnWidth = {200, 200, 200, 200};
        Table table = new Table(ColumnWidth);

        table.addCell(new Cell().add("ID"));
        table.addCell(new Cell().add("Name"));
        table.addCell(new Cell().add("Release Year"));
        table.addCell(new Cell().add("Genre"));

        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document FilmsDoc = builder.parse(datasource);
            FilmsDoc.getDocumentElement().normalize();

            NodeList FilmsList = FilmsDoc.getElementsByTagName("film");
            for (int i = 0; i < FilmsList.getLength(); i++) {
                Node film = FilmsList.item(i);
                NamedNodeMap attrs = film.getAttributes();
                String id = attrs.getNamedItem("id").getNodeValue();
                String name = attrs.getNamedItem("name").getNodeValue();
                String release_year = attrs.getNamedItem("release_year").getNodeValue();
                String genre = attrs.getNamedItem("genre").getNodeValue();


                table.addCell(new Cell().add(id));
                table.addCell(new Cell().add(name));
                table.addCell(new Cell().add(release_year));
                table.addCell(new Cell().add(genre));
            }

        } catch (ParserConfigurationException | SAXException | java.io.IOException e) {
            e.printStackTrace();
        }

        doc.add(ReportType);
        doc.add(table);

        doc.close();
    }

    /**
     * Frame crating method
     * */
    private JFrame getFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("CinemaApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        frame.setBounds(
            dimension.width/2 - gui_width/2,
            dimension.height/2 - gui_height/2,
            gui_width,
            gui_height
        );
        return frame;
    }

    private JFrame getAddFilmDialog() {
        JFrame frame = new JFrame();
        JTextField nameFilm = new JTextField(20);
        JTextField genreFilm = new JTextField(20);
        JTextField ReleaseYearFilm = new JTextField(20);

        JLabel labelName = new JLabel("Type name");
        JLabel labelgenre = new JLabel("Type genre");
        JLabel labelReleaseYear = new JLabel("Type Release Year");
        frame.setTitle("Add new Film");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        frame.setBounds(
                dimension.width/2 - popup_width/2,
                dimension.height/2 - popup_height/2,
                popup_width,
                popup_height
        );

        JPanel jPanel = new JPanel();
        frame.add(jPanel);
        jPanel.setLayout(new BorderLayout());
        GridBagLayout gridBagLayout = new GridBagLayout();

        JPanel bPanel = new JPanel();
        bPanel.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        bPanel.add(labelName,c);
        c.gridx = 1;
        c.gridy = 0;
        bPanel.add(nameFilm, c);

        c.gridx = 0;
        c.gridy = 1;
        bPanel.add(labelgenre,c);
        c.gridx = 1;
        c.gridy = 1;
        bPanel.add(genreFilm, c);

        c.gridx = 0;
        c.gridy = 2;
        bPanel.add(labelReleaseYear, c);
        c.gridx = 1;
        c.gridy = 2;
        bPanel.add(ReleaseYearFilm, c);

        JPanel cPanel = new JPanel();
        cPanel.setLayout(new FlowLayout());

        JLabel labelTable = new JLabel("Select Persons");

        DefaultTableModel TableModel = new DefaultTableModel(
                data.getPersonList(),
                new String[]{"ID", "Name", "Surname", "Years Old", "Status"}
        );
        JTable table = new JTable(TableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        cPanel.add(labelTable);
        cPanel.add(scrollPane);

        JButton SaveButton = new JButton("Save");
        SaveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameFilm.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type name",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (genreFilm.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type genre",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (ReleaseYearFilm.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Type Release Year",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (ReleaseYearFilm.getText().length() != 4) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type correct Release Year",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean flag = true;
                    try {
                        Integer.parseInt(ReleaseYearFilm.getText());
                    } catch (NumberFormatException a) {
                        flag = false;
                        JOptionPane.showMessageDialog(frame,
                                "Please Type correct Release Year",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    if(table.getSelectedRows().length == 0) {
                        flag = false;
                        JOptionPane.showMessageDialog(frame,
                                "Please Select Person",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    if(flag) {
                        if( !data.addFilm(nameFilm.getText(), genreFilm.getText(), Integer.parseInt(ReleaseYearFilm.getText()), table.getSelectedRows())) {
                            logger.error("Add Film error");
                        }
                        JOptionPane.showMessageDialog(frame,
                                "Film was added",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        logger.info("Add film " + nameFilm.getText());
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }
                }
            }
        });

        JButton ExitButton = new JButton("Exit");
        ExitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });



        JPanel sPanel = new JPanel();
        sPanel.setLayout(new FlowLayout());
        sPanel.add(SaveButton);
        sPanel.add(ExitButton);

        jPanel.add(bPanel, BorderLayout.NORTH);
        jPanel.add(cPanel, BorderLayout.CENTER);
        jPanel.add(sPanel, BorderLayout.SOUTH);
        jPanel.revalidate();
        return frame;
    }

    private JFrame getInfoFilmDialog(int Row) {
        Film fm = data.getFilm(Row);
        String[][] SessionList;
        String[][] ActorList;
        String[][] ProducerList;

        List<Session> snlist = fm.GetSessionList();
        List<Person>  aclist = fm.GetActorList();
        List<Person>  prlist = fm.GetProducerList();


        SessionList = new String[snlist.size()][5];
        ActorList = new String[aclist.size()][5];
        ProducerList = new String[prlist.size()][5];

        int i = 0;
        for (Session sn:  snlist){
            SessionList[i][0] = String.valueOf(sn.GetSessionID());
            SessionList[i][1] = String.valueOf(sn.GetDate());
            SessionList[i][2] = String.valueOf(sn.GetStartTime());
            SessionList[i][3] = String.valueOf(sn.GetFinishTime());
            SessionList[i][4] = String.valueOf(sn.GetTicketCount());
            i++;
        }

        i = 0;
        for (Person pr:  aclist){
            ActorList[i][0] = String.valueOf(pr.GetPersonID());
            ActorList[i][1] = pr.GetFirstName();
            ActorList[i][2] = pr.GetLastName();
            ActorList[i][3] = String.valueOf(pr.GetYearsOld());
            ActorList[i][4] = pr.GetStatus();
            i++;
        }

        i = 0;
        for (Person pr:  prlist){
            ProducerList[i][0] = String.valueOf(pr.GetPersonID());
            ProducerList[i][1] = pr.GetFirstName();
            ProducerList[i][2] = pr.GetLastName();
            ProducerList[i][3] = String.valueOf(pr.GetYearsOld());
            ProducerList[i][4] = pr.GetStatus();
            i++;
        }

        JFrame frame = new JFrame();
        JTextField nameFilm = new JTextField( fm.GetName(),20);
        JTextField genreFilm = new JTextField( fm.GetGenre(),20);
        JTextField ReleaseYearFilm = new JTextField( Integer.toString(fm.GetReleaseYear()), 20);

        JLabel labelName = new JLabel("Name: ");
        JLabel labelgenre = new JLabel("Genre: ");
        JLabel labelReleaseYear = new JLabel("Release Year: ");
        frame.setTitle("Info about: " + fm.GetName());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        frame.setBounds(
                dimension.width/2 - popup_width/2,
                dimension.height/2 - popup_height/2,
                popup_width,
                popup_height
        );

        JPanel jPanel = new JPanel();
        frame.add(jPanel);
        jPanel.setLayout(new BorderLayout());
        GridBagLayout gridBagLayout = new GridBagLayout();

        JPanel bPanel = new JPanel();
        bPanel.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        bPanel.add(labelName,c);
        c.gridx = 1;
        c.gridy = 0;
        bPanel.add(nameFilm, c);

        c.gridx = 0;
        c.gridy = 1;
        bPanel.add(labelgenre,c);
        c.gridx = 1;
        c.gridy = 1;
        bPanel.add(genreFilm, c);

        c.gridx = 0;
        c.gridy = 2;
        bPanel.add(labelReleaseYear, c);
        c.gridx = 1;
        c.gridy = 2;
        bPanel.add(ReleaseYearFilm, c);

        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

        //Таблица Актеры
        DefaultTableModel modelActor = new DefaultTableModel(
                ActorList,
                new String[]{"ID", "Name", "Surname", "Years Old", "Status"}
        );
        JTable TableActor = new JTable(modelActor);
        JScrollPane scrollPaneActors = new JScrollPane(TableActor);

        //Таблица Режисеры
        DefaultTableModel modelProducer = new DefaultTableModel(
                ProducerList,
                new String[]{"ID", "Name", "Surname", "Years Old", "Status"}
        );
        JTable TableProducer = new JTable(modelProducer);
        JScrollPane scrollPaneProducer = new JScrollPane(TableProducer);

        //Таблица Сеансы
        DefaultTableModel modelSession = new DefaultTableModel(
                SessionList,
                new String[]{"ID", "Date", "Start Time", "Finish Time", "Number of tickets"}
        );
        JTable TableSession = new JTable(modelSession);
        JScrollPane scrollPaneSession = new JScrollPane(TableSession);

        cPanel.add(new JLabel("Actors"), cPanel);
        cPanel.add(scrollPaneActors, cPanel);
        cPanel.add(new JLabel("Producers"), cPanel);
        cPanel.add(scrollPaneProducer, cPanel);
        cPanel.add(new JLabel("Sessions"), cPanel);
        cPanel.add(scrollPaneSession, cPanel);

        JButton SaveButton = new JButton("Save");
        SaveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameFilm.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type name",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (genreFilm.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type genre",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (ReleaseYearFilm.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Type Release Year",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (ReleaseYearFilm.getText().length() != 4) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type correct Release Year",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean flag = true;
                    try {
                        Integer.parseInt(ReleaseYearFilm.getText());
                    } catch (NumberFormatException a) {
                        flag = false;
                        JOptionPane.showMessageDialog(frame,
                                "Please Type correct Release Year",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    if(flag) {
                        if( !data.RewriteFilm(Row,nameFilm.getText(), genreFilm.getText(), Integer.parseInt(ReleaseYearFilm.getText()))) {
                            logger.error("Rewrite Film error");
                        }
                        JOptionPane.showMessageDialog(frame,
                                "Film was saved",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        logger.info("Reload film " + fm.GetName());
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }
                }
            }
        });


        JButton ExitButton = new JButton("Exit");
        ExitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });


        JPanel sPanel = new JPanel();
        sPanel.setLayout(new FlowLayout());
        sPanel.add(SaveButton);
        sPanel.add(ExitButton);

        jPanel.add(bPanel, BorderLayout.NORTH);
        jPanel.add(cPanel, BorderLayout.CENTER);
        jPanel.add(sPanel, BorderLayout.SOUTH);
        jPanel.revalidate();
        return frame;
    }

    private JFrame getAddPersonDialog() {
        JFrame frame = new JFrame();
        JTextField firstname = new JTextField(20);
        JTextField lastname = new JTextField(20);
        JTextField yearsold = new JTextField(20);
        JTextField status = new JTextField(20);

        JLabel labelfirstname = new JLabel("Type firstname");
        JLabel labellastname = new JLabel("Type lastname");
        JLabel labelyearsold = new JLabel("Type years old");
        JLabel labelstatus = new JLabel("Type status");
        frame.setTitle("Add new Person");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        frame.setBounds(
                dimension.width/2 - popup_width/2,
                dimension.height/2 - popup_height/2,
                popup_width,
                popup_height
        );

        JPanel jPanel = new JPanel();
        frame.add(jPanel);
        jPanel.setLayout(new BorderLayout());
        GridBagLayout gridBagLayout = new GridBagLayout();

        JPanel bPanel = new JPanel();
        bPanel.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        bPanel.add(labelfirstname,c);
        c.gridx = 1;
        c.gridy = 0;
        bPanel.add(firstname, c);

        c.gridx = 0;
        c.gridy = 1;
        bPanel.add(labellastname,c);
        c.gridx = 1;
        c.gridy = 1;
        bPanel.add(lastname, c);

        c.gridx = 0;
        c.gridy = 2;
        bPanel.add(labelyearsold, c);
        c.gridx = 1;
        c.gridy = 2;
        bPanel.add(yearsold, c);

        c.gridx = 0;
        c.gridy = 3;
        bPanel.add(labelstatus, c);
        c.gridx = 1;
        c.gridy = 3;
        bPanel.add(status, c);

        JPanel cPanel = new JPanel();
        cPanel.setLayout(new FlowLayout());

        JLabel labelTable = new JLabel("Select Films");

        DefaultTableModel TableModel = new DefaultTableModel(
                data.getFilmList(),
                new String[]{"ID", "Name", "Release Year", "Genre"}
        );
        JTable table = new JTable(TableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        cPanel.add(labelTable);
        cPanel.add(scrollPane);

        JButton SaveButton = new JButton("Save");
        SaveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstname.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type firstname",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (lastname.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type lastname",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (status.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type status",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (yearsold.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Type years old",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean flag = true;
                    try {
                        Integer.parseInt(yearsold.getText());
                    } catch (NumberFormatException a) {
                        flag = false;
                        JOptionPane.showMessageDialog(frame,
                                "Please Type correct years old",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    if(table.getSelectedRows().length == 0) {
                        flag = false;
                        JOptionPane.showMessageDialog(frame,
                                "Please Select Film",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    if(flag) {
                        if( !data.addPerson(firstname.getText(), lastname.getText(), Integer.parseInt(yearsold.getText()), status.getText(), table.getSelectedRows())) {
                            logger.error("Add Person error");
                        }
                        JOptionPane.showMessageDialog(frame,
                                "Person was added",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        logger.info("Add person " + firstname.getText());
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }
                }
            }
        });

        JButton ExitButton = new JButton("Exit");
        ExitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });



        JPanel sPanel = new JPanel();
        sPanel.setLayout(new FlowLayout());
        sPanel.add(SaveButton);
        sPanel.add(ExitButton);

        jPanel.add(bPanel, BorderLayout.NORTH);
        jPanel.add(cPanel, BorderLayout.CENTER);
        jPanel.add(sPanel, BorderLayout.SOUTH);
        jPanel.revalidate();
        return frame;
    }

    private JFrame getAddSessionDialog() {
        JFrame frame = new JFrame();
        JTextField Date = new JTextField(20);
        JTextField StartTime = new JTextField(20);
        JTextField FinishTime = new JTextField(20);
        JTextField TicketCount = new JTextField(20);

        JLabel labelDate = new JLabel("Select Date(yyyy-mm-dd): ");
        JLabel labelStartTime = new JLabel("Select Start Time(HH:mm): ");
        JLabel labelFinishTime= new JLabel("Select Finish Time(HH:mm): ");
        JLabel labelTicketCount = new JLabel("Type count of Tickets: ");
        frame.setTitle("Add new Session");



        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        frame.setBounds(
                dimension.width/2 - popup_width/2,
                dimension.height/2 - popup_height/2,
                popup_width,
                popup_height
        );

        JPanel jPanel = new JPanel();
        frame.add(jPanel);
        jPanel.setLayout(new BorderLayout());
        GridBagLayout gridBagLayout = new GridBagLayout();

        JPanel bPanel = new JPanel();
        bPanel.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        bPanel.add(labelDate,c);
        c.gridx = 1;
        c.gridy = 0;
        bPanel.add(Date, c);

        c.gridx = 0;
        c.gridy = 1;
        bPanel.add(labelStartTime,c);
        c.gridx = 1;
        c.gridy = 1;
        bPanel.add(StartTime, c);

        c.gridx = 0;
        c.gridy = 2;
        bPanel.add(labelFinishTime, c);
        c.gridx = 1;
        c.gridy = 2;
        bPanel.add(FinishTime, c);

        c.gridx = 0;
        c.gridy = 3;
        bPanel.add(labelTicketCount, c);
        c.gridx = 1;
        c.gridy = 3;
        bPanel.add(TicketCount, c);

        JPanel cPanel = new JPanel();
        cPanel.setLayout(new FlowLayout());

        JLabel labelTable = new JLabel("Select Film");

        DefaultTableModel TableModel = new DefaultTableModel(
                data.getFilmList(),
                new String[]{"ID", "Name", "Release Year", "Genre"}
        );
        JTable table = new JTable(TableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        cPanel.add(labelTable);
        cPanel.add(scrollPane);


        JButton SaveButton = new JButton("Save");
        SaveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                if(table.getSelectedRows().length == 0) {
                    flag = false;
                    JOptionPane.showMessageDialog(frame,
                            "Please Select Film",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                if(flag) {
                    try {
                        if( !data.addSession(Date.getText(), StartTime.getText(), FinishTime.getText(), Integer.parseInt(TicketCount.getText()), table.getSelectedRows())) {
                            logger.error("Add Session error");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(frame,
                            "Session was added",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Add session " + Date.getText() + " Start Time: " + StartTime.getText());
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            }
        });

        JButton ExitButton = new JButton("Exit");
        ExitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });



        JPanel sPanel = new JPanel();
        sPanel.setLayout(new FlowLayout());
        sPanel.add(SaveButton);
        sPanel.add(ExitButton);

        jPanel.add(bPanel, BorderLayout.NORTH);
        jPanel.add(cPanel, BorderLayout.CENTER);
        jPanel.add(sPanel, BorderLayout.SOUTH);
        jPanel.revalidate();
        return frame;
    }

    private JFrame getAddMonthDialog() {
        JFrame frame = new JFrame();
        frame.setTitle("Session for a month");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        List<Session> snlist = data.GetSnList();

        String[][] SessionList = new String[snlist.size()][5];

        int i = 0;
        for (Session sn:  snlist){
            if(sn.GetDate().getMonth() == Calendar.getInstance().getTime().getMonth()) {
                SessionList[i][0] = String.valueOf(sn.GetSessionID());
                SessionList[i][1] = String.valueOf(sn.GetDate());
                SessionList[i][2] = String.valueOf(sn.GetStartTime());
                SessionList[i][3] = String.valueOf(sn.GetFinishTime());
                SessionList[i][4] = String.valueOf(sn.GetTicketCount());
                i++;
            }
        }

        frame.setBounds(
                dimension.width/2 - popup_width/2,
                dimension.height/2 - popup_height/2,
                popup_width,
                popup_height
        );

        JPanel jPanel = new JPanel();
        frame.add(jPanel);
        jPanel.setLayout(new BorderLayout());



        JPanel cPanel = new JPanel();
        cPanel.setLayout(new FlowLayout());

        JLabel labelTable = new JLabel("Session for a month");

        DefaultTableModel TableModel = new DefaultTableModel(
                SessionList,
                new String[]{"ID", "Date", "Start Time", "Finish Time", "Number of tickets"}
        );
        JTable table = new JTable(TableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        cPanel.add(labelTable);
        cPanel.add(scrollPane);


        JButton ExitButton = new JButton("Exit");
        ExitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });



        JPanel sPanel = new JPanel();
        sPanel.setLayout(new FlowLayout());
        sPanel.add(ExitButton);

        jPanel.add(cPanel, BorderLayout.CENTER);
        jPanel.add(sPanel, BorderLayout.SOUTH);
        jPanel.revalidate();
        return frame;
    }

    /**
     * Constructor
     * */
    public Interface() throws Exception {
        JFrame frame = getFrame();
        JPanel jPanel = new JPanel();
        frame.add(jPanel);
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        /** Menus*/
        JMenuBar MenuBar = new JMenuBar();

        JMenu FileMenu = new JMenu("File");
        JMenu AddMenu = new JMenu("Add");
        FileMenu.setMnemonic('F');
        MenuBar.add(FileMenu);
        MenuBar.add(AddMenu);

        //FileMenu
        JMenuItem Films = FileMenu.add(new JMenuItem("Films"));
        JMenuItem Sessions = FileMenu.add(new JMenuItem("Sessions"));
        JMenuItem Persons = FileMenu.add(new JMenuItem("Actors and Producers"));
        JMenuItem Month = FileMenu.add(new JMenuItem("For a month"));

        Month.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame AddMonthFrame = getAddMonthDialog();
                AddMonthFrame.setVisible(true);
            }
        });

        FileMenu.addSeparator();

        JMenuItem GeneratePDF = FileMenu.add(new JMenuItem("Generate PDF"));

        FileMenu.addSeparator();

        FileMenu.add(new JMenuItem("Save", 'S'));

        JMenuItem Exit = FileMenu.add(new JMenuItem("Exit", 'E'));
        Exit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("Exit from APP");
                System.exit(0);
            }
        });
        Exit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));

        //EditMenu
        JMenuItem addFilm = AddMenu.add(new JMenuItem("Film"));
        JMenuItem addSession = AddMenu.add(new JMenuItem("Session"));
        JMenuItem addPerson = AddMenu.add(new JMenuItem("Actor or Producer"));

        addFilm.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame AddFilmFrame = getAddFilmDialog();
                AddFilmFrame.setVisible(true);
            }
        });

        addSession.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addSession = getAddSessionDialog();
                addSession.setVisible(true);
            }
        });

        addPerson.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame AddPersonFrame = getAddPersonDialog();
                AddPersonFrame.setVisible(true);
            }
        });


        frame.setJMenuBar(MenuBar);

        GridBagLayout gridBagLayout = new GridBagLayout();

        jPanel.setLayout(new BorderLayout());

        GeneratePDF.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GeneratePDF("films.xml", "films.pdf");
                    logger.info("PDF Generate!");
                    JOptionPane.showMessageDialog(frame,
                            "PDF - generated!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException | XMLFileNotFoundExeption ex) {
                    logger.warn("PDF Generate error - XML File Not Found");
                    ex.printStackTrace();
                }
            }
        });

        DefaultTableModel TableModel = new DefaultTableModel(
                data.getFilmList(),
                new String[]{"ID", "Name", "Release Year", "Genre"}
        );



        Films.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableModel.setDataVector(
                        data.getFilmList(),
                        new String[]{"ID", "Name", "Release Year", "Genre"}
                );
                ClassType = 0;
                logger.info("Get Data Films from DB");
            }
        });
        Sessions.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableModel.setDataVector(
                        data.getSessionList(),
                        new String[]{"ID", "Date", "Start Time", "Finish Time", "Number of tickets"}
                );
                ClassType = 1;
                logger.info("Get Data Sessions from DB");
            }
        });
        Persons.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableModel.setDataVector(
                        data.getPersonList(),
                        new String[]{"ID", "Name", "Surname", "Years Old", "Status"}
                );
                ClassType = 2;
                logger.info("Get Data Persons from DB");
            }
        });

        JTable table = new JTable(TableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        jPanel.add(scrollPane,BorderLayout.CENTER);

        JButton DeleteButton = new JButton("Delete");
        DeleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    logger.info("Deleted Row " + table.getSelectedRow() + " in table");
                    if(ClassType == 0){ data.deleteFilm(table.getSelectedRow()); }
                    if(ClassType == 1){ data.deleteSession(table.getSelectedRow()); }
                    if(ClassType == 2){ data.deletePerson(table.getSelectedRow()); }
                    TableModel.removeRow(table.getSelectedRow());
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Please select row",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }


            }
        });

        JButton SaveButton = new JButton("Save");
        SaveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String FileName = "films.xml";
                try{
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    org.w3c.dom.Document doc = builder.newDocument();

                    Node FilmList = doc.createElement("FilmList");
                    doc.appendChild(FilmList);
                    for (int i = 0; i < TableModel.getRowCount(); i++) {
                        Element Film = doc.createElement("film");
                        FilmList.appendChild(Film);
                        Film.setAttribute("id", (String) TableModel.getValueAt(i, 0));
                        Film.setAttribute("name", (String) TableModel.getValueAt(i, 1));
                        Film.setAttribute("release_year", (String) TableModel.getValueAt(i, 2));
                        Film.setAttribute("genre", (String) TableModel.getValueAt(i, 3));
                    }

                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    java.io.FileWriter writer = new FileWriter(FileName);
                    trans.transform(new DOMSource(doc), new StreamResult(writer));

                    logger.info("XML File saved!");
                    JOptionPane.showMessageDialog(frame,
                            "Data saved to XML!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }catch (ParserConfigurationException | TransformerException | IOException a) { a.printStackTrace(); }
            }
        });

        JButton ReloadButton = new JButton("Reload");
        ReloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ClassType == 0){
                    TableModel.setDataVector(
                            data.getFilmList(),
                            new String[]{"ID", "Name", "Release Year", "Genre"}
                    );
                }
                if(ClassType == 1){
                    TableModel.setDataVector(
                            data.getSessionList(),
                            new String[]{"ID", "Date", "Start Time", "Finish Time", "Number of tickets"}
                    );
                }
                if(ClassType == 2){
                    TableModel.setDataVector(
                            data.getPersonList(),
                            new String[]{"ID", "Name", "Surname", "Years Old", "Status"}
                    );
                }
                JOptionPane.showMessageDialog(frame,
                        "Data was reloaded!",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout());


        JButton FilmInfo = new JButton("Info about Film");
        FilmInfo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ClassType != 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Please return to Film list",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (table.getSelectedRow() == -1){
                    JOptionPane.showMessageDialog(frame,
                            "Please select Film",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JFrame InfoFilmFrame = getInfoFilmDialog(table.getSelectedRow());
                    InfoFilmFrame.setVisible(true);
                }
            }
        });

        bPanel.add(FilmInfo);
        bPanel.add(DeleteButton);
        bPanel.add(SaveButton);
        bPanel.add(ReloadButton);


        jPanel.add(bPanel,BorderLayout.SOUTH);


        jPanel.revalidate();
        frame.setVisible(true);
    }
}
