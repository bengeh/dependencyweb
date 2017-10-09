package org.owasp.dependencycheck;

import java.awt.*;
import java.io.*;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.apache.commons.compress.utils.IOUtils;
import org.owasp.dependencycheck.data.nvdcve.CveDB;
import org.owasp.dependencycheck.data.nvdcve.DatabaseException;
import org.owasp.dependencycheck.data.nvdcve.DatabaseProperties;
import org.owasp.dependencycheck.data.update.exception.UpdateException;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.dependency.Vulnerability;
import org.owasp.dependencycheck.ReportGenerator;
import org.owasp.dependencycheck.utils.InvalidSettingException;
import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.owasp.dependencycheck.exception.ReportException;
import org.owasp.dependencycheck.utils.Settings;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import org.owasp.dependencycheck.FileRepo;


import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.owasp.dependencycheck.FileService;
import org.owasp.dependencycheck.FileModel;

@Controller
public class FileController{


    @GetMapping("/")
    public String index(){
        return "welcome";
    }

    @RequestMapping("/partials/{page}")
    String partialHandler(@PathVariable("page") final String page){
        return page;
    }


    @GetMapping("/uploadFile")
    public String uploadPage(){
        return "upload";
    }


    private String fileName = "";
    private long startTime = 0;
    private File file = null;

    FileModel fm = new FileModel(fileName, startTime, file);
    FileEntity fe = new FileEntity();

    @Autowired
    FileService fs = new FileService();

