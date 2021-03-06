/*******************************************************************************
 * Copyright (c) 2004, 2006 KOBAYASHI Tadashi and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    KOBAYASHI Tadashi - initial API and implementation
 *******************************************************************************/

package net.sourceforge.veditor.wizard;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.veditor.HdlNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class NewProjectWizard extends BasicNewResourceWizard
{
	private WizardNewProjectCreationPage page;

	public NewProjectWizard()
	{
		super();
	}

	public void addPages()
	{
		page = new WizardNewProjectCreationPage("NewProjectWizardPage");
		page.setTitle("Verilog/VHDL Project");
		page.setDescription("Create a new Verilog/VHDL project resource.");
		addPage(page);
	}

	public boolean performFinish()
	{
        IProject newProject = page.getProjectHandle();

		IPath newPath = null;
		if (!page.useDefaults())
			newPath = page.getLocationPath();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProjectDescription description;
		description = workspace.newProjectDescription(newProject.getName());
		description.setLocation(newPath);

		CreateProjectOperation op;
		op = new CreateProjectOperation(newProject, description);

		try
		{
			getContainer().run(true, true, op);			
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			return false;
		}
		
		// add the nature if one does not exist yet
		try {
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = HdlNature.NATURE_ID;
			description.setNatureIds(newNatures);
			newProject.setDescription(description, null);
		} catch (CoreException e) {
			// Something went wrong
			return false;
		}
		return true;
	}

	private class CreateProjectOperation extends WorkspaceModifyOperation
	{
		private IProject project;
		private IProjectDescription description;
		
		public CreateProjectOperation(IProject project, IProjectDescription description)
		{
			this.project = project;
			this.description = description;
		}
		protected void execute(IProgressMonitor monitor) throws CoreException,
				InvocationTargetException, InterruptedException
		{
			try
			{
				monitor.beginTask("", 2000);

				project.create(description, null);

				monitor.worked(1000);
				if (monitor.isCanceled())
					throw new OperationCanceledException();

				project.open(IResource.BACKGROUND_REFRESH, null);
			}
			finally
			{
				monitor.done();
			}

		}
		
	}
}  





