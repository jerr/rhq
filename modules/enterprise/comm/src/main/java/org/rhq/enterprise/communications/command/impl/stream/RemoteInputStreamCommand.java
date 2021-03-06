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
package org.rhq.enterprise.communications.command.impl.stream;

import java.util.Map;
import org.jboss.remoting.invocation.NameBasedInvocation;
import org.rhq.enterprise.communications.command.AbstractCommand;
import org.rhq.enterprise.communications.command.Command;
import org.rhq.enterprise.communications.command.CommandType;
import org.rhq.enterprise.communications.command.param.InvalidParameterDefinitionException;
import org.rhq.enterprise.communications.command.param.ParameterDefinition;

/**
 * The command to be sent when a remote invocation on an input stream is to be made.
 *
 * @author John Mazzitelli
 */
public class RemoteInputStreamCommand extends AbstractCommand {
    /**
     * command type constant identifying this type of command
     */
    public static final CommandType COMMAND_TYPE = new CommandType("remotestream", 1);

    /**
     * the required command parameter name containing the NameBasedInvocation object to describe what method to invoke
     * on the remote stream.
     */
    public static final ParameterDefinition PARAM_INVOCATION = new ParameterDefinition("invocation",
        NameBasedInvocation.class.getName(), ParameterDefinition.REQUIRED, ParameterDefinition.NOT_NULLABLE,
        ParameterDefinition.NOT_HIDDEN, null);

    /**
     * the required command parameter name containing the opaque identification object used to specify the particular
     * input stream that is to be invoked.
     */
    public static final ParameterDefinition PARAM_STREAM_ID = new ParameterDefinition("streamId", Long.class.getName(),
        ParameterDefinition.REQUIRED, ParameterDefinition.NOT_NULLABLE, ParameterDefinition.NOT_HIDDEN, null);

    /**
     * the Serializable UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for {@link RemoteInputStreamCommand}.
     *
     * @see AbstractCommand#AbstractCommand()
     */
    public RemoteInputStreamCommand() throws IllegalArgumentException, InvalidParameterDefinitionException {
        super();
        setCommandInResponse(true);
    }

    /**
     * Constructor for {@link RemoteInputStreamCommand}.
     *
     * @see AbstractCommand#AbstractCommand(Map)
     */
    public RemoteInputStreamCommand(Map<String, Object> commandParameters) throws IllegalArgumentException,
        InvalidParameterDefinitionException {
        super(commandParameters);
        setCommandInResponse(true);
    }

    /**
     * Constructor for {@link RemoteInputStreamCommand}.
     *
     * @see AbstractCommand#AbstractCommand(Command)
     */
    public RemoteInputStreamCommand(Command commandToTransform) {
        super(commandToTransform);
        setCommandInResponse(true);
    }

    /**
     * Gets the invocation information that describes what needs to be invoked on the remote POJO.
     *
     * @return the invocation information
     */
    public NameBasedInvocation getNameBasedInvocation() {
        return (NameBasedInvocation) getParameterValue(PARAM_INVOCATION.getName());
    }

    /**
     * Sets the invocation information that describes what needs to be invoked on the remote POJO.
     *
     * @param invocation the invocation information
     */
    public void setNameBasedInvocation(NameBasedInvocation invocation) {
        setParameterValue(PARAM_INVOCATION.getName(), invocation);
    }

    /**
     * Get the opaque ID object used to identify the input stream that is being invoked by this command.
     *
     * @return stream identifier
     */
    public Long getStreamId() {
        return (Long) getParameterValue(PARAM_STREAM_ID.getName());
    }

    /**
     * Sets the opaque ID object used to identify the input stream that is being invoked by this command.
     *
     * @param id stream identifier
     */
    public void setStreamId(Long id) {
        setParameterValue(PARAM_STREAM_ID.getName(), id);
    }

    /**
     * The RemoteInputStreamCommandResponse <b>must</b> have the command in it in order to support its mimicing of
     * pass-by-reference semantics since the command contains the byte buffer that will contain the bytes when reading
     * the stream. Therefore, this method forces its return value to be <code>true</code> always.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean isCommandInResponse() {
        return true;
    }

    /**
     * The RemoteInputStreamCommandResponse <b>must</b> have the command in it in order to support its mimicing of
     * pass-by-reference semantics since the command contains the byte buffer that will contain the bytes when reading
     * the stream. Therefore, this method forces the command-in-response flag to <code>true</code>, no matter that
     * <code>flag</code>'s value is.
     *
     * @see AbstractCommand#setCommandInResponse(boolean)
     */
    @Override
    public void setCommandInResponse(boolean flag) {
        super.setCommandInResponse(true);
    }

    /**
     * @see AbstractCommand#buildCommandType()
     */
    @Override
    protected CommandType buildCommandType() {
        return COMMAND_TYPE;
    }

    /**
     * @see AbstractCommand#buildParameterDefinitions()
     */
    @Override
    protected ParameterDefinition[] buildParameterDefinitions() {
        return new ParameterDefinition[] { PARAM_INVOCATION, PARAM_STREAM_ID };
    }
}