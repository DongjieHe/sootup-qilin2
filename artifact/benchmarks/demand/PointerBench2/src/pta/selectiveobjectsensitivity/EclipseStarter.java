package pta.selectiveobjectsensitivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

abstract interface Location {
    public abstract boolean allowsDefault();

    public abstract URL getDefault();

    public abstract Location getParentLocation();

    public abstract URL getURL();

    public abstract boolean isSet();

    public abstract boolean isReadOnly();

    public abstract boolean setURL(URL paramURL, boolean paramBoolean)
            throws IllegalStateException;
}

public class EclipseStarter {
    public static final String PROP_BUNDLES = "osgi.bundles";
    public static final String PROP_BUNDLES_STARTLEVEL = "osgi.bundles.defaultStartLevel";
    public static final String PROP_EXTENSIONS = "osgi.framework.extensions";
    public static final String PROP_INITIAL_STARTLEVEL = "osgi.startLevel";
    public static final String PROP_DEBUG = "osgi.debug";
    public static final String PROP_DEV = "osgi.dev";
    public static final String PROP_CLEAN = "osgi.clean";
    public static final String PROP_CONSOLE = "osgi.console";
    public static final String PROP_CONSOLE_CLASS = "osgi.consoleClass";
    public static final String PROP_CHECK_CONFIG = "osgi.checkConfiguration";
    public static final String PROP_OS = "osgi.os";
    public static final String PROP_WS = "osgi.ws";
    public static final String PROP_NL = "osgi.nl";
    public static final String PROP_ARCH = "osgi.arch";
    public static final String PROP_ADAPTOR = "osgi.adaptor";
    public static final String PROP_SYSPATH = "osgi.syspath";
    public static final String PROP_LOGFILE = "osgi.logfile";
    public static final String PROP_FRAMEWORK = "osgi.framework";
    public static final String PROP_INSTALL_AREA = "osgi.install.area";
    public static final String PROP_FRAMEWORK_SHAPE = "osgi.framework.shape";
    public static final String PROP_NOSHUTDOWN = "osgi.noShutdown";
    public static final String PROP_EXITCODE = "eclipse.exitcode";
    public static final String PROP_EXITDATA = "eclipse.exitdata";
    public static final String PROP_CONSOLE_LOG = "eclipse.consoleLog";
    public static final String PROP_IGNOREAPP = "eclipse.ignoreApp";
    public static final String PROP_REFRESH_BUNDLES = "eclipse.refreshBundles";
    protected static final String DEFAULT_ADAPTOR_CLASS = "org.eclipse.core.runtime.adaptor.EclipseAdaptor";
    protected static final String DEFAULT_CONSOLE_CLASS = "org.eclipse.osgi.framework.internal.core.FrameworkConsole";
    public static boolean debug = false;

    public static void main(String[] args) throws Exception {
        processCommandLine(args);
        LocationManager.initializeLocations();

        finalizeProperties();
    }

