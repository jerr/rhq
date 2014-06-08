package org.rhq.plugins.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;

/**
 * Descovery members of Apache Balancer component for Apache balancer-manager handler.
 *
 * @author Jeremie Lagarde
 */
public class ApacheBalancerMemberDiscoveryComponent implements ResourceDiscoveryComponent<ApacheBalancerComponent> {

    private static final Pattern BALANCER_PATTERN = Pattern.compile("<a href(.*)>balancer://(.*)</a>");
    private static final Pattern MEMBER_PATTERN = Pattern.compile("<a href=\\\"(.*)\\?b=(.*)&w=(.*)&nonce.*>(.*)</a>");

    /* (non-Javadoc)
     * @see org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent#discoverResources(org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext)
     */
    public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<ApacheBalancerComponent> context)
        throws InvalidPluginConfigurationException, Exception {

        Set<DiscoveredResourceDetails> discoveredResources = new LinkedHashSet<DiscoveredResourceDetails>();
        ApacheBalancerComponent balancer = context.getParentResourceComponent();
        ApacheLocationComponent location = (ApacheLocationComponent) context.getParentResourceContext()
            .getParentResourceComponent();
        URL url = new URL(location.getURL());
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(2000);
        Map<String, List<String>> result = parse(connection.getInputStream());
        List<String> workers = result.get(balancer.getName());
        for (String worker : workers) {
            discoveredResources.add(new DiscoveredResourceDetails(context.getResourceType(), worker, worker, null,
                null, null, null));
        }
        return discoveredResources;
    }

    private Map<String, List<String>> parse(InputStream balancerPage) {
        String currentBalancer = null;
        List<String> currentMembers = null;
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(balancerPage, "UTF-8"));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher balancerMatcher = BALANCER_PATTERN.matcher(line);
                    if (balancerMatcher.find()) {
                        String balancer = balancerMatcher.group(2);
                        if (!balancer.equals(currentBalancer)) {
                            currentBalancer = balancer;
                            currentMembers = new ArrayList<String>();
                            results.put(currentBalancer, currentMembers);
                        }
                    } else if (currentMembers != null) {
                        Matcher memberMatcher = MEMBER_PATTERN.matcher(line);
                        if (memberMatcher.find()) {
                            currentMembers.add(memberMatcher.group(4));
                        }
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