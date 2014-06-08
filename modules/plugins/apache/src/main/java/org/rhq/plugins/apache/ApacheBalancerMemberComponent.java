/*
 * RHQ Management Platform
 * Copyright (C) 2005-2009 Red Hat, Inc.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2, as
 * published by the Free Software Foundation, and/or the GNU Lesser
 * General Public License, version 2.1, also as published by the Free
 * Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package org.rhq.plugins.apache;

import java.util.Set;

import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;

/**
 * Represents balancer menbers of an Apache balancer-manager handler in Apache configuration &lt;Location&gt; section.
 * 
 * @author Jeremie Lagarde
 */
public class ApacheBalancerMemberComponent implements ResourceComponent<ApacheLocationComponent>, MeasurementFacet {

    ResourceContext<ApacheLocationComponent> resourceContext;

    public void start(ResourceContext<ApacheLocationComponent> context) throws InvalidPluginConfigurationException,
        Exception {
        resourceContext = context;
    }

    public void stop() {
    }

    public AvailabilityType getAvailability() {
        //TODO implement this
        return AvailabilityType.UP;
    }

    @Override
    public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> schedules) throws Exception {
    }
    
    public String getName() {
        return resourceContext.getResourceKey();
    }

}
