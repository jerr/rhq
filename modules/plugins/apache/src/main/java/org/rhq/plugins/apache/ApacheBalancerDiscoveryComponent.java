package org.rhq.plugins.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.core.util.StringUtil;

/**
 * Apache Balancer component for Apache balancer-manager handler.
 * 
 * @author Jeremie Lagarde
 */
public class ApacheBalancerDiscoveryComponent implements ResourceDiscoveryComponent<ApacheLocationComponent> {

    public static final String BALANCER_HANDLER = "balancer-manager";
    private static final Pattern BALANCER_PATTERN = Pattern.compile("<a href(.*)>balancer://(.*)</a>");

    /* (non-Javadoc)
     * @see org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent#discoverResources(org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext)
     */
    public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<ApacheLocationComponent> context)
        throws InvalidPluginConfigurationException, Exception {

        Set<DiscoveredResourceDetails> discoveredResources = new LinkedHashSet<DiscoveredResourceDetails>();
        ApacheLocationComponent location = context.getParentResourceComponent();
        String handler = ((PropertySimple) location.resourceContext.getPluginConfiguration().get(
            ApacheLocationComponent.HANDLER_PROP)).getStringValue();
        if (StringUtil.isNotBlank(handler) && BALANCER_HANDLER.equals(handler)) {
            URL url = new URL(location.getURL());
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(2000);
            List<String> result = parse(connection.getInputStream());
            for (String balancer : result) {
                discoveredResources.add(new DiscoveredResourceDetails(context.getResourceType(), balancer, balancer,
                    null, null, null, null));
            }
        }
        return discoveredResources;
    }

    private List<String> parse(InputStream balancerPage) {
        List<String> results = new ArrayList<String>();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(balancerPage, "UTF-8"));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher m = BALANCER_PATTERN.matcher(line);
                    if (m.find()) {
                        results.add(m.group(2));
                    }
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                balancerPage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}