    @PostMapping("/scanned")
    protected void processScanning(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException{

        final String path = "/Users/bengeh/Desktop";


        fileName = request.getParameter("fileName");


        System.out.println("Everything in the database: " + fs.getFilesinDB());

        //fs.findAFile(fileName);

        fs.getFileByFileName(fileName).getReportResults();


        System.out.println("this should be my rport results from the database: " + fs.getFileByFileName(fileName).getReportResults());

        System.out.println("this are the items in the table: " + fs.getFileByFileName(fileName));

        byte[] source = fs.getFileByFileName(fileName).getReportResults();
        System.out.println("this is the byte source from db: " + source);
        ByteArrayInputStream bis = new ByteArrayInputStream(source);
        //System.out.println("this is the byte inputstream from db: " + bis);

        OutputStream out = new FileOutputStream(fileName + ".html");

// Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = bis.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    try {
            System.setProperty("java.awt.headless", "false");
            String htmlFilePath = "file:///Users/bengeh/Desktop/DependencyCheck/dependencycheckparent/" + fileName + ".html"; // path to your new file

            URI uri = new URI(htmlFilePath);
            uri.normalize();
            Desktop.getDesktop().browse(uri);
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String page = "uploadStatusSuccessful.html";

        request.getRequestDispatcher(page).forward(request, response);


    }

    @PostMapping("/upload")
    protected void processRequest(HttpServletRequest request,
                                    HttpServletResponse response, RedirectAttributes redirectAttributes)
            throws ServletException, IOException, InvalidScanPathException, ReportException, ExceptionCollection {

        response.setContentType("text/html;charset=UTF-8");

        String page = "";

        try {

            // Create path components to save the file
            final String path = "/Users/bengeh/Desktop";
            final Part filePart = request.getPart("file");
            final String fileName = getFileName(filePart);

            OutputStream out = null;
            InputStream filecontent = null;
            final PrintWriter writer = response.getWriter();


            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            //File file = new File("/Users/bengeh/Desktop/template.py");
            //File file = new File("/Users/bengeh/Desktop/lab1/Lab1/src/frontend/Token.java");


            System.out.println("Before scanning the files!#@!$!#$@#$");


            long startTime = System.currentTimeMillis();
            Scan("HTML", fileName);
            long endTime = System.currentTimeMillis();

            long timeTaken = endTime - startTime;

            ByteArrayOutputStream bos = null;

            try {
                File file = new File("/Users/bengeh/Desktop/" + fileName + ".html");

                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                bos = new ByteArrayOutputStream();
                for (int len; (len = fis.read(buffer)) != -1; ) {
                    bos.write(buffer, 0, len);
                }
            }
            catch(FileNotFoundException e){
                System.err.println(e.getMessage());
            }
            catch(IOException e2){
                System.err.println(e2.getMessage());
            }

            System.out.println("this is the byte out put: " + bos);
            byte[] storage = bos.toByteArray();



            System.out.println("Total execution time: " + timeTaken);


           System.out.println("YAY, manage to scan the files!");


           fm.setFileName(fileName);
           fm.setTime(timeTaken);

           fs.addNewFile(fileName, storage, timeTaken);

            //redirectAttributes.addFlashAttribute("dataForNextPage", timeTaken);




            //app.run(array);
/*


            writer.println("New file " + fileName + " created at " + path);
            writer.println("The system took: " + (endTime - startTime) + " amount of time to execute");*/

            page = "uploadStatusSuccessful.html";



        } catch (FileNotFoundException fne) {
            /*writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());*/
            page = "uploadStatusError.html";

            String message = "file not found please choose a file before pressing submit";

            request.setAttribute("errorMessage", message);
        }



        /*finally {
            page = "uploadStatusSuccessful.html";
            if (out != null) {
                response.sendRedirect(page);
                out.close();
            }
            if (filecontent != null) {
                response.sendRedirect(page);
                filecontent.close();
            }
            if (writer != null) {
                response.sendRedirect(page);
                writer.close();
            }


        }*/
       /* finally {

            page = "uploadStatusSuccessful.html";

        }*/

        /*RequestDispatcher dd=request.getRequestDispatcher(page);
        dd.forward(request, response);*/


        //return "redirect:/" + page;
        request.getRequestDispatcher(page).forward(request, response);



    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    @RequestMapping("/uploadStatusError")
    public String uploadStatusError(){
        return "uploadStatusError";}



    @RequestMapping("/uploadStatusSuccessful")
    public String uploadStatusSuccessful(HttpSession session) {



        long timeTaken = fm.getTime();

        System.out.println("Finally get my time in the status message #$#$@@#$@#$@  " + timeTaken);

        session.setAttribute("mySessionAttribute", timeTaken);



        return "uploadStatusSuccessful";
    }

    @GetMapping("/about")
    public String aboutUs(){
        return "about";
    }

    @GetMapping("/navBar")
    public String navBar(){
        return "navigationBar";
    }

    @RequestMapping("/view")
    public void viewReport(){


        try {

            System.setProperty("java.awt.headless", "false");

            String htmlFilePath = "file:///Users/bengeh/Desktop/" + fm.getFileName() + ".html"; // path to your new file

            System.out.println("this is my report results: " + fm.getReportResults());
            URI uri = new URI(htmlFilePath);
            uri.normalize();
            Desktop.getDesktop().browse(uri);
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



    private int Scan(String outputFormat, String applicationName) throws InvalidScanPathException, DatabaseException, ExceptionCollection,
            ReportException{

        System.out.println("inside the scan method #$#@#$@$#");
        Engine engine = null;
        System.out.println("after engine engine = null");

        try{

            File file = new File("/Users/bengeh/Desktop/" + applicationName);

            fm.setReportResults(file);


            Settings.initialize();
            engine = new Engine();


            System.out.println("Inside the try for Scan() method and my file is from: " + file);
            System.out.println("this is what is inside my engine: " + engine);
            engine.scan(file);
            System.out.println("After engine.scan the files is completed and inside my engine: " + engine);

            int cvssFailScore = 5;
            String reportDirectory = "/Users/bengeh/Desktop";
            int retCode = 0;


            ExceptionCollection exCol = null;
            System.out.println("can i pass here? #$@#$@#$@#$@#$");
            try {
                System.out.println("inside the try for analyzing any dependencies");
                engine.analyzeDependencies();
                System.out.println("can I analyze any dependencies???");
            } catch (ExceptionCollection ex) {
                if (ex.isFatal()) {
                    System.out.println("entered the catch part of the code");
                    throw ex;
                }
                exCol = ex;
            }

            final List<Dependency> dependencies = engine.getDependencies();
            System.out.println("List of dependencies: " + dependencies + "%$%#$%#$%$#%#$");
            DatabaseProperties prop = null;
            try (CveDB cve = CveDB.getInstance()) {
                System.out.println("inside the try to find for any cve db %^%^$%^$%^$$");
                prop = cve.getDatabaseProperties();
                System.out.println("Are there any properties in my database???");
            } catch (DatabaseException ex) {
                //TODO shouldn't this be a fatal exception
                //    LOGGER.debug("Unable to retrieve DB Properties", ex);
            }
            final ReportGenerator report = new ReportGenerator(applicationName, dependencies, engine.getAnalyzers(), prop);

            try {
                report.write(reportDirectory, applicationName, outputFormat);
                System.out.println("Manage to print a report out $%$%#%");
            } catch (ReportException ex) {
                if (exCol != null) {
                    exCol.addException(ex);
                    throw exCol;
                } else {
                    throw ex;
                }
            }
            if (exCol != null && exCol.getExceptions().size() > 0) {
                throw exCol;
            }

            //Set the exit code based on whether we found a high enough vulnerability
            for (Dependency dep : dependencies) {
                if (!dep.getVulnerabilities().isEmpty()) {
                    for (Vulnerability vuln : dep.getVulnerabilities()) {
                        //        LOGGER.debug("VULNERABILITY FOUND " + dep.getDisplayFileName());
                        if (vuln.getCvssScore() > cvssFailScore) {
                            retCode = 1;
                        }
                    }
                }
            }

            return retCode;
    } finally {
            Settings.cleanup(true);
        if (engine != null) {
            engine.cleanup();
        }
    }
}





    /**
     * Only executes the update phase of dependency-check.
     *
     * @throws UpdateException thrown if there is an error updating
     * @throws DatabaseException thrown if a fatal error occurred and a
     * connection to the database could not be established
     */
    private void runUpdateOnly() throws UpdateException, DatabaseException {
        Engine engine = null;
        try {
            engine = new Engine();
            engine.doUpdates();
        } finally {
            if (engine != null) {
                engine.cleanup();
            }
        }
    }

    /**
     * Updates the global Settings.
     *
     * @param cli a reference to the CLI Parser that contains the command line
     * arguments used to set the corresponding settings in the core engine.
     *
     * @throws InvalidSettingException thrown when a user defined properties
     * file is unable to be loaded.
     */
    private void populateSettings(CliParser cli) throws InvalidSettingException {
        final boolean autoUpdate = cli.isAutoUpdate();
        final String connectionTimeout = cli.getConnectionTimeout();
        final String proxyServer = cli.getProxyServer();
        final String proxyPort = cli.getProxyPort();
        final String proxyUser = cli.getProxyUsername();
        final String proxyPass = cli.getProxyPassword();
        final String dataDirectory = cli.getDataDirectory();
        final File propertiesFile = cli.getPropertiesFile();
        final String suppressionFile = cli.getSuppressionFile();
        final String hintsFile = cli.getHintsFile();
        final String nexusUrl = cli.getNexusUrl();
        final String databaseDriverName = cli.getDatabaseDriverName();
        final String databaseDriverPath = cli.getDatabaseDriverPath();
        final String connectionString = cli.getConnectionString();
        final String databaseUser = cli.getDatabaseUser();
        final String databasePassword = cli.getDatabasePassword();
        final String additionalZipExtensions = cli.getAdditionalZipExtensions();
        final String pathToMono = cli.getPathToMono();
        final String cveMod12 = cli.getModifiedCve12Url();
        final String cveMod20 = cli.getModifiedCve20Url();
        final String cveBase12 = cli.getBaseCve12Url();
        final String cveBase20 = cli.getBaseCve20Url();
        final Integer cveValidForHours = cli.getCveValidForHours();
        final boolean experimentalEnabled = cli.isExperimentalEnabled();

        if (propertiesFile != null) {
            try {
                Settings.mergeProperties(propertiesFile);
            } catch (FileNotFoundException ex) {
                throw new InvalidSettingException("Unable to find properties file '" + propertiesFile.getPath() + "'", ex);
            } catch (IOException ex) {
                throw new InvalidSettingException("Error reading properties file '" + propertiesFile.getPath() + "'", ex);
            }
        }
        // We have to wait until we've merged the properties before attempting to set whether we use
        // the proxy for Nexus since it could be disabled in the properties, but not explicitly stated
        // on the command line
        final boolean nexusUsesProxy = cli.isNexusUsesProxy();
        if (dataDirectory != null) {
            Settings.setString(Settings.KEYS.DATA_DIRECTORY, dataDirectory);
        } else if (System.getProperty("basedir") != null) {
            final File dataDir = new File(System.getProperty("basedir"), "data");
            Settings.setString(Settings.KEYS.DATA_DIRECTORY, dataDir.getAbsolutePath());
        } else {
            final File jarPath = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            final File base = jarPath.getParentFile();
            final String sub = Settings.getString(Settings.KEYS.DATA_DIRECTORY);
            final File dataDir = new File(base, sub);
            Settings.setString(Settings.KEYS.DATA_DIRECTORY, dataDir.getAbsolutePath());
        }
        Settings.setBoolean(Settings.KEYS.AUTO_UPDATE, autoUpdate);
        Settings.setStringIfNotEmpty(Settings.KEYS.PROXY_SERVER, proxyServer);
        Settings.setStringIfNotEmpty(Settings.KEYS.PROXY_PORT, proxyPort);
        Settings.setStringIfNotEmpty(Settings.KEYS.PROXY_USERNAME, proxyUser);
        Settings.setStringIfNotEmpty(Settings.KEYS.PROXY_PASSWORD, proxyPass);
        Settings.setStringIfNotEmpty(Settings.KEYS.CONNECTION_TIMEOUT, connectionTimeout);
        Settings.setStringIfNotEmpty(Settings.KEYS.SUPPRESSION_FILE, suppressionFile);
        Settings.setStringIfNotEmpty(Settings.KEYS.HINTS_FILE, hintsFile);
        Settings.setIntIfNotNull(Settings.KEYS.CVE_CHECK_VALID_FOR_HOURS, cveValidForHours);

        //File Type Analyzer Settings
        Settings.setBoolean(Settings.KEYS.ANALYZER_EXPERIMENTAL_ENABLED, experimentalEnabled);
        Settings.setBoolean(Settings.KEYS.ANALYZER_JAR_ENABLED, !cli.isJarDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_ARCHIVE_ENABLED, !cli.isArchiveDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_PYTHON_DISTRIBUTION_ENABLED, !cli.isPythonDistributionDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_PYTHON_PACKAGE_ENABLED, !cli.isPythonPackageDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_AUTOCONF_ENABLED, !cli.isAutoconfDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_CMAKE_ENABLED, !cli.isCmakeDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NUSPEC_ENABLED, !cli.isNuspecDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_ASSEMBLY_ENABLED, !cli.isAssemblyDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_BUNDLE_AUDIT_ENABLED, !cli.isBundleAuditDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_OPENSSL_ENABLED, !cli.isOpenSSLDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_COMPOSER_LOCK_ENABLED, !cli.isComposerDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NODE_PACKAGE_ENABLED, !cli.isNodeJsDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_SWIFT_PACKAGE_MANAGER_ENABLED, !cli.isSwiftPackageAnalyzerDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_COCOAPODS_ENABLED, !cli.isCocoapodsAnalyzerDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_RUBY_GEMSPEC_ENABLED, !cli.isRubyGemspecDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_CENTRAL_ENABLED, !cli.isCentralDisabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NEXUS_ENABLED, !cli.isNexusDisabled());

        Settings.setStringIfNotEmpty(Settings.KEYS.ANALYZER_BUNDLE_AUDIT_PATH, cli.getPathToBundleAudit());
        Settings.setStringIfNotEmpty(Settings.KEYS.ANALYZER_NEXUS_URL, nexusUrl);
        Settings.setBoolean(Settings.KEYS.ANALYZER_NEXUS_USES_PROXY, nexusUsesProxy);
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_DRIVER_NAME, databaseDriverName);
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_DRIVER_PATH, databaseDriverPath);
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_CONNECTION_STRING, connectionString);
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_USER, databaseUser);
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_PASSWORD, databasePassword);
        Settings.setStringIfNotEmpty(Settings.KEYS.ADDITIONAL_ZIP_EXTENSIONS, additionalZipExtensions);
        Settings.setStringIfNotEmpty(Settings.KEYS.ANALYZER_ASSEMBLY_MONO_PATH, pathToMono);
        if (cveBase12 != null && !cveBase12.isEmpty()) {
            Settings.setString(Settings.KEYS.CVE_SCHEMA_1_2, cveBase12);
            Settings.setString(Settings.KEYS.CVE_SCHEMA_2_0, cveBase20);
            Settings.setString(Settings.KEYS.CVE_MODIFIED_12_URL, cveMod12);
            Settings.setString(Settings.KEYS.CVE_MODIFIED_20_URL, cveMod20);
        }
    }

    /**
     * Creates a file appender and adds it to logback.
     *
     * @param verboseLog the path to the verbose log file
     */
    private void prepareLogger(String verboseLog) {
        final StaticLoggerBinder loggerBinder = StaticLoggerBinder.getSingleton();
        final LoggerContext context = (LoggerContext) loggerBinder.getLoggerFactory();

        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%d %C:%L%n%-5level - %msg%n");
        encoder.setContext(context);
        encoder.start();
        final FileAppender<ILoggingEvent> fa = new FileAppender<>();
        fa.setAppend(true);
        fa.setEncoder(encoder);
        fa.setContext(context);
        fa.setFile(verboseLog);
        final File f = new File(verboseLog);
        String name = f.getName();
        final int i = name.lastIndexOf('.');
        if (i > 1) {
            name = name.substring(0, i);
        }
        fa.setName(name);
        fa.start();
        final ch.qos.logback.classic.Logger rootLogger = context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(fa);
    }

    /**
     * Takes a path and resolves it to be a canonical &amp; absolute path. The
     * caveats are that this method will take an Ant style file selector path
     * (../someDir/**\/*.jar) and convert it to an absolute/canonical path (at
     * least to the left of the first * or ?).
     *
     * @param path the path to canonicalize
     * @return the canonical path
     */
    protected String ensureCanonicalPath(String path) {
        String basePath;
        String wildCards = null;
        final String file = path.replace('\\', '/');
        if (file.contains("*") || file.contains("?")) {

            int pos = getLastFileSeparator(file);
            if (pos < 0) {
                return file;
            }
            pos += 1;
            basePath = file.substring(0, pos);
            wildCards = file.substring(pos);
        } else {
            basePath = file;
        }

        File f = new File(basePath);
        try {
            f = f.getCanonicalFile();
            if (wildCards != null) {
                f = new File(f, wildCards);
            }
        } catch (IOException ex) {
            System.out.println("IO exception");
        }
        return f.getAbsolutePath().replace('\\', '/');
    }

    /**
     * Returns the position of the last file separator.
     *
     * @param file a file path
     * @return the position of the last file separator
     */
    private int getLastFileSeparator(String file) {
        if (file.contains("*") || file.contains("?")) {
            int p1 = file.indexOf('*');
            int p2 = file.indexOf('?');
            p1 = p1 > 0 ? p1 : file.length();
            p2 = p2 > 0 ? p2 : file.length();
            int pos = p1 < p2 ? p1 : p2;
            pos = file.lastIndexOf('/', pos);
            return pos;
        } else {
            return file.lastIndexOf('/');
        }
    }

}
