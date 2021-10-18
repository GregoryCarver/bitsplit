package messages;

import java.io.Serializable;

/***********************************************************************************************************************
 * Empty interface so messages.Message and messages.HandShake can share a common type.
 * Implemented by: messages.Message; messages.Handshake
 **********************************************************************************************************************/

public interface IMessage extends Serializable
{ }