    private static String[] processCommandLine(String[] args)
            throws Exception {
        int[] configArgs = new int[args.length];
        configArgs[0] = -1;
        int configArgIndex = 0;
        for (int i = 0; i < args.length; i++) {
            boolean found = false;
            if ((args[i].equalsIgnoreCase("-debug")) && ((i + 1 == args.length) || ((i + 1 < args.length) && (args[(i + 1)].startsWith("-"))))) {
                System.getProperties().put("osgi.debug", "");
                debug = true;
                found = true;
            }
            if ((args[i].equalsIgnoreCase("-dev")) && ((i + 1 == args.length) || ((i + 1 < args.length) && (args[(i + 1)].startsWith("-"))))) {
                System.getProperties().put("osgi.dev", "");
                found = true;
            }
            if (args[i].equalsIgnoreCase("-initialize")) {
                found = true;
            }
            if (args[i].equalsIgnoreCase("-clean")) {
                System.getProperties().put("osgi.clean", "true");
                found = true;
            }
            if (args[i].equalsIgnoreCase("-consoleLog")) {
                System.getProperties().put("eclipse.consoleLog", "true");
                found = true;
            }
            if ((args[i].equalsIgnoreCase("-console")) && ((i + 1 == args.length) || ((i + 1 < args.length) && (args[(i + 1)].startsWith("-"))))) {
                System.getProperties().put("osgi.console", "");
                found = true;
            }
            if (args[i].equalsIgnoreCase("-noExit")) {
                System.getProperties().put("osgi.noShutdown", "true");
                found = true;
            }
            if (found) {
                configArgs[(configArgIndex++)] = i;
            } else if ((i != args.length - 1) && (!args[(i + 1)].startsWith("-"))) {
                String arg = args[(++i)];
                if (args[(i - 1)].equalsIgnoreCase("-console")) {
                    System.getProperties().put("osgi.console", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-configuration")) {
                    System.getProperties().put("osgi.configuration.area", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-data")) {
                    System.getProperties().put("osgi.instance.area", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-user")) {
                    System.getProperties().put("osgi.user.area", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-dev")) {
                    System.getProperties().put("osgi.dev", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-debug")) {
                    System.getProperties().put("osgi.debug", arg);
                    debug = true;
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-ws")) {
                    System.getProperties().put("osgi.ws", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-os")) {
                    System.getProperties().put("osgi.os", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-arch")) {
                    System.getProperties().put("osgi.arch", arg);
                    found = true;
                }
                if (args[(i - 1)].equalsIgnoreCase("-nl")) {
                    System.getProperties().put("osgi.nl", arg);
                    found = true;
                }
                if (found) {
                    configArgs[(configArgIndex++)] = (i - 1);
                    configArgs[(configArgIndex++)] = i;
                }
            }
        }

        String[] appArgs = new String[args.length - configArgIndex];
        String[] frameworkArgs = new String[configArgIndex];
        configArgIndex = 0;
        int j = 0;
        int k = 0;
        for (int i = 0; i < args.length; i++) {
            if (i == configArgs[configArgIndex]) {
                frameworkArgs[(k++)] = args[i];
                configArgIndex++;
            } else {
                appArgs[(j++)] = args[i];
            }
        }

        return appArgs;
    }

    protected static String getSysPath() {
        String result = System.getProperty("osgi.syspath");
        if (result != null) {
            return result;
        }
        result = getSysPathFromURL(System.getProperty("osgi.framework"));
        if (result == null) {
            result = getSysPathFromCodeSource();
        }
        if (result == null) {
            throw new IllegalStateException("Can not find the system path.");
        }
        if (Character.isUpperCase(result.charAt(0))) {
            char[] chars = result.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            result = new String(chars);
        }
        System.getProperties().put("osgi.syspath", result);
        return result;
    }

    private static String getSysPathFromURL(String urlSpec) {
        if (urlSpec == null) {
            return null;
        }
        URL url = null;
        try {
            url = new URL(urlSpec);
        } catch (MalformedURLException localMalformedURLException) {
            return null;
        }
        File fwkFile = new File(url.getFile());
        fwkFile = new File(fwkFile.getAbsolutePath());
        fwkFile = new File(fwkFile.getParent());
        return fwkFile.getAbsolutePath();
    }

    private static String getSysPathFromCodeSource() {
        ProtectionDomain pd = EclipseStarter.class.getProtectionDomain();
        if (pd == null) {
            return null;
        }
        CodeSource cs = pd.getCodeSource();
        if (cs == null) {
            return null;
        }
        URL url = cs.getLocation();
        if (url == null) {
            return null;
        }
        String result = url.getFile();
        if (result.endsWith(".jar")) {
            result = result.substring(0, result.lastIndexOf('/'));
            if ("folder".equals(System.getProperty("osgi.framework.shape"))) {
                result = result.substring(0, result.lastIndexOf('/'));
            }
        } else {
            if (result.endsWith("/")) {
                result = result.substring(0, result.length() - 1);
            }
            result = result.substring(0, result.lastIndexOf('/'));
            result = result.substring(0, result.lastIndexOf('/'));
        }
        return result;
    }

    private static void finalizeProperties() {
        if ((System.getProperty("osgi.dev") != null) && (System.getProperty("osgi.checkConfiguration") == null)) {
            System.getProperties().put("osgi.checkConfiguration", "true");
        }
    }
}

class LocationManager {
    public static final String READ_ONLY_AREA_SUFFIX = ".readOnly";
    public static final String PROP_INSTALL_AREA = "osgi.install.area";
    public static final String PROP_CONFIG_AREA = "osgi.configuration.area";
    public static final String PROP_CONFIG_AREA_DEFAULT = "osgi.configuration.area.default";
    public static final String PROP_SHARED_CONFIG_AREA = "osgi.sharedConfiguration.area";
    public static final String PROP_INSTANCE_AREA = "osgi.instance.area";
    public static final String PROP_INSTANCE_AREA_DEFAULT = "osgi.instance.area.default";
    public static final String PROP_USER_AREA = "osgi.user.area";
    public static final String PROP_USER_AREA_DEFAULT = "osgi.user.area.default";
    public static final String PROP_MANIFEST_CACHE = "osgi.manifest.cache";
    public static final String PROP_USER_HOME = "user.home";
    public static final String PROP_USER_DIR = "user.dir";
    public static final String BUNDLES_DIR = "bundles";
    public static final String STATE_FILE = ".state";
    public static final String LAZY_FILE = ".lazy";
    public static final String BUNDLE_DATA_FILE = ".bundledata";
    public static final String MANIFESTS_DIR = "manifests";
    public static final String CONFIG_FILE = "config.ini";
    public static final String ECLIPSE_PROPERTIES = "eclipse.properties";
    private static Location installLocation = null;
    private static Location configurationLocation = null;
    private static Location instanceLocation = null;

    public static URL buildURL(String spec, boolean trailingSlash) {
        if (spec == null) {
            return null;
        }
        boolean isFile = spec.startsWith("file:");
        try {
            if (isFile) {
                return adjustTrailingSlash(new File(spec.substring(5)).toURL(), trailingSlash);
            }
            return new URL(spec);
        } catch (MalformedURLException localMalformedURLException1) {
            if (isFile) {
                return null;
            }
            try {
                return adjustTrailingSlash(new File(spec).toURL(), trailingSlash);
            } catch (MalformedURLException localMalformedURLException2) {
            }
        }
        return null;
    }

    private static URL adjustTrailingSlash(URL url, boolean trailingSlash)
            throws MalformedURLException {
        String file = url.getFile();
        if (trailingSlash == file.endsWith("/")) {
            return url;
        }
        file = trailingSlash ? file + "/" : file.substring(0, file.length() - 1);
        return new URL(url.getProtocol(), url.getHost(), file);
    }

    private static void mungeConfigurationLocation() {
        String location = System.getProperty("osgi.configuration.area");
        if (location != null) {
            location = buildURL(location, false).toExternalForm();
            if (location.endsWith(".cfg")) {
                int index = location.lastIndexOf('/');
                location = location.substring(0, index + 1);
            }
            if (!location.endsWith("/")) {
                location = location + "/";
            }
            System.getProperties().put("osgi.configuration.area", location);
        }
    }

    public static void initializeLocations() {
        Location temp = buildLocation("osgi.user.area.default", null, "", false);
        URL defaultLocation = temp == null ? null : temp.getURL();
        if (defaultLocation == null) {
            defaultLocation = buildURL(new File(System.getProperty("user.home"), "user").getAbsolutePath(), true);
        }
        buildLocation("osgi.user.area", defaultLocation, "", false);

        temp = buildLocation("osgi.instance.area.default", null, "", false);
        defaultLocation = temp == null ? null : temp.getURL();
        if (defaultLocation == null) {
            defaultLocation = buildURL(new File(System.getProperty("user.dir"), "workspace").getAbsolutePath(), true);
        }
        instanceLocation = buildLocation("osgi.instance.area", defaultLocation, "", false);

        mungeConfigurationLocation();

        temp = buildLocation("osgi.configuration.area.default", null, "", false);
        defaultLocation = temp == null ? null : temp.getURL();
        if (defaultLocation == null) {
            defaultLocation = buildURL(computeDefaultConfigurationLocation(), true);
        }
        configurationLocation = buildLocation("osgi.configuration.area", defaultLocation, "", false);

        URL parentLocation = computeSharedConfigurationLocation();
        if ((parentLocation != null) && (!parentLocation.equals(configurationLocation.getURL()))) {
            Location parent = new BasicLocation(null, parentLocation, true);
            ((BasicLocation) configurationLocation).setParent(parent);
        }
        initializeDerivedConfigurationLocations();
    }

    private static Location buildLocation(String property, URL defaultLocation, String userDefaultAppendage, boolean readOnlyDefault) {
        String location = (String) System.getProperties().remove(property);

        String userReadOnlySetting = System.getProperty(property + ".readOnly");
        boolean readOnly = userReadOnlySetting == null ? readOnlyDefault : Boolean.valueOf(userReadOnlySetting).booleanValue();
        if (location == null) {
            return new BasicLocation(property, defaultLocation, readOnly);
        }
        String trimmedLocation = location.trim();
        if (trimmedLocation.equalsIgnoreCase("@none")) {
            return null;
        }
        if (trimmedLocation.equalsIgnoreCase("@noDefault")) {
            return new BasicLocation(property, null, readOnly);
        }
        if (trimmedLocation.startsWith("@user.home")) {
            String base = substituteVar(location, "@user.home", "user.home");
            location = new File(base, userDefaultAppendage).getAbsolutePath();
        } else if (trimmedLocation.startsWith("@user.dir")) {
            String base = substituteVar(location, "@user.dir", "user.dir");
            location = new File(base, userDefaultAppendage).getAbsolutePath();
        }
        URL url = buildURL(location, true);
        BasicLocation result = null;
        if (url != null) {
            result = new BasicLocation(property, null, readOnly);
            result.setURL(url, false);
        }
        return result;
    }

    private static String substituteVar(String source, String var, String prop) {
        String value = System.getProperty(prop, "");
        return value + source.substring(var.length());
    }

    private static void initializeDerivedConfigurationLocations() {
        if (System.getProperty("osgi.manifest.cache") == null) {
            System.getProperties().put("osgi.manifest.cache", getConfigurationFile("manifests").getAbsolutePath());
        }
    }

    private static URL computeInstallConfigurationLocation() {
        String property = System.getProperty("osgi.install.area");
        if (property != null) {
            try {
                return new URL(property);
            } catch (MalformedURLException localMalformedURLException) {
            }
        }
        return null;
    }

    private static URL computeSharedConfigurationLocation() {
        String property = System.getProperty("osgi.sharedConfiguration.area");
        if (property == null) {
            return null;
        }
        try {
            URL sharedConfigurationURL = new URL(property);
            if (sharedConfigurationURL.getPath().startsWith("/")) {
                return sharedConfigurationURL;
            }
            URL installURL = installLocation.getURL();
            if (!sharedConfigurationURL.getProtocol().equals(installURL.getProtocol())) {
                return sharedConfigurationURL;
            }
            sharedConfigurationURL = new URL(installURL, sharedConfigurationURL.getPath());
            System.getProperties().put("osgi.sharedConfiguration.area", sharedConfigurationURL.toExternalForm());
        } catch (MalformedURLException localMalformedURLException) {
        }
        return null;
    }

    private static String computeDefaultConfigurationLocation() {
        URL installURL = computeInstallConfigurationLocation();
        if (installURL != null) {
            File installDir = new File(installURL.getFile());
            if (("file".equals(installURL.getProtocol())) && (canWrite(installDir))) {
                return new File(installDir, "configuration").getAbsolutePath();
            }
        }
        return computeDefaultUserAreaLocation("configuration");
    }

    private static boolean canWrite(File installDir) {
        if (!installDir.canWrite()) {
            return false;
        }
        if (!installDir.isDirectory()) {
            return false;
        }
        File fileTest = null;
        try {
            fileTest = File.createTempFile("writtableArea", null, installDir);
        } catch (IOException localIOException) {
            return false;
        } finally {
            if (fileTest != null) {
                fileTest.delete();
            }
        }
        return true;
    }

    private static String computeDefaultUserAreaLocation(String pathAppendage) {
        String installProperty = System.getProperty("osgi.install.area");
        URL installURL = buildURL(installProperty, true);
        if (installURL == null) {
            return null;
        }
        File installDir = new File(installURL.getFile());
        String appName = ".eclipse";
        File eclipseProduct = new File(installDir, ".eclipseproduct");
        if (eclipseProduct.exists()) {
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(eclipseProduct));
                String appId = props.getProperty("id");
                if ((appId == null) || (appId.trim().length() == 0)) {
                    appId = "eclipse";
                }
                String appVersion = props.getProperty("version");
                if ((appVersion == null) || (appVersion.trim().length() == 0)) {
                    appVersion = "";
                }
                appName = appName + File.separator + appId + "_" + appVersion;
            } catch (IOException localIOException) {
            }
        }
        String userHome = System.getProperty("user.home");
        return new File(userHome, appName + "/" + pathAppendage).getAbsolutePath();
    }


    public static Location getConfigurationLocation() {
        return configurationLocation;
    }

    public static Location getInstallLocation() {
        return installLocation;
    }

    public static Location getInstanceLocation() {
        return instanceLocation;
    }

    public static File getOSGiConfigurationDir() {
        return new File(configurationLocation.getURL().getFile(), "org.eclipse.osgi");
    }

    public static File getConfigurationFile(String filename) {
        File dir = getOSGiConfigurationDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, filename);
    }
}

class BasicLocation implements Location {
    public static final String PROP_OSGI_LOCKING = "osgi.locking";
    public static boolean DEBUG;
    private static String LOCK_FILENAME = ".metadata/.lock";
    private boolean isReadOnly;
    private URL location = null;
    private Location parent;
    private URL defaultValue;
    private String property;

    public BasicLocation(String property, URL defaultValue, boolean isReadOnly) {
        this.property = property;
        this.defaultValue = defaultValue;
        this.isReadOnly = isReadOnly;
    }

    public boolean allowsDefault() {
        return this.defaultValue != null;
    }

    public URL getDefault() {
        return this.defaultValue;
    }

    public Location getParentLocation() {
        return this.parent;
    }

    public synchronized URL getURL() {
        if ((this.location == null) && (this.defaultValue != null)) {
            setURL(this.defaultValue, false);
        }
        return this.location;
    }

    public synchronized boolean isSet() {
        return this.location != null;
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public synchronized boolean setURL(URL value, boolean lock)
            throws IllegalStateException {
        if (value.getProtocol().equalsIgnoreCase("file")) {
            new File(value.getFile(), LOCK_FILENAME);
        }
        this.location = LocationManager.buildURL(value.toExternalForm(), true);
        if (this.property != null) {
            System.getProperties().put(this.property, this.location.toExternalForm());
        }
        return lock;
    }

    public synchronized void setParent(Location value) {
        this.parent = value;
    }
}

