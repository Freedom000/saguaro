package com.willowtreeapps.saguaro.maven.util;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;

import java.util.List;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 1:55 PM
 */
public class ProjectHelper {
    private MavenProject project;
    private List<ArtifactRepository> remoteRepositories;
    private ArtifactRepository localRepository;
    private MavenProjectBuilder projectBuilder;

    public ProjectHelper(MavenProject project, MavenProjectBuilder projectBuilder, List<ArtifactRepository> remoteRepositories, ArtifactRepository localRepository) {
        this.project = project;
        this.projectBuilder = projectBuilder;
        this.remoteRepositories = remoteRepositories;
        this.localRepository = localRepository;
    }

    public Set<Artifact> getArtifacts() {
        return project.getDependencyArtifacts();
    }

    public MavenProject buildFromRepository(Artifact artifact) throws MojoExecutionException {
        try {
            return projectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
        } catch (ProjectBuildingException e) {
            throw new MojoExecutionException( "Unable to build project: " + artifact.getDependencyConflictId(), e);
        }
    }

}
