/*
 * RHQ Management Platform
 * Copyright (C) 2005-2008 Red Hat, Inc.
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
package org.rhq.enterprise.server.cloud;

import javax.ejb.Local;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.cloud.AffinityGroup;
import org.rhq.core.domain.cloud.Server;
import org.rhq.core.domain.cloud.composite.AffinityGroupCountComposite;
import org.rhq.core.domain.resource.Agent;
import org.rhq.core.domain.util.PageControl;
import org.rhq.core.domain.util.PageList;

/**
 * @author Joseph Marques
 */
@Local
public interface AffinityGroupManagerLocal {
    AffinityGroup getById(Subject subject, int affinityGroupId);

    /**
     * Creates a new affinity group.
     * 
     * Subject needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param affinityGroup @see AffinityGroup
     * @return new id of the group
     * @throws AffinityGroupException
     */
    int create(Subject subject, AffinityGroup affinityGroup) throws AffinityGroupException;

    /**
     * Deletes the affinity groups.
     * 
     * Subject needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param affinityGroupIds
     * @return
     */
    int delete(Subject subject, Integer[] affinityGroupIds);

    /**
     * Updates the affinity group.
     * 
     * Subject needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param affinityGroup @see AffinityGroup
     * @return
     * @throws AffinityGroupException
     */
    AffinityGroup update(Subject subject, AffinityGroup affinityGroup) throws AffinityGroupException;

    /**
     * Adds agents to the affinity group.
     * 
     * Subject needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param affinityGroupId
     * @param agentIds
     */
    void addAgentsToGroup(Subject subject, int affinityGroupId, Integer[] agentIds);

    /**
     * This should only be called if the agent is currently assigned to an affinity group and that group
     * is being removed, setting the agent to no affinity. Otherwise, unnecessary partition events can be generated.
     * 
     * Subjects needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param agentIds
     */
    void removeAgentsFromGroup(Subject subject, Integer[] agentIds);

    /**
     * Adds servers to the affinity group.
     * 
     * Subject needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param affinityGroupId
     * @param serverIds
     */
    void addServersToGroup(Subject subject, int affinityGroupId, Integer[] serverIds);

    /**
     * This should only be called if the server is currently assigned to an affinity group and that group
     * is being removed, setting the server to no affinity. Otherwise, unnecessary partition events can be generated.
     * 
     * Subject needs MANAGE_SETTINGS and MANAGE_INVENTORY permissions.
     * 
     * @param subject the Subject instance
     * @param agentIds
     */
    void removeServersFromGroup(Subject subject, Integer[] serverIds);

    int getAffinityGroupCount();

    /**
     * @deprecated portal war was using it (use finders <code>AgentManagerBean.findAgentsByCriteria()</code> and 
     * <code>TopologyManagerBean.findServersByCriteria()</code> instead)
     */
    PageList<Server> getServerMembers(Subject subject, int affinityGroupId, PageControl pageControl);

    /**
     * @deprecated portal war was using it (use finders <code>AgentManagerBean.findAgentsByCriteria()</code> and 
     * <code>TopologyManagerBean.findServersByCriteria()</code> instead)
     */
    PageList<Server> getServerNonMembers(Subject subject, int affinityGroupId, PageControl pageControl);

    /**
     * @deprecated portal war was using it (use finders <code>AgentManagerBean.findAgentsByCriteria()</code> and 
     * <code>TopologyManagerBean.findServersByCriteria()</code> instead)
     */
    PageList<Agent> getAgentMembers(Subject subject, int affinityGroupId, PageControl pageControl);

    /**
     * @deprecated portal war was using it (use finders <code>AgentManagerBean.findAgentsByCriteria()</code> and 
     * <code>TopologyManagerBean.findServersByCriteria()</code> instead)
     */
    PageList<Agent> getAgentNonMembers(Subject subject, int affinityGroupId, PageControl pageControl);

    PageList<AffinityGroupCountComposite> getComposites(Subject subject, PageControl pageControl);
}
