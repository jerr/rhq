/*
 * RHQ Management Platform
 * Copyright (C) 2005-2014 Red Hat, Inc.
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
package org.rhq.core.domain.cloud;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Michael Burman
 */
public class StorageNodeConfigurationCompositeTest {

    @Test
    public void testEqualityMethods() throws Exception {
        StorageNode mockStorageNode = new StorageNode();
        StorageNodeConfigurationComposite oldConfig = new StorageNodeConfigurationComposite(mockStorageNode);
        StorageNodeConfigurationComposite newConfig = new StorageNodeConfigurationComposite(mockStorageNode);

        // oldConfig and newConfig have same values
        oldConfig.setCommitLogLocation("a");
        newConfig.setCommitLogLocation("a");

        assertTrue(oldConfig.equals(newConfig));

        // newConfig does have new values
        newConfig.setHeapNewSize("123m");
        assertFalse(oldConfig.equals(newConfig));

        // do dataDirectories match?
        newConfig.setSavedCachesLocation("d");
        assertFalse(newConfig.equals(oldConfig));
        assertFalse(newConfig.isDirectoriesEqual(oldConfig));
    }

}
