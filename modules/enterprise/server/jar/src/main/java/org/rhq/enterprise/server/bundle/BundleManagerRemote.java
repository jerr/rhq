/*
 * RHQ Management Platform
 * Copyright (C) 2005-2009 Red Hat, Inc.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.rhq.enterprise.server.bundle;

import java.util.List;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.rhq.core.clientapi.agent.bundle.BundleScheduleResponse;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.bundle.Bundle;
import org.rhq.core.domain.bundle.BundleDeployDefinition;
import org.rhq.core.domain.bundle.BundleDeployment;
import org.rhq.core.domain.bundle.BundleDeploymentHistory;
import org.rhq.core.domain.bundle.BundleType;
import org.rhq.core.domain.bundle.BundleVersion;
import org.rhq.core.domain.criteria.BundleCriteria;
import org.rhq.core.domain.criteria.BundleDeployDefinitionCriteria;
import org.rhq.core.domain.criteria.BundleDeploymentCriteria;
import org.rhq.core.domain.criteria.BundleDeploymentHistoryCriteria;
import org.rhq.core.domain.criteria.BundleVersionCriteria;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.server.system.ServerVersion;

/**
 * Remote interface to the manager responsible for creating and managing bundles.
 *  
 * @author John Mazzitelli
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@WebService(targetNamespace = ServerVersion.namespace)
@Remote
public interface BundleManagerRemote {
    @WebMethod
    List<BundleType> getAllBundleTypes( //
        @WebParam(name = "subject") Subject subject);

    PageList<Bundle> findBundlesByCriteria(Subject subject, BundleCriteria criteria);

    PageList<BundleVersion> findBundleVersionsByCriteria(Subject subject, BundleVersionCriteria criteria);

    PageList<BundleDeployDefinition> findBundleDeployDefinitionsByCriteria(Subject subject,
        BundleDeployDefinitionCriteria criteria);

    void deleteBundles(Subject subject, int[] bundleIds);

    void deleteBundleVersions(Subject subject, int[] bundleVersionIds);

    @WebMethod
    List<BundleDeploymentHistory> findBundleDeploymentHistoryByCriteria(@WebParam(name = "subject") Subject subject,
        @WebParam(name = "criteria") BundleDeploymentHistoryCriteria criteria);

    PageList<BundleDeployment> findBundleDeploymentsByCriteria(Subject subject, BundleDeploymentCriteria criteria);

    /**
     * Deploy the bundle as described in the provided deploy definition to the specified resource.
     * Deployment is asynchronous so return of this method does not indicate deployments are complete. The
     * returned BundleDeployment can be used to track the history of the deployment.
     * 
     *  TODO: Add the scheduling capability, currently it's Immediate. 
     * 
     * @param subject must be InventoryManager
     * @param bundleDeployDefinitionId the BundleDeployDefinition being used to guide the deployments
     * @param resourceId the target resource (must exist), typically platforms, for the deployments
     * @return the BundleScheduleResponse created to track the deployment. 
     * @throws Exception
     */
    BundleScheduleResponse scheduleBundleDeployment(Subject subject, int bundleDeployDefinitionId, int resourceId)
        throws Exception;

    void addBundleDeploymentHistoryByBundleDeployment(BundleDeploymentHistory history) throws IllegalArgumentException;

}
