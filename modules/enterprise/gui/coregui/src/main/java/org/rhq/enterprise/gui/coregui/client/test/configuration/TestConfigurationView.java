/*
 * RHQ Management Platform
 * Copyright 2010-2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.rhq.enterprise.gui.coregui.client.test.configuration;

import java.util.EnumSet;
import java.util.Map;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.definition.ConfigurationDefinition;
import org.rhq.enterprise.gui.coregui.client.CoreGUI;
import org.rhq.enterprise.gui.coregui.client.components.configuration.ConfigurationEditor;
import org.rhq.enterprise.gui.coregui.client.components.configuration.PropertyValueChangeEvent;
import org.rhq.enterprise.gui.coregui.client.components.configuration.PropertyValueChangeListener;
import org.rhq.enterprise.gui.coregui.client.util.message.Message;
import org.rhq.enterprise.gui.coregui.client.util.message.MessageCenter;
import org.rhq.enterprise.gui.coregui.client.util.selenium.LocatableIButton;
import org.rhq.enterprise.gui.coregui.client.util.selenium.LocatableToolStrip;
import org.rhq.enterprise.gui.coregui.client.util.selenium.LocatableVLayout;

/**
 * @author Ian Springer
 */
public class TestConfigurationView
    extends LocatableVLayout implements PropertyValueChangeListener {

    private ConfigurationEditor editor;
    private LocatableIButton saveButton;
    private ConfigurationDefinition configurationDefinition;
    private Configuration configuration;

    public TestConfigurationView(String locatorId) {
        super(locatorId);
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        build();
    }

    public void build() {
        setWidth100();
        setHeight100();
        
        LocatableToolStrip toolStrip = new LocatableToolStrip(extendLocatorId("ToolStrip"));
        toolStrip.setWidth100();

        toolStrip.addMember(new LayoutSpacer());

        this.saveButton = new LocatableIButton(toolStrip.extendLocatorId("Save"), MSG.common_button_save());
        this.saveButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                save();
            }
        });
        this.saveButton.disable();
        toolStrip.addMember(this.saveButton);

        addMember(toolStrip);

        this.configurationDefinition = TestConfigurationFactory.createConfigurationDefinition();
        this.configuration = TestConfigurationFactory.createConfiguration();

        reloadConfiguration();
    }

    @Override
    public void propertyValueChanged(PropertyValueChangeEvent event) {
        MessageCenter messageCenter = CoreGUI.getMessageCenter();
        Message message;
        if (event.isInvalidPropertySetChanged()) {
            Map<String, String> invalidPropertyNames = event.getInvalidPropertyNames();
            if (invalidPropertyNames.isEmpty()) {
                this.saveButton.enable();
                message = new Message("All properties now have valid values, so the configuration can now be saved.",
                    Message.Severity.Info, EnumSet.of(Message.Option.Transient, Message.Option.Sticky));
            }
            else {
                this.saveButton.disable();
                message = new Message(
                    "The following properties have invalid values: " + invalidPropertyNames.values()
                        + " - the values must be corrected before the configuration can be saved.",
                    Message.Severity.Error, EnumSet.of(Message.Option.Transient, Message.Option.Sticky));
            }
            messageCenter.notify(message);
        }
        else {
            this.saveButton.enable();
        }
    }

    private void reloadConfiguration() {
        this.saveButton.disable();
        if (editor != null) {
            editor.destroy();
            removeMember(editor);
        }

        editor = createConfigurationEditor();
        addMember(editor);
        markForRedraw();
    }

    protected ConfigurationEditor createConfigurationEditor() {
        ConfigurationEditor editor = new ConfigurationEditor(extendLocatorId("Editor"), this.configurationDefinition,
                this.configuration);
        editor.setEditorTitle("Test Configuration");
        editor.setOverflow(Overflow.AUTO);
        editor.addPropertyValueChangeListener(this);
        return editor;
    }

    private void save() {
        CoreGUI.getMessageCenter().notify(
            new Message("Configuration updated.", "Test configuration updated."));
        reloadConfiguration();
    }

}
