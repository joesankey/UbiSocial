package ubiEvents;

import java.math.BigInteger;

import data.live.Position;
import data.live.Tag;

public interface UbiEventListener {

	public void tagAdded(Tag tag);

	public void tagUpdated(Tag tag);

	public void tagStream(BigInteger tagId, Position newPosition);

}
