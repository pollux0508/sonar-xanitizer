
package org.owasp.webgoat.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * <p>RandomLessonTracker class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class RandomLessonTracker extends LessonTracker
{

	private String[] stages;

	private String stage;

	private Map<String, Boolean> completed = new HashMap<String, Boolean>();

	/**
	 * <p>Constructor for RandomLessonTracker.</p>
	 *
	 * @param stages an array of {@link java.lang.String} objects.
	 */
	public RandomLessonTracker(String[] stages)
	{
		if (stages == null) stages = new String[0];
		this.stages = stages;
	}

	/**
	 * <p>Setter for the field <code>stage</code>.</p>
	 *
	 * @param stage a {@link java.lang.String} object.
	 */
	public void setStage(String stage)
	{
		this.stage = stage;
	}

	/**
	 * <p>Getter for the field <code>stage</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getStage()
	{
		if (this.stage == null && stages.length > 0) return stages[0];
		return this.stage;
	}

	/**
	 * <p>setStageComplete.</p>
	 *
	 * @param stage a {@link java.lang.String} object.
	 * @param complete a boolean.
	 */
	public void setStageComplete(String stage, boolean complete)
	{
		completed.put(stage, Boolean.valueOf(complete));
		if (!complete) return;
		int i = getStageNumber(stage);
		if (i < stages.length - 1) setStage(stages[i + 1]);
	}

	/**
	 * <p>getStageNumber.</p>
	 *
	 * @param stage a {@link java.lang.String} object.
	 * @return a int.
	 */
	public int getStageNumber(String stage)
	{
		for (int i = 0; i < stages.length; i++)
			if (stages[i].equals(stage)) return i;
		return -1;
	}

	/**
	 * <p>hasCompleted.</p>
	 *
	 * @param stage a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean hasCompleted(String stage)
	{
		Boolean complete = completed.get(stage);
		return complete == null ? false : complete.booleanValue();
	}

	/** {@inheritDoc} */
	@Override
	public boolean getCompleted()
	{
		for (int i = 0; i < stages.length; i++)
			if (!hasCompleted(stages[i])) return false;
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void setCompleted(boolean complete)
	{
		if (complete == true) throw new UnsupportedOperationException("Use individual stage completion instead");
		for (int i = 0; i < stages.length; i++)
			setStageComplete(stages[i], false);
		setStage(stages[0]);
	}

	/** {@inheritDoc} */
	protected void setProperties(Properties props, Screen screen)
	{
		super.setProperties(props, screen);
		for (int i = 0; i < stages.length; i++)
		{
			String p = props.getProperty(screen.getTitle() + "." + stages[i] + ".completed");
			if (p != null)
			{
				setStageComplete(stages[i], Boolean.valueOf(p));
			}
		}
		setStage(props.getProperty(screen.getTitle() + ".stage"));
	}

	/** {@inheritDoc} */
	public void store(WebSession s, Screen screen, String user)
	{
		for (int i = 0; i < stages.length; i++)
		{
			if (hasCompleted(stages[i]))
			{
				lessonProperties.setProperty(screen.getTitle() + "." + stages[i] + ".completed", Boolean.TRUE
						.toString());
			}
			else
			{
				lessonProperties.remove(screen.getTitle() + "." + stages[i] + ".completed");
			}
		}
		lessonProperties.setProperty(screen.getTitle() + ".stage", getStage());
		super.store(s, screen, user);
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString()
	{
		StringBuffer buff = new StringBuffer();
		buff.append(super.toString());
		for (int i = 0; i < stages.length; i++)
		{
			buff.append("    - completed " + stages[i] + " :....... " + hasCompleted(stages[i]) + "\n");
		}
		buff.append("    - currentStage:....... " + getStage() + "\n");
		return buff.toString();
	}

}
